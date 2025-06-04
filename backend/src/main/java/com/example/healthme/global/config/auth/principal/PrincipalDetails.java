package com.example.healthme.global.config.auth.principal;

import com.example.healthme.domain.user.dto.UserDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

// 로그인한 유저의 정보를 Security가 기억하게 해주는 클래스
@Getter
public class PrincipalDetails implements UserDetails,OAuth2User {

	private final UserDto userDto;
	// OAuth2에 사용
	private final Map<String, Object> attributes;
	private final String accessToken;

	// 일반 로그인
	public PrincipalDetails(UserDto userDto){
		this.userDto = userDto;
		this.attributes = null;
		this.accessToken = null;
	}

	// OAuth2로 로그인
	public PrincipalDetails(UserDto userDto, Map<String, Object> attributes, String accessToken){
		this.userDto = userDto;
		this.attributes = attributes;
		this.accessToken = accessToken;
	}

	// 권한 반환
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority(userDto.getRole()));
	}

	// 인증 ID
	@Override
	public String getUsername() {
		// String security 템플릿 때문에 getUsername 사용. 요구값은 id
		return userDto.getUserid();
	}
	@Override
	public String getPassword() {
		return userDto.getPassword();
	}

	// ---------계정 상태 관련 (true 설정)------------
	@Override
	public boolean isAccountNonExpired() {return true;}
	@Override
	public boolean isAccountNonLocked() {return true;}
	@Override
	public boolean isCredentialsNonExpired() {return true;}
	@Override
	public boolean isEnabled() {return true;}
	// ------------------------------------------

	// OAuth 2 유저용
	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}
	@Override
	public String getName() {
		return userDto.getUsername();
	}

}
