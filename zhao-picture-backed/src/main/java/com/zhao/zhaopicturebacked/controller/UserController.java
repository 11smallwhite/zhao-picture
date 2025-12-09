package com.zhao.zhaopicturebacked.controller;


import cn.hutool.core.util.ObjUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhao.zhaopicturebacked.annotation.AuthType;
import com.zhao.zhaopicturebacked.constant.BaseResponse;
import com.zhao.zhaopicturebacked.constant.UserConstant;
import com.zhao.zhaopicturebacked.domain.User;
import com.zhao.zhaopicturebacked.enums.CodeEnum;
import com.zhao.zhaopicturebacked.model.UserVO;
import com.zhao.zhaopicturebacked.request.*;
import com.zhao.zhaopicturebacked.request.user.UserEditRequest;
import com.zhao.zhaopicturebacked.request.user.UserLoginRequest;
import com.zhao.zhaopicturebacked.request.user.UserQueryRequest;
import com.zhao.zhaopicturebacked.request.user.UserRegisterRequest;
import com.zhao.zhaopicturebacked.service.UserService;
import com.zhao.zhaopicturebacked.upload.AvatarUploadService;
import com.zhao.zhaopicturebacked.utils.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    @Resource
    private UserService userService;
    
    @Resource
    private AvatarUploadService avatarUploadService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;



    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){

        Long id = userService.userRegister(userRegisterRequest);
        log.info("用户注册成功：{}", id);
        return ResultUtil.success(id,"注册成功");
    }


    /**
     * 用户登录
     * @param userLoginRequest
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<UserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request, HttpServletResponse response){

        User user = userService.userLogin(userLoginRequest);
        UserVO loginUserVO = UserUtil.getUserVOByUser(user);
        
        // 使用Spring Session存储用户信息
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STORE, loginUserVO);

        log.info("用户登录成功：{}", loginUserVO);
        return ResultUtil.success(loginUserVO,"登录成功");
    }

    @GetMapping("/get")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request,HttpServletResponse response){
        // 从session中获取用户信息
        UserVO loginUserVO = (UserVO) request.getSession().getAttribute(UserConstant.USER_LOGIN_STORE);
        if (loginUserVO == null) {
            log.info("用户未登录");
            ThrowUtil.throwBusinessException(CodeEnum.NOT_AUTH,"用户未登录");
        }

        return ResultUtil.success(loginUserVO,"获取用户信息成功");
    }


    /**
     * 用户退出
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request,HttpServletResponse response){
        //删除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STORE);
        // 使session失效
        request.getSession().invalidate();
        return ResultUtil.success(true,"退出成功");
    }

    /**
     * 用户编辑
     * @param userEditRequest
     * @param request
     * @return
     */
    @PostMapping("/edit")
    @AuthType(userType = 0)
    public BaseResponse<UserVO> userEdit(@RequestBody UserEditRequest userEditRequest, HttpServletRequest request){

        //3.调用service层方法
        UserVO userVO = userService.userEdit(userEditRequest, request);
        return ResultUtil.success(userVO,"编辑成功");
    }
    
    /**
     * 上传用户头像
     * @param multipartFile 头像文件
     * @param request 请求上下文
     * @return 用户信息
     */
    @PostMapping("/avatar")
    @AuthType(userType = 0)
    public BaseResponse<UserVO> uploadAvatar(@RequestPart("file") MultipartFile multipartFile, HttpServletRequest request){
        // 从session中获取当前登录用户
        UserVO loginUserVO = (UserVO) request.getSession().getAttribute(UserConstant.USER_LOGIN_STORE);
        if (ObjUtil.hasNull(loginUserVO, multipartFile)){
            log.error("参数为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }
        
        // 上传头像并更新用户信息
        String avatarUrl = avatarUploadService.uploadAvatar(multipartFile, request);

        // 重新获取更新后的用户信息
        loginUserVO.setUserAvatar(avatarUrl);
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STORE, loginUserVO);
        return ResultUtil.success(loginUserVO, "头像上传成功");
    }


    /**
     * 分页查询用户----用户可用
     * @param userQueryRequest
     * @return
     */
    @PostMapping("/select")
    @AuthType(userType = 0)
    public BaseResponse<Page<UserVO>> select(@RequestBody UserQueryRequest userQueryRequest){

       
        //想要得到LoginUserVO的分页数据，就得先得到User的分页数据,也就是Page<User>,
        // 不能说 先得到List<User>，然后将其转换为List<LoginUserVO>,再转换为Page<LoginUserVO>，这样得到的Page<LoginUserVO>其实就是只有一页数据的Page，
        // 因为你new的Page对象，是没有进行过分页的，没有分页参数的
        // 如果你先通过分页查询得到Page<User>，那么你就可以这样子new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal())，这样才是真正的分页.
        Page<User> userPage = userService.selectPage(userQueryRequest);
        List<UserVO> userVOList = userPage.getRecords().stream().map(user -> UserUtil.getUserVOByUser(user)).collect(Collectors.toList());
        // 构造返回的分页对象
        Page<UserVO> userVOPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        userVOPage.setRecords(userVOList);
        return ResultUtil.success(userVOPage,"查询成功");
    }

    /**
     * 分页查询用户----管理员专用(废案，因为用户的密码会在网络间传输，不安全)
     * @param
     * @return
     */
//    @PostMapping("/page/select/query/admin")
//    @AuthType(userType = 1)
//    public BaseResponse<Page<User>> adminSelect(@RequestBody UserQueryRequest userQueryRequest){
//        Long id = userQueryRequest.getId();
//        String userAccount = userQueryRequest.getUserAccount();
//        String userName = userQueryRequest.getUserName();
//        String userIntroduction = userQueryRequest.getUserIntroduction();
//        Integer pageNum = userQueryRequest.getPageNum();
//        Integer pageSize = userQueryRequest.getPageSize();
//        String sortField = userQueryRequest.getSortField();
//
//        String sortOrder = userQueryRequest.getSortOrder();
//
//        //想要得到LoginUserVO的分页数据，就得先得到User的分页数据,也就是Page<User>,
//        // 不能说 先得到List<User>，然后将其转换为List<LoginUserVO>,再转换为Page<LoginUserVO>，这样得到的Page<LoginUserVO>其实就是只有一页数据的Page，
//        // 因为你new的Page对象，是没有进行过分页的，没有分页参数的
//        // 如果你先通过分页查询得到Page<User>，那么你就可以这样子new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal())，这样才是真正的分页.
//        Page<User> userPage = userService.selectPage(id, userAccount, userName, userIntroduction, sortField, sortOrder, pageNum, pageSize);
//
//        return ResultUtil.success(userPage,"查询成功");
//    }


    @PostMapping("/delete")
    @AuthType(userType = 1)
    public BaseResponse<Long> userDeleteById(@RequestBody DeleteRequest deleteRequest){

        Long delete = userService.userDelete(deleteRequest);
        return ResultUtil.success(delete,"删除成功");
    }

}