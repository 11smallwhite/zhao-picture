package com.zhao.zhaopicturebacked.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.zhao.zhaopicturebacked.domain.Space;
import com.zhao.zhaopicturebacked.domain.SpaceUser;
import com.zhao.zhaopicturebacked.domain.User;
import com.zhao.zhaopicturebacked.enums.CodeEnum;
import com.zhao.zhaopicturebacked.enums.SpaceRoleEnum;
import com.zhao.zhaopicturebacked.enums.SpaceTypeEnum;
import com.zhao.zhaopicturebacked.model.SpaceUserVO;
import com.zhao.zhaopicturebacked.model.SpaceVO;
import com.zhao.zhaopicturebacked.model.UserVO;
import com.zhao.zhaopicturebacked.request.DeleteRequest;
import com.zhao.zhaopicturebacked.request.space.SpaceQueryRequest;
import com.zhao.zhaopicturebacked.request.spaceuser.SpaceUserAddRequest;
import com.zhao.zhaopicturebacked.request.spaceuser.SpaceUserDeleteRequest;
import com.zhao.zhaopicturebacked.request.spaceuser.SpaceUserEditRequest;
import com.zhao.zhaopicturebacked.request.spaceuser.SpaceUserQueryRequest;
import com.zhao.zhaopicturebacked.service.SpaceService;
import com.zhao.zhaopicturebacked.service.SpaceUserService;
import com.zhao.zhaopicturebacked.mapper.SpaceUserMapper;
import com.zhao.zhaopicturebacked.service.UserService;
import com.zhao.zhaopicturebacked.utils.ThrowUtil;
import com.zhao.zhaopicturebacked.utils.TokenUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author Vip
* @description 针对表【space_user(空间用户关联)】的数据库操作Service实现
* @createDate 2025-12-06 16:33:48
*/
@Service
public class SpaceUserServiceImpl extends ServiceImpl<SpaceUserMapper, SpaceUser>
    implements SpaceUserService{

    // @Resource
    // private SpaceService spaceService;
    
    @Autowired
    @Lazy
    private SpaceService spaceService;
    
    @Resource
    private UserService userService;


    //添加成员到空间
    @Override
    public long addSpaceUser(SpaceUserAddRequest spaceUserAddRequest) {
        SpaceUser spaceUser = new SpaceUser();
        BeanUtils.copyProperties(spaceUserAddRequest,spaceUser);
        this.save(spaceUser);
        return spaceUser.getId();
    }

    //从空间移除成员（踢人）
    @Override
    public boolean deleteSpaceUser(SpaceUserDeleteRequest spaceUserDeleteRequest) {
        Long spaceId = spaceUserDeleteRequest.getSpaceId();
        Long userId = spaceUserDeleteRequest.getUserId();

        SpaceUserQueryRequest spaceUserQueryRequest = new SpaceUserQueryRequest();
        BeanUtils.copyProperties(spaceUserDeleteRequest, spaceUserQueryRequest);
        QueryWrapper<SpaceUser> spaceUserQueryWrapper = getSpaceUserQueryWrapper(spaceUserQueryRequest);
        boolean b = this.removeById(spaceUserQueryWrapper);
        if (!b){
            log.error("踢除人员失败");
            ThrowUtil.throwBusinessException(CodeEnum.SYSTEM_ERROR,"踢除人员失败");
        }
        return b;
    }

    //删除团队空间
    @Override
    public boolean deleteSpaceTeam(DeleteRequest deleteRequest) {
        Long id = deleteRequest.getId();
        SpaceUserQueryRequest spaceUserQueryRequest = new SpaceUserQueryRequest();
        BeanUtils.copyProperties(deleteRequest, spaceUserQueryRequest);
        QueryWrapper<SpaceUser> spaceUserQueryWrapper = getSpaceUserQueryWrapper(spaceUserQueryRequest);
        boolean remove = this.remove(spaceUserQueryWrapper);
        if (!remove){
            log.error("删除全部空间成员失败");
            ThrowUtil.throwBusinessException(CodeEnum.SYSTEM_ERROR,"删除全部空间成员失败");
        }
        SpaceQueryRequest spaceQueryRequest = new SpaceQueryRequest();
        BeanUtils.copyProperties(deleteRequest, spaceQueryRequest);
        QueryWrapper<Space> spaceQueryWrapper = spaceService.getSpaceQueryWrapper(spaceQueryRequest);
        boolean b = spaceService.remove(spaceQueryWrapper);
        if (!b){
            log.error("删除团队空间失败");
            ThrowUtil.throwBusinessException(CodeEnum.SYSTEM_ERROR,"删除团队空间失败");
        }
        return b;
    }

    //查询空间成员列表
    @Override
    public List<SpaceUserVO> selectSpaceUserVO(SpaceUserQueryRequest spaceUserQueryRequest) {
        QueryWrapper<SpaceUser> spaceUserQueryWrapper = this.getSpaceUserQueryWrapper(spaceUserQueryRequest);
        List<SpaceUser> spaceUserList = this.list(spaceUserQueryWrapper);
        List<SpaceUserVO> spaceUserVOList = listToVOList(spaceUserList);
        return spaceUserVOList;
    }


    @Override
    public SpaceUserVO getSpaceUserVO(SpaceUserQueryRequest spaceUserQueryRequest) {
        QueryWrapper<SpaceUser> spaceUserQueryWrapper = this.getSpaceUserQueryWrapper(spaceUserQueryRequest);
        SpaceUser spaceUser = this.getOne(spaceUserQueryWrapper);
        SpaceUserVO spaceUserVO = SpaceUserVO.objToVo(spaceUser);
        User user = userService.getById(spaceUserVO.getUserId());
        spaceUserVO.setUser(UserVO.objToVo(user));
        Space space = spaceService.getById(spaceUserVO.getSpaceId());
        spaceUserVO.setSpace(SpaceVO.objToVo(space));
        return spaceUserVO;
    }
    @Override
    public SpaceUser getSpaceUser(SpaceUserQueryRequest spaceUserQueryRequest) {
        QueryWrapper<SpaceUser> spaceUserQueryWrapper = this.getSpaceUserQueryWrapper(spaceUserQueryRequest);
        SpaceUser spaceUser = this.getOne(spaceUserQueryWrapper);

        return spaceUser;
    }

    @Override
    public boolean editSpaceUser(SpaceUserEditRequest spaceUserEditRequest) {
        SpaceUser spaceUser = new SpaceUser();
        BeanUtils.copyProperties(spaceUserEditRequest,spaceUser);
        boolean b = this.updateById(spaceUser);
        if (!b){
            log.error("设置角色权限失败");
            ThrowUtil.throwBusinessException(CodeEnum.SYSTEM_ERROR,"设置角色权限失败");
        }
        return b;
    }

    @Override
    public List<SpaceUserVO> selectMySpaceUser(HttpServletRequest request) {
        UserVO loginUserVO = TokenUtil.getLoginUserVOFromCookie(request);
        SpaceUserQueryRequest spaceUserQueryRequest = new SpaceUserQueryRequest();
        spaceUserQueryRequest.setUserId(loginUserVO.getId());
        QueryWrapper<SpaceUser> spaceUserQueryWrapper  = getSpaceUserQueryWrapper(spaceUserQueryRequest);
        List<SpaceUser> spaceUserList = this.list(spaceUserQueryWrapper);
        List<SpaceUserVO> spaceUserVOList = listToVOList(spaceUserList);
        return spaceUserVOList;
    }



    @Override
    public List<SpaceUserVO> listToVOList( List<SpaceUser> spaceUserList) {
        List<SpaceUserVO> spaceUserVOList = spaceUserList.stream().map(SpaceUserVO::objToVo).collect(Collectors.toList());
        //todo 这里的效率可以优化
        spaceUserVOList = spaceUserVOList.stream().map(spaceUserVO -> {
            Long userId = spaceUserVO.getUserId();
            User user = userService.getById(userId);
            spaceUserVO.setUser(UserVO.objToVo(user));
            Long spaceId = spaceUserVO.getSpaceId();
            Space space = spaceService.getById(spaceId);
            spaceUserVO.setSpace(SpaceVO.objToVo(space));
            return spaceUserVO;
        }).collect(Collectors.toList());
        return spaceUserVOList;
    }


    @Override
    public QueryWrapper<SpaceUser> getSpaceUserQueryWrapper(SpaceUserQueryRequest spaceUserQueryRequest) {
        Long id = spaceUserQueryRequest.getId();
        Long spaceId = spaceUserQueryRequest.getSpaceId();
        Long userId = spaceUserQueryRequest.getUserId();
        String spaceRole = spaceUserQueryRequest.getSpaceRole();
        QueryWrapper<SpaceUser> spaceUserQueryWrapper = new QueryWrapper<>();

        spaceUserQueryWrapper.eq(ObjUtil.isNotEmpty(id),"id",id);
        spaceUserQueryWrapper.eq(ObjUtil.isNotEmpty(spaceId),"spaceId",spaceId);
        spaceUserQueryWrapper.eq(ObjUtil.isNotEmpty(userId),"userId",userId);
        spaceUserQueryWrapper.eq(ObjUtil.isNotEmpty(spaceRole),"spaceRole",spaceRole);

        return spaceUserQueryWrapper ;
    }
    @Override
    public void validSpaceUserAdmin(Long userId,Long spaceId){
        SpaceUserQueryRequest spaceUserQueryRequest = new SpaceUserQueryRequest();
        spaceUserQueryRequest.setUserId(userId);
        spaceUserQueryRequest.setSpaceId(spaceId);
        QueryWrapper<SpaceUser> spaceUserQueryWrapper = this.getSpaceUserQueryWrapper(spaceUserQueryRequest);
        SpaceUser spaceUser = this.getOne(spaceUserQueryWrapper);
        if (spaceUser.getSpaceRole()!= SpaceRoleEnum.ADMIN.getValue()){
            log.warn("不是空间管理员无法添加成员");
            ThrowUtil.throwBusinessException(CodeEnum.NOT_AUTH,"不是空间管理员无法添加成员");
        }

    }

}


