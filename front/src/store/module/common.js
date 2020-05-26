export default {
    // 开启命名空间（防止各模块间命名冲突），访问时需要使用 模块名 + 方法名
    namespaced: true,
    // 管理数据（状态）
    state: {
        // 用于保存语言设置（国际化），默认为中文
        language: 'zh'
    },
    // 更改 state（同步）
    mutations: {
        updateLanguage(state, data) {
            state.language = data
        }
    },
    // 异步触发 mutations
    actions: {
        updateLanguage({commit, state}, data) {
            commit("updateLanguage", data)
        }
    }
}