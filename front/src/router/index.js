import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

// 前台页面
import Home from '../views/front/Home.vue'
import NewsList from '../views/front/NewsList.vue'
import NewsDetail from '../views/front/NewsDetail.vue'
import Learning from '../views/front/Learning.vue'
import LearningDetail from '../views/front/LearningDetail.vue'
import Activities from '../views/front/Activities.vue'
import ActivityDetail from '../views/front/ActivityDetail.vue'
import AboutUs from '../views/front/AboutUs.vue'
import Login from '../views/front/Login.vue'
import Register from '../views/front/Register.vue'
import Profile from '../views/front/Profile.vue'
import NoticeList from '../views/front/NoticeList.vue'
import NoticeDetail from '../views/front/NoticeDetail.vue'
import ModelBranchDetail from '../views/front/ModelBranchDetail.vue'
import Employment from '../views/front/Employment.vue'
import EmploymentDetail from '../views/front/EmploymentDetail.vue'
import EmploymentPolicyDetail from '../views/front/EmploymentPolicyDetail.vue'
// 就业相关新页面
import EmploymentNewsList from '../views/front/EmploymentNewsList.vue'
import EmploymentPoliciesList from '../views/front/EmploymentPoliciesList.vue'
import EmploymentGuidanceList from '../views/front/EmploymentGuidanceList.vue'
import EmploymentGuidanceDetail from '../views/front/EmploymentGuidanceDetail.vue'
import EmploymentCompanyRecruitmentList from '../views/front/EmploymentCompanyRecruitmentList.vue'
import EmploymentCompanyRecruitmentDetail from '../views/front/employment/EmploymentCompanyRecruitmentDetail.vue'
import EmploymentServicesList from '../views/front/employment/EmploymentServicesList.vue'

// 后台页面
import Dashboard from '../views/back/Dashboard.vue'
import UserManagement from '../views/back/UserManagement.vue'
import NewsManagement from '../views/back/NewsManagement.vue'
import OrganizationManagement from '../views/back/OrganizationManagement.vue'
import ThreeMeetingsOneLesson from '../views/back/ThreeMeetingsOneLesson.vue'
import TestComponent from '../views/back/TestComponent.vue'
import ThemePartyDay from '../views/back/ThemePartyDay.vue'
import PartyMemberDevelopment from '../views/back/PartyMemberDevelopment.vue'
import PartyMemberManagement from '../views/back/PartyMemberManagement.vue'
import DuesCollection from '../views/back/DuesCollection.vue'
import LearningResources from '../views/back/LearningResources.vue'
import ExamManagement from '../views/back/ExamManagement.vue'
import QuestionManagement from '../views/back/QuestionManagement.vue'
import DataAnalysis from '../views/back/DataAnalysis.vue'
import InteractionManagement from '../views/back/InteractionManagement.vue'
import SystemSettings from '../views/back/SystemSettings.vue'
import EducationManagement from '../views/back/EducationManagement.vue'
import PartyBuildingActivity from '../views/back/PartyBuildingActivity.vue'

