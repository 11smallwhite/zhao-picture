package com.zhao.zhaopicturebacked.controller;

import cn.hutool.core.util.ObjUtil;
import com.zhao.zhaopicturebacked.annotation.AuthType;
import com.zhao.zhaopicturebacked.constant.BaseResponse;
import com.zhao.zhaopicturebacked.constant.UserConstant;
import com.zhao.zhaopicturebacked.domain.SpaceUser;
import com.zhao.zhaopicturebacked.enums.CodeEnum;
import com.zhao.zhaopicturebacked.model.SpaceUserVO;
import com.zhao.zhaopicturebacked.model.UserVO;
import com.zhao.zhaopicturebacked.request.spaceuser.SpaceUserAddRequest;
import com.zhao.zhaopicturebacked.request.spaceuser.SpaceUserDeleteRequest;
import com.zhao.zhaopicturebacked.request.spaceuser.SpaceUserEditRequest;
import com.zhao.zhaopicturebacked.request.spaceuser.SpaceUserQueryRequest;
import com.zhao.zhaopicturebacked.service.SpaceUserService;
import com.zhao.zhaopicturebacked.service.UserService;
import com.zhao.zhaopicturebacked.utils.ResultUtil;
import com.zhao.zhaopicturebacked.utils.ThrowUtil;
import com.zhao.zhaopicturebacked.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/spaceUser")
@Slf4j
public class SpaceUserController {

    @Resource
    private SpaceUserService spaceUserService;

    @Resource
    private UserService userService;

    /**
     * 添加成员到空间
     */
    @PostMapping("/add")
    public BaseResponse<Long> addSpaceUser(@RequestBody SpaceUserAddRequest spaceUserAddRequest, HttpServletRequest request) {
        //校验参数
        if(ObjUtil.hasNull(spaceUserAddRequest.getSpaceId(),spaceUserAddRequest.getUserId(),spaceUserAddRequest.getSpaceRole())){
            log.error("sapceId或userId为空或空间身份为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }
        //校验是否是空间管理员
        UserVO loginUserVO = TokenUtil.getLoginUserVOFromCookie(request);
        Long userId = loginUserVO.getId();
        Long spaceId = spaceUserAddRequest.getSpaceId();
        spaceUserService.validSpaceUserAdmin(userId,spaceId);
        //数据库处理
        long l = spaceUserService.addSpaceUser(spaceUserAddRequest);
        return ResultUtil.success(l);


    }

    /**
     * 从空间移除成员（踢人）
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteSpaceUser(@RequestBody SpaceUserDeleteRequest spaceUserDeleteRequest,
                                                 HttpServletRequest request) {
        //校验参数
        if (ObjUtil.hasNull(spaceUserDeleteRequest.getSpaceId(),spaceUserDeleteRequest.getUserId())){
            log.error("spaceId或UserId为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }
        //校验是否是空间管理员
        UserVO loginUserVO = TokenUtil.getLoginUserVOFromCookie(request);
        Long userId = loginUserVO.getId();
        Long spaceId = spaceUserDeleteRequest.getSpaceId();
        spaceUserService.validSpaceUserAdmin(userId,spaceId);
        //数据库操作
        boolean b = spaceUserService.deleteSpaceUser(spaceUserDeleteRequest);
        return ResultUtil.success(b);
    }

    /**
     * 查询某个成员在某个空间的信息
     */
    @PostMapping("/get")
    public BaseResponse<SpaceUser> getSpaceUser(@RequestBody SpaceUserQueryRequest spaceUserQueryRequest,
                                                HttpServletRequest request) {
        //校验参数
        if (ObjUtil.hasNull(spaceUserQueryRequest.getSpaceId(),spaceUserQueryRequest.getUserId())){
            log.error("spaceId或UserId为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }
        //校验是否是空间管理员
        UserVO loginUserVO = TokenUtil.getLoginUserVOFromCookie(request);
        Long userId = loginUserVO.getId();
        Long spaceId = spaceUserQueryRequest.getSpaceId();
        spaceUserService.validSpaceUserAdmin(userId,spaceId);
        //数据库操作
        SpaceUser spaceUser = spaceUserService.getSpaceUser(spaceUserQueryRequest);
        return ResultUtil.success(spaceUser);
    }

    /**
     * 查询成员信息列表
     */
    @PostMapping("/list")
    public BaseResponse<List<SpaceUserVO>> listSpaceUser(@RequestBody SpaceUserQueryRequest spaceUserQueryRequest,
                                                         HttpServletRequest request) {
        //校验参数
        if (spaceUserQueryRequest == null){
            log.error("spaceUserQueryRequest为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }
        //校验是否是空间管理员
        UserVO loginUserVO = TokenUtil.getLoginUserVOFromCookie(request);
        Long userId = loginUserVO.getId();
        Long spaceId = spaceUserQueryRequest.getSpaceId();
        spaceUserService.validSpaceUserAdmin(userId,spaceId);
        //数据库操作
        List<SpaceUserVO> spaceUserVOList = spaceUserService.selectSpaceUserVO(spaceUserQueryRequest);
        return ResultUtil.success(spaceUserVOList);


    }

    /**
     * 编辑成员信息（设置权限）
     */
    @PostMapping("/edit")
    public BaseResponse<Boolean> editSpaceUser(@RequestBody SpaceUserEditRequest spaceUserEditRequest,
                                               HttpServletRequest request) {
        //校验参数
        if (ObjUtil.hasNull(spaceUserEditRequest.getSpaceRole(),spaceUserEditRequest.getUserId(),spaceUserEditRequest.getSpaceId())){
            log.error("spaceRole或userId或spaceId为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }
        //校验是否是空间管理员
        UserVO loginUserVO = TokenUtil.getLoginUserVOFromCookie(request);
        Long userId = loginUserVO.getId();
        Long spaceId = spaceUserEditRequest.getSpaceId();
        spaceUserService.validSpaceUserAdmin(userId,spaceId);
        //数据库操作
        boolean b = spaceUserService.editSpaceUser(spaceUserEditRequest);
        return ResultUtil.success(b);

    }

    /**
     * 查询我加入的团队空间列表
     */
    @PostMapping("/list/my")
    @AuthType(userType = UserConstant.USER)
    public BaseResponse<List<SpaceUserVO>> listMyTeamSpace(HttpServletRequest request) {
        List<SpaceUserVO> spaceUserVOList = spaceUserService.selectMySpaceUser(request);
        return ResultUtil.success(spaceUserVOList);
    }
}
