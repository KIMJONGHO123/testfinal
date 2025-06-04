package com.example.healthme.domain.user.service;

import com.example.healthme.domain.user.dto.JoinRequestDto;
import com.example.healthme.domain.user.dto.LoginRequestDto;
import com.example.healthme.domain.user.dto.UserDto;
import com.example.healthme.domain.user.entity.User;
import com.example.healthme.domain.user.repository.UserRepository;
import com.example.healthme.global.config.auth.jwt.JwtTokenProvider;
import com.example.healthme.global.config.auth.jwt.TokenInfo;
import com.example.healthme.global.config.auth.principal.PrincipalDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final  PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // 일반(폼) 회원가입
    public void join(JoinRequestDto dto) {
        // 전화번호 합치기
        String fullTel = dto.getTel1() + "-" + dto.getTel2() + "-" + dto.getTel3();

        User user = new User();
        user.setUserid(dto.getUserid());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); // 이후 암호화 필요
        user.setUsername(dto.getUsername());
        user.setZip(dto.getZip());
        user.setAddress(dto.getAddress());
        user.setAddressDetail(dto.getAddressDetail());
        user.setGender(dto.getGender());
        user.setTel(fullTel);
        // 기본값으로 지정한다
        user.setRole("ROLE_USER");
        user.setGrade("새싹");
        // 소셜 로그인 정보는 null로 처리
        user.setProvider(null);
        user.setProviderId(null);

        userRepository.save(user);
    }
    // 아이디 중복확인
    public boolean isUseridExists(String userid) {
        return userRepository.existsByUserid(userid);
    }


    // 로그인
    public TokenInfo login(LoginRequestDto dto){
        User user = userRepository.findByUserid(dto.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // PrincipalDetails로 Authentication 생성
        PrincipalDetails principalDetails = new PrincipalDetails(UserDto.fromEntity(user));
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principalDetails, null, principalDetails.getAuthorities()
        );

        // JWT 생성
        return jwtTokenProvider.generateToken(authentication); // TokenInfo 반환
    }

    // 소셜 로그인 계정 생성
    public UserDto createSocialUser(String userid, String username, String provider, String providerId) {
        User user = new User();
        user.setUserid(userid);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode("1234")); // 소셜 전용
        user.setProvider(provider);
        user.setProviderId(providerId);
        user.setRole("ROLE_USER");
        user.setGrade("새싹");
        user.setZip("");
        user.setAddress("");
        user.setTel("");

        userRepository.save(user);
        return UserDto.fromEntity(user);
    }
}
