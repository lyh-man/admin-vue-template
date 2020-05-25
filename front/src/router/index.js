import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'

Vue.use(VueRouter)

const routes = [{
        path: '/',
        redirect: {
            name: "Login"
        }
    },
    {
        path: '/404',
        name: '404',
        component: () => import('@/components/common/404.vue')
    },
    {
        path: '/Login',
        name: 'Login',
        component: () => import('@/components/common/Login.vue')
    },
    {
        path: '/Home',
        name: 'Home',
        component: () => import('@/views/Home.vue'),
        redirect: {
            name: 'HomePage'
        },
        children: [{
                path: '/Home/Page',
                name: 'HomePage',
                component: () => import('@/views/menu/HomePage.vue')
            },
            {
                path: '/Home/Demo/Echarts',
                name: 'Echarts',
                component: () => import('@/views/menu/Echarts.vue')
            },
            {
                path: '/Home/Demo/Ueditor',
                name: 'Ueditor',
                component: () => import('@/views/menu/Ueditor.vue')
            }
        ]
    },
]

const router = new VueRouter({
    // routes 用于定义 路由跳转 规则
    routes,
    // mode 用于去除地址中的 #
    mode: 'history',
    // scrollBehavior 用于定义路由切换时，页面滚动。
    scrollBehavior: () => ({
        y: 0
    })
})

// 解决相同路径跳转报错
const routerPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location, onResolve, onReject) {
    if (onResolve || onReject)
        return routerPush.call(this, location, onResolve, onReject)
    return routerPush.call(this, location).catch(error => error)
};

export default router