import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import { getToken } from '@/http/auth.js'

Vue.use(VueRouter)

// 定义路由跳转规则
// component 采用 路由懒加载形式
// 此项目中，均采用 name 方式指定路由进行跳转
// meta 用于定义路由元信息，其中 isRouter 用于指示是否开启路由守卫（true 表示开启）。
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
	            component: () => import('@/views/menu/HomePage.vue'),
	            meta: {
	                isRouter: true
	            }
	        },
	        {
	            path: '/Home/Demo/Echarts',
	            name: 'Echarts',
	            component: () => import('@/views/menu/Echarts.vue'),
	            meta: {
	                isRouter: true,
	                isTab: true
	            }
	        },
	        {
	            path: '/Home/Demo/Ueditor',
	            name: 'Ueditor',
	            component: () => import('@/views/menu/Ueditor.vue'),
	            meta: {
	                isRouter: true,
	                isTab: true
	            }
	        },
	        {
	            path: '/Home/Demo/Baidu',
	            name: 'Baidu',
	            meta: {
	                isRouter: true,
	                isTab: true,
	                iframeUrl: 'https://www.baidu.com/'
	            }
	        }
	    ]
	},
	// 路由匹配失败时，跳转到 404 页面
	{
		path: "*",
		redirect: {
			name: '404'
		}
	}
]

// 创建一个 router 实例
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

// 添加全局路由导航守卫
router.beforeEach((to, from, next) => {
	// 当开启导航守卫时，验证 token 是否存在。
	if (to.meta.isRouter) {
		// 获取 token 值
		let token = getToken()
		// token 不存在时，跳转到 登录页面
		if (!token || !/\S/.test(token)) {
			next({
				name: 'Login'
			})
		}
	}
	next()
})

// 解决相同路径跳转报错
const routerPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location, onResolve, onReject) {
	if (onResolve || onReject)
		return routerPush.call(this, location, onResolve, onReject)
	return routerPush.call(this, location).catch(error => error)
};

export default router
