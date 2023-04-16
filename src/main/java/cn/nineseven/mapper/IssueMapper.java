package cn.nineseven.mapper;

import cn.nineseven.entity.po.Issue;
import cn.nineseven.entity.vo.IssueVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * (Issue)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-14 16:36:01
 */
public interface IssueMapper extends BaseMapper<Issue> {

    @Select("<script>SELECT issue.id, issue.user_id,u.nickname, content,url, issue.create_time,issue.update_time FROM issue\n" +
            "left join user u on u.id = issue.user_id\n" +
            "WHERE issue.is_del=0 " +
            "<if test=\"isReply != null\"> AND issue.is_reply = #{isReply} </if>" +
            "<if test=\"isPublic != null\"> AND issue.is_public = #{isPublic} </if>\n" +
            "ORDER BY issue.create_time DESC LIMIT #{pageNum}, #{pageSize} </script>")
    List<IssueVo> list(@Param("pageNum") Integer pageNum, @Param("pageSize") Integer pageSize,
                       @Param("isReply") Integer isReply, @Param("isPublic") Integer isPublic);
}

