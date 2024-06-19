package com.sparta.icy;

import com.sparta.icy.dto.SignoutRequestDto;
import com.sparta.icy.dto.SignupRequestDto;
import com.sparta.icy.dto.UserProfileResponse;
import com.sparta.icy.entity.User;
import com.sparta.icy.entity.UserStatus;
import com.sparta.icy.error.DuplicateUsernameException;
import com.sparta.icy.error.PasswordDoesNotMatchException;
import com.sparta.icy.repository.UserRepository;
import com.sparta.icy.service.LogService;
import com.sparta.icy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceIntegrationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private LogService logService;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() {
        // 레포지토리 동작 mock 설정
        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    public void signup_NewUser_Successful() {
        // 새로운 사용자 등록 성공 테스트
        SignupRequestDto requestDto = new SignupRequestDto(
                "newUser12345",
                "StrongPassword123!",
                "ValidNickname",
                "valid@example.com",
                "Valid intro text"
        );

        when(passwordEncoder.encode("StrongPassword123!")).thenReturn("encodedPassword");

        assertDoesNotThrow(() -> userService.signup(requestDto));
    }

    @Test
    public void signup_ExistingUser_DuplicateUsernameExceptionThrown() {
        // 기존 사용자 중복 예외 발생 테스트
        SignupRequestDto requestDto = new SignupRequestDto(
                "existingUser",
                "password1234",
                "Nickname",
                "email@example.com",
                "Intro text"
        );

        when(passwordEncoder.encode("password1234")).thenReturn("encodedPassword");

        assertThrows(DuplicateUsernameException.class, () -> userService.signup(requestDto));
    }

    @Test
    public void getUser_ValidId_UserProfileResponseReturned() {
        // 유효한 ID로 사용자 프로필 응답 반환 테스트
        User user = new User("testUser", "password", "Nickname", "email@example.com", "Intro", UserStatus.IN_ACTION);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserProfileResponse userProfileResponse = userService.getUser(1L);
        assertNotNull(userProfileResponse);
        assertEquals(user.getUsername(), userProfileResponse.getUsername());
    }

    @Test
    public void getUser_InvalidId_ThrowsIllegalArgumentException() {
        // 부적절한 ID로 IllegalArgumentException 발생 테스트
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> userService.getUser(999L));
    }

    @Test
    public void loadUserByUsername_ExistingUser_UserDetailsImplReturned() {
        // 존재하는 사용자의 UserDetailsImpl 반환 테스트
        User user = new User("testUser", "password", "Nickname", "email@example.com", "Intro", UserStatus.IN_ACTION);
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
    }

    @Test
    public void loadUserByUsername_NonExistingUser_ThrowsUsernameNotFoundException() {
        // 존재하지 않는 사용자로 UsernameNotFoundException 발생 테스트
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonExistingUser"));
    }

    @Test
    public void signout_ValidSignoutRequest_Successful() {
        // 유효한 로그아웃 요청 성공 테스트
        String userDetailsUsername = "testUser";
        SignoutRequestDto signoutRequestDto = new SignoutRequestDto("StrongPassword123!");
        User user = new User(userDetailsUsername, "encodedPassword", "Nickname", "email@example.com", "Intro", UserStatus.IN_ACTION);

        when(userRepository.findByUsername(userDetailsUsername)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(signoutRequestDto.getPassword(), user.getPassword())).thenReturn(true);

        assertDoesNotThrow(() -> userService.signout(userDetailsUsername, signoutRequestDto));

        // 사용자 상태가 업데이트되고 logService가 한 번 호출되었는지 확인
        assertEquals(UserStatus.SECESSION.getStatus(), user.getStatus());
        verify(userRepository, times(1)).save(user);
        verify(logService, times(1)).addLog(userDetailsUsername, "탈퇴");
    }

    @Test
    public void signout_InvalidPassword_ThrowsPasswordDoesNotMatchException() {
        // 부적절한 비밀번호로 PasswordDoesNotMatchException 발생 테스트
        String userDetailsUsername = "testUser";
        SignoutRequestDto signoutRequestDto = new SignoutRequestDto("weakpwd");
        User user = new User(userDetailsUsername, "encodedPassword", "Nickname", "email@example.com", "Intro", UserStatus.IN_ACTION);

        when(userRepository.findByUsername(userDetailsUsername)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(signoutRequestDto.getPassword(), user.getPassword())).thenReturn(false);

        assertThrows(PasswordDoesNotMatchException.class, () -> userService.signout(userDetailsUsername, signoutRequestDto));
    }
}