<template>
  <div class="user-detail-page">
    <a-card title="用户信息">
      <template #extra>
        <a-button v-if="!isEditing" type="primary" @click="startEdit">编辑</a-button>
        <div v-else>
          <a-space>
            <a-button @click="cancelEdit">取消</a-button>
            <a-button type="primary" @click="saveEdit" :loading="saving">保存</a-button>
          </a-space>
        </div>
      </template>
      
      <a-row :gutter="[24, 24]">
        <a-col :span="24" style="text-align: center;">
          <a-avatar 
            :size="128" 
            :src="editingUser.userAvatar || 'https://xsgames.co/randomusers/avatar.php?g=pixel&key=1'" 
          />
          <h2 style="margin-top: 16px;">{{ editingUser.userName }}</h2>
          
          <!-- 头像上传区域（仅在编辑模式下显示） -->
          <div v-if="isEditing" style="margin-top: 16px;">
            <a-button @click="openAvatarUpload">更换头像</a-button>
            <a-upload
              ref="avatarUploaderRef"
              name="file"
              :show-upload-list="false"
              :before-upload="beforeAvatarUpload"
              :customRequest="handleAvatarUpload"
              style="display: none"
            />
          </div>
        </a-col>
        
        <a-col :span="24">
          <!-- 查看模式 -->
          <a-descriptions 
            v-if="!isEditing" 
            bordered 
            :column="{ xs: 1, sm: 1, md: 1, lg: 1, xl: 1, xxl: 1 }"
          >
            <a-descriptions-item label="用户ID">
              {{ loginUser.id }}
            </a-descriptions-item>
            <a-descriptions-item label="用户名">
              {{ loginUser.userName }}
            </a-descriptions-item>
            <a-descriptions-item label="账号">
              {{ loginUser.userAccount }}
            </a-descriptions-item>
            <a-descriptions-item label="角色">
              {{ loginUser.userType === 1 ? '管理员' : '普通用户' }}
            </a-descriptions-item>
            <a-descriptions-item label="注册时间">
              {{ formatDate(loginUser.createTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="最后更新时间">
              {{ formatDate(loginUser.updateTime) }}
            </a-descriptions-item>
            <a-descriptions-item label="个人简介" :span="2">
              {{ loginUser.userIntroduction || '暂无简介' }}
            </a-descriptions-item>
          </a-descriptions>
          
          <!-- 编辑模式 -->
          <a-form 
            v-else
            :model="editingUser"
            layout="vertical"
          >
            <a-form-item label="用户名">
              <a-input v-model:value="editingUser.userName" />
            </a-form-item>
            
            <a-form-item label="个人简介">
              <a-textarea v-model:value="editingUser.userIntroduction" :rows="4" />
            </a-form-item>
            
            <a-form-item label="账号">
              <a-input v-model:value="editingUser.userAccount" disabled />
            </a-form-item>
            
            <a-form-item label="角色">
              <a-input :value="editingUser.userType === 1 ? '管理员' : '普通用户'" disabled />
            </a-form-item>
            
            <a-form-item label="注册时间">
              <a-input :value="formatDate(editingUser.createTime)" disabled />
            </a-form-item>
            
            <a-form-item label="最后更新时间">
              <a-input :value="formatDate(editingUser.updateTime)" disabled />
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </a-card>
    
    <!-- 头像裁剪模态框 -->
    <ImageCropper 
      ref="imageCropperRef" 
      :on-success="handleAvatarUploadSuccess" 
    />
  </div>
</template>

<script setup lang="ts">
import { useLoginUserStore } from '@/stores/user.ts';
import { ref, reactive } from 'vue';
import { Avatar, message } from 'ant-design-vue';
import { userEditUsingPost, uploadAvatarUsingPost } from '@/api/userController.ts';
import ImageCropper from '@/components/ImageCropper.vue';
import type { UploadProps } from 'ant-design-vue';

const loginUserStore = useLoginUserStore();

// 用户信息状态
const loginUser = ref({ ...loginUserStore.loginUser });

// 编辑状态
const isEditing = ref(false);
const saving = ref(false);

// 编辑中的用户信息
const editingUser = reactive({ ...loginUserStore.loginUser });

// 组件引用
const avatarUploaderRef = ref();
const imageCropperRef = ref();

/**
 * 开始编辑
 */
const startEdit = () => {
  isEditing.value = true;
  Object.assign(editingUser, loginUserStore.loginUser);
};

/**
 * 取消编辑
 */
const cancelEdit = () => {
  isEditing.value = false;
};

/**
 * 保存编辑
 */
const saveEdit = async () => {
  saving.value = true;
  try {
    const res = await userEditUsingPost({
      id: editingUser.id,
      userName: editingUser.userName,
      userIntroduction: editingUser.userIntroduction,
      userAvatar: editingUser.userAvatar,
    });
    
    if (res.data.code === 0) {
      message.success('用户信息更新成功');
      
      // 强制刷新用户信息，确保下次获取的是最新数据
      await loginUserStore.fetchLoginUser();
      
      // 更新显示的用户信息（使用从服务器获取的最新数据）
      Object.assign(loginUser.value, loginUserStore.loginUser);
      Object.assign(editingUser, loginUserStore.loginUser);
      
      isEditing.value = false;
    } else {
      message.error('更新失败: ' + res.data.message);
    }
  } catch (err) {
    message.error('更新失败: ' + err.message);
  } finally {
    saving.value = false;
  }
};

/**
 * 打开头像上传
 */
const openAvatarUpload = () => {
  avatarUploaderRef.value?.$el.querySelector('input[type="file"]')?.click();
};

/**
 * 头像上传前检查
 */
const beforeAvatarUpload: UploadProps['beforeUpload'] = (file) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJpgOrPng) {
    message.error('只能上传 JPG/PNG 格式的文件!');
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    message.error('图片大小不能超过 2MB!');
  }
  return isJpgOrPng && isLt2M;
};

/**
 * 处理头像上传
 */
const handleAvatarUpload = async ({ file }: any) => {
  try {
    const res = await uploadAvatarUsingPost({}, file);
    if (res.data.code === 0 && res.data.data) {
      editingUser.userAvatar = res.data.data.userAvatar;
      message.success('头像上传成功');
    } else {
      message.error('头像上传失败: ' + res.data.message);
    }
  } catch (err) {
    message.error('头像上传失败: ' + err.message);
  }
};

/**
 * 处理头像上传成功
 * @param newPicture 新上传的图片信息
 */
const handleAvatarUploadSuccess = (newPicture: API.PictureVO) => {
  editingUser.userAvatar = newPicture.pUrl;
  message.success('头像上传成功');
};

/**
 * 格式化日期
 * @param dateStr 日期字符串
 */
const formatDate = (dateStr: string) => {
  if (!dateStr) return '未知';
  const date = new Date(dateStr);
  return date.toLocaleString('zh-CN');
};
</script>

<style scoped>
.user-detail-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

:deep(.ant-descriptions-item-label) {
  font-weight: bold;
  width: 150px;
}
</style>