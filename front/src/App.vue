<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <el-menu router :default-active="route.path" class="el-menu-demo" mode="horizontal"
                 @select="handleSelect">
          <el-menu-item index="/first">Processing Center</el-menu-item>
          <el-menu-item index="/second">Orders</el-menu-item>
          <el-menu-item index="/">Orders</el-menu-item>
        </el-menu>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>


</template>

<script lang="ts" setup>
import {ref} from 'vue'
import {useRoute} from "vue-router"

const route = useRoute();
const activeIndex = ref('1')
console.log(route.path)
const handleSelect = (key: string, keyPath: string[]) => {
  console.log(key, keyPath)
}
</script>
