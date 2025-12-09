declare namespace API {
  type BaseResponseBoolean_ = {
    code?: number;
    data?: boolean;
    message?: string;
  };

  type BaseResponseCreateOutPaintingTaskResponse_ = {
    code?: number;
    data?: CreateOutPaintingTaskResponse;
    message?: string;
  };

  type BaseResponseGetOutPaintingTaskResponse_ = {
    code?: number;
    data?: GetOutPaintingTaskResponse;
    message?: string;
  };

  type BaseResponseInt_ = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponseListSpace_ = {
    code?: number;
    data?: Space[];
    message?: string;
  };

  type BaseResponseListSpaceAnalyCategoryResponse_ = {
    code?: number;
    data?: SpaceAnalyCategoryResponse[];
    message?: string;
  };

  type BaseResponseListSpaceAnalySizeResponse_ = {
    code?: number;
    data?: SpaceAnalySizeResponse[];
    message?: string;
  };

  type BaseResponseListSpaceAnalyTagResponse_ = {
    code?: number;
    data?: SpaceAnalyTagResponse[];
    message?: string;
  };

  type BaseResponseListSpaceAnalyUploadResponse_ = {
    code?: number;
    data?: SpaceAnalyUploadResponse[];
    message?: string;
  };

  type BaseResponseListSpaceLevel_ = {
    code?: number;
    data?: SpaceLevel[];
    message?: string;
  };

  type BaseResponseListSpaceUserVO_ = {
    code?: number;
    data?: SpaceUserVO[];
    message?: string;
  };

  type BaseResponseLong_ = {
    code?: number;
    data?: number;
    message?: string;
  };

  type BaseResponsePagePicture_ = {
    code?: number;
    data?: PagePicture_;
    message?: string;
  };

  type BaseResponsePagePictureVO_ = {
    code?: number;
    data?: PagePictureVO_;
    message?: string;
  };

  type BaseResponsePageSpace_ = {
    code?: number;
    data?: PageSpace_;
    message?: string;
  };

  type BaseResponsePageUserVO_ = {
    code?: number;
    data?: PageUserVO_;
    message?: string;
  };

  type BaseResponsePictureTagCategory_ = {
    code?: number;
    data?: PictureTagCategory;
    message?: string;
  };

  type BaseResponsePictureVO_ = {
    code?: number;
    data?: PictureVO;
    message?: string;
  };

  type BaseResponseSpaceAnalyUsageResponse_ = {
    code?: number;
    data?: SpaceAnalyUsageResponse;
    message?: string;
  };

  type BaseResponseSpaceUser_ = {
    code?: number;
    data?: SpaceUser;
    message?: string;
  };

  type BaseResponseSpaceVO_ = {
    code?: number;
    data?: SpaceVO;
    message?: string;
  };

  type BaseResponseUserVO_ = {
    code?: number;
    data?: UserVO;
    message?: string;
  };

  type CreateOutPaintingTaskResponse = {
    code?: string;
    message?: string;
    output?: Output;
    requestId?: string;
  };

  type CreatePictureOutPaintingTaskRequest = {
    parameters?: Parameters;
    pictureId?: number;
  };

  type DeleteRequest = {
    id?: number;
  };

  type GetOutPaintingTaskResponse = {
    output?: Output1;
    requestId?: string;
  };

  type getPictureOutPaintingTaskUsingGETParams = {
    /** taskId */
    taskId?: string;
  };

  type getPictureVOByIdUsingGETParams = {
    /** id */
    id?: number;
  };

  type getSpaceVOUsingPOSTParams = {
    id?: number;
    pageNum?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    spaceLevel?: number;
    spaceName?: string;
    spaceType?: number;
    userId?: number;
  };

  type Output = {
    taskId?: string;
    taskStatus?: string;
  };

  type Output1 = {
    code?: string;
    endTime?: string;
    message?: string;
    outputImageUrl?: string;
    scheduledTime?: string;
    submitTime?: string;
    taskId?: string;
    taskMetrics?: TaskMetrics;
    taskStatus?: string;
  };

  type PagePicture_ = {
    current?: number;
    pages?: number;
    records?: Picture[];
    size?: number;
    total?: number;
  };

  type PagePictureVO_ = {
    current?: number;
    pages?: number;
    records?: PictureVO[];
    size?: number;
    total?: number;
  };

  type PageSpace_ = {
    current?: number;
    pages?: number;
    records?: Space[];
    size?: number;
    total?: number;
  };

  type PageUserVO_ = {
    current?: number;
    pages?: number;
    records?: UserVO[];
    size?: number;
    total?: number;
  };

  type Parameters = {
    addWatermark?: boolean;
    angle?: number;
    bestQuality?: boolean;
    bottomOffset?: number;
    leftOffset?: number;
    limitImageSize?: boolean;
    outputRatio?: string;
    rightOffset?: number;
    topOffset?: number;
    xScale?: number;
    yScale?: number;
  };

  type Picture = {
    auditMsg?: string;
    auditStatus?: number;
    auditTime?: string;
    auditorId?: number;
    createTime?: string;
    editTime?: string;
    id?: number;
    isDelete?: number;
    pCategory?: string;
    pFormat?: string;
    pHeight?: number;
    pIntroduction?: string;
    pName?: string;
    pScale?: number;
    pSize?: number;
    pTags?: string;
    pUrl?: string;
    pWidth?: number;
    spaceId?: number;
    thumbnailUrl?: string;
    updateTime?: string;
    userId?: number;
  };

  type PictureAuditRequest = {
    auditMsg?: string;
    auditStatus?: number;
    pictureId?: number;
  };

  type PictureEditRequest = {
    id?: number;
    pCategory?: string;
    pIntroduction?: string;
    pName?: string;
    pTags?: string[];
    spaceId?: number;
  };

  type PictureQueryRequest = {
    auditId?: number;
    auditStatus?: number;
    endEditTime?: string;
    id?: number;
    nullSpaceId?: boolean;
    pCategory?: string;
    pFormat?: string;
    pHeight?: number;
    pIntroduction?: string;
    pName?: string;
    pScale?: number;
    pSize?: number;
    pTags?: string[];
    pWidth?: number;
    pageNum?: number;
    pageSize?: number;
    searchText?: string;
    sortField?: string;
    sortOrder?: string;
    spaceId?: number;
    startEditTime?: string;
    userId?: number;
  };

  type PictureTagCategory = {
    categoryList?: string[];
    tagList?: string[];
  };

  type PictureUploadByBatchRequest = {
    count?: number;
    namePrefix?: string;
    searchText?: string;
  };

  type PictureUploadRequest = {
    fileUrl?: string;
    id?: number;
    picName?: string;
    spaceId?: number;
  };

  type PictureVO = {
    createTime?: string;
    editTime?: string;
    id?: number;
    pCategory?: string;
    pFormat?: string;
    pHeight?: number;
    pIntroduction?: string;
    pName?: string;
    pScale?: number;
    pSize?: number;
    pTags?: string[];
    pUrl?: string;
    pWidth?: number;
    spaceId?: number;
    thumbnailUrl?: string;
    userId?: number;
    userVO?: UserVO;
  };

  type Space = {
    createTime?: string;
    editTime?: string;
    id?: number;
    isDelete?: number;
    maxCount?: number;
    maxSize?: number;
    spaceLevel?: number;
    spaceName?: string;
    spaceType?: number;
    totalCount?: number;
    totalSize?: number;
    updateTime?: string;
    userId?: number;
  };

  type SpaceAddRequest = {
    spaceLevel?: number;
    spaceName?: string;
    spaceType?: number;
  };

  type SpaceAnalyCategoryResponse = {
    category?: string;
    count?: number;
    totalSize?: number;
  };

  type SpaceAnalyRequest = {
    queryAll?: boolean;
    queryPublic?: boolean;
    spaceId?: number;
  };

  type SpaceAnalySizeResponse = {
    count?: number;
    sizeRange?: string;
  };

  type SpaceAnalyTagResponse = {
    count?: number;
    tag?: string;
  };

  type SpaceAnalyUploadResponse = {
    count?: number;
    period?: string;
  };

  type SpaceAnalyUsageResponse = {
    countUsageRatio?: number;
    maxCount?: number;
    maxSize?: number;
    sizeUsageRatio?: number;
    usedCount?: number;
    usedSize?: number;
  };

  type SpaceEditRequest = {
    id?: number;
    spaceLevel?: number;
    spaceName?: string;
  };

  type SpaceLevel = {
    maxCount?: number;
    maxSize?: number;
    text?: string;
    value?: number;
  };

  type SpaceQueryRequest = {
    id?: number;
    pageNum?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    spaceLevel?: number;
    spaceName?: string;
    spaceType?: number;
    userId?: number;
  };

  type SpaceRankAnalyzeRequest = {
    topN?: number;
  };

  type SpaceUser = {
    createTime?: string;
    id?: number;
    spaceId?: number;
    spaceRole?: string;
    updateTime?: string;
    userId?: number;
  };

  type SpaceUserAddRequest = {
    spaceId?: number;
    spaceRole?: string;
    userId?: number;
  };

  type SpaceUserAnalyzeRequest = {
    queryAll?: boolean;
    queryPublic?: boolean;
    spaceId?: number;
    timeDimension?: string;
    userId?: number;
  };

  type SpaceUserDeleteRequest = {
    spaceId?: number;
    userId?: number;
  };

  type SpaceUserEditRequest = {
    spaceId?: number;
    spaceRole?: string;
    userId?: number;
  };

  type SpaceUserQueryRequest = {
    id?: number;
    spaceId?: number;
    spaceRole?: string;
    userId?: number;
  };

  type SpaceUserVO = {
    createTime?: string;
    id?: number;
    space?: SpaceVO;
    spaceId?: number;
    spaceRole?: string;
    updateTime?: string;
    user?: UserVO;
    userId?: number;
  };

  type SpaceVO = {
    createTime?: string;
    editTime?: string;
    id?: number;
    maxCount?: number;
    maxSize?: number;
    spaceLevel?: number;
    spaceName?: string;
    spaceType?: number;
    totalCount?: number;
    totalSize?: number;
    updateTime?: string;
    userId?: number;
    userVO?: UserVO;
  };

  type TaskMetrics = {
    failed?: number;
    succeeded?: number;
    total?: number;
  };

  type uploadPictureUsingPOSTParams = {
    fileUrl?: string;
    id?: number;
    picName?: string;
    spaceId?: number;
  };

  type UserEditRequest = {
    id?: number;
    userAvatar?: string;
    userIntroduction?: string;
    userName?: string;
  };

  type UserLoginRequest = {
    userAccount?: string;
    userPassword?: string;
  };

  type UserQueryRequest = {
    id?: number;
    pageNum?: number;
    pageSize?: number;
    sortField?: string;
    sortOrder?: string;
    userAccount?: string;
    userIntroduction?: string;
    userName?: string;
  };

  type UserRegisterRequest = {
    checkPassword?: string;
    userAccount?: string;
    userPassword?: string;
  };

  type UserVO = {
    createTime?: string;
    editTime?: string;
    id?: number;
    updateTime?: string;
    userAccount?: string;
    userAvatar?: string;
    userIntroduction?: string;
    userName?: string;
    userType?: number;
  };
}
