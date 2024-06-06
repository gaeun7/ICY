package com.sparta.icy.Service;

import com.sparta.icy.Dto.UserProfileResponse;
import com.sparta.icy.Dto.UserUpdateRequest;
import com.sparta.icy.Entity.User;
import com.sparta.icy.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserProfileResponse getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다."));
        return new UserProfileResponse(user.getUsername(), user.getNickname(), user.getIntro(), user.getEmail());
    }

    @Transactional
    public User updateUser(long id, UserUpdateRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다."));

        if (!user.getPassword().equals(req.getCurrentPassword())) {
            throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
        }

        if (req.getNewPassword() != null && !isValidPassword(req.getNewPassword())) {
            throw new IllegalArgumentException("비밀번호 형식이 올바르지 않습니다.");
        }

        if (user.getPassword().equals(req.getNewPassword())) {
            throw new IllegalArgumentException("현재 비밀번호와 동일한 비밀번호로 수정할 수 없습니다.");
        }

        user.update(req);
        return userRepository.save(user);
    }

    private boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasUpperCase = false;
        boolean hasLowerCase = false;
        boolean hasDigit = false;
        boolean hasSpecialChar = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) {
                hasUpperCase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowerCase = true;
            } else if (Character.isDigit(c)) {
                hasDigit = true;
            } else if (!Character.isLetterOrDigit(c)) {
                hasSpecialChar = true;
            }
        }

        return hasUpperCase && hasLowerCase && hasDigit && hasSpecialChar;
    }
}
