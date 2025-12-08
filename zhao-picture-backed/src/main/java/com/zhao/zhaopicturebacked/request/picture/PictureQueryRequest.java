package com.zhao.zhaopicturebacked.request.picture;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhao.zhaopicturebacked.request.PageRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class PictureQueryRequest extends PageRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private Long userId;
    private String searchText;
    @JsonProperty("pName")
    private String pName;
    @JsonProperty("pIntroduction")
    private String pIntroduction;
    @JsonProperty("pCategory")
    private String pCategory;
    @JsonProperty("pTags")
    private List<String> pTags;
    @JsonProperty("pFormat")
    private String pFormat;
    @JsonProperty("pSize")
    private Long pSize;
    @JsonProperty("pWidth")
    private Integer pWidth;
    @JsonProperty("pHeight")
    private Integer pHeight;
    @JsonProperty("pScale")
    private Double pScale;
    private Integer auditStatus;
    private Long auditId;
    private String sortField;
    private String sortOrder;
    /**
     * 开始编辑时间
     */
    private Date startEditTime;

    /**
     * 结束编辑时间
     */
    private Date endEditTime;

    private Long spaceId;
    private Boolean nullSpaceId;

}
