package com.example.healthme.domain.user.repository;

import com.example.healthme.domain.user.entity.JwtToken;
import com.example.healthme.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken,Long> {
    // AccessToken 으로 조회
    Optional<JwtToken> findByAccessToken(String accessToken);

    // User 기준 조회 (로그아웃할 때 주로 씀)
    Optional<JwtToken> findByUser(User user);

    // 또는 User의 userid 기준 조회도 가능
    Optional<JwtToken> findByUser_Userid(String userid);

    // AccessToken 삭제
    void deleteByAccessToken(String accessToken);
}
