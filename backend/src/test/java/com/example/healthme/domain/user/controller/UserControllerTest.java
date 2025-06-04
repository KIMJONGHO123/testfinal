package com.example.healthme.domain.user.controller;

import com.example.healthme.domain.user.dto.JoinRequestDto;
import com.example.healthme.domain.user.dto.LoginRequestDto;
import com.example.healthme.domain.user.entity.User;
import com.example.healthme.domain.user.repository.UserRepository;
import com.example.healthme.domain.user.service.UserService;
import com.example.healthme.global.config.auth.jwt.TokenInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();



    // 회원가입
    @Test
    void join_success() throws Exception {
        // Given: 회원가입 요청 DTO 생성
        JoinRequestDto dto = new JoinRequestDto();
        dto.setUserid("testuser");
        dto.setPassword("123456");
        dto.setPassword2("123456");
        dto.setUsername("홍길동");
        dto.setZip("12345");
        dto.setAddress("서울시 강남구");
        dto.setAddressDetail("101동 202호");
        dto.setGender("남성");
        dto.setTel1("010");
        dto.setTel2("1234");
        dto.setTel3("5678");
        String json = objectMapper.writeValueAsString(dto);

        // When: 회원가입 요청 수행
        mvc.perform(post("/healthme/users/join")
                        .with(csrf())
                        .with(user("testuser").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                // Then: 상태 코드 200, 응답 메시지 확인
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("회원가입 성공"))
                .andDo(print());
    }

    // 아이디 중복 확인
    @Test
    void checkUserid_exists() throws Exception {
        // Given: userService가 "testuser" 존재한다고 응답하도록 설정
        given(userService.isUseridExists("testuser")).willReturn(true);

        // When: 중복 체크 API 요청 수행
        mvc.perform(get("/healthme/users/check")
                        .with(user("testuser").roles("USER"))
                        .param("userid", "testuser"))

                // Then: 상태 코드 200, exists=true 확인
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exists").value(true))
                .andDo(print());
    }

    // 로그인
    @Test
    void login_success() throws Exception {
        // Given: 로그인 DTO와 예상되는 TokenInfo, User 설정
        LoginRequestDto dto = new LoginRequestDto();
        dto.setUserid("testuser");
        dto.setPassword("1234");

        TokenInfo tokenInfo = new TokenInfo("granttype", "access123", "refresh456");
        User user = User.builder()
                .userid("testuser")
                .password("12341234")
                .build();

        given(userService.login(any())).willReturn(tokenInfo);
        given(userRepository.findByUserid("testuser")).willReturn(Optional.of(user));

        String json = objectMapper.writeValueAsString(dto);

        // When: 로그인 요청 수행
        mvc.perform(post("/healthme/users/login")
                        .with(csrf())
                        .with(user("testuser").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))

                // Then: 상태 코드 200, 토큰과 사용자 ID 확인
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access123"))
                .andExpect(jsonPath("$.refreshToken").value("refresh456"))
                .andExpect(jsonPath("$.userInfo.userid").value("testuser"))
                .andDo(print());
    }

}