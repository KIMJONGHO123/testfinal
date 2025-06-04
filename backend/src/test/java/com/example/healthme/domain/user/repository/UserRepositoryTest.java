package com.example.healthme.domain.user.repository;

import com.example.healthme.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByUserid() {
        // given
        User user = User.builder()
                .userid("testuser")
                .password("1234")
                .build();
        userRepository.save(user);

        // when
        boolean result = userRepository.existsByUserid("testuser");

        // then
        assertThat(result).isTrue();
    }
}