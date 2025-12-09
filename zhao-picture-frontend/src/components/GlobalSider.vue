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
          @click="doMenuClick"
          class="side-menu"
        >
          <a-menu-item key="/">
            <template #icon>
              <PictureOutlined />
            </template>
            公共图库
          </a-menu-item>
          
          <a-menu-item key="/my_space">
            <template #icon>
              <UserOutlined />
            </template>
            我的空间
          </a-menu-item>
          
          <!-- 团队空间子菜单 -->
          <a-sub-menu key="team_spaces">
            <template #icon>
              <UsergroupAddOutlined />
            </template>
            <template #title>团队空间</template>
            
            <!-- 动态生成的团队空间列表 -->
            <a-menu-item 
              v-for="space in teamSpaces" 
              :key="`/team_space/${space.spaceId}`"
            >
              {{ space.spaceName }}
            </a-menu-item>
            
            <!-- 加入空间和创建空间选项 -->
            <a-menu-item key="/join_space" v-if="teamSpaces.length < 5">
              <PlusOutlined />
              加入空间
            </a-menu-item>
            <a-menu-item key="/create_space" v-if="teamSpaces.length < 5">
              <PlusCircleOutlined />
              创建空间
            </a-menu-item>
          </a-sub-menu>
        </a-menu>
      </div>
    </a-layout-sider>
  </div>
</template>

<script lang="ts" setup>
import { useRouter } from "vue-router";
import { ref, onMounted } from "vue";
import { 
  PictureOutlined, 
  UserOutlined, 
  UsergroupAddOutlined,
  PlusOutlined,
  PlusCircleOutlined
} from "@ant-design/icons-vue";
import { useLoginUserStore } from "@/stores/user.ts";
import { listMyTeamSpaceUsingPost } from "@/api/spaceUserController.ts";

// 菜单列表
const loginUserStore = useLoginUserStore();
// 团队空间列表
const teamSpaces = ref<API.SpaceUserVO[]>([]);

const router = useRouter()

// 当前选中菜单
const current = ref<string[]>([])

// 获取用户加入的团队空间列表
const fetchTeamSpaces = async () => {
  try {
    const res = await listMyTeamSpaceUsingPost();
    if (res.data.code === 0 && res.data.data) {
      // 限制最多显示5个团队空间
      teamSpaces.value = res.data.data.slice(0, 5);
    }
  } catch (error) {
    console.error("获取团队空间列表失败:", error);
  }
};

// 页面加载时获取团队空间列表
onMounted(() => {
  fetchTeamSpaces();
});

// 监听路由变化，更新当前选中菜单
router.afterEach((to, from, failure) => {
  // 智能路径匹配
  if (to.path.startsWith('/space/') || to.path.startsWith('/add_space')||(to.path === '/add_picture' && to.query.spaceId) ) {
    // 空间相关页面都高亮"我的空间"菜单
    current.value = ['/my_space'];
  } else {
    // 精确匹配其他路径
    current.value = [to.path];
  }
})

// 路由跳转事件
const doMenuClick = ({ key }: { key: string }) => {
  if (key === '/join_space') {
    // 加入空间功能待开发，跳转到提示页面
    router.push({ path: '/undeveloped' });
  } else if (key === '/create_space') {
    // 创建空间功能待开发，跳转到提示页面
    router.push({ path: '/undeveloped' });
  } else if (key.startsWith('/team_space/')) {
    // 团队空间详情页面待开发，跳转到提示页面
    router.push({ path: '/undeveloped' });
  } else {
    router.push({ path: key });
  }
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