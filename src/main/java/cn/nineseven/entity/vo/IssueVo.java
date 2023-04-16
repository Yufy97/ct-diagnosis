package cn.nineseven.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class IssueVo {
    private Long id;

    private Long userId;

    private String nickname;

    private String content;

    private String url;

    private Date createTime;
}
