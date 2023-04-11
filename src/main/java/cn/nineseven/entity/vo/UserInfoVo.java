package cn.nineseven.entity.vo;

import lombok.Data;

@Data
public class UserInfoVo {
    private Long id;

    private String username;

    private String nickname;

    private String gender;

    private Integer age;
}
