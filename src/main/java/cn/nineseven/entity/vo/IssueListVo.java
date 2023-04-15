package cn.nineseven.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class IssueListVo {
    private Long id;

    private Long userId;

    private String nickname;

    private String content;

    private String url;

    private Integer isReply;

    private Integer isPublic;

    private Date createTime;
}
