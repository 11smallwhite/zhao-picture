package com.zhao.zhaopicturebacked.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SpaceAnalySizeResponse implements Serializable {

    //图片的大小范围
    private String sizeRange;
    //在该范围内的图片个数
    private Long count;
}
