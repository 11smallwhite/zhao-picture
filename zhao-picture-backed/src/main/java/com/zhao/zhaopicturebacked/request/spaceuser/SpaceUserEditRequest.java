package com.zhao.zhaopicturebacked.request.spaceuser;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpaceUserEditRequest implements Serializable {

    /**
     * id
     */
    private Long userId;

    private Long spaceId;

    /**
     * 空间角色：viewer/editor/admin
     */
    private String spaceRole;

    private static final long serialVersionUID = 1L;
}
