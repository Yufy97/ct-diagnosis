package cn.nineseven.mapper;

import cn.nineseven.entity.po.Issue;
import cn.nineseven.entity.vo.IssueListVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * (Issue)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-14 16:36:01
 */
public interface IssueMapper extends BaseMapper<Issue> {

    @Select("SELECT id,user_id,content,url,is_reply,is_public,create_time,update_time,is_del FROM issue WHERE is_del=0 ORDER BY create_time DESC LIMIT ?,?")
    List<IssueListVo> list(Integer pageNum, Integer pageSize, Integer isReply);

    List<IssueListVo> list(Integer pageNum, Integer pageSize);
}

