package cn.nineseven.filter;

import cn.hutool.core.util.StrUtil;
import cn.nineseven.constant.AppHttpCodeEnum;
import cn.nineseven.constant.SystemConstant;
import cn.nineseven.entity.Result;
import cn.nineseven.entity.po.LoginUser;
import cn.nineseven.utils.JWTUtils;
import cn.nineseven.utils.WebUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    RedisTemplate redisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("token");
        if(StrUtil.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        Long userId = null;
        try {
             userId = JWTUtils.getSubject(token);
        } catch (Exception e) {
            e.printStackTrace();
            WebUtils.renderString(response, JSON.toJSONString(Result.errorResult(AppHttpCodeEnum.NEED_LOGIN)));
            return;
        }

        LoginUser loginUser = (LoginUser) redisTemplate.opsForValue().get(SystemConstant.REDIS_LOGIN_USER + userId);

        if(loginUser == null){
            WebUtils.renderString(response, JSON.toJSONString(Result.errorResult(AppHttpCodeEnum.NEED_LOGIN)));
            return;
        }

        redisTemplate.expire(SystemConstant.REDIS_LOGIN_USER + userId, 1, TimeUnit.DAYS );
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginUser,null,null);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

}
