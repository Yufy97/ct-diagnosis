package cn.nineseven.service;

import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.ReplyDto;
import cn.nineseven.entity.po.Reply;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * (Reply)表服务接口
 *
 * @author makejava
 * @since 2023-04-15 12:44:09
 */
public interface ReplyService extends IService<Reply> {

    Result list(Long issueId);

    Result save(ReplyDto replyDto);
}

