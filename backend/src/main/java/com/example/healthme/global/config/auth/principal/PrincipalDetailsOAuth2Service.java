package com.example.healthme.global.config.auth.principal;

import com.example.healthme.domain.user.dto.UserDto;
import com.example.healthme.domain.user.entity.User;
import com.example.healthme.domain.user.repository.UserRepository;
import com.example.healthme.domain.user.service.UserService;
import com.example.healthme.global.config.auth.oauth.userinfo.GoogleUserInfo;
import com.example.healthme.global.config.auth.oauth.userinfo.KakaoUserInfo;
import com.example.healthme.global.config.auth.oauth.userinfo.NaverUserInfo;
import com.example.healthme.global.config.auth.oauth.userinfo.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsOAuth2Service extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final @Lazy UserService userService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. 기본 OAuth2User 정보 가져오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String provider = userRequest.getClientRegistration().getRegistrationId();// "kakao", "google", "naver"
        Map<String,Object> attributes = oAuth2User.getAttributes();

        // 2. provider별로 OAuth2UserInfo 변환
        OAuth2UserInfo oAuth2UserInfo = null;
        //카카오 로그인시
        if(provider.startsWith("kakao")) {
            Long id = (Long)attributes.get("id");
            LocalDateTime connected_at = OffsetDateTime.parse(attributes.get("connected_at").toString()).toLocalDateTime();
            Map<String,Object> properties = (Map<String,Object>)attributes.get("properties");
            Map<String,Object> kakao_account = (Map<String,Object>) attributes.get("kakao_account");
            oAuth2UserInfo = new KakaoUserInfo(id,connected_at,properties,kakao_account);
        }
        //네이버 로그인시
        else if(provider.startsWith("naver")){
            Map<String,Object> response = (Map<String,Object>)attributes.get("response");
            String id = (String)response.get("id");
            oAuth2UserInfo = new NaverUserInfo(id,response);

        }
        // 구글 로그인 시
        else if(provider.startsWith("google")){
            String id = (String)attributes.get("sub");
            oAuth2UserInfo = new GoogleUserInfo(id,attributes);
        }

        // 3. 사용자 정보 조합
        String userid = provider + "_" + oAuth2UserInfo.getProviderId();

        Optional<User> userOptional =  userRepository.findByUserid(userid);
        UserDto userDto;

        if(userOptional.isEmpty()){
            // 서비스에 위임
            userDto = userService.createSocialUser(
                    userid,
                    oAuth2UserInfo.getName(),
                    provider,
                    oAuth2UserInfo.getProviderId()
            );
        }else{
            //기존 유저 존재(Dto)
            userDto = UserDto.fromEntity(userOptional.get());
        }


        // PrincipalDetails 반환
        return new PrincipalDetails(
                userDto,
                oAuth2User.getAttributes(),
                userRequest.getAccessToken().getTokenValue()
        );

    }
}
