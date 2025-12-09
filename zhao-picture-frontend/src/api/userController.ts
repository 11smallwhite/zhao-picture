// @ts-ignore
/* eslint-disable */
import request from "@/request";

/** uploadAvatar POST /api/user/avatar */
export async function uploadAvatarUsingPost(
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

  return request<API.BaseResponseUserVO_>("/api/user/avatar", {
    method: "POST",
    data: formData,
    requestType: "form",
    ...(options || {}),
  });
}

/** userDeleteById POST /api/user/delete */
export async function userDeleteByIdUsingPost(
  body: API.DeleteRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>("/api/user/delete", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** userEdit POST /api/user/edit */
export async function userEditUsingPost(
  body: API.UserEditRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseUserVO_>("/api/user/edit", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** getLoginUser GET /api/user/get */
export async function getLoginUserUsingGet(options?: { [key: string]: any }) {
  return request<API.BaseResponseUserVO_>("/api/user/get", {
    method: "GET",
    ...(options || {}),
  });
}

/** userLogin POST /api/user/login */
export async function userLoginUsingPost(
  body: API.UserLoginRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseUserVO_>("/api/user/login", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** userLogout POST /api/user/logout */
export async function userLogoutUsingPost(options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>("/api/user/logout", {
    method: "POST",
    ...(options || {}),
  });
}

/** userRegister POST /api/user/register */
export async function userRegisterUsingPost(
  body: API.UserRegisterRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseLong_>("/api/user/register", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}

/** select POST /api/user/select */
export async function selectUsingPost1(
  body: API.UserQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageUserVO_>("/api/user/select", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    data: body,
    ...(options || {}),
  });
}
