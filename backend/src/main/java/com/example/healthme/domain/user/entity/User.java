package com.example.healthme.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String userid;
    private String password;
    private String username;
    private String zip;
    private String address;
    private String addressDetail;
    private String tel;
    private String gender;
    // 회원가입에는 기입x, 기본값 부여
    private String role;
    private String grade;


    @Column(length = 20)
    private String provider;
    @Column(length = 100)
    private String providerId;
}
