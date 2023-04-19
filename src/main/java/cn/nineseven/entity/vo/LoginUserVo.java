package cn.nineseven.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginUserVo {
    private String ctToken;

    private UserInfoVo loginUserVo;
}
