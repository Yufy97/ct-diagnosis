package cn.nineseven.controller;

import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.ReplyDto;
import cn.nineseven.service.ReplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reply")
@Api(tags = "回答问题接口")
public class ReplyController {

    @Autowired
    ReplyService replyService;

    @GetMapping("/list/{id}")
    @ApiOperation("通过问题id获取回答")
    public Result getByIssueId(@PathVariable("id") Long issueId){
        return replyService.list(issueId);
    }

    @PostMapping
    @PreAuthorize("@ps.isAdmin()")
    @ApiOperation("回答问题")
    public Result reply(@RequestBody ReplyDto replyDto){
        return replyService.save(replyDto);
    }
}
