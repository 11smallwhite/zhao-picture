<template>
  <div id="spaceAnalyzePage">
    <h2>
      空间图库分析 -
      <span v-if="queryAll"> 全部空间 </span>
      <span v-else-if="queryPublic"> 公共图库 </span>
      <span v-else>
      <a :href="`/space/${spaceId}`" target="_blank">id：{{ spaceId }}</a>
    </span>
    </h2>
    <a-row :gutter="[16, 16]">
      <!-- 空间使用分析 -->
      <a-col :xs="24" :md="12">
        <SpaceAnalyUsage :spaceId="spaceId" :queryAll="queryAll" :queryPublic="queryPublic" />
      </a-col>
      <!-- 空间分类分析 -->
      <a-col :xs="24" :md="12">
        <SpaceAnalyCategory :spaceId="spaceId" :queryAll="queryAll" :queryPublic="queryPublic" />
      </a-col>
      <!-- 标签分析 -->
      <a-col :xs="24" :md="12">
        <SpaceAnalyTag :spaceId="spaceId" :queryAll="queryAll" :queryPublic="queryPublic" />
      </a-col>
      <!-- 图片大小分段分析 -->
      <a-col :xs="24" :md="12">
        <SpaceAnalySize :spaceId="spaceId" :queryAll="queryAll" :queryPublic="queryPublic" />
      </a-col>
      <!-- 用户上传行为分析 -->
      <a-col :xs="24" :md="12">
        <SpaceAnalyUpload :spaceId="spaceId" :queryAll="queryAll" :queryPublic="queryPublic" />
      </a-col>
      <!-- 空间使用排行分析 -->
      <a-col :xs="24" :md="12">
        <SpaceAnalyRank v-if="isAdmin"
                          :spaceId="spaceId"
                          :queryAll="queryAll"
                          :queryPublic="queryPublic"
        />

      </a-col>
    </a-row>
  </div>

</template>

<script lang="ts" setup>
import { useRoute } from "vue-router";
import { computed } from "vue";
import { useLoginUserStore } from "@/stores/user.ts";
import SpaceAnalyUsage from "@/components/analyze/SpaceAnalyUsage.vue";
import SpaceAnalyCategory from "@/components/analyze/SpaceAnalyCategory.vue";
import SpaceAnalyTag from "@/components/analyze/SpaceAnalyTag.vue";
import SpaceAnalySize from "@/components/analyze/SpaceAnalySize.vue";
import SpaceAnalyUpload from "@/components/analyze/SpaceAnalyUpload.vue";
import SpaceAnalyRank from "@/components/analyze/SpaceAnalyRank.vue";

const route = useRoute()
const loginUserStore = useLoginUserStore()
const loginUser = loginUserStore.loginUser

const isAdmin = computed(() => {
  return loginUser.userType === 1
})

// 空间 id
const spaceId = computed(() => {
  return route.query?.spaceId as string
})

// 是否查询所有空间
const queryAll = computed(() => {
  return !!route.query?.queryAll
})

// 是否查询公共空间
const queryPublic = computed(() => {
  return !!route.query?.queryPublic
})

</script>

<style scoped>
</style>
