package com.example.healthme.global.config.auth.principal;

import com.example.healthme.domain.user.dto.UserDto;
import com.example.healthme.domain.user.entity.User;
import com.example.healthme.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// 로그인 시 Spring Security가 내부적으로 호출해 DB에서 유저를 조회하고
// 인증 처리를 수행할 수 있도록 연결하는 클래스 (UserService의 역할 중 일부를 위임)
@Service
@Slf4j
public class PrincipalDetailsService implements UserDetailsService{

	private final UserRepository userRepository;

	public PrincipalDetailsService(UserRepository userRepository){
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// username 변수는 Security 내부에서 getUsername() 호출 → userid와 매핑해 사용한다
		log.info("loadUserByUsername 호출 " + username);

		User user = userRepository.findByUserid(username)
				.orElseThrow(() -> new UsernameNotFoundException(username + "존재하지 않는 계정입니다."));

		UserDto userDto = UserDto.fromEntity(user);
		return new PrincipalDetails(userDto);
	}

}


