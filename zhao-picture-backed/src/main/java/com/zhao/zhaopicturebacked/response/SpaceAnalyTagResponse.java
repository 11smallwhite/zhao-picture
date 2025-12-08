package com.zhao.zhaopicturebacked.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SpaceAnalyTagResponse implements Serializable {
    //标签名
    private String tag;
    //该标签图片个数
    private Long count;

}
