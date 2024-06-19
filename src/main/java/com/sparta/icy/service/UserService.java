package com.sparta.icy.service;

import com.sparta.icy.dto.*;
import com.sparta.icy.entity.User;
import com.sparta.icy.entity.UserStatus;
import com.sparta.icy.error.AlreadySignedOutUserCannotBeSignoutAgainException;
import com.sparta.icy.error.DuplicateUsernameException;
import com.sparta.icy.error.PasswordDoesNotMatchException;
import com.sparta.icy.repository.UserRepository;
import com.sparta.icy.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final LogService logService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, LogService logService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.logService = logService;
    }

    // 회원가입 메서드
    public void signup(SignupRequestDto signupRequestDto) {
        String username = signupRequestDto.getUsername();
        if (userRepository.findByUsername(username).isPresent()) {
            throw new DuplicateUsernameException("이미 존재하는 사용자 이름입니다");
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());
        User user = new User(
                signupRequestDto.getUsername(),
                encodedPassword,
                signupRequestDto.getNickname(),
                signupRequestDto.getEmail(),
                signupRequestDto.getIntro(),
                UserStatus.IN_ACTION
        );

        userRepository.save(user);
    }

    // 사용자 프로필 조회 메서드
    public UserProfileResponse getUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        return new UserProfileResponse(
                user.getUsername(),
                user.getNickname(),
                user.getEmail(),
                user.getIntro()
        );
    }

    // 사용자 이름으로 사용자 정보 로드 메서드
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

        return new UserDetailsImpl(user);
    }

    // 사용자 정보 업데이트 메서드
    public User updateUser(long id, UserUpdateRequest req) {
        User currentUser = getCurrentUser();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다"));

        if (user.getStatus().equals(UserStatus.SECESSION.getStatus())) {
            throw new IllegalArgumentException("유효하지 않은 사용자입니다");
        }

        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        }

        if (req.getNewPassword() != null && !isValidPassword(req.getNewPassword())) {
            throw new IllegalArgumentException("비밀번호 형식이 올바르지 않습니다");
        }

        if (user.getPassword().equals(req.getNewPassword())) {
            throw new IllegalArgumentException("현재 비밀번호와 동일한 비밀번호로 수정할 수 없습니다");
        }

        if (!currentUser.getUsername().equals(user.getUsername())) {
            throw new IllegalArgumentException("프로필 업데이트 권한이 없습니다");
        }

        user.update(req);
        return userRepository.save(user);
    }

    // 로그아웃 메서드
    public boolean signout(String userDetailsUsername, SignoutRequestDto signoutRequestDto) {
        try {
            User checkUsername = userRepository.findByUsername(userDetailsUsername).orElseThrow();

            // 이미 탈퇴한 회원이라서 재탈퇴 못함
            if (checkUsername.getStatus().equals(UserStatus.SECESSION.getStatus())) {
                throw new AlreadySignedOutUserCannotBeSignoutAgainException("이미 탈퇴한 회원은 재탈퇴가 불가능합니다");
            }

            // 사용자가 입력한 비밀번호가 현재 로그인된 비밀번호와 맞는지 확인
            if (!passwordEncoder.matches(signoutRequestDto.getPassword(), checkUsername.getPassword())) {
                throw new PasswordDoesNotMatchException("기존 비밀번호와 일치하지 않습니다");
            }

            // 탈퇴한 회원으로 전환
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

    // 유효한 비밀번호인지 확인하는 메서드
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

    // 현재 사용자 정보 가져오는 메서드
    private static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalStateException("인증되지 않은 사용자입니다");
        }
        // Principal이 UserDetailsImpl 타입인지 확인
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            throw new IllegalStateException("사용자 정보를 가져올 수 없습니다");
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) principal;
        return userDetails.getUser();
    }
}