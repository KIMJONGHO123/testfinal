package com.example.healthme.domain.user.repository;

import com.example.healthme.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    // 아이디 중복확인
    boolean existsByUserid(String userid);
    // 로그인에 사용
    Optional<User> findByUserid(String userid);
}
