package cn.nineseven.controller;

import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.LoginUserDto;
import cn.nineseven.entity.dto.RegisterUserDto;
import cn.nineseven.entity.dto.UserInfoDto;
import cn.nineseven.entity.vo.UserInfoVo;
import cn.nineseven.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Api("用户相关接口")
public class UserController {

    @Autowired
    UserService userService;
    @PostMapping("/register")
    @ApiOperation("注册")
    public Result register(@RequestBody @Validated RegisterUserDto registerUserDto){
        return userService.register(registerUserDto);
    }

    @PostMapping("/login")
    @ApiOperation("登录")
    public Result login(@RequestBody @Validated LoginUserDto loginUserDto){
        return userService.login(loginUserDto);
    }

    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result logout(){
        return userService.logout();
    }

    @PutMapping("/update")
    @ApiOperation("修改个人信息")
    public Result update(@RequestBody UserInfoDto userInfoDto){
        return userService.update(userInfoDto);
    }

    @GetMapping("/info/{id}")
    @ApiOperation("获取用户信息")
    public Result getInfoById(@PathVariable("id") Long id){
        return userService.getInfoById(id);
    }

    @GetMapping("/list")
    @ApiOperation("用户信息列表")
    public Result getUserList(Integer pageNum, Integer pageSize,
                              @RequestParam(required = false) String name,
                              @RequestParam(required = false) Integer minAge,
                              @RequestParam(required = false) Integer maxAge,
                              @RequestParam(required = false) String gender){
        return userService.getUserList(pageNum, pageSize, name, minAge, maxAge, gender);
    }
}
