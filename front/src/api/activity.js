import axios from 'axios';

// 创建axios实例，使用环境变量中的API基础URL
const service = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL,
  timeout: 15000
});

const activityApi = {
  // 获取首页推荐活动列表
  getHomeActivities: async (params) => {
    try {
      const response = await service.get('/home/activities', { params });
      return response.data;
    } catch (error) {
      console.error('获取首页推荐活动列表失败:', error);
      throw error;
    }
  },
  // 获取活动分类列表
  getActivityCategories: async () => {
    try {
      const response = await service.get('/activities/categories');
      return response.data;
    } catch (error) {
      console.error('获取活动分类列表失败:', error);
      throw error;
    }
  },

  // 获取活动列表
  getActivityList: async (params) => {
    try {
      const response = await service.get('/activities', { params });
      return response.data;
    } catch (error) {
      console.error('获取活动列表失败:', error);
      throw error;
    }
  },

  // 获取活动详情
  getActivityDetail: async (id) => {
    try {
      const response = await service.get(`/activities/${id}`);
      return response.data;
    } catch (error) {
      console.error('获取活动详情失败:', error);
      throw error;
    }
  },

  // 活动报名
  registerActivity: async (id, data) => {
    try {
      const response = await service.post(`/activities/${id}/register`, data);
      return response.data;
    } catch (error) {
      console.error('活动报名失败:', error);
      throw error;
    }
  }
};

export default activityApi;