const routes = [
  // 前台路由
  {
    path: '/',
    name: 'Home',
    component: Home,
    meta: { requireAuth: false }
  },
  {
    path: '/news',
    name: 'NewsList',
    component: NewsList,
    meta: { requireAuth: false }
  },
  {
    path: '/news/:id',
    name: 'NewsDetail',
    component: NewsDetail,
    meta: { requireAuth: false }
  },
  {
    path: '/learning',
    name: 'Learning',
    component: Learning,
    meta: { requireAuth: false }
  },
  {
    path: '/activities',
    name: 'Activities',
    component: Activities,
    meta: { requireAuth: false }
  },
  {
    path: '/activity/:id',
    name: 'ActivityDetail',
    component: ActivityDetail,
    meta: { requireAuth: false }
  },
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { requireAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: { requireAuth: false }
  },
  {    path: '/about',
    name: 'AboutUs',
    component: AboutUs,
    meta: { requireAuth: false }
  },
  {    path: '/notice',
    name: 'NoticeList',
    component: NoticeList,
    meta: { requireAuth: false }
  },
  {    path: '/notice/:id',
    name: 'NoticeDetail',
    component: NoticeDetail,
    meta: { requireAuth: false }
  },
  {    path: '/employment',
    name: 'Employment',
    component: Employment,
    meta: { requireAuth: false }
  },
  {    path: '/employment/:id',
    name: 'EmploymentDetail',
    component: EmploymentDetail,
    meta: { requireAuth: false }
  },
  {    path: '/employment/policy/:id',
    name: 'EmploymentPolicyDetail',
    component: EmploymentPolicyDetail,
    meta: { requireAuth: false }
  },
  // 就业相关新路由
  {    path: '/employment/news/list',
    name: 'EmploymentNewsList',
    component: EmploymentNewsList,
    meta: { requireAuth: false }
  },
  {    path: '/employment/policies/list',
    name: 'EmploymentPoliciesList',
    component: EmploymentPoliciesList,
    meta: { requireAuth: false }
  },
  {    path: '/employment/guidance/list',
    name: 'EmploymentGuidanceList',
    component: EmploymentGuidanceList,
    meta: { requireAuth: false }
  },
  {    path: '/employment/guidance/:id',
    name: 'EmploymentGuidanceDetail',
    component: EmploymentGuidanceDetail,
    meta: { requireAuth: false }
  },
  {    path: '/employment/company-recruitment/list',
    name: 'EmploymentCompanyRecruitmentList',
    component: EmploymentCompanyRecruitmentList,
    meta: { requireAuth: false }
  },
  {    path: '/employment/company-recruitment/:id',
    name: 'EmploymentCompanyRecruitmentDetail',
    component: EmploymentCompanyRecruitmentDetail,
    meta: { requireAuth: false }
  },
  {    path: '/employment/services/list',
    name: 'EmploymentServicesList',
    component: EmploymentServicesList,
    meta: { requireAuth: false }
  },
  {
    path: '/model-branch/:id',
    name: 'ModelBranchDetail',
    component: ModelBranchDetail,
    meta: { requireAuth: false }
  },
  {
    path: '/learning/:id',
    name: 'LearningDetail',
    component: LearningDetail,
    meta: { requireAuth: false }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: Profile,
    meta: { requireAuth: true } // 需要登录才能访问
  },
  
  // 后台路由
  {
    path: '/admin',
    name: 'Dashboard',
    component: Dashboard,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  },
  {
    path: '/admin/users',
    name: 'UserManagement',
    component: UserManagement,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  },
  {
    path: '/admin/news',
    name: 'NewsManagement',
    component: NewsManagement,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  },
  {
    path: '/admin/organization',
    name: 'OrganizationManagement',
    component: OrganizationManagement,
    meta: { requireAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/three-meetings',
    name: 'ThreeMeetingsOneLesson',
    component: ThreeMeetingsOneLesson,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  },
  {
    path: '/admin/test',
    name: 'TestComponent',
    component: TestComponent,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  },
  {
    path: '/admin/party-day',
    name: 'ThemePartyDay',
    component: ThemePartyDay,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  },
  {
    path: '/admin/member-development',
    name: 'PartyMemberDevelopment',
    component: PartyMemberDevelopment,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  },
  {
    path: '/admin/dues',
    name: 'DuesCollection',
    component: DuesCollection,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  },
  {
    path: '/admin/learning-resources',
    name: 'LearningResources',
    component: LearningResources,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  },
  { path: '/admin/exams', name: 'ExamManagement', component: ExamManagement, meta: { requireAuth: true, roles: ['admin', 'manager'] }, key: 'exam-management' },
  { path: '/admin/questions', name: 'QuestionManagement', component: QuestionManagement, meta: { requireAuth: true, roles: ['admin', 'manager'] }, key: 'question-management' },
  {
    path: '/admin/analysis',
    name: 'DataAnalysis',
    component: DataAnalysis,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  },
  {
    path: '/admin/interaction',
    name: 'InteractionManagement',
    component: InteractionManagement,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  },
  {
    path: '/admin/settings',
    name: 'SystemSettings',
    component: SystemSettings,
    meta: { requireAuth: true, roles: ['admin'] }
  },
  {
    path: '/admin/education',
    name: 'EducationManagement',
    component: EducationManagement,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  },
  {
    path: '/admin/party-activities',
    name: 'PartyBuildingActivity',
    component: PartyBuildingActivity,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  },
  {
    path: '/admin/party-members',
    name: 'PartyMemberManagement',
    component: PartyMemberManagement,
    meta: { requireAuth: true, roles: ['admin', 'manager'] }
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const requiresAuth = to.matched.some(record => record.meta.requireAuth)
  const isLoggedIn = userStore.isLoggedIn
  
  // 调试信息
  console.log('路由守卫调试信息：')
  console.log('当前路径:', to.path)
  console.log('需要认证:', requiresAuth)
  console.log('是否登录:', isLoggedIn)
  
  if (requiresAuth && !isLoggedIn) {
    console.log('需要登录，跳转到登录页')
    next({ name: 'Login' })
  } else if (requiresAuth) {
    // 检查角色权限
    const userInfo = userStore.userInfo
    const requiredRoles = to.meta.roles
    
    console.log('用户角色:', userInfo.role)
    console.log('需要的角色:', requiredRoles)
    
    // 如果用户还没有角色信息，默认设置为admin以便测试
    if (!userInfo.role) {
      console.log('用户没有角色信息，设置默认角色为admin')
      userInfo.role = 'admin'
    }
    
    if (requiredRoles && !requiredRoles.includes(userInfo.role)) {
      // 没有权限，跳转到首页
      console.log('权限不足，跳转到首页')
      next({ name: 'Home' })
    } else {
      console.log('权限通过，正常访问')
      next()
    }
  } else {
    console.log('不需要认证，正常访问')
    next()
  }
})

export default router
