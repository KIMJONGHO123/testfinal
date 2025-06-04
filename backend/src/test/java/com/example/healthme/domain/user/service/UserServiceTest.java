package com.example.healthme.domain.user.service;

import com.example.healthme.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("아이디 존재 여부 반환 테스트")
    void isUseridExists_True() {
        // given
        String userid = "testuser";
        given(userRepository.existsByUserid(userid)).willReturn(true);

        // when
        boolean result = userService.isUseridExists(userid);

        // then
        assertThat(result).isTrue();

        // 콘솔 출력
        System.out.println("아이디 '" + userid + "' 중복 결과: " + result);  // true 출력
    }

    @Test
    @DisplayName("아이디 존재하지 않을 경우 false 반환 테스트")
    void isUseridExists_False() {
        // given
        String userid = "nonexist";
        given(userRepository.existsByUserid(userid)).willReturn(false);

        // when
        boolean result = userService.isUseridExists(userid);

        // then
        assertThat(result).isFalse();

        // 콘솔 출력
        System.out.println("아이디 '" + userid + "' 중복 결과: " + result);  // false 출력
    }
}