<template>
  <div id="userRegisterPage">
    <div class="register-container">
      <div class="register-header">
        <h2 class="title">用户注册</h2>
        <div class="desc">多功能云图库</div>
      </div>
      
      <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit" class="register-form">
        <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
          <a-input v-model:value="formState.userAccount" placeholder="请输入10位账号(数字/字母)">
            <template #prefix>
              <UserOutlined />
            </template>
          </a-input>
        </a-form-item>
        
        <a-form-item name="userPassword" :rules="[
          { required: true, message: '请输入密码' },
        ]">
          <a-input-password v-model:value="formState.userPassword" placeholder="请输入6-16位密码">
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>

        <a-form-item name="checkPassword" :rules="[
          { required: true, message: '请再次输入确认密码' },
        ]">
          <a-input-password v-model:value="formState.checkPassword" placeholder="请再次输入确认密码">
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>

        <div class="tips">
          已有账号!
          <RouterLink to="/user/login">去登录</RouterLink>
        </div>
        
        <a-form-item>
          <a-button type="primary" html-type="submit" class="register-button">注册</a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from 'vue';
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';
import { useRouter } from "vue-router";
import { message } from "ant-design-vue";
import { userRegisterUsingPost } from "@/api/userController.ts";

const formState = reactive<API.UserRegisterRequest>({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
});

const router = useRouter();

const handleSubmit = async (values: any) => {
  if (formState.checkPassword != formState.userPassword) {
    message.error("两次输入的密码不一致")
    return;
  }

  const res = await userRegisterUsingPost(values);
  if (res.data.code === 0 && res.data.data) {
    message.success("注册成功")
    router.push({
      path: "/user/login",
      replace: true,
    })
  } else {
    message.error("注册失败" + res.data.message);
  }
};
</script>

<style scoped>
#userRegisterPage {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 100px);
  padding: 24px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4edf9 100%);
}

.register-container {
  width: 100%;
  max-width: 400px;
  padding: 40px 30px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  text-align: center;
}

.register-header {
  margin-bottom: 30px;
}

.title {
  font-size: 28px;
  font-weight: 600;
  color: #333;
  margin-bottom: 8px;
}

.desc {
  font-size: 16px;
  color: #645bff;
  font-weight: 500;
}

.register-form {
  text-align: left;
}

.tips {
  margin-bottom: 20px;
  color: #888;
  font-size: 14px;
  text-align: right;
}

.tips a {
  color: #645bff;
  transition: color 0.3s;
}

.tips a:hover {
  color: #8a85ff;
  text-decoration: underline;
}

.register-button {
  width: 100%;
  height: 42px;
  font-size: 16px;
  font-weight: 500;
  background: linear-gradient(45deg, #645bff, #8a85ff);
  border: none;
  border-radius: 6px;
  box-shadow: 0 4px 12px rgba(100, 91, 255, 0.3);
  transition: all 0.3s ease;
}

.register-button:hover {
  background: linear-gradient(45deg, #8a85ff, #645bff);
  box-shadow: 0 6px 16px rgba(100, 91, 255, 0.4);
  transform: translateY(-2px);
}
</style>