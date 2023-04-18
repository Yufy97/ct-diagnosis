package cn.nineseven.entity.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CtRecordDto {

    private Long userId;
    @NotNull
    private String imgId;
}
