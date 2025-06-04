package com.example.healthme.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 액세스 토큰 (JWT 문자열)
    @Column(name="accessToken", columnDefinition = "TEXT", nullable = false)
    private String accessToken;

    // 리프레시 토큰 (JWT 문자열)
    @Column(name="refreshToken", columnDefinition = "TEXT", nullable = false)
    private String refreshToken;

    // User 테이블의 id를 외래키로 참조 >> 어떤 유저와 연결된지 파악하기 위함
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 토큰이 발급된 시각
    @Column(name="createdAt", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime createdAt;
}