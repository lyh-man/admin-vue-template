import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import {
	getToken
} from '@/http/auth.js'
import http from '@/http/http.js'
import {
	isURL,
	isDynamicRoutes
} from '@/utils/validate.js'

Vue.use(VueRouter)

// 定义路由跳转规则
// component 采用 路由懒加载形式
// 此项目中，均采用 name 方式指定路由进行跳转
/* 
    meta 用于定义路由元信息，
其中 
    isRouter 用于指示是否开启路由守卫（true 表示开启）。
    isTab 用于表示是否显示为标签页（true 表示显示）
    iframeUrl 用于表示 url，使用 http 或者 https 开头的 url 使用 iframe 标签展示
*/
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
					iframeUrl: '@/test.html'
				}
			},
			// 路由匹配失败时，跳转到 404 页面
			{
				path: "*",
				redirect: {
					name: '404'
				}
			}
		]
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
	}),
	// isAddDynamicMenuRoutes 表示是否已经添加过动态菜单（防止频繁请求动态菜单）
	isAddDynamicMenuRoutes: false
})

// 添加全局路由导航守卫
router.beforeEach((to, from, next) => {
	// 当开启导航守卫时，验证 token 是否存在。
	// to.meta.isRouter 表示是否开启动态路由
	// isDynamicRoutes 判断该路由是否为动态路由（页面刷新时，动态路由没有 isRouter 值，此时 to.meta.isRouter 条件不成立，即动态路由拼接逻辑不能执行）
	if (to.meta.isRouter || isDynamicRoutes(to.path)) {
		// console.log(router)
		// 获取 token 值
		let token = getToken()
		// token 不存在时，跳转到 登录页面
		if (!token || !/\S/.test(token)) {
			next({
				name: 'Login'
			})
		}
		// token 存在时，判断是否已经获取过 动态菜单，未获取，即 false 时，需要获取
		if (!router.options.isAddDynamicMenuRoutes) {
			http.menu.getMenus().then((response => {
				// 数据返回成功时
				if (response && response.data.code === 200) {
					// 设置动态菜单为 true，表示不用再次获取
					router.options.isAddDynamicMenuRoutes = true
					// 获取动态菜单数据
					let results = fnAddDynamicMenuRoutes(response.data.data)
					// 如果动态菜单数据存在，对其进行处理
					if (results && results.length > 0) {
						// 遍历第一层数据
						results.map(value => {
							// 如果 path 值不存在，则对其赋值，并指定 component 为 Home.vue
							if (!value.path) {
								value.path = `/DynamicRoutes-${value.meta.menuId}`
								value.name = `DynamicHome-${value.meta.menuId}`
								value.component = () => import('@/views/Home.vue')
							}
						})
					}
					// 使用 vuex 管理动态路由数据
					router.app.$options.store.dispatch('common/updateDynamicRoutes', results)
					// 使用 router 实例的 addRoutes 方法，给当前 router 实例添加一个动态路由
					router.addRoutes(results)
				}
			}))
		}
	}
	next()
})

// 用于处理动态菜单数据，将其转为 route 形式
function fnAddDynamicMenuRoutes(menuList = [], routes = []) {
	// 用于保存普通路由数据
	let temp = []
	// 用于保存存在子路由的路由数据
	let route = []
	// 遍历数据
	for (let i = 0; i < menuList.length; i++) {
		// 存在子路由，则递归遍历，并返回数据作为 children 保存
		if (menuList[i].subMenuList && menuList[i].subMenuList.length > 0) {
			// 获取路由的基本格式
			route = getRoute(menuList[i])
			// 递归处理子路由数据，并返回，将其作为路由的 children 保存
			route.children = fnAddDynamicMenuRoutes(menuList[i].subMenuList)
			// 保存存在子路由的路由
			routes.push(route)
		} else {
			// 保存普通路由
			temp.push(getRoute(menuList[i]))
		}
	}
	// 返回路由结果
	return routes.concat(temp)
}

// 返回路由的基本格式
function getRoute(item) {
	// 路由基本格式
	let route = {
		// 路由的路径
		path: '',
		// 路由名
		name: '',
		// 路由所在组件
		component: null,
		meta: {
			// 开启路由守卫标志
			isRouter: true,
			// 开启标签页显示标志
			isTab: true,
			// iframe 标签指向的地址（数据以 http 或者 https 开头时，使用 iframe 标签显示）
			iframeUrl: '',
			// 开启动态路由标志
			isDynamic: true,
			// 动态菜单名称（nameZH 显示中文， nameEN 显示英文）
			name_zh: item.name_zh,
			name_en: item.name_en,
			// 动态菜单项的图标
			icon: item.icon,
			// 菜单项的 ID
			menuId: item.menuId,
			// 菜单项的父菜单 ID
			parentId: item.parentId,
			// 菜单项排序依据
			orderNum: item.orderNum,
			// 菜单项类型（0: 目录，1: 菜单项，2: 按钮）
			type: item.type
		},
		// 路由的子路由
		children: []
	}
	// 如果存在 url，则根据 url 进行相关处理（判断是 iframe 类型还是 普通类型）
	if (item.url && /\S/.test(item.url)) {
		// 若 url 有前缀 /，则将其去除，方便后续操作。
		item.url = item.url.replace(/^\//, '')
		// 定义基本路由规则，将 / 使用 - 代替
		route.path = item.url.replace('/', '-')
		route.name = item.url.replace('/', '-')
		// 如果是 外部 url，使用 iframe 标签展示，不用指定 component，重新指定 path、name 以及 iframeUrl。
		if (isURL(item.url)) {
			route['path'] = `iframe-${item.menuId}`
			route['name'] = `iframe-${item.menuId}`
			route['meta']['iframeUrl'] = item.url
		} else {
			// 如果是项目模块，使用 route-view 标签展示，需要指定 component(加载指定目录下的 vue 组件)
			route.component = () => import(`@/views/dynamic/${item.url}.vue`) || null
		}
	}
	// 返回 route
	return route
}

// 解决相同路径跳转报错
const routerPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location, onResolve, onReject) {
	if (onResolve || onReject)
		return routerPush.call(this, location, onResolve, onReject)
	return routerPush.call(this, location).catch(error => error)
}

export default router
