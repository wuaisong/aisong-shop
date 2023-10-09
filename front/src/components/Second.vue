<template>
  <table class="table_default1" style="width: 400px;">
    <tr>
      <th v-for="item in tableHeader">
        {{ item }}
      </th>
    </tr>
    <tr v-for="tr in tableBody">
      <td v-for="td in tr">
        {{ td }}
      </td>
    </tr>
  </table>
  <el-button @click="getData">BUTTON</el-button>
</template>
<script setup>
import axios from 'axios'

let result = 'data'
const getData = async () => {
  const response = await axios.get('/api/consumer/provider?name=test')
  console.log(response)
}

const testdata = [
  {
    studentID: '0001',
    name: '小红',
    subject: '数学',
    mark: 100
  }, {
    studentID: '0001',
    name: '小红',
    subject: '语文',
    mark: 90
  }, {
    studentID: '0001',
    name: '小红',
    subject: '英语',
    mark: 91
  }, {
    studentID: '0002',
    name: '小明',
    subject: '数学',
    mark: 93
  }, {
    studentID: '0002',
    name: '小明',
    subject: '语文',
    mark: 87
  }, {
    studentID: '0002',
    name: '小明',
    subject: '英语',
    mark: 88
  }, {
    studentID: '0002',
    name: '小明',
    subject: '物理',
    mark: 88
  }, {
    studentID: '0003',
    name: '韩梅梅',
    subject: '数学',
    mark: 94
  }, {
    studentID: '0003',
    name: '韩梅梅',
    subject: '语文',
    mark: 99
  }, {
    studentID: '0003',
    name: '韩梅梅',
    subject: '英语',
    mark: 98
  }
];
//第一次遍历，制作 tableHeader
const th = {};
th["studentID"] = '学号';
//固定列
th["name"] = '姓名';
//筛选科目
const thKey = {};
for (let i = 0; i < testdata.length; i++) {
  thKey[testdata[i].subject] = 1;
  //动态列
}
//把科目加到th里面 ，动态增加列
for (const key in thKey) {
  th[key] = key;
}
const tableHeader = th;
console.log('表头格式', tableHeader)
//第二次遍历，制作tr的行
let tr = {};
for (let i = 0; i < testdata.length; i++) {
  const d = testdata[i];
  tr['s' + d.studentID] = {
    studentID: d.studentID,
    //固定列
    name: d.name
  }
}
//第三次遍历，添加科目的成绩
for (let i = 0; i < testdata.length; i++) {
  const d = testdata[i];
  tr['s' + d.studentID][d.subject] = d.mark + 100;
  //动态列
}
const tableBody = tr;
</script>
