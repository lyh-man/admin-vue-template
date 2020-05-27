<template>
	<div class="login-wrapper">
		<div class="login-content">
			<div class="login-main">
				<h2 class="login-main-title">{{language.title}}</h2>
				<el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon>
					<el-form-item prop="userName">
						<el-input v-model="dataForm.userName" :placeholder="language.userName"></el-input>
					</el-form-item>
					<el-form-item prop="password">
						<el-input v-model="dataForm.password" type="password" :placeholder="language.password"></el-input>
					</el-form-item>
					<el-form-item>
						<el-select v-model="dataForm.language" :placeholder="language.language" class="login-select" @change="updateLanguage">
							<el-option :label="language.zh" value="zh"></el-option>
							<el-option :label="language.en" value="en"></el-option>
						</el-select>
					</el-form-item>
					<el-form-item>
						<el-button class="login-btn-submit" type="primary" @click="dataFormSubmit()">{{language.signIn}}</el-button>
					</el-form-item>
				</el-form>
			</div>
		</div>
	</div>
</template>
<script>
	import {
		mapActions
	} from 'vuex'
	import {
		setToken
	} from '@/http/auth.js'
	export default {
		data() {
			return {
				dataForm: {
					userName: '',
					password: '',
					language: 'zh'
				},
				dataRule: {}
			}
		},
		computed: {
			// 国际化
			language() {
				return {
					title: this.$t("login.title"),
					userName: this.$t("login.userName"),
					password: this.$t("login.password"),
					language: this.$t("login.language"),
					zh: this.$t("language.zh"),
					en: this.$t("language.en"),
					signIn: this.$t("login.signIn"),
					userNameNotNull: this.$t("login.userNameNotNull"),
					passwordNotNull: this.$t("login.passwordNotNull")
				}
			}
		},
		methods: {
			...mapActions('user', ['updateName']),
			...mapActions('common', {
				updateLang: "updateLanguage",
				resetState: "resetState"
			}),
			// 提交表单
			dataFormSubmit() {
			    // TODO：登录代码逻辑待完善
			    // alert("登录代码逻辑未完善")
			    this.$http.login.getToken().then(response => {
			        this.$message({
			            message: this.$t("login.signInSuccess"),
			            type: 'success'
			        })
			        // 保存 token
			        setToken(response.data.token)
			        this.updateName(this.dataForm.userName)
			        this.$router.push({
			            name: 'Home'
			        })
			    })
			},
			// 修改语言
			updateLanguage() {
				this.$i18n.locale = this.dataForm.language
				this.updateLang(this.dataForm.language)
				this.dataRule = {
					userName: [{
						required: true,
						message: this.language.userNameNotNull,
						trigger: 'blur'
					}],
					password: [{
						required: true,
						message: this.language.passwordNotNull,
						trigger: 'blur'
					}]
				}
			}
		},
		created() {
			// 进入画面前，移除主页面保存的 state 信息
			localStorage.removeItem("store")
			this.resetState()
			// 登录页面，默认选择当前语言
			this.dataForm.language = this.$i18n.locale
			this.dataRule = {
				userName: [{
					required: true,
					message: this.language.userNameNotNull,
					trigger: 'blur'
				}],
				password: [{
					required: true,
					message: this.language.passwordNotNull,
					trigger: 'blur'
				}]
			}
		}
	}
</script>
<style>
	.login-wrapper {
		position: absolute;
		top: 0;
		right: 0;
		bottom: 0;
		left: 0;
		overflow: hidden;
		background-color: rgba(38, 50, 56, .6);
		background: url(~@/assets/login_bg.jpg) no-repeat;
		background-size: 100% 100%;
	}

	.login-content {
		position: absolute;
		top: 0;
		right: 0;
		bottom: 0;
		left: 0;
		margin: auto;
		height: 350px;
		width: 400px;
		background-color: #112234;
		opacity: .8;
	}

	.login-main {
		color: beige;
		padding: 20px 20px 10px 20px;
	}

	.el-scrollbar__wrap {
		overflow-x: scroll !important;
	}

	.login-select {
		left: -120px;
		width: 120px;
	}
</style>
