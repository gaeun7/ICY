package com.sparta.icy.service;

import com.sparta.icy.Dto.SignupRequestDto;
import com.sparta.icy.Dto.UserProfileResponse;
import com.sparta.icy.Dto.UserUpdateRequest;
import com.sparta.icy.Entity.Status;
import com.sparta.icy.Entity.User;
import com.sparta.icy.repository.UserRepository;
import com.sparta.icy.error.AlreadySignedOutUserCannotBeSignoutAgainException;
import com.sparta.icy.error.PasswordDoesNotMatchException;
import com.sparta.icy.jwt.JwtUtil;
import com.sparta.icy.security.UserDetailsImpl;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserProfileResponse getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다."));
        if(user.getStatus() == Status.DELETED){
            throw new IllegalArgumentException("유효하지 않은 사용자입니다.");
        }
        return new UserProfileResponse(user.getUsername(), user.getNickname(), user.getIntro(), user.getEmail());
    }

    @Transactional
    public User updateUser(long id, UserUpdateRequest req) {
        User currentUser = getcurrentUser();

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다."));

        if(user.getStatus() == Status.DELETED){
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
        String password = requestDto.getPassword();

        if (!isValidUsername(username)) {
            throw new IllegalArgumentException("유효하지 않은 username입니다. 영문자와 숫자만 사용할 수 있습니다.");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("유효하지 않은 password입니다. 영문자와 숫자, 특수문자를 최소 1글자씩 포함해주세요.");
        }

        String encodedpassword = passwordEncoder.encode(requestDto.getPassword());
        // 회원 중복 확인
        Optional<User> checkUserOptional = userRepository.findByUsername(username);
        if (checkUserOptional.isPresent()) {
            User checkUser = checkUserOptional.get();
            if (checkUser.getStatus() == Status.DELETED) {
                throw new IllegalArgumentException("탈퇴한 아이디로 재가입이 불가합니다.");
            }
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        User user = new User(username, encodedpassword, requestDto.getEmail(), requestDto.getIntro(), requestDto.getNickname());
        userRepository.save(user);
    }

    @Transactional
    public void signout(String userDetailsUsername, String password) {
        User checkUsername = userRepository.findByUsername(userDetailsUsername)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        //이미 탈퇴한 회원이라서 재탈퇴 못함
        if (checkUsername.getStatus() == Status.DELETED) {
            throw new AlreadySignedOutUserCannotBeSignoutAgainException("이미 탈퇴한 회원입니다.");
        }

        if (!passwordEncoder.matches(password, checkUsername.getPassword())) {
            throw new PasswordDoesNotMatchException("기존 비밀번호와 일치하지 않지 않습니다.");
        }

        //탈퇴한 회원으로 전환
        checkUsername.setStatus(Status.DELETED);
        userRepository.save(checkUsername); // 변경된 상태를 저장

        System.out.println("User status changed to DELETED for username: " + userDetailsUsername);
    }

    private boolean isValidUsername(String username) {
        String regex = "^[a-zA-Z0-9]+$";
        return username.matches(regex);
    }

    public void logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
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
}


