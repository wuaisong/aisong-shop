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

