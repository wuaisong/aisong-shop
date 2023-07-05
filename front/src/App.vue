<template>
  <div class="container">
    <div style="position: absolute;z-index:1;margin: 10px;">
      <span class="demonstration">评价：</span>
      <el-rate v-model="data"/>
      <el-button type="primary" @click="increment">{{ getCount() }}</el-button>
    </div>
    <div class="container" id="myMap"></div>
  </div>
</template>
<script setup>
import {useStore} from 'vuex' // 引入useStore 方法
import {nextTick} from 'vue'
const store = useStore()  // 该方法用于返回store 实例
let data;
console.log(store)  // store 实例对象
const increment = () => {
  store.commit("increment")
  console.log(data)
}
const getCount = () => {
  return store.state.count
}
nextTick(() => {
  // 百度地图API功能
  var map = new window.BMap.Map("myMap");
  map.centerAndZoom(new BMap.Point(116.404, 39.915), 5);
  map.enableScrollWheelZoom();
  var MAX = 20;
  var markers = [];
  var pt = null;
  var i = 0;
  for (; i < MAX; i++) {
    var b = Math.random() < 0.1;
    const myIcon = new BMap.Icon(!b ? './src/car_blue.png' : './src/car_red.png', new BMap.Size(150, 150));
    pt = new BMap.Point(Math.random() * 40 + 85, Math.random() * 30 + 21);
    var marker = new BMap.Marker(pt, {icon: myIcon});
    marker.needRed = b
    markers.push(marker);
  }
  //最简单的用法，生成一个marker数组，然后调用markerClusterer类即可。
  var markerClusterer = new BMapLib.MarkerClusterer(map, {markers: markers});
  console.log(markerClusterer)
})
</script>
<style scoped>
.container {
  height: 100%;
  width: 100%;
  background-color: #FAFAFA;
  position: fixed;
}
</style>
