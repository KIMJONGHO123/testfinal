package com.example.healthme.global.config.auth.handler.loginHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomLoginFailureHandler implements AuthenticationFailureHandler {

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
										AuthenticationException exception) throws IOException {

		log.error("로그인 실패: {}", exception.getMessage());

		// 1. HTTP 상태 401 Unauthorized
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

		// 2. 응답 타입을 JSON으로 지정
		response.setContentType("application/json;charset=UTF-8");

		// 3. 에러 메시지를 JSON 형식으로 출력
		String errorJson = "{\"error\": \"invalid_credentials\"}";
		response.getWriter().write(errorJson);
	}
}