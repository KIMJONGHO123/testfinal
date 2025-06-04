package com.example.healthme.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequestDto {
    @NotBlank(message = "아이디를 입력해주세요.")
    @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하로 입력해주세요.")
    private String userid;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    @Size(min = 6, max = 30, message = "비밀번호는 6자 이상 30자 이하로 입력해주세요.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String password2;

    @NotBlank(message = "이름을 입력해주세요.")
    @Size(min = 2, message = "이름은 2자 이상 입력해주세요.")
    private String username;

    @NotBlank(message = "우편번호를 입력해주세요.")
    private String zip;

    @NotBlank(message = "주소를 입력해주세요.")
    private String address;

    @NotBlank(message = "상세주소를 입력해주세요.")
    @Size(max = 100, message = "상세 주소는 100자 이하로 입력해주세요.")
    private String addressDetail;

    @NotBlank(message = "성별을 입력해주세요.")
    private String gender;
    // 전화번호는 숫자만 허용 (010, 011 등)
    private String tel1;
    @Pattern(regexp = "\\d+", message = "전화번호는 숫자만 입력해주세요.")
    @Size(min = 4, max = 4, message = "전화번호는 숫자 4자리여야 합니다")
    private String tel2;
    @Pattern(regexp = "\\d+", message = "전화번호는 숫자만 입력해주세요.")
    @Size(min = 4, max = 4, message = "전화번호는 숫자 4자리여야 합니다")
    private String tel3;
}
