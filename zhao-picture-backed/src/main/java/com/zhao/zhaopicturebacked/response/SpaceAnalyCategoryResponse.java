package com.zhao.zhaopicturebacked.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SpaceAnalyCategoryResponse implements Serializable {
    /**
     * 图片分类
     */
    private String category;

    /**
     * 图片数量
     */
    private Long count;

    /**
     * 分类图片总大小
     */
    private Long totalSize;

    private static final long serialVersionUID = 1L;
}