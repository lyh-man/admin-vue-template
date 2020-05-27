<template>
    <div>
        <!-- 系统 Logo -->
        <el-aside class="header-logo" :width="asideWidth">
            <div @click="$router.push({ name: 'Home' })">
                <a v-if="foldAside">{{language.adminCenter}}</a>
                <a v-else>{{language.admin}}</a>
            </div>
        </el-aside>
        <el-aside class="aside" :width="asideWidth" :class='"icon-size-" + iconSize'>
            <el-scrollbar style="height: 100%; width: 100%;">
                <!--
                        default-active 表示当前选中的菜单，默认为 HomePage。
                        collapse 表示是否折叠菜单，仅 mode 为 vertical（默认）可用。 
                        collapseTransition 表示是否开启折叠动画，默认为 true。
                        background-color 表示背景颜色。
                        text-color 表示字体颜色。
                    -->
                <el-menu :default-active="menuActiveName || 'HomePage'" :collapse="!foldAside" :collapseTransition="false"
                 background-color="#263238" text-color="#8a979e">
                    <el-menu-item index="HomePage" @click="$router.push({ name: 'Home' })">
                        <i class="el-icon-s-home"></i>
                        <span slot="title">{{language.homePage}}</span>
                    </el-menu-item>
                    <el-submenu index="demo">
                        <template slot="title">
                            <i class="el-icon-star-off"></i>
                            <span>demo</span>
                        </template>
                        <el-menu-item index="Echarts" @click="$router.push({ name: 'Echarts' })">
                            <i class="el-icon-s-data"></i>
                            <span slot="title">echarts</span>
                        </el-menu-item>
                        <el-menu-item index="Ueditor" @click="$router.push({ name: 'Ueditor' })">
                            <i class="el-icon-document"></i>
                            <span slot="title">ueditor</span>
                        </el-menu-item>
                        <el-menu-item index="Baidu" @click="$router.push({ name: 'Baidu' })">
                            <i class="el-icon-document"></i>
                            <span slot="title">baidu</span>
                        </el-menu-item>
                    </el-submenu>
                </el-menu>
            </el-scrollbar>
        </el-aside>
    </div>
</template>

<script>
	import {
		mapState,
		mapActions
	} from 'vuex'
	import {
		isURL
	} from '@/utils/validate.js'
	export default {
		name: 'Aside',
		props: ['foldAside'],
		data() {
			return {
				// 保存当前选中的菜单
				// menuActiveName: 'home',
				// 保存当前侧边栏的宽度
				asideWidth: '200px',
				// 用于拼接当前图标的 class 样式
				iconSize: 'true'
			}
		},
		computed: {
			...mapState('common', ['menuActiveName', 'mainTabs']),
			// 国际化
			language() {
				return {
					adminCenter: this.$t("aside.adminCenter"),
					admin: this.$t("aside.admin"),
					homePage: this.$t("aside.homePage")
				}
			}
		},
		methods: {
			...mapActions('common', ['updateMenuActiveName', 'updateMainTabs', 'updateMainTabsActiveName'])
		},
		watch: {
			// 监视是否折叠侧边栏，折叠则宽度为 64px。
			foldAside(val) {
				this.asideWidth = val ? '200px' : '64px'
				this.iconSize = val
			},
			// 监视路由的变化，每次点击菜单项时会触发
			$route(route) {
				// 路由变化时，修改当前选中的菜单项
				this.updateMenuActiveName(route.name)
				// 是否显示标签页
				if (route.meta.isTab) {
					// 判断当前标签页数组中是否存在当前选中的标签，根据标签名匹配
					let tab = this.mainTabs.filter(item => item.name === route.name)[0]
					// 若当前标签页数组不存在该标签，则向数组中添加标签
					if (!tab) {
						// 设置标签页数据
						tab = {
							name: route.name,
							params: route.params,
							query: route.query,
							type: isURL(route.meta.iframeUrl) ? 'iframe' : 'module',
							iframeUrl: route.meta.iframeUrl || ''
						}
						// 将数据保存到标签页数组中
						this.updateMainTabs(this.mainTabs.concat(tab))
					}
					// 保存标签页中当前选中的标签名
					this.updateMainTabsActiveName(route.name)
				}
			}
		}
	}
</script>

<style>
	.aside {
		margin-bottom: 0;
		height: 100%;
		max-height: calc(100% - 50px);
		width: 100%;
		max-width: 200px;
		background-color: #263238;
		text-align: left;
		right: 0;
	}

	.header-logo {
		background-color: #17b3a3;
		text-align: center;
		height: 50px;
		line-height: 50px;
		width: 200px;
		font-size: 24px;
		color: #fff;
		font-weight: bold;
		margin-bottom: 0;
		cursor: pointer;
	}

	.el-submenu .el-menu-item {
		max-width: 200px !important;
	}

	.el-scrollbar__wrap {
		overflow-x: hidden !important;
	}

	.icon-size-false i {
		font-size: 30px !important;
	}

	.icon-size-true i {
		font-size: 18px !important;
	}
</style>
