package com.zhao.zhaopicturebacked.request.space;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpaceAddRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String spaceName;
    private int spaceLevel;
    /**
     * 空间类型：0-私有 1-团队
     */
    private Integer spaceType;

}
