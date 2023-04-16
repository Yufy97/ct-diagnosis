package cn.nineseven.entity.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ReplyVo {
    private Long id;

    private Long userId;

    private String nickname;

    private Long replyUserId;

    private String replyNickname;

    private String content;

    private Date updateTime;
}
