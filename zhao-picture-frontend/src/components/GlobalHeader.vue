<template>
  <a-row :wrap="false" align="middle">
    <a-col flex="220px">
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
          class="nav-menu"
        />
      </div>
    </a-col>

    <a-col flex="180px">
      <div class="user-login-status">
        <div v-if="loginUserStores.loginUser.id">
          <a-dropdown>
            <a-space class="user-dropdown">
              <a-avatar
                :size="40"
                :src="loginUserStores.loginUser.userAvatar || 'https://xsgames.co/randomusers/avatar.php?g=pixel&key=1'"
                class="user-avatar"
              />
              <span class="username">{{ loginUserStores.loginUser.userName ?? "无名" }}</span>
            </a-space>

            <template #overlay>
              <a-menu class="user-menu">
                <a-menu-item>
                  <router-link to="/user/detail">
                    <InfoCircleOutlined />
                    用户信息
                  </router-link>
                </a-menu-item>
                <a-menu-item>
                  <router-link to="/my_space">
                    <UserOutlined />
                    我的空间
                  </router-link>
                </a-menu-item>
                <a-menu-item>
                  <router-link to="/team_space">
                    <UsergroupAddOutlined />
                    团队空间
                  </router-link>
                </a-menu-item>
                <a-menu-divider />
                <a-menu-item @click="doLogout">
                  <LogoutOutlined />
                  退出登录
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
import { HomeOutlined, UserOutlined, InfoCircleOutlined, LogoutOutlined, UsergroupAddOutlined, PlusCircleOutlined, GithubOutlined } from "@ant-design/icons-vue";
import { MenuProps, message } from "ant-design-vue";
import { useRouter } from "vue-router";

import { useLoginUserStore } from "@/stores/user.ts";
import { userLogoutUsingPost } from "@/api/userController.ts";
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
    key: "/team_space",
    icon: () => h(UsergroupAddOutlined),
    label: "团队空间",
    title: "团队空间",
  },
  {
    key: "/admin/userManage",
    label: "用户管理",
    title: "用户管理",
  },
  {
    key: "/add_picture",
    icon: () => h(PlusCircleOutlined),
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
    icon: () => h(GithubOutlined),
    label: h(
      "a",
      {
        href: "https://github.com/11smallwhite/public_zhaoPicture_backed/tree/20251125",
        target: "_blank",
      },
      "开发者GitHub"
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
#globalHeader {
  display: flex;
  align-items: center;
  height: 100%;
}

.title-bar {
  display: flex;
  align-items: center;
  height: 48px;
}

.title {
  color: #645bff;
  font-size: 18px;
  font-weight: bold;
  margin-left: 12px;
  white-space: nowrap;
}

.logo {
  height: 36px;
  transition: transform 0.3s ease;
}

.logo:hover {
  transform: scale(1.05);
}

.nav-menu {
  border: none !important;
  background: transparent !important;
  line-height: 48px;
}

.user-login-status {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 48px;
}

.user-dropdown {
  cursor: pointer;
  transition: all 0.3s ease;
  padding: 4px 8px;
  border-radius: 6px;
}

.user-dropdown:hover {
  background-color: #f0f2ff;
}

.user-avatar {
  border: 2px solid #e8e8e8;
}

.username {
  font-weight: 500;
  color: #333;
}

.user-menu {
  margin-top: 8px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 6px 16px 0 rgba(0, 0, 0, 0.08);
}
</style>
