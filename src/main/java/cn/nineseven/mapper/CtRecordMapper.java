package cn.nineseven.mapper;

import cn.nineseven.entity.po.CtRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;


/**
 * (CtRecord)表数据库访问层
 *
 * @author makejava
 * @since 2023-04-10 21:05:07
 */
public interface CtRecordMapper extends BaseMapper<CtRecord> {

    @Select("select  status as status, count(status) as count from ct_record  group by status;")
    List<Map<Integer, Integer>> dataAnalyseAll();

    @Select("select  status as status, count(status) as count from ct_record\n" +
            "left join user u on ct_record.user_id = u.id\n" +
            "where gender = #{gender}\n" +
            "group by status;")
    List<Map<Integer, Integer>> dataAnalyseByGender(@Param("gender") String gender);
}

