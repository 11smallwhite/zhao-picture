package com.zhao.zhaopicturebacked.controller;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zhao.zhaopicturebacked.annotation.AuthType;
import com.zhao.zhaopicturebacked.constant.BaseResponse;
import com.zhao.zhaopicturebacked.constant.UserConstant;
import com.zhao.zhaopicturebacked.domain.Space;
import com.zhao.zhaopicturebacked.enums.CodeEnum;
import com.zhao.zhaopicturebacked.enums.SpaceLevelEnum;
import com.zhao.zhaopicturebacked.model.SpaceLevel;
import com.zhao.zhaopicturebacked.model.SpaceVO;
import com.zhao.zhaopicturebacked.request.DeleteRequest;
import com.zhao.zhaopicturebacked.request.space.SpaceAddRequest;
import com.zhao.zhaopicturebacked.request.space.SpaceEditRequest;
import com.zhao.zhaopicturebacked.request.space.SpaceQueryRequest;
import com.zhao.zhaopicturebacked.service.SpaceService;
import com.zhao.zhaopicturebacked.utils.ResultUtil;
import com.zhao.zhaopicturebacked.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/space")
@ResponseBody
@Controller
@Slf4j
public class SpaceController {

    @Resource
    private SpaceService spaceService;


    @AuthType(userType = UserConstant.USER)
    @PostMapping("/add")
    public BaseResponse<Long> addSpace(@RequestBody SpaceAddRequest spaceAddRequest, HttpServletRequest  request){

        Long b = spaceService.addSpace(spaceAddRequest, request);
        return ResultUtil.success(b);
    }

    @AuthType(userType = UserConstant.USER)
    @PostMapping("/edit")
    public BaseResponse<Boolean> editSpace(@RequestBody SpaceEditRequest spaceEditRequest, HttpServletRequest  request){
        //校验前端传的参数是否为空
        if (spaceEditRequest==null){
            log.warn("前端传来的参数为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }
        Boolean b = spaceService.editSpace(spaceEditRequest, request);
        return ResultUtil.success(b);
    }
    @AuthType(userType = UserConstant.USER)
    @PostMapping("/delete")
    //todo 删除空间也要把空间对应的图片删除掉
    public BaseResponse<Long> deleteSpace(@RequestBody DeleteRequest deleteRequest,HttpServletRequest request){
        if(deleteRequest==null){
            log.warn("前端传来的参数为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }
        Long l = spaceService.deleteSpaceById(deleteRequest, request);
        return ResultUtil.success(l);
    }

    @AuthType(userType = UserConstant.ADMIN)
    @PostMapping("/select/admin")
    public BaseResponse<Page<Space>> selectSpace(@RequestBody SpaceQueryRequest spaceQueryRequest){
        Page<Space> spacePage = spaceService.selectSpace(spaceQueryRequest);

        return ResultUtil.success(spacePage);
    }

    @AuthType(userType = UserConstant.USER)
    @PostMapping("/get/spaceVO")
    public BaseResponse<SpaceVO> getSpaceVO(SpaceQueryRequest spaceQueryRequest){
        Long userId = spaceQueryRequest.getUserId();
        Long id = spaceQueryRequest.getId();
        QueryWrapper<Space> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjUtil.isNotEmpty(userId),"user_id",userId);
        queryWrapper.eq(ObjUtil.isNotEmpty(id),"id",id);
        Space space = spaceService.getOne(queryWrapper);
        SpaceVO spaceVO = SpaceVO.objToVo(space);
        return ResultUtil.success(spaceVO);
    }


    @GetMapping("/list/level")
    public BaseResponse<List<SpaceLevel>> listSpaceLevel(){
        List<SpaceLevel> spaceLevels = Arrays.stream(SpaceLevelEnum.values())
                .map(spaceLevelEnum -> {
                    long maxSize = spaceLevelEnum.getMaxSize();
                    String levelName = spaceLevelEnum.getLevelName();
                    long maxCount = spaceLevelEnum.getMaxCount();
                    int code = spaceLevelEnum.getCode();
                    return new SpaceLevel(code, levelName, maxCount, maxSize);
                }).collect(Collectors.toList());

        return ResultUtil.success(spaceLevels);
    }
}
