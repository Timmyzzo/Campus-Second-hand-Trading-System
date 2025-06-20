// vite.config.js
import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  // 代理配置
  server: {
    proxy: {
      '/api': { // 捕获所有以 /api 开头的请求
        target: 'http://localhost:8080', // 转发到后端地址
        changeOrigin: true, // 必须设置为true
        // rewrite: (path) => path.replace(/^\/api/, '') // 如果后端接口本身没有/api前缀，就需要这行去掉/api
      }
    }
  }
})
