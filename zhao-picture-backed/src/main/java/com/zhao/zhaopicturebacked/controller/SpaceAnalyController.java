package com.zhao.zhaopicturebacked.controller;

import cn.hutool.core.util.ObjUtil;
import com.zhao.zhaopicturebacked.constant.BaseResponse;
import com.zhao.zhaopicturebacked.domain.Space;
import com.zhao.zhaopicturebacked.enums.CodeEnum;
import com.zhao.zhaopicturebacked.request.space.SpaceAnalyRequest;
import com.zhao.zhaopicturebacked.request.space.SpaceRankAnalyzeRequest;
import com.zhao.zhaopicturebacked.request.space.SpaceUserAnalyzeRequest;
import com.zhao.zhaopicturebacked.response.*;
import com.zhao.zhaopicturebacked.service.impl.SpaceAnalyServiceImpl;
import com.zhao.zhaopicturebacked.utils.ResultUtil;
import com.zhao.zhaopicturebacked.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/space/analy")
public class SpaceAnalyController {

    @Resource
    private SpaceAnalyServiceImpl spaceAnalyService;

    @PostMapping("/usage")
    public BaseResponse<SpaceAnalyUsageResponse> getSpaceAnaly(@RequestBody SpaceAnalyRequest spaceAnalyRequest, HttpServletRequest httpServletRequest){
        //第一层，校验参数是否为空
        if (ObjUtil.hasNull(spaceAnalyRequest,httpServletRequest)){
            log.warn("参数为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }

        SpaceAnalyUsageResponse spaceAnaly = spaceAnalyService.getSpaceAnaly(spaceAnalyRequest, httpServletRequest);
        return ResultUtil.success(spaceAnaly);
    }

    @PostMapping("/category")
    public BaseResponse<List<SpaceAnalyCategoryResponse>> getSpaceAnalyCategory(@RequestBody SpaceAnalyRequest spaceAnalyRequest
            , HttpServletRequest request){
        //第一层，校验参数是否为空
        if(ObjUtil.hasNull(spaceAnalyRequest,request)){
            log.warn("参数为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }
        List<SpaceAnalyCategoryResponse> spaceAnalyCategory = spaceAnalyService.getSpaceAnalyCategory(spaceAnalyRequest, request);
        return ResultUtil.success(spaceAnalyCategory);
    }

    @PostMapping("/tag")
    public BaseResponse<List<SpaceAnalyTagResponse>> getSpaceAnalyTag(@RequestBody SpaceAnalyRequest spaceAnalyRequest
            , HttpServletRequest request){
        //第一层，校验参数是否为空
        if(ObjUtil.hasNull(spaceAnalyRequest,request)){
            log.warn("参数为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }
        List<SpaceAnalyTagResponse> spaceAnalyTagResponses = spaceAnalyService.getSpaceAnalyTag(spaceAnalyRequest, request);
        return ResultUtil.success(spaceAnalyTagResponses);
    }


    @PostMapping("/size")
    public BaseResponse<List<SpaceAnalySizeResponse>> getSpaceAnalySize(@RequestBody SpaceAnalyRequest spaceAnalyRequest
            , HttpServletRequest request){
        //第一层，校验参数是否为空
        if(ObjUtil.hasNull(spaceAnalyRequest,request)){
            log.warn("参数为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }
        List<SpaceAnalySizeResponse> spaceAnalySize = spaceAnalyService.getSpaceAnalySize(spaceAnalyRequest, request);
        return ResultUtil.success(spaceAnalySize);
    }

    @PostMapping("/upload")
    public BaseResponse<List<SpaceAnalyUploadResponse>> getSpaceAnalyUpload(@RequestBody SpaceUserAnalyzeRequest spaceAnalyRequest
            , HttpServletRequest request){
        //第一层，校验参数是否为空
        if(ObjUtil.hasNull(spaceAnalyRequest.getTimeDimension(),request,spaceAnalyRequest.getUserId())){
            log.warn("参数为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }
        List<SpaceAnalyUploadResponse> spaceAnalyUpload = spaceAnalyService.getSpaceAnalyUpload(spaceAnalyRequest, request);
        return ResultUtil.success(spaceAnalyUpload);

    }

    @PostMapping("/used")
    public BaseResponse<List<Space>> getSpaceAnalyUsed(@RequestBody SpaceRankAnalyzeRequest spaceAnalyRequest
            , HttpServletRequest request){
        //第一层，校验参数是否为空
        if(ObjUtil.hasNull(spaceAnalyRequest,request)){
            log.warn("参数为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"参数为空");
        }

        List<Space> spaceAnalyRank = spaceAnalyService.getSpaceAnalyRank(spaceAnalyRequest, request);
        return ResultUtil.success(spaceAnalyRank);

    }

}
