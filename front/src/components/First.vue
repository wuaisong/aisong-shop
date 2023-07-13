<template>
  <div class="container">
    <div style="position: absolute;z-index:1;margin: 10px;">
      <span class="demonstration">评价：{{ store.double }}-{{ store.count }}</span>

      <el-button type="primary" @click="increment">{{ getCount() }}</el-button>
    </div>
    <div class="container" id="myMap"></div>
  </div>
</template>
<script setup>
import {nextTick} from 'vue'
import {useCounterStore} from '@/stores/counter'

import carBlue from '@/assets/car_blue.png'
import carRed from '@/assets/car_red.png'

const obj = {};
console.log(obj?.a?.b ?? 233 ) //233
const obj = {a: {b: 1}};
console.log(obj?.a?.b??233) //1



// 可以在组件中的任意位置访问 `store` 变量 ✨
const store = useCounterStore()
console.log(store)  // store 实例对象
const increment = () => {
  store.increment()
  store.double()
}
const getCount = () => {
  return store.count
}
nextTick(() => {
  // 百度地图API功能
  var map = new window.BMap.Map("myMap");
  map.centerAndZoom(new BMap.Point(116.404, 39.915), 5);
  map.enableScrollWheelZoom();
  var MAX = 10;
  var markers = [];
  var pt = null;
  var i = 0;
  for (; i < MAX; i++) {
    var b = Math.random() < 0.1;
    const myIcon = new BMap.Icon(!b ? carBlue : carRed, new BMap.Size(150, 150));
    pt = new BMap.Point(Math.random() * 10 + 115, Math.random() * 9 + 25);
    var marker = new BMap.Marker(pt, {icon: myIcon});
    marker.needRed = b
    markers.push(marker);
  }
  //最简单的用法，生成一个marker数组，然后调用markerClusterer类即可。
  var markerClusterer = new BMapLib.MarkerClusterer(map, {
    markers,
    minClusterSize: 2,
    maxZoom: 15,
    isAverangeCenter: true
  });
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
