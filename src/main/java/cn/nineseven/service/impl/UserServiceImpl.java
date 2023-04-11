package cn.nineseven.service.impl;

import cn.nineseven.constant.AppHttpCodeEnum;
import cn.nineseven.constant.SystemConstant;
import cn.nineseven.entity.Result;
import cn.nineseven.entity.dto.LoginUserDto;
import cn.nineseven.entity.dto.RegisterUserDto;
import cn.nineseven.entity.dto.UserInfoDto;
import cn.nineseven.entity.po.LoginUser;
import cn.nineseven.entity.po.User;
import cn.nineseven.entity.vo.LoginUserVo;
import cn.nineseven.entity.vo.UserInfoVo;
import cn.nineseven.mapper.UserMapper;
import cn.nineseven.service.UserService;
import cn.nineseven.utils.BeanCopyUtils;
import cn.nineseven.utils.JWTUtils;
import cn.nineseven.utils.SecurityUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * (User)表服务实现类
 *
 * @author makejava
 * @since 2023-04-10 21:06:14
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Result register(RegisterUserDto registerUserDto) {
        if(!registerUserDto.getPassword().equals(registerUserDto.getConfirm_password())){
            return Result.errorResult(AppHttpCodeEnum.DIFFERENT_PASSWORD);
        }

        User one = lambdaQuery().eq(User::getUsername, registerUserDto.getUsername()).one();
        if(one != null) return Result.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        String password = passwordEncoder.encode(registerUserDto.getPassword());

        User user = BeanCopyUtils.copyBean(registerUserDto, User.class);

        user.setPassword(password);

        boolean fl = save(user);
        if(!fl) {
            return Result.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
        }
        return Result.okResult();
    }

    @Override
    public Result login(LoginUserDto loginUserDto) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUserDto.getUsername(),loginUserDto.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        long userId = loginUser.getUser().getId();
        String jwt = JWTUtils.createJWT(userId);
        redisTemplate.opsForValue().set(SystemConstant.REDIS_LOGIN_USER + userId, loginUser);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        LoginUserVo vo = new LoginUserVo(jwt,userInfoVo);
        return Result.okResult(vo);
    }

    @Override
    public Result logout() {
        Long userId = SecurityUtils.getUserId();
        redisTemplate.delete(SystemConstant.REDIS_LOGIN_USER + userId);
        return Result.okResult();
    }

    @Override
    public Result update(UserInfoDto userInfoVo) {
        User user = BeanCopyUtils.copyBean(userInfoVo, User.class);
        updateById(user);
        return null;
    }

    @Override
    public Result getInfoById(Long id) {
        User user = getById(id);
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return Result.okResult(userInfoVo);
    }
}

