package cn.nineseven.entity.dto;

import lombok.Data;

@Data
public class UserInfoDto {

    private Long id;

    private String nickname;

    private String gender;

    private Integer age;
}
