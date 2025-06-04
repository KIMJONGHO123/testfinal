package com.example.healthme.global.config.auth.handler.logoutHandler;


import com.example.healthme.domain.user.repository.JwtTokenRepository;
import com.example.healthme.global.config.auth.principal.PrincipalDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CustomLogoutHandler implements LogoutHandler {

	private final JwtTokenRepository jwtTokenRepository;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		log.info("CustomLogoutHandler 실행");

		// 1. accessToken → DB에서 제거
		String header = request.getHeader("Authorization");
		if (header != null && header.startsWith("Bearer ")) {
			String token = header.substring(7);
			jwtTokenRepository.deleteByAccessToken(token);
			log.info("accessToken 삭제 완료: {}", token);
		}

		// 2. OAuth2 provider별 외부 로그아웃 요청 (선택)
		if (authentication != null && authentication.getPrincipal() instanceof PrincipalDetails principal) {
			String provider = principal.getUserDto().getProvider();
			if (provider != null) {
				switch (provider) {
					case "kakao" -> log.info("카카오 로그아웃 처리 준비 중...");
					case "naver" -> log.info("네이버 로그아웃 처리 준비 중...");
					case "google" -> log.info("구글 로그아웃 처리 준비 중...");
				}
			}
		}

		// 3. 세션 무효화
		HttpSession session = request.getSession(false);
		if (session != null) session.invalidate();
	}
}
