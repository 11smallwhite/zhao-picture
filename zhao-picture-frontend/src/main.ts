

import { createApp } from 'vue'
import App from './App.vue'
import Antd from 'ant-design-vue'
import "ant-design-vue/dist/reset.css"
import router from './router'
import {createPinia} from 'pinia'
import '@/access'
import VueCropper from 'vue-cropper';
import 'vue-cropper/dist/index.css'


const pinia = createPinia()
createApp(App)
  .use(router)
  .use(Antd)
  .use(pinia)
  .use(VueCropper)
  .mount('#app')
