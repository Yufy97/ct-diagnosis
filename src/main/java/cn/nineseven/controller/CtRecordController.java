package cn.nineseven.controller;

import cn.nineseven.constant.AppHttpCodeEnum;
import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.CtRecordDto;
import cn.nineseven.handler.exception.SystemException;
import cn.nineseven.service.CtRecordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/record")
@Api(tags = "ct记录接口")
public class CtRecordController {

    @Autowired
    CtRecordService ctRecordService;

    @GetMapping("/analyse")
    @ApiOperation("ct分析")
    @ApiImplicitParams(@ApiImplicitParam(name = "url", value = "图片url"))
    public Result analyse(String url){
        return ctRecordService.analyse(url);
    }

    @PostMapping("/saveRecord")
    @ApiOperation("保存分析记录")
    public Result saveRecord(@RequestBody @Validated CtRecordDto ctRecordDto){
        return ctRecordService.saveRecord(ctRecordDto);
    }

    @GetMapping("/user/{userId}")
    @ApiOperation("用户id查询用户的ct分析记录")
    @ApiImplicitParams(@ApiImplicitParam(name = "timeSort", value = "按创建时间排序， 1是升序， 2是降序(非必要参数)"))
    public Result getByUserId(@PathVariable("userId") Long userId, Integer pageNum, Integer pageSize,
                              @RequestParam(defaultValue = "0") Integer timeSort){
        return ctRecordService.getByUserId(userId, pageNum, pageSize, timeSort);
    }

    @GetMapping("/record/{id}")
    @ApiOperation("id查询ct记录")
    public Result getById(@PathVariable Long id){
        return ctRecordService.selectById(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("id删除ct分析记录")
    public Result remove(@PathVariable Long id){
        boolean fl = ctRecordService.removeById(id);
        if(!fl){
            throw new SystemException(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return Result.okResult();
    }

    @GetMapping("/data")
    @ApiOperation(value = "数据统计",notes = "对用户上传的ct分析出来的status进行统计")
    @ApiImplicitParams(@ApiImplicitParam(name = "option",value = "选项,1是所有用户,2是男用户，3是女用户"))
    @PreAuthorize("@ps.isAdmin()")
    public Result dataAnalyse(Integer option){
        return ctRecordService.dataAnalyse(option);
    }
}
