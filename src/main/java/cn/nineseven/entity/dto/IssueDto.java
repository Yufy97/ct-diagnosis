package cn.nineseven.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class IssueDto {


    @NotNull
    private Long userId;

    @NotNull
    private String content;

    private String url;

    @NotNull
    private Integer isReply;

    @NotNull
    private Integer isPublic;
}
