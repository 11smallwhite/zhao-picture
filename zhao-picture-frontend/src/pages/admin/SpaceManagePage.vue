<template>
  <div id="SpaceManagePage">
    <a-flex justify="space-between">
      <h2>空间管理</h2>
      <a-space>
        <a-button type="primary" href="/add_space" target="_self">+ 创建空间</a-button>
        <a-button type="primary" ghost href="/space_analyze?queryPublic=1" target="_blank">
          分析公共图库
        </a-button>
        <a-button type="primary" ghost href="/space_analyze?queryAll=1" target="_blank">
          分析全空间
        </a-button>
      </a-space>

    </a-flex>


    <a-form layout="inline" :model="searchParams" @finish="onSearch">
      <a-form-item label="关键词" name="spaceName">
        <a-input v-model:value="searchParams.spaceName" placeholder="从名称搜索" allow-clear />
      </a-form-item>

      <a-form-item label="空间id" name="id">
        <a-input v-model:value="searchParams.id" placeholder="请输入空间id" allow-clear />
      </a-form-item>
      <a-form-item label="用户Id" name="userId">
        <a-input v-model:value="searchParams.userId" placeholder="请输入用户id" allow-clear />
      </a-form-item>

      <a-form-item label="空间级别" name="spaceLevel">
        <a-select
          v-model:value="searchParams.spaceLevel"
          :options = "SPACE_LEVEL_OPTIONS"
          placeholder="请选择空间等级"
          style="min-width: 180px"
          allow-clear
        />
      </a-form-item>

      <a-form-item>
        <a-button type="primary" html-type="submit">搜索</a-button>
      </a-form-item>
    </a-form>

    <a-table
      :columns="columns"
      :data-source="dataList"
      :pagination="pagination"
      @change="doTableChange"
    >
      <template #bodyCell="{ column, record }">
        <template v-if="column.dataIndex === 'spaceLevel'">
          <a-tag>{{ SPACE_LEVEL_MAP[record.spaceLevel] }}</a-tag>
        </template>

        <template v-if="column.dataIndex === 'spaceUseInfo'">
          <div>大小：{{ formatSize(record.totalSize) }}/{{ formatSize(record.maxSize) }}</div>
          <div>数量：{{ record.totalCount }}/{{ record.maxCount }}</div>
        </template>

        <template v-else-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
        </template>
        <template v-else-if="column.dataIndex === 'editTime'">
          {{ dayjs(record.editTime).format("YYYY-MM-DD HH:mm:ss") }}
        </template>

        <!--        如果图片是未进行审核的，就给按钮，让管理员进行审核-->
        <template v-else-if="column.key === 'action'">
          <a-space wrap>
            <a-button type="link" :href="`/add_space?id=${record.id}`" target="_self"
              >编辑
            </a-button>
            <a-button type="link" danger @click="doDelete(record.id)">删除</a-button>
            <a-button type="link" :href="`/space_analyze?spaceId=${record.id}`" target="_blank">
              分析
            </a-button>

          </a-space>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from "vue";
import { message } from "ant-design-vue";
import dayjs from "dayjs";

import { deleteSpaceUsingPost, selectSpaceUsingPost } from "@/api/spaceController.ts";
import { SPACE_LEVEL_MAP, SPACE_LEVEL_OPTIONS } from "@/constants/space.ts";
import { formatSize } from "@/utils/indext.ts";
const columns = [
  {
    title: "id",
    dataIndex: "id",
    width: 80,
  },
  {
    title: "空间名称",
    dataIndex: "spaceName",
  },
  {
    title: "空间等级",
    dataIndex: "spaceLevel",
  },
  {
    title: "使用情况",
    dataIndex: "spaceUseInfo",
  },
  {
    title: "用户 id",
    dataIndex: "userId",
    width: 80,
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "编辑时间",
    dataIndex: "editTime",
  },
  {
    title: "操作",
    key: "action",
  },
];

const dataList = ref([]);
const total = ref(0);

// 删除数据
const doDelete = async (id: string) => {
  if (!id) {
    return;
  }
  const res = await deleteSpaceUsingPost({ id });
  if (res.data.code === 0) {
    message.success("删除成功");
    // 刷新数据
    fecthData();
  } else {
    message.error("删除失败");
  }
};

//搜索函数
const onSearch = (searchValue: string) => {
  searchParams.pageNum = 1;
  fecthData();
};

//搜索参数
const searchParams = reactive<API.SpaceQueryRequest>({
  pageSize: 5,
  pageNum: 1,
  sortField: "createTime",
  sortOrder: "desc",
});
// 分页参数
const pagination = computed(() => {
  return {
    pageNum: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    showSizeChanger: true,
    showTotal: (total) => `共 ${total} 条`,
  };
});
// 表格变化处理
const doTableChange = (page: any) => {
  searchParams.pageNum = page.current;
  searchParams.pageSize = page.pageSize;
  fecthData();
};

const fecthData = async () => {
  const res = await selectSpaceUsingPost({
    ...searchParams,
  });
  if (res.data.code === 0 && res.data.data) {
    dataList.value = res.data.data.records ?? [];
    total.value = res.data.data.total ?? 0;
  } else {
    message.error("加载数据失败" + res.data.message);
  }
};

onMounted(() => {
  console.log('searchParams初始值：', searchParams.spaceLevel); // 查看 spaceLevel 是否为 0
  fecthData();
});
</script>
