package com.zhao.zhaopicturebacked.auth;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;
import com.zhao.zhaopicturebacked.auth.model.SpaceUserAuthConfig;
import com.zhao.zhaopicturebacked.auth.model.SpaceUserRole;
import com.zhao.zhaopicturebacked.enums.CodeEnum;
import com.zhao.zhaopicturebacked.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Component
@Slf4j
public class SpaceUserAuthManage {



    public static final SpaceUserAuthConfig SPACE_USER_AUTH_CONFIG;

    static {
        String spaceUserAuthJson = ResourceUtil.readUtf8Str("biz/SpaceUserAuthConfig.json");
        SPACE_USER_AUTH_CONFIG = JSONUtil.toBean(spaceUserAuthJson, SpaceUserAuthConfig.class);
    }


    public List<String> getPermissionByRole(String spaceUserRole){
        //校验参数
        if(spaceUserRole==null){
            log.error("spaceUserRole为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }
        SpaceUserRole r = SPACE_USER_AUTH_CONFIG.getRoles().stream()
                .filter(role -> {
                    return spaceUserRole.equals(role.getKey());
                })
                .findFirst()
                .orElse(null);
        if (r==null){
            return new ArrayList<>();
        }
        return r.getPermissions();
    }

}
