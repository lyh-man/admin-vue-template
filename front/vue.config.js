module.exports = {
	lintOnSave: false,
	devServer: {
		port: process.env.NODE_ENV == "production" ? 8000 : 9000,
		proxy: {
			'/api': {
				target: process.env.VUE_APP_URL,
				// 允许跨域
				changeOrigin: true,
				ws: true,
				pathRewrite: {
					'^/api': ''
				}
			}
		}
	}
}
