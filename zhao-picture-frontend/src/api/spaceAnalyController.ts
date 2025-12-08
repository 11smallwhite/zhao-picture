// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** getSpaceAnalyCategory POST /api/space/analy/category */
export async function getSpaceAnalyCategoryUsingPost(
  body: API.SpaceAnalyRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListSpaceAnalyCategoryResponse_>(
    "/api/space/analy/category",
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

/** getSpaceAnalySize POST /api/space/analy/size */
export async function getSpaceAnalySizeUsingPost(
  body: API.SpaceAnalyRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListSpaceAnalySizeResponse_>(
    "/api/space/analy/size",
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

/** getSpaceAnalyTag POST /api/space/analy/tag */
export async function getSpaceAnalyTagUsingPost(
  body: API.SpaceAnalyRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListSpaceAnalyTagResponse_>(
    "/api/space/analy/tag",
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

/** getSpaceAnalyUpload POST /api/space/analy/upload */
export async function getSpaceAnalyUploadUsingPost(
  body: API.SpaceUserAnalyzeRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListSpaceAnalyUploadResponse_>(
    "/api/space/analy/upload",
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

/** getSpaceAnaly POST /api/space/analy/usage */
export async function getSpaceAnalyUsingPost(
  body: API.SpaceAnalyRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseSpaceAnalyUsageResponse_>(
    "/api/space/analy/usage",
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

/** getSpaceAnalyUsed POST /api/space/analy/used */
export async function getSpaceAnalyUsedUsingPost(
  body: API.SpaceRankAnalyzeRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseListSpace_>("/api/space/analy/used", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
