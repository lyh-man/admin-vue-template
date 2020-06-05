/**
 * URL地址
 * @param {*} s
 */
export function isURL (s) {
  return /^http[s]?:\/\/.*/.test(s)
}

/**
 * 判断是否为 动态路由
 * @param {*} s
 */
export function isDynamicRoutes(s) {
    return /DynamicRoutes/.test(s)
}