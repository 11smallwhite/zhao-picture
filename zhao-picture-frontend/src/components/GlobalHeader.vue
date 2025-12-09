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
        <!-- 使用 overflowedIndicator 插槽来避免菜单折叠 -->
        <a-menu
          v-model:selectedKeys="current"
          mode="horizontal"
          @click="doMenuClick"
          class="nav-menu"
          :selectable="true"
        >
          <a-menu-item key="/">
            <HomeOutlined />
            公共图库
          </a-menu-item>
          
          <a-menu-item key="/team_space">
            <UsergroupAddOutlined />
            团队空间
          </a-menu-item>
          
          <!-- 管理员菜单项 -->
          <template v-if="showAdminMenus">
            <a-menu-item key="/admin/userManage">
              <SettingOutlined />
              用户管理
            </a-menu-item>
            
            <a-menu-item key="/add_picture">
              <PlusCircleOutlined />
              创建图片
            </a-menu-item>
            
            <a-menu-item key="/admin/pictureManage">
              <PictureOutlined />
              图片管理
            </a-menu-item>
            
            <a-menu-item key="/admin/spaceManage">
              <DatabaseOutlined />
              空间管理
            </a-menu-item>
          </template>
          
          <!-- 普通用户也显示创建图片菜单 -->
          <template v-else>
            <a-menu-item key="/add_picture">
              <PlusCircleOutlined />
              创建图片
            </a-menu-item>
          </template>
          
          <a-menu-item key="github">
            <GithubOutlined />
            开发者GitHub
          </a-menu-item>
        </a-menu>
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
import { ref, computed, onMounted } from "vue";
import { 
  HomeOutlined, 
  UserOutlined, 
  InfoCircleOutlined, 
  LogoutOutlined, 
  UsergroupAddOutlined, 
  PlusCircleOutlined, 
  GithubOutlined,
  SettingOutlined,
  PictureOutlined,
  DatabaseOutlined
} from "@ant-design/icons-vue";
import { message } from "ant-design-vue";
import { useRouter } from "vue-router";

import { useLoginUserStore } from "@/stores/user.ts";
import { userLogoutUsingPost } from "@/api/userController.ts";
const loginUserStores = useLoginUserStore();

// 页面加载时获取用户信息
onMounted(() => {
  if (!loginUserStores.loginUser.id) {
    loginUserStores.fetchLoginUser();
  }
});

// 判断是否显示管理员菜单
const showAdminMenus = computed(() => {
  const loginUser = loginUserStores.loginUser;
  return loginUser && loginUser.userType === 1;
});

const router = useRouter();

const doMenuClick = ({ key }: { key: string }) => {
  // GitHub链接特殊处理
  if (key === "github") {
    window.open("https://github.com/11smallwhite/public_zhaoPicture_backed/tree/20251125", "_blank");
    return;
  }
  
  router.push({
    path: key,
  });
};

// 当前选中菜单
const current = ref<string[]>(["/"]);

// 监听路由变化，更新当前选中菜单
router.afterEach((to, from) => {
  current.value = [to.path];
});

const doLogout = async () => {
  const res = await userLogoutUsingPost();
  if (res.data.code === 0) {
    loginUserStores.setLoginUser({
      userName: "未登录",
      id: "", // 重置用户ID
      userType: 0
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
  width: 100%;
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
  white-space: nowrap;
  width: 100%;
}

.nav-menu :deep(.ant-menu-item) {
  display: inline-block !important;
  vertical-align: top;
  border-bottom: 2px solid transparent;
}

.nav-menu :deep(.ant-menu-item-selected) {
  border-bottom: 2px solid #645bff;
}

.nav-menu :deep(.ant-menu-submenu) {
  display: inline-block !important;
  vertical-align: top;
}

.nav-menu :deep(.ant-menu-overflowed-submenu) {
  display: none !important;
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