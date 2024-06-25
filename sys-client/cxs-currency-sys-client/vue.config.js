const {defineConfig} = require('@vue/cli-service')
const name = '工大圈子' // page title
const path = require('path')
const CompressionWebpackPlugin = require('compression-webpack-plugin');
const productionGzipExtensions = ['js', 'css'];

function resolve(dir) {
    return path.join(__dirname, dir)
}

module.exports = defineConfig({
    lintOnSave: false,     // 禁用eslint
    transpileDependencies: true,
    productionSourceMap: false,
    // configureWebpack: {
    //   devtool: 'source-map', // 配置本地调试,浏览器显示源码
    //   module: {}
    // },
    devServer: {
        host: '127.0.0.1',
        allowedHosts: [process.env.DEV_DOMAIN],
        port: 3000,
        open: true,
        proxy: {
            '/server': {
                target: `http://${process.env.DEV_DOMAIN}:${process.env.DEV_SERVER_PORT}`,
                pathRewrite: {'^/server': ''}, // 重写路径
                ws: true, // 用于支持websocket
                changeOrigin: true // 控制请求头中的host,设置服务器看到的请求来源
            }
        }
    },
    publicPath: '/',
    configureWebpack: {
        // provide the app's title in webpack's name field, so that
        // it can be accessed in index.html to inject the correct title.
        name: name,
        resolve: {
            alias: {
                '@': resolve('src')
            }
        },
        plugins: process.env.NODE_ENV === 'production' ? [
            // 代码压缩
            new CompressionWebpackPlugin({
                algorithm: 'gzip',
                test: new RegExp('\\.(' + productionGzipExtensions.join('|') + ')$'),
                threshold: 10240,
                minRatio: 0.8
            })
        ] : []
    },
})
