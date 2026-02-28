// 测试用户画像API的脚本
import axios from 'axios';
import { getCurrentUserPortrait, processPortraitData } from './src/api/user.js';

// 创建axios实例
const api = axios.create({
  baseURL: process.env.VITE_API_BASE_URL || 'http://localhost:3000/user',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=UTF-8',
    'Accept': 'application/json;charset=UTF-8'
  }
});

// 模拟API响应数据
const mockApiResponse = {
  "code": 200,
  "message": "success",
  "data": {
    "id": 1,
    "name": "张三",
    "email": "zhangsan@example.com",
    "personalityLabels": ["忠诚可靠", "勇于担当", "乐于助人"],
    "preferenceLabels": ["习近平新时代中国特色社会主义思想", "党史学习", "党风廉政建设"],
    "preferences": {
      "newsCategories": {
        "时政要闻": 15,
        "党建工作": 12,
        "廉政建设": 8
      },
      "learningTopics": {
        "习近平新时代中国特色社会主义思想": 20,
        "党史学习教育": 18,
        "党风廉政建设": 15
      }
    }
  }
};

// 测试processPortraitData函数
console.log('=== 测试processPortraitData函数 ===');
const processedData = processPortraitData(mockApiResponse.data);
console.log('处理后的数据:', JSON.stringify(processedData, null, 2));

// 检查字段映射是否正确
console.log('\n=== 检查字段映射 ===');
console.log('personalityLabels -> personalityTraits:', processedData.personalityTraits);
console.log('preferenceLabels -> learningTags:', processedData.learningTags);
console.log('preferences.newsCategories -> learningPreferences.newsReading:', processedData.learningPreferences.newsReading);
console.log('preferences.learningTopics -> learningPreferences.learningTopics:', processedData.learningPreferences.learningTopics);

// 如果有真实API，可以取消注释下面的代码进行测试
/*
console.log('\n=== 测试真实API调用 ===');
try {
  const response = await getCurrentUserPortrait();
  console.log('API响应:', JSON.stringify(response, null, 2));
  
  if (response.code === 200) {
    const portraitData = response.data;
    const processedPortrait = processPortraitData(portraitData);
    console.log('处理后的用户画像:', JSON.stringify(processedPortrait, null, 2));
  }
} catch (error) {
  console.error('API调用失败:', error);
}
*/