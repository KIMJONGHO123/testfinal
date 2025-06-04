package com.example.healthme.domain.user.dto;

import com.example.healthme.domain.user.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;
    private String userid;
    private String username;
    private String role;
    private String provider;

    public static UserResponseDto fromEntity(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUserid(user.getUserid());
        dto.setUsername(user.getUsername());
        dto.setRole(user.getRole());
        dto.setProvider(user.getProvider());
        return dto;
    }
}
