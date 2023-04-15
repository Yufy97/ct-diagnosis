package cn.nineseven.controller;

import cn.nineseven.entity.Result;
import cn.nineseven.service.IssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/issue")
@Api("问题留言接口")
public class IssueController {

    @Autowired
    IssueService issueService;
    @GetMapping("/list")
    @ApiOperation("获取问题列表")
    @ApiImplicitParams(
            @ApiImplicitParam(name = "isReply", value = "是否回复，0未回复， 1已回复")
    )
    public Result list(Integer pageNum, Integer pageSize, @RequestParam(required = false) Integer isReply){
        return issueService.list(pageNum, pageSize, isReply);
    }
}
