package com.zhao.zhaopicturebacked.service.impl;

import cn.hutool.core.util.NumberUtil;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zhao.zhaopicturebacked.constant.UserConstant;
import com.zhao.zhaopicturebacked.domain.Picture;
import com.zhao.zhaopicturebacked.domain.Space;
import com.zhao.zhaopicturebacked.enums.CodeEnum;
import com.zhao.zhaopicturebacked.exception.BusinessException;
import com.zhao.zhaopicturebacked.model.UserVO;
import com.zhao.zhaopicturebacked.request.space.SpaceAnalyRequest;
import com.zhao.zhaopicturebacked.request.space.SpaceRankAnalyzeRequest;
import com.zhao.zhaopicturebacked.request.space.SpaceUserAnalyzeRequest;
import com.zhao.zhaopicturebacked.response.*;
import com.zhao.zhaopicturebacked.service.PictureService;
import com.zhao.zhaopicturebacked.service.SpaceService;
import com.zhao.zhaopicturebacked.utils.ThrowUtil;
import com.zhao.zhaopicturebacked.utils.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
@Slf4j
public class SpaceAnalyServiceImpl {

    @Resource
    private PictureService pictureService;
    @Resource
    private SpaceService spaceService;

    //获取空间使用情况
    public SpaceAnalyUsageResponse getSpaceAnaly(SpaceAnalyRequest spaceAnalyRequest, HttpServletRequest request){
        //第二层，校验权限
        validSpaceAnaly(spaceAnalyRequest,request);
        QueryWrapper<Picture> pictureQueryWrapper = new QueryWrapper<>();
        SpaceAnalyUsageResponse spaceAnalyUsedResponse = new SpaceAnalyUsageResponse();
        if(spaceAnalyRequest.getSpaceId()==null){
            //先查到图片总大小
            pictureQueryWrapper.select(
                    "p_size"
            );
            List<Object> objects = pictureService.getBaseMapper().selectObjs(pictureQueryWrapper);
            long usedSize = objects.stream()
                    .filter(Objects::nonNull)  // 先过滤 null
                    .mapToLong(obj -> obj instanceof Long ?
                            (Long) obj : 0L)
                    .sum();

            long usedCount = objects.size();
            //填充返回数据
            spaceAnalyUsedResponse.setUsedSize(usedSize);
            spaceAnalyUsedResponse.setUsedCount(usedCount);
        }
        //如果是私有空间
        else{
            Long spaceId = spaceAnalyRequest.getSpaceId();
            Space space = spaceService.getById(spaceId);
            Long totalSize = space.getTotalSize();
            Long totalCount = space.getTotalCount();
            Long maxSize = space.getMaxSize()!=0?space.getMaxSize():1L;
            Long maxCount = space.getMaxCount()!=0?space.getMaxCount():1L;;
            spaceAnalyUsedResponse.setMaxSize(maxSize);
            spaceAnalyUsedResponse.setMaxCount(maxCount);
            spaceAnalyUsedResponse.setUsedSize(totalSize);
            spaceAnalyUsedResponse.setUsedCount(totalCount);
            double countPercent = NumberUtil.round(totalCount * 100 / maxCount, 2).doubleValue();
            double sizePercent = NumberUtil.round(totalSize * 100 / maxSize, 2).doubleValue();
            spaceAnalyUsedResponse.setCountUsageRatio(countPercent);
            spaceAnalyUsedResponse.setSizeUsageRatio(sizePercent);
        }
        return spaceAnalyUsedResponse;
    }


