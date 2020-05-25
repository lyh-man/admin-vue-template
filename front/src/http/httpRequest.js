import Vue from 'vue'
import {
    getToken,
    removeToken
} from '@/http/auth.js'
import router from '@/router'
import {
    Message
} from 'element-ui'
import axios from 'axios'

// 创建 axios 实例
const http = axios.create({
    // 统一 url 配置，定义访问前缀 baseURL
    baseURL: '/api',
    // 定义请求超时时间
    timeout: 10000,
    // 请求带上 cookie
    withCredentials: true,
    // 定义消息头
    headers: {
        'Content-Type': 'application/json; charset=utf-8'
    }
})

// 定义请求拦截器
http.interceptors.request.use(
    config => {
        // 让每个请求携带 token
        config.headers['Admin-Token'] = getToken()
        return config
    },
    error => {
        Promise.reject(error)
    }
)

// 定义响应拦截器
http.interceptors.response.use(
    response => {
        const res = response.data
        // 当 token 失效时，清除 cookie 保存的 token 值，并跳转到登陆界面
        if (res && res.code === 401) {
            removeToken()
            Message({
                message: res.message,
                type: 'error',
                duration: 5000
            })
            router.push({
                name: 'Login'
            })
        }
        // 未找到页面时，跳转到 404 页面
        if (res && res.code === 404) {
            router.push({
                name: '404'
            })
        }
        return response
    },
    error => {
        return Promise.reject(error)
    }
)

export default http