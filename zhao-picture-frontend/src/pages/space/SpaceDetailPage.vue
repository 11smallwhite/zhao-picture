<template>
  <!-- 空间信息 -->
  <a-flex justify="space-between">
    <h2>{{ space.spaceName }}（私有空间）</h2>
    <a-space size="middle">
      <a-button
        type="primary"
        ghost
        :icon="h(BarChartOutlined)"
        :href="`/space_analyze?spaceId=${id}`"
        target="_blank"
      >
        空间分析
      </a-button>

      <a-button type="primary" :href="`/add_picture?spaceId=${id}`" target="_self">
        + 创建图片
      </a-button>
      <a-tooltip :title="`占用空间 ${formatSize(space.totalSize)} / ${formatSize(space.maxSize)}`">
        <a-progress
          type="circle"
          :percent="((space.totalSize * 100) / space.maxSize).toFixed(1)"
          :size="42"
        />
      </a-tooltip>
    </a-space>
  </a-flex>
  <!-- 搜索表单 -->
  <PictureSearchForm :onSearch="onSearch" />

  <!-- 图片列表 -->
  <PictureList :dataList="dataList" :loading="loading" showOp :onReload="fetchData" />
  <a-pagination
    style="text-align: right"
    v-model:current="searchParams.pageNum"
    v-model:pageSize="searchParams.pageSize"
    :total="total"
    :show-total="() => `图片总数 ${total} / ${space.maxCount}`"
    @change="onPageChange"
  />
</template>
<script lang="ts" setup>
import { h, onMounted, reactive, ref } from "vue";
import { message } from "ant-design-vue";
import { getSpaceVoUsingPost } from "@/api/spaceController.ts";
import { formatSize } from "@/utils/indext.ts";
import { selectBySpaceUsingPost } from "@/api/pictureController.ts";
import PictureList from "@/pages/picture/PictureListPage.vue";
import PictureSearchForm from "@/pages/picture/PictureSearchForm.vue";
import { BarChartOutlined } from "@ant-design/icons-vue";

const props = defineProps<{
  id: string | number;
}>();
const space = ref<API.SpaceVO>({});

// 获取空间详情
const fetchSpaceDetail = async () => {
  try {
    const res = await getSpaceVoUsingPost({
      id: props.id,
    });
    if (res.data.code === 0 && res.data.data) {
      space.value = res.data.data;
    } else {
      message.error("获取空间详情失败，" + res.data.message);
    }
  } catch (e: any) {
    message.error("获取空间详情失败：" + e.message);
  }
};

// 数据
const dataList = ref([]);
const total = ref(0);
const loading = ref(true);

// 搜索条件
const searchParams = reactive<API.PictureQueryRequest>({
  pageNum: 1,
  pageSize: 12,
  sortField: "createTime",
  sortOrder: "descend",
});

// 分页参数
const onPageChange = (pageNum, pageSize) => {
  searchParams.pageNum = pageNum;
  searchParams.pageSize = pageSize;
  fetchData();
};
const onSearch = (newSpaceParams: API.PictureQueryRequest) => {
  // 直接展开所有参数，确保扁平化
  Object.assign(searchParams, {
    ...newSpaceParams,
    ...newSpaceParams.value, // 将value内的参数提升到外层
    pageNum: 1,
  });

  // 删除可能残留的value属性
  delete searchParams.value;

  fetchData();
};
// 获取数据
const fetchData = async () => {
  loading.value = true;
  // 转换搜索参数
  const params = {
    spaceId: props.id,
    ...searchParams,
  };
  const res = await selectBySpaceUsingPost(params);
  if (res.data.data) {
    dataList.value = res.data.data.records ?? [];
    total.value = res.data.data.total ?? 0;
  } else {
    message.error("获取数据失败，" + res.data.message);
  }
  loading.value = false;
};

// 页面加载时请求一次
onMounted(() => {
  fetchData();
  fetchSpaceDetail();
});
</script>
<style scoped></style>
