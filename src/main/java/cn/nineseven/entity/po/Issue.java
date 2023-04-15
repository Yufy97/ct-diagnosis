package cn.nineseven.entity.po;

import java.util.Date;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Issue)表实体类
 *
 * @author makejava
 * @since 2023-04-14 20:47:15
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Issue {
    
    private Long id;
    
    private Long userId;
    
    private String content;
    
    private String url;
    
    private Integer isReply;
    
    private Integer isPublic;
    
    private Date createTime;
    
    private Date updateTime;
    
    private Integer isDel;

}

