package cn.nineseven.entity.dto;


import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterUserDto {
    @NotNull
    private String username;
    @NotNull
    private String password;
    @NotNull
    private String confirm_password;
    @NotNull
    private String nickname;
    @NotNull
    private String gender;
    @NotNull
    private Integer age;
}