    //获取图片分类使用情况
    public List<SpaceAnalyCategoryResponse> getSpaceAnalyCategory(SpaceAnalyRequest spaceAnalyRequest,HttpServletRequest request){
        //第二层，校验权限
        validSpaceAnaly(spaceAnalyRequest,request);
        QueryWrapper<Picture> pictureQueryWrapper = new QueryWrapper<>();
        pictureQueryWrapper= fillSpaceAnaly(spaceAnalyRequest, pictureQueryWrapper);
        pictureQueryWrapper.select(
                "p_category AS category", "COUNT(*) as totalCount","SUM(p_size) as totalSize"
        ).groupBy("p_category");
        List<Map<String, Object>> maps = pictureService.getBaseMapper().selectMaps(pictureQueryWrapper);

        List<SpaceAnalyCategoryResponse> collect = maps.stream().map(result -> {
            String category = result.get("category") != null ? result.get("category").toString() : "未分类";
            Long totalCount = Optional.ofNullable(result.get("totalCount")).map(o -> (Long) o).orElse(0L);
            Long totalSize = Optional.ofNullable(result.get("totalSize")).map(o -> ((BigDecimal) o).longValue()).orElse(0L);

            return new SpaceAnalyCategoryResponse(category,totalCount,totalSize);
        }).collect(Collectors.toList());
        return collect;
    }

    //获取图片标签使用情况
    public List<SpaceAnalyTagResponse> getSpaceAnalyTag(SpaceAnalyRequest spaceAnalyRequest, HttpServletRequest request){
        //第二层，校验权限
        validSpaceAnaly(spaceAnalyRequest,request);
        QueryWrapper<Picture> pictureQueryWrapper = new QueryWrapper<>();
        pictureQueryWrapper= fillSpaceAnaly(spaceAnalyRequest, pictureQueryWrapper);
        pictureQueryWrapper.select(
                "p_tags AS tag"
        );
        List<Object> objects = pictureService.getBaseMapper().selectObjs(pictureQueryWrapper);
        List<String> tagJsonList = objects.stream().map(o -> {
            return Optional.ofNullable(o).map(Object::toString).orElse("未设置");
        }).collect(Collectors.toList());

        List<String> tagList = tagJsonList.stream().flatMap(tagJson -> {
            if (!tagJson.equals("未设置")){
                return JSONUtil.toList(tagJson, String.class).stream();
            }else{
                return Stream.of("未设置");
            }

        }).collect(Collectors.toList());
        Map<String, Long> tagCountMap = tagList.stream().collect(Collectors.groupingBy(tag -> tag, Collectors.counting()));
        List<SpaceAnalyTagResponse> spaceAnalyTagResponses = tagCountMap.entrySet().stream().sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                .map(entry -> {
                    return new SpaceAnalyTagResponse(entry.getKey(), entry.getValue());
                })
                .collect(Collectors.toList());

        return spaceAnalyTagResponses;

    }




    public List<SpaceAnalySizeResponse> getSpaceAnalySize(SpaceAnalyRequest spaceAnalyRequest, HttpServletRequest request){
        //第二层，校验权限
        validSpaceAnaly(spaceAnalyRequest,request);
        QueryWrapper<Picture> pictureQueryWrapper = new QueryWrapper<>();
        pictureQueryWrapper= fillSpaceAnaly(spaceAnalyRequest, pictureQueryWrapper);
        pictureQueryWrapper.select(
                "p_size AS size"
        );
        List<Object> objects = pictureService.getBaseMapper().selectObjs(pictureQueryWrapper);
        List<Long> sizeList = objects.stream().map(o -> {
            return Optional.ofNullable(o).map(result -> (Long) result).orElse(0L);
        }).collect(Collectors.toList());
        HashMap<String, Long> stringLongHashMap = new HashMap<>();
        stringLongHashMap.put("<100KB", sizeList.stream().filter(size -> size < 100 * 1024).count());
        stringLongHashMap.put("100KB-500KB", sizeList.stream().filter(size -> size >= 100 * 1024 && size < 500 * 1024).count());
        stringLongHashMap.put("500KB-1MB", sizeList.stream().filter(size -> size >= 500 * 1024 && size < 1024 * 1024).count());
        stringLongHashMap.put(">1MB", sizeList.stream().filter(size -> size >= 1024 * 1024).count());

        List<SpaceAnalySizeResponse> collect = stringLongHashMap.entrySet().stream().map(entry -> {
            return new SpaceAnalySizeResponse(entry.getKey(), entry.getValue());
        }).collect(Collectors.toList());

        return collect;

    }

