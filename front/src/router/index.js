import {createRouter, createWebHistory} from 'vue-router';

const router = createRouter({
    routes: [
        {
            path: '/',
            //路由到的地址(自定义)
            component: () => import('@/components/Index.vue'),
            //引入组件，组件Home.vue所在路径
            //Home.vue是需要路由的vue组件
            name: 'Index'
            //组件名称
        },
        {
            path: '/first',
            //路由到的地址(自定义)
            component: () => import('@/components/First.vue'),
            //引入组件，组件Home.vue所在路径
            //Home.vue是需要路由的vue组件
            name: 'First'
            //组件名称
        },
        {
            path: '/second',
            //路由到的地址(自定义)
            component: () => import('@/components/Second.vue'),
            //引入组件，组件Home.vue所在路径
            //Home.vue是需要路由的vue组件
            name: 'Second'
            //组件名称
        },

    ],
    history: createWebHistory()
})
export default router;
