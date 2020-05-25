<template>
    <div class="login-wrapper">
        <div class="login-content">
            <div class="login-main">
                <h2 class="login-main-title">管理员登录</h2>
                <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" status-icon>
                    <el-form-item prop="userName">
                        <el-input v-model="dataForm.userName" placeholder="帐号"></el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input v-model="dataForm.password" type="password" placeholder="密码"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button class="login-btn-submit" type="primary" @click="dataFormSubmit()">登录</el-button>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>
<script>
	import {setToken} from '@/http/auth.js'
    export default {
        data() {
            return {
                dataForm: {
                    userName: '',
                    password: ''
                },
                dataRule: {
                    userName: [{
                        required: true,
                        message: '帐号不能为空',
                        trigger: 'blur'
                    }],
                    password: [{
                        required: true,
                        message: '密码不能为空',
                        trigger: 'blur'
                    }]
                }
            }
        },
        methods: {
            // 提交表单
            dataFormSubmit() {
            // TODO：登录代码逻辑待完善
            // alert("登录代码逻辑未完善")
            this.$http({
                url: '/auth/token',
                method: 'get'
            }).then(response => {
                console.log(response)
				setToken(response.data.token)
                this.$router.replace({name: 'Home'})
            })
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
        height: 300px;
        width: 400px;
        background-color: #112234;
        opacity: .8;
    }

    .login-main {
        color: beige;
        padding: 20px 20px 10px 20px;
    }
</style>