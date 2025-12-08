<template>

  <div id="homePage">
    <div class ="search-bar">
      <a-space style="width: 100%" direction="vertical">
        <a-input-search
          style="width: 100%"
          v-model:value="searchParams.searchText"
          placeholder="从海量图片里寻找你的心仪"
          enter-button="搜索"
          size="large"
          @search="doSearch"
        />
      </a-space>
    </div>
    <!-- 分类 + 标签 -->
    <a-tabs v-model:activeKey="selectedCategory" @change="doSearch">
      <a-tab-pane key="all" tab="全部" />
      <a-tab-pane v-for="category in categoryList" :key="category" :tab="category" />
    </a-tabs>
    <div class="tag-bar">
      <span style="margin-right: 8px">标签：</span>
      <a-space :size="[0, 8]" wrap>
        <a-checkable-tag
          v-for="(tag, index) in tagList"
          :key="tag"
          v-model:checked="selectedTagList[index]"
          @change="doSearch"
        >
          {{ tag }}
        </a-checkable-tag>
      </a-space>
    </div>



    <!-- 图片列表 -->
    <!-- 图片列表 -->
    <PictureList :dataList="dataList" :loading="loading" />
    <a-pagination
      style="text-align: right"
      v-model:current="searchParams.pageNum"
      v-model:pageSize="searchParams.pageSize"
      :total="total"
      @change="onPageChange"
    />

  </div>
</template>

<script setup lang="ts">
// 数据
import { listPictureTagCategoryUsingGet, selectUsingPost } from "@/api/pictureController.ts";
import { message } from "ant-design-vue";
import { computed, onMounted, reactive, ref } from "vue";
import {useRouter} from "vue-router";
import PictureList from "@/pages/picture/PictureListPage.vue";

const dataList = ref([]);
const total = ref(0);
const loading = ref(true);
const categoryList = ref<string[]>([]);
const selectedCategory = ref<string>("all");
const tagList = ref<string[]>([]);
const selectedTagList = ref<string[]>([]);
const router = useRouter()


// 跳转至图片详情
const doClickPicture = (picture) => {
  router.push({
    path: `/picture/${picture.id}`,
  })
}


// 获取标签和分类选项
const getTagCategoryOptions = async () => {
  const res = await listPictureTagCategoryUsingGet();
  if (res.data.code === 0 && res.data.data) {
    // 转换成下拉选项组件接受的格式
    categoryList.value = res.data.data.categoryList ?? [];
    tagList.value = res.data.data.tagList ?? [];
  } else {
    message.error("加载分类标签失败，" + res.data.message);
  }
};



// 搜索条件
const searchParams = reactive<API.PictureQueryRequest>({
  pageNum: 1,
  pageSize: 12,
  sortField: "createTime",
  sortOrder: "descend",
});

// 分页参数
const pagination = computed(() => {
  return {
    current: searchParams.pageNum ?? 1,
    pageSize: searchParams.pageSize ?? 10,
    total: total.value,
    // 切换页号时，会修改搜索参数并获取数据
    onChange: (page, pageSize) => {
      searchParams.pageNum = page;
      searchParams.pageSize = pageSize;
      fetchData();
    },
  };
});

// 获取数据
const fetchData = async () => {
  loading.value = true
  // 转换搜索参数
  const params = {
    ...searchParams,
    pTags: [],
  }
  if (selectedCategory.value !== 'all') {
    params.pCategory = selectedCategory.value
  }
  selectedTagList.value.forEach((useTag, index) => {
    if (useTag) {
      params.pTags.push(tagList.value[index])
    }
  })
  const res = await selectUsingPost(params)
  if (res.data.data) {
    dataList.value = res.data.data.records ?? []
    total.value = res.data.data.total ?? 0
  } else {
    message.error('获取数据失败，' + res.data.message)
  }
  loading.value = false
}




// 页面加载时请求一次
onMounted(() => {
  fetchData();
  getTagCategoryOptions();

});
const doSearch = () => {
  // 重置搜索条件
  searchParams.pageNum = 1;
  fetchData();
};
</script>

<style scoped>
#homePage{
  margin-bottom: 16px;
}
.search-bar {
  max-width: 800px; /* 最大宽度限制 */
  margin: 0 auto 16px; /* 水平居中 */
}
.tag-bar{
  margin-bottom: 16px;
}
</style>
