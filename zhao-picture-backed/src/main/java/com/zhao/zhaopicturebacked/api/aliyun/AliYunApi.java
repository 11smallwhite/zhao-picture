package com.zhao.zhaopicturebacked.api.aliyun;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.zhao.zhaopicturebacked.enums.CodeEnum;
import com.zhao.zhaopicturebacked.utils.ThrowUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;



@Slf4j
@Component
public class AliYunApi {
    @Value("${aliyunAi.apiKey}")
    private String apiKey;


    //创建任务的地址
    private final String CREATE_TASK_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/image2image/out-painting";
    //查询任务的地址
    private final String GET_TASK_URL = "https://dashscope.aliyuncs.com/api/v1/tasks/%s";

    public CreateOutPaintingTaskResponse createAiTask(CreateOutPaintingTaskRequest createOutPaintingTaskRequest) {
        if(createOutPaintingTaskRequest==null){
            log.warn("扩图参数为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"扩图参数为空");
        }
        //发起请求
        HttpRequest httpRequest = HttpRequest.post(CREATE_TASK_URL)
                .header(Header.CONTENT_TYPE, "application/json")
                .header(Header.AUTHORIZATION, "Bearer " + apiKey)
                .header("X-DashScope-Async","enable")
                .body(JSONUtil.toJsonStr(createOutPaintingTaskRequest));
        //判断请求是否成功发送
        try(HttpResponse response = httpRequest.execute()){
            if(!response.isOk()){
                log.error("请求异常{}",response.body());
                ThrowUtil.throwBusinessException(CodeEnum.REQUEST_ERROR,"请求异常");
            }
            //将Response转化为对应的响应类
            CreateOutPaintingTaskResponse imageOutPaintingResponse = JSONUtil.toBean(response.body(), CreateOutPaintingTaskResponse.class);

            String errorCode = imageOutPaintingResponse.getCode();
            String errorMessage = imageOutPaintingResponse.getMessage();
            if (StrUtil.isNotEmpty(errorCode)){
                log.error("AI扩图失败,errorCode:{},errorMessage:{}",errorCode,errorMessage);
                ThrowUtil.throwBusinessException(CodeEnum.SYSTEM_ERROR,"AI扩图接口异常");
            }
            return imageOutPaintingResponse;
        }
    }

    public GetOutPaintingTaskResponse getAiTask(String taskId) {
        if(taskId==null){
            log.warn("任务id为空");
            ThrowUtil.throwBusinessException(CodeEnum.PARAMES_ERROR,"任务id为空");
        }
        //发起请求
        HttpRequest httpRequest = HttpRequest.get(String.format(GET_TASK_URL,taskId))
                .header(Header.AUTHORIZATION, "Bearer " + apiKey);
        //判断请求是否成功发送
        try(HttpResponse response = httpRequest.execute()){
            if(!response.isOk()){
                log.error("请求异常{}",response.body());
                ThrowUtil.throwBusinessException(CodeEnum.REQUEST_ERROR,"请求异常");
            }
            //将Response转化为对应的响应类
            GetOutPaintingTaskResponse imageGetPaintingResponse = JSONUtil.toBean(response.body(), GetOutPaintingTaskResponse.class);
            return imageGetPaintingResponse;
        }
    }

}
