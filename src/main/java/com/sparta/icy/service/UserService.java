package com.sparta.icy.service;

import com.sparta.icy.dto.SignupRequestDto;
import com.sparta.icy.dto.UserProfileResponse;
import com.sparta.icy.dto.UserUpdateRequest;
import com.sparta.icy.entity.User;
import com.sparta.icy.entity.UserStatus;
import com.sparta.icy.error.AlreadySignedOutUserCannotBeSignoutAgainException;
import com.sparta.icy.error.DuplicateUsernameException;
import com.sparta.icy.error.PasswordDoesNotMatchException;
import com.sparta.icy.jwt.JwtUtil;
import com.sparta.icy.repository.UserRepository;
import com.sparta.icy.security.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final LogService logService; // LogService 주입

    public UserProfileResponse getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다."));
        if (user.getStatus().equals(UserStatus.SECESSION.getStatus())) {
            throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
        }
        return new UserProfileResponse(user.getUsername(), user.getNickname(), user.getIntro(), user.getEmail());
    }

    @Transactional
    public User updateUser(long id, UserUpdateRequest req) {
        User currentUser = getcurrentUser();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다."));
        if (user.getStatus().equals(UserStatus.SECESSION.getStatus())) {
            throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
        }
        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (req.getNewPassword() != null && !isValidPassword(req.getNewPassword())) {
            throw new IllegalArgumentException("비밀번호 형식이 올바르지 않습니다.");
        }
        if (user.getPassword().equals(req.getNewPassword())) {
            throw new IllegalArgumentException("현재 비밀번호와 동일한 비밀번호로 수정할 수 없습니다.");
        }
        if (!currentUser.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("프로필 업데이트 권한이 없습니다.");
        }
        user.update(req);
        return userRepository.save(user);
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }
        boolean hasAlpha = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c) || Character.isLowerCase(c)) {
                hasAlpha = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }
        return hasAlpha && hasDigit && hasSpecialChar;
    }

    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());
        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new DuplicateUsernameException("중복된 사용자가 존재합니다.");
        }

        //회원 상태 등록
        UserStatus status = UserStatus.IN_ACTION;

        // 사용자 등록
        //String username, String nickname, String password, String email, String intro, UserStatus status
        User user = new User(username, requestDto.getNickname(), password, requestDto.getEmail(), requestDto.getIntro(), status);
        userRepository.save(user);
    }

    public boolean signout(String userDetailsUsername, String password) {
        try {
            User checkUsername = userRepository.findByUsername(userDetailsUsername).orElseThrow();

            //이미 탈퇴한 회원이라서 재탈퇴 못함
            if (checkUsername.getStatus().equals(UserStatus.SECESSION.getStatus())) {
                throw new AlreadySignedOutUserCannotBeSignoutAgainException("이미 탈퇴한 회원은 재탈퇴가 불가능");

            }
            //사용자가 입력한 비밀번호가 현재 로그인된 비밀번호와 맞는지 확인
            if (!checkUsername.getPassword().equals(password)) {
                throw new PasswordDoesNotMatchException("기존 비밀번호와 일치하지 않음");
            }

            //탈퇴한 회원으로 전환
            checkUsername.setStatus(UserStatus.SECESSION.getStatus());
            userRepository.save(checkUsername); // 변경된 상태를 저장

            // 탈퇴한 회원 로그 추가
            logService.addLog(userDetailsUsername, "탈퇴"); // LogService의 addLog 메서드 호출

            return true;

        } catch (PasswordDoesNotMatchException | AlreadySignedOutUserCannotBeSignoutAgainException e) {
            // 예외 발생 시 로그를 남기고 false 반환
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private static User getcurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증되지 않은 사용자입니다.");
        }
        // Principal이 UserDetailsImpl 타입인지 확인
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            throw new IllegalStateException("사용자 정보를 가져올 수 없습니다.");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        User currentUser = userDetails.getUser();
        return currentUser;
    }

    public void logout(HttpServletResponse response) {
    }
}
