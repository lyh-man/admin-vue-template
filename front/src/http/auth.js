// 引入 token，此处直接引入，也可以在 main.js 中全局引入
import Cookies from 'js-cookie'

// 设置 token 存储的 key
const TokenKey = 'Admin-Token'

// 获取 token
export function getToken() {
  return Cookies.get(TokenKey)
}

// 设置 token
export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

// 移除 token
export function removeToken() {
  return Cookies.remove(TokenKey)
}