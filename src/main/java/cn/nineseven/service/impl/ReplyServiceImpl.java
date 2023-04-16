package cn.nineseven.service.impl;

import cn.nineseven.entity.vo.ReplyVo;
import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.ReplyDto;
import cn.nineseven.entity.po.Reply;
import cn.nineseven.mapper.ReplyMapper;
import cn.nineseven.service.ReplyService;
import cn.nineseven.service.UserService;
import cn.nineseven.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * (Reply)表服务实现类
 *
 * @author makejava
 * @since 2023-04-15 12:44:09
 */
@Service("replyService")
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply> implements ReplyService {

    @Autowired
    UserService userService;
    @Override
    public Result list(Long issueId) {
        Reply reply = lambdaQuery().eq(Reply::getRootIssueId, issueId).one();
        Long replyUserId = reply.getReplyUserId();
        String nickname = userService.getById(replyUserId).getNickname();
        ReplyVo replyVo = BeanCopyUtils.copyBean(reply, ReplyVo.class);
        replyVo.setReplyNickname(nickname);
        return Result.okResult(replyVo);
    }

    @Override
    public Result save(ReplyDto replyDto) {
        Reply reply = BeanCopyUtils.copyBean(replyDto, Reply.class);
        save(reply);
        return Result.okResult();
    }
}

