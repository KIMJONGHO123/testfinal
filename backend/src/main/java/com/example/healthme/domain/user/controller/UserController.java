package com.example.healthme.domain.user.controller;

import com.example.healthme.domain.user.dto.JoinRequestDto;
import com.example.healthme.domain.user.dto.LoginRequestDto;
import com.example.healthme.domain.user.dto.UserResponseDto;
import com.example.healthme.domain.user.entity.User;
import com.example.healthme.domain.user.repository.UserRepository;
import com.example.healthme.domain.user.service.UserService;
import com.example.healthme.global.config.auth.jwt.TokenInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/healthme/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;
    // 회원가입
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody @Valid JoinRequestDto dto, BindingResult bindingResult) {
        // 유효성 검사
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", errorMessage));
        }

        // 비밀번호 확인
        if (!dto.getPassword().equals(dto.getPassword2())) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", "비밀번호가 일치하지 않습니다."));
        }

        // 아이디 중복확인
        try {
            userService.join(dto); // DB 저장 시도 (중복 가능성 있음)
            return ResponseEntity.ok(Collections.singletonMap("message", "회원가입 성공"));
        } catch (Exception e) {
            return ResponseEntity.status(409) // 중복 등 DB 제약 에러
                    .body(Collections.singletonMap("error", "이미 존재하는 아이디입니다."));
        }
    }
    
    
    // 로그인 중복확인
    @GetMapping("/check")
    public Map<String, Boolean> checkUserid(@RequestParam String userid) {
        boolean exists = userService.isUseridExists(userid);
        return Collections.singletonMap("exists", exists);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDto dto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldError().getDefaultMessage();
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", errorMessage));
        }

        try {
            // 1. JWT 발급
            TokenInfo tokenInfo = userService.login(dto);

            // 2. 사용자 정보 조회 (userService.login은 userDto 반환 안 하니까 여기서 따로 불러옴)
            User user = userRepository.findByUserid(dto.getUserid())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
            UserResponseDto userResponseDto = UserResponseDto.fromEntity(user);

            // 3. 응답 조립
            Map<String, Object> result = new HashMap<>();
            result.put("accessToken", tokenInfo.getAccessToken());
            result.put("refreshToken", tokenInfo.getRefreshToken());
            result.put("userInfo", userResponseDto);

            return ResponseEntity.ok(result);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", e.getMessage()));
        }
    }

}