    //用户上传行为分析
    public List<SpaceAnalyUploadResponse> getSpaceAnalyUpload(SpaceUserAnalyzeRequest spaceAnalyRequest, HttpServletRequest request){
        //第二层，校验权限
        validSpaceAnaly(spaceAnalyRequest,request);
        QueryWrapper<Picture> pictureQueryWrapper = new QueryWrapper<>();
        pictureQueryWrapper= fillSpaceAnaly(spaceAnalyRequest, pictureQueryWrapper);
        pictureQueryWrapper.eq("user_id",spaceAnalyRequest.getUserId());

        String timeDimension = spaceAnalyRequest.getTimeDimension();
        String dateFormat;
        String groupByClause;

        switch (timeDimension.toLowerCase()) {
            case "day":
                // 按天分组：2024-12-05
                dateFormat = "DATE_FORMAT(create_time, '%Y-%m-%d') AS date";

                break;

            case "week":
                // 按周分组：2024-W49（第49周）
                dateFormat = "DATE_FORMAT(create_time, '%Y-W%v') AS date";
                break;

            case "month":
                // 按月分组：2024-12
                dateFormat = "DATE_FORMAT(create_time, '%Y-%m') AS date";
                break;

            default:
                throw new BusinessException(CodeEnum.PARAMES_ERROR,
                        "不支持的时间维度，仅支持：day、week、month");
        }
        pictureQueryWrapper.select(
                        dateFormat,
                        "COUNT(*) AS count"
                ).groupBy("date")
                .orderByAsc("date");
        List<Map<String, Object>> maps = pictureService.getBaseMapper().selectMaps(pictureQueryWrapper);
        List<SpaceAnalyUploadResponse> collect = maps.stream().map(result -> {
            Long count = Optional.ofNullable(result.get("count")).map(o -> (Long) result.get("count")).orElse(0L);
            String uploadtime = result.get("date").toString();
            return new SpaceAnalyUploadResponse(uploadtime, count);
        }).collect(Collectors.toList());
        return collect;
    }




    public List<Space> getSpaceAnalyRank(SpaceRankAnalyzeRequest spaceAnalyRequest, HttpServletRequest request){
        Integer topN = spaceAnalyRequest.getTopN();
        QueryWrapper<Space> spaceQueryWrapper = new QueryWrapper<>();
        spaceQueryWrapper.select(
                "id","space_name as spaceName","user_id as userId","total_size as totalSize"
        ).orderByDesc("total_size").last("LIMIT "+topN);
        List<Space> list = spaceService.list(spaceQueryWrapper);
        return list;



    }





    public void validSpaceAnaly(SpaceAnalyRequest spaceAnalyRequest, HttpServletRequest request){
        UserVO loginUser = TokenUtil.getLoginUserVOFromCookie(request);
        Boolean isAll = spaceAnalyRequest.getQueryAll();
        Long spaceId = spaceAnalyRequest.getSpaceId();
        Boolean isPublic = spaceAnalyRequest.getQueryPublic();
        if(isAll||isPublic){
            if(!loginUser.getUserType().equals(UserConstant.ADMIN)){
                log.warn("无权限");
                ThrowUtil.throwBusinessException(CodeEnum.NOT_AUTH,"权限不足");
            }
        }else if(spaceId!=null){
            Space space = spaceService.getById(spaceId);
            spaceService.validSpaceAndUserVO(space,loginUser);
        }
    }
    public QueryWrapper<Picture> fillSpaceAnaly(SpaceAnalyRequest spaceAnalyRequest,QueryWrapper<Picture> pictureQueryWrapper){
        Boolean isAll = spaceAnalyRequest.getQueryAll();
        Long spaceId = spaceAnalyRequest.getSpaceId();
        Boolean isPublic = spaceAnalyRequest.getQueryPublic();
        if(isPublic){
            pictureQueryWrapper.isNull("space_id");
        }else if(spaceId!=null){
            pictureQueryWrapper.eq("space_id",spaceId);
        }
        return pictureQueryWrapper;
    }



}
