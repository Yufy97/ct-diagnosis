package cn.nineseven.entity.dto;

import lombok.Data;

@Data
public class ReplyDto {

    private Long userId;

    private String content;

    private Long rootIssueId;

    private Long replyUserId;
}
