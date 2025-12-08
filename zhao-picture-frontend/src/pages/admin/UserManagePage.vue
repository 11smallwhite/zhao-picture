<template>
  <div id="UserManagePage">
    <a-form layout="inline" :model="searchParams" @finish="onSearch">
      <a-form-item label="账号">
        <a-input v-model:value="searchParams.userAccount" placeholder="输入账号" />
      </a-form-item>
      <a-form-item label="昵称">
        <a-input v-model:value="searchParams.userName" placeholder="输入昵称" />
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
        <template v-if="column.dataIndex === 'userAvatar'">
          <a-image :src="record.userAvatar" :width="120" />
        </template>

        <template v-if="column.dataIndex === 'userType'">
          <div v-if="record.userType === 1">
            <a-tag color="green">管理员</a-tag>
          </div>
          <div v-else>
            <a-tag color="blue">普通用户</a-tag>
          </div>
        </template>

        <template v-if="column.dataIndex === 'createTime'">
          {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
        </template>

        <template v-else-if="column.key === 'action'">
          <a-button danger @click="doDelete(record.id)">删除</a-button>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script lang="ts" setup>
import { computed, onMounted, reactive, ref } from "vue";
import { selectUsingPost1, userDeleteByIdUsingPost } from "@/api/userController.ts";
import { message } from "ant-design-vue";
import dayjs from "dayjs";
const columns = [
  {
    title: "id",
    dataIndex: "id",
  },
  {
    title: "账号",
    dataIndex: "userAccount",
  },
  {
    title: "用户名",
    dataIndex: "userName",
  },
  {
    title: "头像",
    dataIndex: "userAvatar",
  },
  {
    title: "简介",
    dataIndex: "userIntroduction",
  },
  {
    title: "用户角色",
    dataIndex: "userType",
  },
  {
    title: "创建时间",
    dataIndex: "createTime",
  },
  {
    title: "更新时间",
    dataIndex: "updateTime",
  },
  {
    title: "操作",
    key: "action",
  },
];

const dataList = ref<API.UserVO[]>([]);
const total = ref(0);

// 删除数据
const doDelete = async (id: string) => {
  if (!id) {
    return;
  }
  const res = await userDeleteByIdUsingPost({ id });
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
const searchParams = reactive<API.UserQueryRequest>({
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
  const res = await selectUsingPost1({
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
  fecthData();
});
</script>
