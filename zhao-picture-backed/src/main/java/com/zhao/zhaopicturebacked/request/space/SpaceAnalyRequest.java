package com.zhao.zhaopicturebacked.request.space;

import lombok.Data;

import java.io.Serializable;

@Data
public class SpaceAnalyRequest implements Serializable {
    private Boolean queryAll;
    private Long spaceId;
    private Boolean queryPublic;
    private static final long serialVersionUID = 1L;
}
