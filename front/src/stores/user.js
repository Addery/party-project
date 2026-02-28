import { defineStore } from 'pinia'
import { getCurrentUserPortrait, updateCurrentUserPortrait, processPortraitData } from '@/api/user'

export const useUserStore = defineStore('user', {
  state: () => ({
    // 测试环境：默认登录状态
    token: localStorage.getItem('token') || 'mock-token-' + Date.now(),
    loginStatus: (localStorage.getItem('token') && localStorage.getItem('loginExpire') && Date.now() < parseInt(localStorage.getItem('loginExpire'))) ? true : true, // 默认登录
    userInfo: JSON.parse(localStorage.getItem('userInfo')) || {
      userId: '1',
      username: 'admin',
      name: '管理员',
      email: 'admin@example.com',
      phone: '13800138000',
      role: 'admin',
      department: '组织部',
      position: '干事',
      avatar: '',
      joinDate: '2023-01-01',
      status: 'active',
      permissions: ['read', 'write'],
      currentActivityId: '',
      userProfile: {
        educationBackground: '本科',
        workExperience: '2年',
        partyJoinDate: '2022-06-30',
        partyPosition: '党员',
        politicalStatus: '中共党员',
        honors: [],
        penalties: [],
        familyMembers: [],
        address: '北京市朝阳区'
      },
      browsingHistory: [],
      studyRecords: [],
      examResults: []
    },
    error: null,
    loading: false,
    userPortrait: {
      learningPreferences: {
        newsReading: {},
        learningTopics: {}
      },
      activityParticipation: [],
      interestTags: [],
      communicationFrequency: 0,
      learningDuration: 0,
      quizPerformance: {
        averageScore: 0,
        totalAttempts: 0,
        correctRate: 0
      },
      contributionLevel: 0,
      lastActiveTime: new Date().toISOString()
    },
    selectedActivityId: ''
  }),
  
  getters: {
    isLoggedIn: (state) => state.loginStatus,
    hasPermission: (state) => (permission) => {
      return state.userInfo.permissions.includes(permission);
    },
    getUserRoles: (state) => state.userInfo.role,
    getUserName: (state) => state.userInfo.name || state.userInfo.username,
    getAvatar: (state) => state.userInfo.avatar,
    getLearningProgress: (state) => {
      const totalRecords = state.userInfo.studyRecords.length;
      if (totalRecords === 0) return 0;
      const completed = state.userInfo.studyRecords.filter(record => record.completed).length;
      return Math.round((completed / totalRecords) * 100);
    },
    getRecentActivities: (state) => {
      return state.userInfo.browsingHistory
        .filter(item => item.type === 'activity')
        .slice(0, 5)
        .reverse();
    },
    getBrowsingHistory: (state) => state.userInfo.browsingHistory,
    getUserProfile: (state) => state.userPortrait,
    getUserPortrait: (state) => state.userPortrait,
    // 添加缺少的计算属性
    preferenceLabels: (state) => {
      // 从userPortrait.learningTags提取学习兴趣标签，确保是数组
      return Array.isArray(state.userPortrait.learningTags) ? state.userPortrait.learningTags : [];
    },
    personalityLabels: (state) => {
      // 从userPortrait.personalityTraits提取党员特质标签，确保是数组
      return Array.isArray(state.userPortrait.personalityTraits) ? state.userPortrait.personalityTraits : [];
    },
    // 兼容直接访问userProfile，返回完整的userPortrait
    userProfile: (state) => state.userPortrait
  },
  
  actions: {
    setUserInfo(userData) {
      this.userInfo = { ...this.userInfo, ...userData };
      localStorage.setItem('userInfo', JSON.stringify(this.userInfo));
    },
    
    setToken(token) {
      this.token = token;
      this.loginStatus = !!token;
      localStorage.setItem('token', token);
    },
    
    async login(credentialsOrUserInfo, token) {
      this.loading = true;
      this.error = null;
      try {
        // 处理两种调用方式：
        // 1. login(credentials) - 发送API请求
        // 2. login(userInfo, token) - 直接设置用户信息和token
        if (token) {
          // 直接设置用户信息和token
          this.setToken(token);
          this.setUserInfo(credentialsOrUserInfo);
          // 设置登录过期时间（7天）
          localStorage.setItem('loginExpire', Date.now() + 7 * 24 * 60 * 60 * 1000);
          return { user: credentialsOrUserInfo, token };
        } else {
          // 这里应该是API调用，暂时模拟
          const response = {
            data: {
              token: 'mock-token-' + Date.now(),
              userInfo: {
                userId: '1',
                username: credentialsOrUserInfo.username,
                name: '张三',
                email: 'zhangsan@example.com',
                phone: '13800138000',
                role: 'admin',
                department: '组织部',
                position: '干事',
                avatar: '',
                joinDate: '2023-01-01',
                status: 'active',
                permissions: ['read', 'write'],
                currentActivityId: '',
                userProfile: {
                  educationBackground: '本科',
                  workExperience: '2年',
                  partyJoinDate: '2022-06-30',
                  partyPosition: '党员',
                  politicalStatus: '中共党员',
                  honors: [],
                  penalties: [],
                  familyMembers: [],
                  address: '北京市朝阳区'
                },
                browsingHistory: [],
                studyRecords: [],
                examResults: []
              }
            }
          };
          
          this.setToken(response.data.token);
          this.setUserInfo(response.data.userInfo);
          // 设置登录过期时间（7天）
          localStorage.setItem('loginExpire', Date.now() + 7 * 24 * 60 * 60 * 1000);
          return response;
        }
      } catch (err) {
        this.error = '登录失败，请检查用户名和密码';
        throw err;
      } finally {
        this.loading = false;
      }
    },
    
    logout() {
      this.token = '';
      this.loginStatus = false;
      this.userInfo = {
        userId: '',
        username: '',
        name: '',
        email: '',
        phone: '',
        role: '',
        department: '',
        position: '',
        avatar: '',
        joinDate: '',
        status: '',
        permissions: [],
        currentActivityId: '',
        userProfile: {
          educationBackground: '',
          workExperience: '',
          partyJoinDate: '',
          partyPosition: '',
          politicalStatus: '',
          honors: [],
          penalties: [],
          familyMembers: [],
          address: ''
        },
        browsingHistory: [],
        studyRecords: [],
        examResults: []
      };
      localStorage.removeItem('token');
      localStorage.removeItem('userInfo');
      localStorage.removeItem('loginExpire');
    },
    
    async updateUserProfile(profileData) {
      this.loading = true;
      this.error = null;
      try {
        // 这里应该是API调用，暂时模拟
        const updatedProfile = { ...this.userInfo.userProfile, ...profileData };
        this.setUserInfo({ userProfile: updatedProfile });
        return { success: true };
      } catch (err) {
        this.error = '更新个人资料失败';
        throw err;
      } finally {
        this.loading = false;
      }
    },
    
    addBrowsingHistory(item) {
      const existingItemIndex = this.userInfo.browsingHistory.findIndex(
        (history) => history.id === item.id && history.type === item.type
      );
      
      if (existingItemIndex !== -1) {
        this.userInfo.browsingHistory.splice(existingItemIndex, 1);
      }
      
      this.userInfo.browsingHistory.unshift({
        ...item,
        timestamp: new Date().toISOString()
      });
      
      if (this.userInfo.browsingHistory.length > 50) {
        this.userInfo.browsingHistory.pop();
      }
      
      localStorage.setItem('userInfo', JSON.stringify(this.userInfo));
    },
    
    addStudyRecord(record) {
      this.userInfo.studyRecords.push({
        ...record,
        timestamp: new Date().toISOString()
      });
      localStorage.setItem('userInfo', JSON.stringify(this.userInfo));
    },
    
    addExamResult(result) {
      this.userInfo.examResults.push({
        ...result,
        timestamp: new Date().toISOString()
      });
      localStorage.setItem('userInfo', JSON.stringify(this.userInfo));
    },
    
    updateUserPortrait(data) {
      this.userPortrait = { ...this.userPortrait, ...data };
      this.userPortrait.lastActiveTime = new Date().toISOString();
    },
    
    // 手动更新用户画像
    async syncUserPortrait() {
      try {
        if (!this.token) {
          throw new Error('未登录');
        }

        const response = await updateCurrentUserPortrait(this.token);
        if (response.code === 200 && response.data) {
          // 处理并更新用户画像数据
          const processedData = processPortraitData(response.data);
          this.userPortrait = {
            ...this.userPortrait,
            ...processedData,
            lastActiveTime: new Date().toISOString()
          };
          return response;
        } else {
          throw new Error(response.message || '更新失败');
        }
      } catch (error) {
        console.error('更新用户画像失败:', error);
        this.error = '更新用户画像失败';
        throw error;
      }
    },
    
    setSelectedActivityId(activityId) {
      this.selectedActivityId = activityId;
    },
    
    // 获取用户画像
async fetchUserProfile() {
  this.loading = true;
  try {
    // 调用真实后端接口获取用户画像数据
    const response = await getCurrentUserPortrait();
    
    console.log('fetchUserProfile获取的完整API响应:', JSON.stringify(response, null, 2));
    
    // 从API响应中提取用户画像数据（API返回结构：{code: 200, message: 'xxx', data: {...}}）
    const portraitData = response.data.data;
    
    console.log('fetchUserProfile提取的portraitData:', JSON.stringify(portraitData, null, 2));
    
    // 处理API返回的数据，适配前端数据结构
    const processedData = processPortraitData(portraitData);
    
    console.log('fetchUserProfile处理后的processedData:', JSON.stringify(processedData, null, 2));
    
    // 更新用户画像数据
    this.userPortrait = {
      ...this.userPortrait,
      ...processedData,
      lastActiveTime: new Date().toISOString()
    };
    
    console.log('fetchUserProfile更新后的userPortrait:', JSON.stringify(this.userPortrait, null, 2));
    
    return this.userPortrait;
  } catch (error) {
    console.error('获取用户画像失败:', error);
    this.error = '获取用户画像失败';
    throw error;
  } finally {
    this.loading = false;
  }
}
  }
});
