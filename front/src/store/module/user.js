export default {
    // 开启命名空间（防止各模块间命名冲突），访问时需要使用 模块名 + 方法名
    namespaced: true,
    // 管理数据（状态）
    state: {
        // 用于保存用户名
        userName: 'Admin'
    },
    // 更改 state（同步）
    mutations: {
        updateName(state, data) {
            if (data) {
                state.userName = data
            } else {
                state.userName = 'Admin'
            }
        }
    },
    // 异步触发 mutations
    actions: {
        updateName({commit, state}, data) {
            commit("updateName", data)
        }
    }
}