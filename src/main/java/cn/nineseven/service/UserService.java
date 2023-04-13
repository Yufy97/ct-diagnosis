package cn.nineseven.service;

import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.LoginUserDto;
import cn.nineseven.entity.dto.RegisterUserDto;
import cn.nineseven.entity.dto.UserInfoDto;
import cn.nineseven.entity.po.User;
import cn.nineseven.entity.vo.UserInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;


/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2023-04-10 21:06:13
 */
public interface UserService extends IService<User> {

    Result register(RegisterUserDto registerUserDto);

    Result login(LoginUserDto loginUserDto);

    Result logout();

    Result update(UserInfoDto userInfoVo);

    Result getInfoById(Long id);

    Result getUserList(Integer pageNum, Integer pageSize, String name, Integer minAge, Integer maxAge, String gender);
}

