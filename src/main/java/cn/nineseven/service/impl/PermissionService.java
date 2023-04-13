package cn.nineseven.service.impl;


import cn.nineseven.utils.SecurityUtils;
import org.springframework.stereotype.Service;

@Service("ps")
public class PermissionService {
    public boolean isAdmin(){
        return SecurityUtils.getLoginUser().isPerms();
    }
}