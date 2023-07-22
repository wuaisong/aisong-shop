import {createApp} from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import {createPinia} from 'pinia'
import router from './router/index.js';
import VXETable from 'vxe-table'
import 'vxe-table/lib/style.css'

const pinia = createPinia()
createApp(App).use(ElementPlus).use(VXETable).use(router).use(pinia).mount('#app');
