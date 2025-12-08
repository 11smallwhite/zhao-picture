package com.zhao.zhaopicturebacked.request.spaceuser;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpaceUserDeleteRequest implements Serializable {

    /**
     * 空间 ID
     */
    private Long spaceId;

    /**
     * 用户 ID
     */
    private Long userId;


    private static final long serialVersionUID = 1L;
}
