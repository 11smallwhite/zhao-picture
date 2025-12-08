// ✅ 正确的写法
import { createRouter, createWebHistory, type RouteRecordRaw } from "vue-router";
import HomePage from "@/pages/HomePage.vue";
import UserLoginPage from "@/pages/user/UserLoginPage.vue";
import UserRegisterPage from "@/pages/user/UserRegisterPage.vue";
import UserManagePage from "@/pages/admin/UserManagePage.vue";
import AddPicturePage from "@/pages/picture/AddPicturePage.vue";
import PictureManagePage from "@/pages/admin/PictureManagePage.vue";
import SpaceManagePage from "@/pages/admin/SpaceManagePage.vue";
import PictureDetailPage from "@/pages/picture/PictureDetailPage.vue";
import AddPictureBatchPage from "@/pages/admin/AddPictureBatchPage.vue";
import AddSpacePage from "@/pages/space/AddSpacePage.vue";
import MySpacePage from "@/pages/space/mySpacePage.vue";
import SpaceDetailPage from "@/pages/space/SpaceDetailPage.vue";
import SpaceAnalyPage from "@/components/SpaceAnalyPage.vue";

const routes: Array<RouteRecordRaw >  = [
  {
    path: '/',
    name: 'Home',
    component: HomePage
  },
  {
    path: '/admin/userManage',
    name: '用户管理',
    component: UserManagePage
  }
  ,
  {
    path: '/user/login',
    name: '用户登录',
    component: UserLoginPage
  }
  ,
  {
    path: '/user/register',
    name: '用户注册',
    component: UserRegisterPage
  },
  {
    path: '/add_picture',
    name: '创建图片',
    component: AddPicturePage,
  },
  {
    path: '/admin/pictureManage',
    name: '图片管理',
    component: PictureManagePage,
  },
  {
    path: '/picture/:id',
    name: '图片详情',
    component: PictureDetailPage,
    props: true,
  },
  {
    path: '/add_picture/batch',
    name: '批量创建图片',
    component: AddPictureBatchPage,
  },
  {
    path: '/admin/spaceManage',
    name: '空间管理',
    component: SpaceManagePage,
  },
  {
    path: '/add_space',
    name: '创建空间',
    component: AddSpacePage,
  },
  {
    path: '/my_space',
    name: '我的空间',
    component: MySpacePage,

  },
  {
    path: '/space/:id',
    name: '空间详情',
    component: SpaceDetailPage,
    props: true,
  },
  {
    path: '/space_analyze',
    name: '空间分析',
    component: SpaceAnalyPage,
  }



]


const router = createRouter({
  history: createWebHistory(),
  routes
})


// 确保有这一行！
export default router
