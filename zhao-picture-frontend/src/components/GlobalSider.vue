<template>
  <div id="globalSider">
    <a-layout-sider
      v-if="loginUserStore.loginUser.id"
      class="sider"
      width="200"
      breakpoint="lg"
      collapsed-width="0"
      :zero-width-trigger-style="{ 
        backgroundColor: '#645bff', 
        borderRadius: '50%',
        width: '24px',
        height: '24px',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center'
      }"
    >
      <div class="sider-content">
        <a-menu
          mode="inline"
          v-model:selectedKeys="current"
          :items="menuItems"
          @click="doMenuClick"
          class="side-menu"
        />
      </div>
    </a-layout-sider>
  </div>
</template>

<script lang="ts" setup>
import { useRouter } from "vue-router";
import { h, ref } from "vue";
import { PictureOutlined, UserOutlined, UsergroupAddOutlined } from "@ant-design/icons-vue";
import { useLoginUserStore } from "@/stores/user.ts";

// 菜单列表
const loginUserStore = useLoginUserStore();
// 菜单列表
const menuItems = [
  {
    key: '/',
    label: '公共图库',
    icon: () => h(PictureOutlined),
  },
  {
    key: '/my_space',
    label: '我的空间',
    icon: () => h(UserOutlined),
  },
  {
    key: '/team_space',
    label: '团队空间',
    icon: () => h(UsergroupAddOutlined),
  },
]

const router = useRouter()

// 当前选中菜单
const current = ref<string[]>([])
// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, failure) => {
  // 智能路径匹配
  if (to.path.startsWith('/space/') || to.path.startsWith('/add_space')||(to.path === '/add_picture' && to.query.spaceId) ) {
    // 空间相关页面都高亮"我的空间"菜单
    current.value = ['/my_space'];
  } else {
    // 精确匹配其他路径
    const matchedKey = menuItems.find(item => item.key === to.path)?.key;
    current.value = matchedKey ? [matchedKey] : [];
  }
})

// 路由跳转事件
const doMenuClick = ({ key }: { key: string }) => {
  router.push({
    path: key,
  })
}
</script>

<style scoped>
.sider {
  background: #f8f9fa !important;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
  height: fit-content;
  max-height: calc(100vh - 140px);
  overflow-y: auto;
  margin-left: 24px;
}

.sider-content {
  padding: 16px 0;
}

.side-menu {
  background: transparent !important;
  border: none !important;
}

.side-menu :deep(.ant-menu-item) {
  margin: 2px 8px;
  border-radius: 6px !important;
  transition: all 0.3s ease;
}

.side-menu :deep(.ant-menu-item:hover) {
  background-color: #f0f2ff !important;
}

.side-menu :deep(.ant-menu-item-selected) {
  background-color: #f0f2ff !important;
}

/* 响应式断点触发器样式 */
:deep(.ant-layout-sider-zero-width-trigger) {
  top: 12px;
  right: -12px;
}
</style>