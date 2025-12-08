// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** auditPicture POST /api/picture/audit/admin */
export async function auditPictureUsingPost(
  body: API.PictureAuditRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>("/api/picture/audit/admin", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** deletePictureById POST /api/picture/delete */
export async function deletePictureByIdUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>("/api/picture/delete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** editPicture POST /api/picture/edit */
export async function editPictureUsingPost(
  body: API.PictureEditRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePictureVO_>("/api/picture/edit", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** getPictureVOById GET /api/picture/get/vo */
export async function getPictureVoByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPictureVOByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePictureVO_>("/api/picture/get/vo", {
    method: "GET",
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** createPictureOutPaintingTask POST /api/picture/out_painting/create_task */
export async function createPictureOutPaintingTaskUsingPost(
  body: API.CreatePictureOutPaintingTaskRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseCreateOutPaintingTaskResponse_>(
    "/api/picture/out_painting/create_task",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      data: body,
      ...(options || {}),
    }
  );
}

/** getPictureOutPaintingTask GET /api/picture/out_painting/get_task */
export async function getPictureOutPaintingTaskUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPictureOutPaintingTaskUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseGetOutPaintingTaskResponse_>(
    "/api/picture/out_painting/get_task",
    {
      method: "GET",
      params: {
        ...params,
      },
      ...(options || {}),
    }
  );
}

/** select POST /api/picture/page/select/query */
export async function selectUsingPost(
  body: API.PictureQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePictureVO_>(
    "/api/picture/page/select/query",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      data: body,
      ...(options || {}),
    }
  );
}

/** selectBySpace POST /api/picture/page/select/query/space */
export async function selectBySpaceUsingPost(
  body: API.PictureQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePictureVO_>(
    "/api/picture/page/select/query/space",
    {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      data: body,
      ...(options || {}),
    }
  );
}

/** selectAdmin POST /api/picture/select/admin */
export async function selectAdminUsingPost(
  body: API.PictureQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePicture_>("/api/picture/select/admin", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** listPictureTagCategory GET /api/picture/tag_category */
export async function listPictureTagCategoryUsingGet(options?: {
  [key: string]: any;
}) {
  return request<API.BaseResponsePictureTagCategory_>(
    "/api/picture/tag_category",
    {
      method: "GET",
      ...(options || {}),
    }
  );
}

/** uploadPicture POST /api/picture/upload */
export async function uploadPictureUsingPost(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.uploadPictureUsingPOSTParams,
  body: {},
  file?: File,
  options?: { [key: string]: any }
) {
  const formData = new FormData();

  if (file) {
    formData.append("file", file);
  }

  Object.keys(body).forEach((ele) => {
    const item = (body as any)[ele];

    if (item !== undefined && item !== null) {
      if (typeof item === "object" && !(item instanceof File)) {
        if (item instanceof Array) {
          item.forEach((f) => formData.append(ele, f || ""));
        } else {
          formData.append(
            ele,
            new Blob([JSON.stringify(item)], { type: "application/json" })
          );
        }
      } else {
        formData.append(ele, item);
      }
    }
  });

  return request<API.BaseResponsePictureVO_>("/api/picture/upload", {
    method: "POST",
    params: {
      ...params,
    },
    data: formData,
    requestType: "form",
    ...(options || {}),
  });
}

/** uploadPictureByBatch POST /api/picture/upload/batch */
export async function uploadPictureByBatchUsingPost(
  body: API.PictureUploadByBatchRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseInt_>("/api/picture/upload/batch", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** uploadPictureByUrl POST /api/picture/upload/url */
export async function uploadPictureByUrlUsingPost(
  body: API.PictureUploadRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePictureVO_>("/api/picture/upload/url", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
