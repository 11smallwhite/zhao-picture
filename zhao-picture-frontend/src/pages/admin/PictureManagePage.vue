<template>
  <div id="PictureManagePage">
    <a-flex justify="space-between">
      <h2>图片管理</h2>
      <a-space>
        <a-button type="primary" href="/add_picture" target="_self">+ 创建图片</a-button>
        <a-button type="primary" href="/add_picture/batch" target="_self" ghost>批量创建图片</a-button>
      </a-space>
    </a-flex>

    <a-form layout="inline" :model="searchParams" @finish="onSearch">
      <a-form-item label="关键词" name="searchText">
        <a-input
          v-model:value="searchParams.searchText"
          placeholder="从名称和简介搜索"
          allow-clear
        />
      </a-form-item>

      <a-form-item label="类型" name="pCategory">
        <a-input v-model:value="searchParams.pCategory" placeholder="请输入类型" allow-clear />
      </a-form-item>

      <a-form-item label="标签" name="pTags">
        <a-select
          v-model:value="searchParams.pTags"
          mode="tags"
          placeholder="请输入标签"
          style="min-width: 180px"
          allow-clear
        />
      </a-form-item>

      <a-form-item label="审核状态" name="auditStatus">
        <a-select
          v-model:value="searchParams.auditStatus"
          :options="PIC_AUDIT_STATUS_OPTIONS"
          placeholder="请选择审核状态"
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
          <template v-if="column.dataIndex === 'pUrl'">
            <a-image :src="record.pUrl" :width="120" />
          </template>
          <!-- 标签 -->
          <template v-if="column.dataIndex === 'pTags'">
            <a-space wrap>
              <a-tag v-for="tag in JSON.parse(record.pTags || '[]')" :key="tag">{{ tag }}</a-tag>
            </a-space>
          </template>

          <template v-if="column.dataIndex === 'auditMsg'">
            <div>审核状态：{{ PIC_AUDIT_STATUS_MAP[record.auditStatus] }}</div>
            <div>审核信息：{{ record.auditMsg }}</div>
            <div>审核人：{{ record.auditId }}</div>
          </template>

          <!-- 图片信息 -->
          <template v-if="column.dataIndex === 'picInfo'">
            <div>格式：{{ record.pFormat }}</div>
            <div>宽度：{{ record.pWidth }}</div>
            <div>高度：{{ record.pHeight }}</div>
            <div>宽高比：{{ record.pScale }}</div>
            <div>大小：{{ (record.pSize / 1024).toFixed(2) }}KB</div>
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

              <a-button
                v-if="record.auditStatus !== PIC_AUDIT_STATUS_ENUM.REVIEW_PASS"
                type="link"
                @click="handleAudit(record, PIC_AUDIT_STATUS_ENUM.REVIEW_PASS)"
              >
                通过
              </a-button>


              <a-button
                v-if="record.auditStatus !== PIC_AUDIT_STATUS_ENUM.REVIEW_FAIL"
                type="link"
                danger
                @click="handleAudit(record, PIC_AUDIT_STATUS_ENUM.REVIEW_FAIL)"
              >
                拒绝
              </a-button>



              <a-button type="link" :href="`/add_picture?id=${record.id}`" target="_self"
              >编辑
              </a-button>
              <a-button type="link" danger @click="doDelete(record.id)">删除</a-button>
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
import {
  auditPictureUsingPost,
  deletePictureByIdUsingPost,
  selectAdminUsingPost,
} from "@/api/pictureController.ts";
import {
  PIC_AUDIT_STATUS_ENUM,
  PIC_AUDIT_STATUS_MAP,
  PIC_AUDIT_STATUS_OPTIONS,
} from "@/constants/picture.ts";
const columns = [
  {
    title: "id",
    dataIndex: "id",
    width: 80,
  },
  {
    title: "图片",
    dataIndex: "pUrl",
  },
  {
    title: "名称",
    dataIndex: "pName",
  },
  {
    title: "简介",
    dataIndex: "pIntroduction",
    ellipsis: true,
  },
  {
    title: "类型",
    dataIndex: "pCategory",
  },
  {
    title: "标签",
    dataIndex: "pTags",
  },
  {
    title: "图片信息",
    dataIndex: "picInfo",
  },
  {
    title: "用户 id",
    dataIndex: "userId",
    width: 80,
  },
  {
    title: "审核信息",
    dataIndex: "auditMsg",
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
  const res = await deletePictureByIdUsingPost({ id });
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
const searchParams = reactive<API.PictureQueryRequest>({
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
  const res = await selectAdminUsingPost({
    ...searchParams,
  });
  if (res.data.code === 0 && res.data.data) {
    dataList.value = res.data.data.records ?? [];
    total.value = res.data.data.total ?? 0;
  } else {
    message.error("加载数据失败" + res.data.message);
  }
};

//审核函数
const handleAudit = async (record: API.Picture, auditStatus: number) => {
  const auditMsg = auditStatus === PIC_AUDIT_STATUS_ENUM.REVIEW_PASS ? "管理员操作通过" : "管理员操作拒绝";
  const res = await auditPictureUsingPost({
    pictureId: record.id,
    auditStatus,
    auditMsg,
  });
  if (res.data.code === 0) {
    message.success("审核操作成功");
    // 重新获取列表
    fecthData();
  } else {
    message.error("审核操作失败，" + res.data.message);
  }
};

onMounted(() => {
  fecthData();
});
</script>

