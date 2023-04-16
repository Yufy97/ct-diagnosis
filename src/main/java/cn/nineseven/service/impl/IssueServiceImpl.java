package cn.nineseven.service.impl;

import cn.nineseven.constant.AppHttpCodeEnum;
import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.IssueDto;
import cn.nineseven.entity.po.Issue;
import cn.nineseven.entity.vo.IssueVo;
import cn.nineseven.entity.vo.PageVo;
import cn.nineseven.handler.exception.SystemException;
import cn.nineseven.mapper.IssueMapper;
import cn.nineseven.service.IssueService;
import cn.nineseven.service.UserService;
import cn.nineseven.utils.BeanCopyUtils;
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
    public Result list(Integer pageNum, Integer pageSize, Integer isReply, Integer isPublic) {
        IssueMapper mapper = getBaseMapper();
        List<IssueVo> issueVos = mapper.list((pageNum - 1) * pageSize, pageSize, isReply, isPublic);;
        return Result.okResult(new PageVo(issueVos, Integer.toUnsignedLong(issueVos.size())));
    }

    @Override
    public Result save(IssueDto issueDto) {
        Issue issue = BeanCopyUtils.copyBean(issueDto, Issue.class);
        boolean fl = save(issue);
        if(!fl){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return Result.okResult();
    }

    @Override
    public Result selectById(Long id) {
        Issue issue = getById(id);
        IssueVo issueVo = BeanCopyUtils.copyBean(issue, IssueVo.class);
        issueVo.setNickname(userService.getById(issueVo.getUserId()).getNickname());
        return Result.okResult(issueVo);
    }
}

