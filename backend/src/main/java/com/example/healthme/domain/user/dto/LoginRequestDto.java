package com.example.healthme.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {
    @NotBlank (message = "아이디를 입력해주세요")
    private String userid;
    
    @NotBlank (message = "비밀번호를 입력해주세요")
    private String password;

}
