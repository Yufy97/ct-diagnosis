package cn.nineseven.controller;

import cn.nineseven.constant.AppHttpCodeEnum;
import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.CtRecordDto;
import cn.nineseven.entity.po.CtRecord;
import cn.nineseven.entity.po.Img;
import cn.nineseven.handler.exception.SystemException;
import cn.nineseven.service.CtRecordService;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/record")
public class CtRecordController {

    @Autowired
    CtRecordService ctRecordService;

    @GetMapping("/analyse")
    public Result analyse(String url){
        return ctRecordService.analyse(url);
    }

    @PostMapping("/saveRecord")
    public Result saveRecord(@RequestBody @Validated CtRecordDto ctRecordDto){
        return ctRecordService.saveRecord(ctRecordDto);
    }

    @GetMapping("/user/{userId}")
    public Result getByUserId(@PathVariable("userId") Long userId, Integer pageNum, Integer pageSize,
                              @RequestParam(defaultValue = "0") Integer timeSort){
        return ctRecordService.getByUserId(userId, pageNum, pageSize, timeSort);
    }

    @GetMapping("/record/{id}")
    public Result getById(@PathVariable Long id){
        return ctRecordService.selectById(id);
    }

    @DeleteMapping("/{id}")
    public Result remove(@PathVariable Long id){
        boolean fl = ctRecordService.removeById(id);
        if(!fl){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return Result.okResult();
    }
}
