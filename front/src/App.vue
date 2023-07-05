<template>
  <div class="common-layout">
    <el-container>
      <el-header>
        <router-link to="/second">页面一</router-link>
        <router-link to="/first">页面二</router-link>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>
<script lang="ts" setup>
import {ref, watch} from 'vue'
import {useRoute, useRouter} from "vue-router";
import type {TabsPaneContext} from 'element-plus'

const route = useRoute()
const router = useRouter()

const activeName = ref('first')
watch(activeName, (newVal, oldVal) => {
  console.log(`New: ${newVal}, Old: ${oldVal}`)
  console.log(`${route.path}?tab=${newVal}`)
  router.push(`${route.path}?tab=${newVal}`)
})
const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}
</script>
<style>
.demo-tabs > .el-tabs__content {
  padding: 32px;
  color: #6b778c;
  font-size: 32px;
  font-weight: 600;
}
</style>

