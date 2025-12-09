<template>
  <div id="undevelopedPage">
    <a-result 
      status="info" 
      title="功能待开发" 
      sub-title="该功能正在紧张开发中，敬请期待！"
    >
      <template #extra>
        <a-button type="primary" @click="goBack">返回</a-button>
      </template>
    </a-result>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from "vue-router";

const router = useRouter();

const goBack = () => {
  router.go(-1);
};
</script>

<style scoped>
#undevelopedPage {
  padding: 48px;
  text-align: center;
}
</style>