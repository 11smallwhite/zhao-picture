<template>
   <div id="basicLayout">
     <a-layout style="min-height: 100vh">
       <a-layout-header class="header">
         <GlobalHeader/>
       </a-layout-header>

       <a-layout>
         <GlobalSider class="sider"/>
         <a-layout-content class="content">
           <router-view />
         </a-layout-content>
       </a-layout>
     </a-layout>
   </div>
</template>

<script setup lang="ts">
import GlobalHeader from "@/components/GlobalHeader.vue";
import GlobalSider  from "@/components/GlobalSider.vue";
</script>
<style scoped>
 #basicLayout .content {
   background: linear-gradient(135deg, #f5f7fa 0%, #e4edf9 100%);
   margin: 24px;
   padding: 20px;
   border-radius: 8px;
   box-shadow: 0 2px 8px rgba(0, 0, 0, 0.09);
   min-height: calc(100vh - 140px);
 }

#basicLayout .header {
  padding-inline: 20px;
  margin-bottom: 0;
  color: unset;
  background: white;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

#basicLayout .sider {
  background: transparent;
  height: fit-content;
  max-height: calc(100vh - 140px);
}

#basicLayout :deep(.ant-menu-root) {
  border-bottom: none !important;
  border-inline-end: none !important;
  background: transparent;
 }

#basicLayout :deep(.ant-menu-item) {
  margin: 2px 8px;
  border-radius: 6px !important;
 }

#basicLayout :deep(.ant-menu-item:hover) {
  background-color: #f0f2ff !important;
 }

#basicLayout :deep(.ant-menu-item-selected) {
  background-color: #f0f2ff !important;
 }
</style>