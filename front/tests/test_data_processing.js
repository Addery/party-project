// 简单的测试脚本，用于测试processPortraitData函数的逻辑

// 复制processPortraitData函数的代码
const processPortraitData = (portraitData) => {
  if (!portraitData) return {
    learningTags: [],
    learningInterests: [],
    personalityTraits: [],
    learningPreferences: { newsReading: {}, learningTopics: {} }
  }
  
  console.log('processPortraitData输入数据:', JSON.stringify(portraitData, null, 2))
  
  // 适配API返回的数据结构到前端期望的结构
  const processed = {
    ...portraitData,
    // 将API返回的preferenceLabels映射到前端使用的learningTags
    learningTags: portraitData.preferenceLabels || [],
    learningInterests: [],
    // 将API返回的personalityLabels映射到前端使用的personalityTraits
    personalityTraits: portraitData.personalityLabels || [],
    // 将API返回的preferences映射到前端使用的learningPreferences
    learningPreferences: {
      // API返回的preferences.newsCategories映射到learningPreferences.newsReading
      newsReading: portraitData.preferences?.newsCategories || {},
      // API返回的preferences.learningTopics映射到learningPreferences.learningTopics
      learningTopics: portraitData.preferences?.learningTopics || {}
    }
  }
  
  return processed
}

// 模拟API返回的数据
const mockApiResponse = {
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
};

// 测试processPortraitData函数
console.log('=== 测试processPortraitData函数 ===');
const processedData = processPortraitData(mockApiResponse);
console.log('\n处理后的数据:', JSON.stringify(processedData, null, 2));

// 检查字段映射是否正确
console.log('\n=== 检查字段映射 ===');
console.log('personalityLabels -> personalityTraits:', processedData.personalityTraits);
console.log('preferenceLabels -> learningTags:', processedData.learningTags);
console.log('preferences.newsCategories -> learningPreferences.newsReading:', processedData.learningPreferences.newsReading);
console.log('preferences.learningTopics -> learningPreferences.learningTopics:', processedData.learningPreferences.learningTopics);

// 检查数据结构是否符合前端组件的期望
console.log('\n=== 检查数据结构是否符合前端组件期望 ===');
console.log('前端组件期望的结构:');
console.log('1. preferenceLabels计算属性应该获取userPortrait.learningTags');
console.log('   测试结果:', Array.isArray(processedData.learningTags));
console.log('2. personalityLabels计算属性应该获取userPortrait.personalityTraits');
console.log('   测试结果:', Array.isArray(processedData.personalityTraits));
console.log('3. 学习偏好应该在userPortrait.learningPreferences下');
console.log('   newsReading测试结果:', typeof processedData.learningPreferences.newsReading === 'object');
console.log('   learningTopics测试结果:', typeof processedData.learningPreferences.learningTopics === 'object');
