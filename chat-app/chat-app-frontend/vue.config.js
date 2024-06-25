module.exports = {
  publicPath: "./",
  assetsDir: "static",
  outputDir: 'dist',
  devServer: {
    port: 3002,
    proxy: {
      '/api': {
        target: 'http://localhost:3001',
        changeOrigin: true,
        pathRewrite: { '^/api': '' },
      },
    },
  },
};
