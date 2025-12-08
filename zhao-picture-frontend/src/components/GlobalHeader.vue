<template>
  <a-row :wrap="false">
    <a-col :flex="0.3">
      <RouterLink to="/">
        <div class="title-bar">
          <img class="logo" src="../assets/logo.svg" alt="logo" />
          <div class="title">zhao-多功能云图库</div>
        </div>
      </RouterLink>
    </a-col>

    <a-col flex="auto">
      <div id="globalHeader">
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          :items="items"
          @click="doMenuClick"
        />
      </div>
    </a-col>

    <a-col :flex="0.01">
      <div class="user-login-status">
        <div v-if="loginUserStores.loginUser.id">
          <a-dropdown>
            <a-space>
              <a-avatar :size="45" src="https://xsgames.co/randomusers/avatar.php?g=pixel&key=1" />
              {{ loginUserStores.loginUser.userName ?? "无名" }}
            </a-space>

            <template #overlay>
              <a-menu>
                <a-menu-item @click="doLogout"> 退出登录 </a-menu-item>
                <a-menu-item>
                  <router-link to="/my_space">
                    <UserOutlined />
                    我的空间
                  </router-link>
                </a-menu-item>

              </a-menu>


            </template>
          </a-dropdown>
        </div>
        <div v-else>
          <a-button type="primary" href="/user/login">登录</a-button>
        </div>
      </div>
    </a-col>
  </a-row>
</template>

<script lang="ts" setup>
import { computed, h, ref } from "vue";
import { HomeOutlined } from "@ant-design/icons-vue";
import { MenuProps, message } from "ant-design-vue";
import {  useRouter } from "vue-router";

import { useLoginUserStore } from "@/stores/user.ts";
import { userLogoutUsingPost } from "@/api/userController.ts";
import { UserOutlined } from "@ant-design/icons-vue";
const loginUserStores = useLoginUserStore();
loginUserStores.fetchLoginUser();

// 菜单列表
const originItems = [
  {
    key: "/",
    icon: () => h(HomeOutlined),
    label: "公共图库",
    title: "公共图库",
  },
  {
    key: "/admin/userManage",
    label: "用户管理",
    title: "用户管理",
  },
  {
    key: "/add_picture",
    label: "创建图片",
    title: "创建图片",
  },
  {
    key: "/admin/pictureManage",
    label: "图片管理",
    title: "图片管理",
  },
  {
    key: "/admin/spaceManage",
    label: "空间管理",
    title: "空间管理",
  },
  {
    key: "",
    label: h(
      "a",
      {
        href: "https://github.com/11smallwhite/zhao-picture",
        target: "_blank",
      },
      "开发者的github"
    ),
    title: "开发者的github",
  },
];

// 过滤菜单项
const filterMenus = (menus = [] as MenuProps["items"]) => {
  return menus?.filter((menu) => {
    if (menu.key.startsWith("/admin")) {
      const loginUser = loginUserStores.loginUser;
      if (!loginUser || loginUser.userType !== 1) {
        return false;
      }
    }
    return true;
  });
};

// 展示在菜单的路由数组
const items = computed<MenuProps["items"]>(() => filterMenus(originItems));

const router = useRouter();
const doMenuClick = ({ key }) => {
  router.push({
    path: key,
  });
};

// 当前选中菜单
const current = ref<string[]>(["/"]);
// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, next) => {
  current.value = [to.path];
});

const doLogout = async () => {
  const res = await userLogoutUsingPost();
  if (res.data.code === 0) {
    loginUserStores.setLoginUser({
      userName: "未登录",
    });
    message.success("退出登录成功");
    await router.push({
      path: "/user/login",
      replace: true,
    });
  } else {
    message.error("退出登录失败" + res.data.message);
  }
};
</script>

<style scoped>
.title-bar {
  display: flex;
  align-items: center;
}

.title {
  color: black;
  font-size: 18px;
  margin-left: 16px;
}

.logo {
  height: 48px;
}
</style>
