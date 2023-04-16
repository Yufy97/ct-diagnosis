package cn.nineseven.controller;

import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.ReplyDto;
import cn.nineseven.service.ReplyService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reply")
public class ReplyController {

    @Autowired
    ReplyService replyService;

    @GetMapping("/list/{id}")
    @ApiOperation("获取某一问题下的回复")
    public Result getByIssueId(@PathVariable("id") Long issueId){
        return replyService.list(issueId);
    }

    @PostMapping
    @ApiOperation("回复问题")
    public Result reply(@RequestBody ReplyDto replyDto){
        return replyService.save(replyDto);
    }
}
