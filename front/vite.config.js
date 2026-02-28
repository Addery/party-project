import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

// https://vite.dev/config/
export default defineConfig({
  plugins: [vue({
    template: {
      compilerOptions: {
        isCustomElement: (tag) => {
          // 只将图表相关组件视为自定义元素
          return ['el-chart', 'el-line', 'el-pie', 'el-bar', 'el-pie-label', 'el-line-point', 'el-line-label', 'el-tooltip', 'el-bar-label'].includes(tag);
        }
      }
    }
  })],
  resolve: {
    alias: {
      '@': resolve(__dirname, 'src')
    }
  },
  server: {
    // 移除代理配置，使用环境变量中的API基础URL
  }
})
