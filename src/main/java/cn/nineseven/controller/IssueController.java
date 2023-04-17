package cn.nineseven.controller;

import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.IssueDto;
import cn.nineseven.entity.po.Issue;
import cn.nineseven.service.IssueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/issue")
@Api(tags = "问题留言接口")
public class IssueController {

    @Autowired
    IssueService issueService;
    @GetMapping("/list")
    @ApiOperation("获取问题列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "isReply", value = "是否回复，0未回复， 1已回复(非必要参数)"),
                        @ApiImplicitParam(name = "isPublic", value = "是否公开，0不公开， 1公开(非必要参数)")})
    public Result list(Integer pageNum, Integer pageSize,
                       @RequestParam(required = false) Integer isReply,
                       @RequestParam(required = false) Integer isPublic){
        return issueService.list(pageNum, pageSize, isReply, isPublic);
    }

    @PostMapping("/save")
    @ApiOperation("发布提问")
    public Result addIssue(@RequestBody @Validated IssueDto issueDto){
        return issueService.save(issueDto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除提问")
    public Result remove(@PathVariable("id") Long id){
        issueService.removeById(id);
        return Result.okResult();
    }

    @PutMapping("/{id}")
    @ApiOperation("修改提问")
    public Result updateById(@RequestBody Issue issue){
        issueService.updateById(issue);
        return Result.okResult();
    }

    @GetMapping("/get/{id}")
    @ApiOperation("id获取问题")
    public Result getById(@PathVariable("id") Long id){
        return issueService.selectById(id);
    }
}
