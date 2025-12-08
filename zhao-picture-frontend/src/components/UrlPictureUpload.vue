<template>


  <div class="url-picture-upload">
    <a-input-group compact style = "margin-bottom: 16px">
      <a-input v-model:value="fileUrl" style="width: calc(100% - 120px)" placeholder = "请输入图片 URL"/>
      <a-button type ="primary" :loading="loading" @click="handleUpload" style = "width: 120px">上传</a-button>
    </a-input-group>
    <img v-if="picture?.pUrl" :src="picture?.pUrl" alt="avatar"/>
  </div>



</template>
<script lang="ts" setup>
import { ref } from "vue";
import {uploadPictureByUrlUsingPost} from "@/api/pictureController.ts";
import {message} from "ant-design-vue";
const props = defineProps<Props>()
const loading = ref(false);
const fileUrl = ref<String>();
interface Props {
  picture?: API.PictureVO
  spaceId?: number
  onSuccess?: (newPicture: API.PictureVO) => void
}




//上传
const handleUpload = async () => {
  try{
    loading.value = true;
    // 上传时传递 spaceId
    const params: API.PictureUploadRequest = props.picture ? { id: props.picture.id } : {}
    params.spaceId = props.spaceId;
    params.fileUrl = fileUrl.value; // 将输入的URL传递给后端
    const res = await uploadPictureByUrlUsingPost(params)
    if(res.data.code === 0&&res.data.data) {
      message.success("图片上传成功");
      //将上传成功的图片信息传递给父组件
      props.onSuccess?.(res.data.data);
    }else {
      message.error("图片上传失败");
    }
  }catch(error){
    message.error("图片上传失败");
  }finally {
    loading.value = false;
  }


};
</script>
<style scoped></style>
