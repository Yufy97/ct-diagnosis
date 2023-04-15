package cn.nineseven.service;

import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.CtRecordDto;
import cn.nineseven.entity.po.CtRecord;
import cn.nineseven.entity.po.Img;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * (CtRecord)表服务接口
 *
 * @author makejava
 * @since 2023-04-10 21:05:10
 */
public interface CtRecordService extends IService<CtRecord> {

    Result analyse(String url);

    Result saveRecord(CtRecordDto ctRecordDto);

    Result getByUserId(Long userId, Integer pageNum, Integer pageSize, Integer timeSort);

    Result selectById(Long id);

    Result dataAnalyse(Integer option);


}

