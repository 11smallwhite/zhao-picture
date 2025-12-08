<template>
  <div id="globalSider">

    <a-layout-sider
      v-if="loginUserStore.loginUser.id"
      class="sider"
      width="200"
      breakpoint="lg"
      collapsed-width="0"
    >
      <a-menu
        mode="inline"
        v-model:selectedKeys="current"
        :items="menuItems"
        @click="doMenuClick" />

    </a-layout-sider>

  </div>
</template>

<script lang="ts" setup>
// 菜单列表
import { useRouter } from "vue-router";
import { PictureOutlined, UserOutlined } from "@ant-design/icons-vue";
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


import { computed, h, ref } from "vue";
import { useLoginUserStore } from "@/stores/user.ts";
</script>
