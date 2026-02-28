import axios from 'axios'

// 创建axios实例
const chatApi = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL + '/ai', // 使用环境变量配置API地址
  timeout: 30000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
    'Accept': 'application/json;charset=UTF-8'
  },
  responseType: 'blob' // 以Blob形式接收响应
})

// 自定义sendChatMessage函数，使用FileReader处理响应
const sendChatMessage = (message) => {
  return new Promise((resolve, reject) => {
    chatApi.post('/chat', { message: message })
      .then(response => {
        const reader = new FileReader();
        reader.onload = (e) => {
          try {
            const jsonResponse = JSON.parse(e.target.result);
            resolve({ data: jsonResponse });
          } catch (error) {
            console.error('解析JSON失败:', error);
            resolve({ data: e.target.result });
          }
        };
        reader.onerror = (error) => {
          console.error('读取响应失败:', error);
          reject(error);
        };
        reader.readAsText(response.data, 'UTF-8');
      })
      .catch(error => {
        reject(error);
      });
  });
}

// 导出sendChatMessage函数
export { sendChatMessage }