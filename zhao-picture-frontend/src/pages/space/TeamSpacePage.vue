<template>
  <div id="teamSpacePage">
    <a-result status="info" title="待开发" sub-title="团队空间功能正在开发中，敬请期待！">
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
#teamSpacePage {
  padding: 48px;
  text-align: center;
}
</style>