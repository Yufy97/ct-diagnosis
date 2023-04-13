package cn.nineseven.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginUserDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
}
