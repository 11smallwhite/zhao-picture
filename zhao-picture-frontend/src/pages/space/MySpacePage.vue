<template>
  <div id = "mySpace">
    <p>正在跳转中</p>
  </div>

</template>


<script lang="ts" setup>
import { useRouter } from "vue-router";
import { useLoginUserStore } from "@/stores/user.ts";
import { getSpaceVoUsingPost } from "@/api/spaceController.ts";
import { message } from "ant-design-vue";
import { onMounted } from "vue";

const router = useRouter();
const loginUserStore = useLoginUserStore();

const checkUserLogin = async () => {
  //检查用户是否登录
  const loginUser = loginUserStore.loginUser;
  if(!loginUser?.id){
    router.push("/user/login");
    return;
  }
  //如果用户登录了，就去查询登录用户的空间
  const res = await getSpaceVoUsingPost({
    userId: loginUser.id
  })
  if(res.data.code ===0&&res.data.data){
    const space = res.data.data;
    router.replace(`/space/${space.id}`);
  }else{
    router.push({
      path: "/add_space",
    })
    message.warn("请先创建空间")
  }

}


onMounted(() =>{
  checkUserLogin();
})

</script>
