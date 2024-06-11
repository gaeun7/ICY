package com.sparta.icy.security;

import com.sparta.icy.entity.User;
import com.sparta.icy.entity.UserStatus;
import com.sparta.icy.repository.UserRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));

        if (user.getStatus().equals(UserStatus.SECESSION.getStatus())) {
            throw new DisabledException("탈퇴한 사용자입니다.");
        }

        return new UserDetailsImpl(user);
    }
}