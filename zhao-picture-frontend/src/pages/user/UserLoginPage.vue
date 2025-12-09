<template>
  <div id="userLoginPage">
    <div class="login-container">
      <div class="login-header">
        <h2 class="title">用户登录</h2>
        <div class="desc">多功能云图库</div>
      </div>
      
      <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit" class="login-form">
        <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
          <a-input v-model:value="formState.userAccount" placeholder="请输入账号">
            <template #prefix>
              <UserOutlined />
            </template>
          </a-input>
        </a-form-item>
        
        <a-form-item
          name="userPassword"
          :rules="[
            { required: true, message: '请输入密码' },
          ]"
        >
          <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码">
            <template #prefix>
              <LockOutlined />
            </template>
          </a-input-password>
        </a-form-item>
        
        <div class="tips">
          没有账号？
          <RouterLink to="/user/register">去注册</RouterLink>
        </div>
        
        <a-form-item>
          <a-button type="primary" html-type="submit" class="login-button">登录</a-button>
        </a-form-item>
      </a-form>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { reactive } from 'vue';
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';
import { userLoginUsingPost } from "@/api/userController.ts";
import { useLoginUserStore } from "@/stores/user.ts";
import { message } from "ant-design-vue";
import { useRouter } from "vue-router";

const formState = reactive<API.UserLoginRequest>({
  userAccount: '',
  userPassword: '',
});

const loginUserStore = useLoginUserStore();
const router = useRouter();

const handleSubmit = async (values: any) => {
  const res = await userLoginUsingPost(values)

  if (res.data.code === 0 && res.data.data) {
    await loginUserStore.fetchLoginUser()
    message.success("登录成功")
    router.push({
      path: "/",
      replace: true,
    })
  } else {
    message.error("登录失败," + res.data.message)
  }
};
</script>

<style scoped>
#userLoginPage {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 100px);
  padding: 24px;
  background: linear-gradient(135deg, #f5f7fa 0%, #e4edf9 100%);
}

.login-container {
  width: 100%;
  max-width: 400px;
  padding: 40px 30px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
  text-align: center;
}

.login-header {
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

.login-form {
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

.login-button {
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

.login-button:hover {
  background: linear-gradient(45deg, #8a85ff, #645bff);
  box-shadow: 0 6px 16px rgba(100, 91, 255, 0.4);
  transform: translateY(-2px);
}
</style>