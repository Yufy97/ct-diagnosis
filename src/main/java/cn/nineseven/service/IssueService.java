package cn.nineseven.service;

import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.IssueDto;
import cn.nineseven.entity.po.Issue;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * (Issue)表服务接口
 *
 * @author makejava
 * @since 2023-04-14 16:36:01
 */
public interface IssueService extends IService<Issue> {

    Result list(Integer pageNum, Integer pageSize, Integer isReply, Integer isPublic);

    Result save(IssueDto issueDto);

    Result selectById(Long id);

    Result selectByUserId(Long id);
}

