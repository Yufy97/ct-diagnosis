package cn.nineseven.entity.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class CtAnalyseVo {

    private String nickname;

    private String gender;

    private Integer age;

    private Integer status;

    private String analyse;

    private Integer isAnalysing;

    private String forecast;

    private String url;

    private LocalDateTime createTime;
}
