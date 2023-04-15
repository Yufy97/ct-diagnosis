package cn.nineseven.service.impl;

import cn.nineseven.entity.Result;
import cn.nineseven.entity.po.Issue;
import cn.nineseven.entity.po.User;
import cn.nineseven.entity.vo.IssueListVo;
import cn.nineseven.mapper.IssueMapper;
import cn.nineseven.service.IssueService;
import cn.nineseven.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * (Issue)表服务实现类
 *
 * @author makejava
 * @since 2023-04-14 16:36:01
 */
@Service("issueService")
public class IssueServiceImpl extends ServiceImpl<IssueMapper, Issue> implements IssueService {

    @Autowired
    UserService userService;
    @Override
    public Result list(Integer pageNum, Integer pageSize, Integer isReply) {
        IssueMapper mapper = getBaseMapper();
        List<IssueListVo> issueListVos = null;
        if(isReply == null) issueListVos = mapper.list(pageNum, pageSize, isReply);
        else issueListVos = mapper.list(pageNum, pageSize);
        return Result.okResult(issueListVos);
    }
}

