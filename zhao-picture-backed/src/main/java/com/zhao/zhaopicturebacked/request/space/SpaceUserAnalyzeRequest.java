package com.zhao.zhaopicturebacked.request.space;

import lombok.Data;

@Data
public class SpaceUserAnalyzeRequest extends SpaceAnalyRequest {

    /**
     * 用户 ID
     */
    private Long userId;

    /**
     * 时间维度：day / week / month
     */
    private String timeDimension;
}
