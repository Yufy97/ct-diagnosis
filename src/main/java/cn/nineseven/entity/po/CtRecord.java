package cn.nineseven.entity.po;

import java.util.Date;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (CtRecord)表实体类
 *
 * @author makejava
 * @since 2023-04-10 21:05:09
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CtRecord {
    
    private Long id;
    
    private Long userId;
    
    private String imgId;
    
    private Integer status;
    
    private String description;
    
    private String forecast;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    
    private Integer isDel;

}

