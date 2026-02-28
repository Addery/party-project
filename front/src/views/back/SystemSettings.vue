<template>
  <AdminLayout>
    <div class="system-settings">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item><a href="#/back/dashboard">首页</a></el-breadcrumb-item>
          <el-breadcrumb-item>系统设置</el-breadcrumb-item>
        </el-breadcrumb>
      </div>

      <!-- 页面标题 -->
      <h2 class="page-title">系统设置</h2>

      <!-- 设置模块标签页 -->
      <div class="settings-modules">
        <el-tabs v-model="activeTab" type="card">
          <!-- 个人资料设置 -->
          <el-tab-pane label="个人资料设置" name="profile">
            <div class="module-content">
              <el-card class="setting-card">
                <template #header>
                  <div class="card-header">
                    <span>个人信息</span>
                  </div>
                </template>
                <div class="profile-content">
                  <div class="avatar-section">
                    <el-avatar :size="120" :src="userAvatar" class="user-avatar">
                      <el-icon><User /></el-icon>
                    </el-avatar>
                    <div class="avatar-actions">
                      <el-button type="primary" size="small">
                        <el-icon><Upload /></el-icon> 上传头像
                      </el-button>
                      <el-button size="small">
                        <el-icon><Delete /></el-icon> 清除头像
                      </el-button>
                    </div>
                  </div>
                  <div class="profile-form">
                    <el-form :model="profileForm" label-width="120px">
                      <el-form-item label="用户名">
                        <el-input v-model="profileForm.username" disabled></el-input>
                      </el-form-item>
                      <el-form-item label="真实姓名">
                        <el-input v-model="profileForm.realName"></el-input>
                      </el-form-item>
                      <el-form-item label="邮箱">
                        <el-input v-model="profileForm.email" type="email"></el-input>
                      </el-form-item>
                      <el-form-item label="手机号码">
                        <el-input v-model="profileForm.phone" type="tel"></el-input>
                      </el-form-item>
                      <el-form-item label="所属支部">
                        <el-select v-model="profileForm.branch" placeholder="请选择支部">
                          <el-option label="第一支部" value="first"></el-option>
                          <el-option label="第二支部" value="second"></el-option>
                          <el-option label="第三支部" value="third"></el-option>
                        </el-select>
                      </el-form-item>
                      <el-form-item label="性别">
                        <el-radio-group v-model="profileForm.gender">
                          <el-radio label="male">男</el-radio>
                          <el-radio label="female">女</el-radio>
                          <el-radio label="other">其他</el-radio>
                        </el-radio-group>
                      </el-form-item>
                      <el-form-item label="出生日期">
                        <el-date-picker v-model="profileForm.birthDate" type="date" placeholder="选择日期"></el-date-picker>
                      </el-form-item>
                      <el-form-item>
                        <el-button type="primary" @click="saveProfile">保存设置</el-button>
                        <el-button @click="resetProfile">重置</el-button>
                      </el-form-item>
                    </el-form>
                  </div>
                </div>
              </el-card>
            </div>
          </el-tab-pane>

          <!-- 密码修改 -->
          <el-tab-pane label="密码修改" name="password">
            <div class="module-content">
              <el-card class="setting-card">
                <template #header>
                  <div class="card-header">
                    <span>修改密码</span>
                  </div>
                </template>
                <el-form :model="passwordForm" label-width="120px">
                  <el-form-item label="原密码">
                    <el-input v-model="passwordForm.oldPassword" type="password"></el-input>
                  </el-form-item>
                  <el-form-item label="新密码">
                    <el-input v-model="passwordForm.newPassword" type="password"></el-input>
                  </el-form-item>
                  <el-form-item label="确认新密码">
                    <el-input v-model="passwordForm.confirmPassword" type="password"></el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="changePassword">修改密码</el-button>
                    <el-button @click="resetPasswordForm">重置</el-button>
                  </el-form-item>
                </el-form>
              </el-card>
            </div>
          </el-tab-pane>

          <!-- 系统参数设置 -->
          <el-tab-pane label="系统参数设置" name="system">
            <div class="module-content">
              <el-card class="setting-card">
                <template #header>
                  <div class="card-header">
                    <span>系统参数</span>
                  </div>
                </template>
                <el-form :model="systemForm" label-width="180px">
                  <el-form-item label="系统名称">
                    <el-input v-model="systemForm.systemName"></el-input>
                  </el-form-item>
                  <el-form-item label="系统版本">
                    <el-input v-model="systemForm.systemVersion" disabled></el-input>
                  </el-form-item>
                  <el-form-item label="系统描述">
                    <el-input v-model="systemForm.systemDescription" type="textarea" :rows="3"></el-input>
                  </el-form-item>
                  <el-form-item label="是否开启注册">
                    <el-switch v-model="systemForm.allowRegistration"></el-switch>
                  </el-form-item>
                  <el-form-item label="是否开启邮箱验证">
                    <el-switch v-model="systemForm.emailVerification"></el-switch>
                  </el-form-item>
                  <el-form-item label="默认每页显示条数">
                    <el-select v-model="systemForm.pageSize" placeholder="请选择">
                      <el-option label="10条/页" value="10"></el-option>
                      <el-option label="20条/页" value="20"></el-option>
                      <el-option label="50条/页" value="50"></el-option>
                      <el-option label="100条/页" value="100"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="系统公告">
                    <el-input v-model="systemForm.systemNotice" type="textarea" :rows="5"></el-input>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="saveSystemSettings">保存设置</el-button>
                    <el-button @click="resetSystemForm">重置</el-button>
                  </el-form-item>
                </el-form>
              </el-card>
            </div>
          </el-tab-pane>

          <!-- 角色管理 -->
          <el-tab-pane label="角色管理" name="roles">
            <div class="module-content">
              <div class="roles-header">
                <el-button type="primary" @click="showAddRoleDialog">
                  <el-icon><Plus /></el-icon> 新增角色
                </el-button>
              </div>
              <el-table :data="rolesData" style="width: 100%">
                <el-table-column prop="id" label="角色ID" width="80"></el-table-column>
                <el-table-column prop="name" label="角色名称" width="150"></el-table-column>
                <el-table-column prop="code" label="角色编码" width="150"></el-table-column>
                <el-table-column prop="description" label="角色描述"></el-table-column>
                <el-table-column prop="status" label="状态" width="100">
                  <template #default="scope">
                    <el-switch v-model="scope.row.status" @change="updateRoleStatus(scope.row)"></el-switch>
                  </template>
                </el-table-column>
                <el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
                <el-table-column label="操作" width="200" fixed="right">
                  <template #default="scope">
                    <el-button type="primary" size="small" @click="editRole(scope.row)">
                      <el-icon><Edit /></el-icon> 编辑
                    </el-button>
                    <el-button type="danger" size="small" @click="deleteRole(scope.row.id)">
                      <el-icon><Delete /></el-icon> 删除
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>
            </div>
          </el-tab-pane>

          <!-- 权限管理 -->
          <el-tab-pane label="权限管理" name="permissions">
            <div class="module-content">
              <el-card class="setting-card">
                <template #header>
                  <div class="card-header">
                    <span>权限列表</span>
                  </div>
                </template>
                <el-tree
                  :data="permissionTreeData"
                  :props="permissionTreeProps"
                  show-checkbox
                  node-key="id"
                  :default-expanded-keys="[1, 2]"
                  :default-checked-keys="[1, 2, 3]"
                  class="permission-tree"
                >
                </el-tree>
                <div class="permission-actions">
                  <el-button type="primary" @click="savePermissions">保存权限</el-button>
                </div>
              </el-card>
            </div>
          </el-tab-pane>

          <!-- 日志管理 -->
          <el-tab-pane label="日志管理" name="logs">
            <div class="module-content">
              <div class="logs-header">
                <el-form :inline="true" :model="logSearchForm" class="log-search-form">
                  <el-form-item label="用户">
                    <el-input v-model="logSearchForm.username" placeholder="请输入用户名"></el-input>
                  </el-form-item>
                  <el-form-item label="操作类型">
                    <el-select v-model="logSearchForm.operationType" placeholder="请选择操作类型">
                      <el-option label="登录" value="login"></el-option>
                      <el-option label="查看" value="view"></el-option>
                      <el-option label="新增" value="add"></el-option>
                      <el-option label="编辑" value="edit"></el-option>
                      <el-option label="删除" value="delete"></el-option>
                    </el-select>
                  </el-form-item>
                  <el-form-item label="操作时间">
                    <el-date-picker v-model="logSearchForm.operationTime" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
                  </el-form-item>
                  <el-form-item>
                    <el-button type="primary" @click="searchLogs">
                      <el-icon><Search /></el-icon> 搜索
                    </el-button>
                    <el-button @click="resetLogSearch">重置</el-button>
                  </el-form-item>
                </el-form>
              </div>
              <el-table :data="logsData" style="width: 100%">
                <el-table-column prop="id" label="ID" width="80" align="center"></el-table-column>
                <el-table-column prop="username" label="用户名" min-width="120" show-overflow-tooltip></el-table-column>
                <el-table-column prop="operationType" label="操作类型" width="120">
                  <template #default="scope">
                    <el-tag :type="getLogTypeTagType(scope.row.operationType)">
                      {{ getLogTypeText(scope.row.operationType) }}
                    </el-tag>
                  </template>
                </el-table-column>
                <el-table-column prop="operationContent" label="操作内容" min-width="200" show-overflow-tooltip></el-table-column>
                <el-table-column prop="ipAddress" label="IP地址" min-width="150"></el-table-column>
                <el-table-column prop="operationTime" label="操作时间" width="180"></el-table-column>
              </el-table>
              <div class="pagination">
                <el-pagination
                  @size-change="handleLogSizeChange"
                  @current-change="handleLogCurrentChange"
                  :current-page="logPage.currentPage"
                  :page-sizes="[10, 20, 50, 100]"
                  :page-size="logPage.pageSize"
                  layout="total, sizes, prev, pager, next, jumper"
                  :total="logPage.total"
                >
                </el-pagination>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

      <!-- 新增/编辑角色对话框 -->
      <el-dialog
        v-model="roleDialogVisible"
        title="角色信息"
        width="500px"
        @close="resetRoleForm"
      >
        <el-form :model="roleForm" label-width="100px">
          <el-form-item label="角色名称" prop="name">
            <el-input v-model="roleForm.name"></el-input>
          </el-form-item>
          <el-form-item label="角色编码" prop="code">
            <el-input v-model="roleForm.code"></el-input>
          </el-form-item>
          <el-form-item label="角色描述" prop="description">
            <el-input v-model="roleForm.description" type="textarea" :rows="3"></el-input>
          </el-form-item>
        </el-form>
        <template #footer>
          <div class="dialog-footer">
            <el-button @click="roleDialogVisible = false">取消</el-button>
            <el-button type="primary" @click="saveRole">确定</el-button>
          </div>
        </template>
      </el-dialog>
    </div>
  </AdminLayout>
</template>

<script>
import { ref } from 'vue';
import AdminLayout from '@/components/AdminLayout.vue';
import { User, Upload, Delete, Plus, Edit, Search } from '@element-plus/icons-vue';

export default {
  name: 'SystemSettings',
  components: {
    AdminLayout,
    User,
    Upload,
    Delete,
    Plus,
    Edit,
    Search
  },
  setup() {
    // 活跃标签页
    const activeTab = ref('profile');

    // 个人资料表单
    const profileForm = ref({
      username: 'admin',
      realName: '管理员',
      email: 'admin@example.com',
      phone: '13800138000',
      branch: 'first',
      gender: 'male',
      birthDate: new Date('1990-01-01')
    });

    // 用户头像
    const userAvatar = ref('');

    // 密码表单
    const passwordForm = ref({
      oldPassword: '',
      newPassword: '',
      confirmPassword: ''
    });

    // 系统参数表单
    const systemForm = ref({
      systemName: '党员教育与管理系统',
      systemVersion: 'v1.0.0',
      systemDescription: '党员教育与管理系统，提供党员信息管理、党组织管理、学习教育等功能',
      allowRegistration: true,
      emailVerification: true,
      pageSize: '20',
      systemNotice: '欢迎使用党员教育与管理系统！'
    });

    // 角色管理
    const roleDialogVisible = ref(false);
    const roleForm = ref({
      id: null,
      name: '',
      code: '',
      description: ''
    });

    // 角色数据
    const rolesData = ref([
      {
        id: 1,
        name: '超级管理员',
        code: 'super_admin',
        description: '拥有系统所有权限',
        status: true,
        createTime: '2023-01-01 10:00:00'
      },
      {
        id: 2,
        name: '管理员',
        code: 'admin',
        description: '拥有系统大部分权限',
        status: true,
        createTime: '2023-01-01 10:00:00'
      },
      {
        id: 3,
        name: '普通用户',
        code: 'user',
        description: '拥有基础功能权限',
        status: true,
        createTime: '2023-01-01 10:00:00'
      }
    ]);

    // 权限树数据
    const permissionTreeData = ref([
      {
        id: 1,
        label: '系统管理',
        children: [
          {
            id: 2,
            label: '用户管理',
            children: [
              { id: 3, label: '查看用户' },
              { id: 4, label: '添加用户' },
              { id: 5, label: '编辑用户' },
              { id: 6, label: '删除用户' }
            ]
          },
          {
            id: 7,
            label: '角色管理',
            children: [
              { id: 8, label: '查看角色' },
              { id: 9, label: '添加角色' },
              { id: 10, label: '编辑角色' },
              { id: 11, label: '删除角色' }
            ]
          },
          {
            id: 12,
            label: '权限管理',
            children: [
              { id: 13, label: '查看权限' },
              { id: 14, label: '编辑权限' }
            ]
          }
        ]
      },
      {
        id: 15,
        label: '党员管理',
        children: [
          {
            id: 16,
            label: '党员信息',
            children: [
              { id: 17, label: '查看党员' },
              { id: 18, label: '添加党员' },
              { id: 19, label: '编辑党员' },
              { id: 20, label: '删除党员' }
            ]
          }
        ]
      }
    ]);

    // 权限树属性配置
    const permissionTreeProps = ref({
      children: 'children',
      label: 'label'
    });

    // 日志搜索表单
    const logSearchForm = ref({
      username: '',
      operationType: '',
      operationTime: []
    });

    // 日志分页
    const logPage = ref({
      currentPage: 1,
      pageSize: 10,
      total: 50
    });

    // 日志数据
    const logsData = ref([
      {
        id: 1,
        username: 'admin',
        operationType: 'login',
        operationContent: '用户登录系统',
        ipAddress: '127.0.0.1',
        operationTime: '2023-06-01 10:00:00'
      },
      {
        id: 2,
        username: 'admin',
        operationType: 'add',
        operationContent: '添加新党员',
        ipAddress: '127.0.0.1',
        operationTime: '2023-06-01 10:30:00'
      },
      {
        id: 3,
        username: 'user',
        operationType: 'view',
        operationContent: '查看党员列表',
        ipAddress: '192.168.1.100',
        operationTime: '2023-06-01 11:00:00'
      }
    ]);

    // 保存个人资料
    const saveProfile = () => {
      console.log('保存个人资料:', profileForm.value);
      alert('个人资料保存成功');
    };

    // 重置个人资料
    const resetProfile = () => {
      profileForm.value = {
        username: 'admin',
        realName: '管理员',
        email: 'admin@example.com',
        phone: '13800138000',
        branch: 'first',
        gender: 'male',
        birthDate: new Date('1990-01-01')
      };
    };

    // 修改密码
    const changePassword = () => {
      if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
        alert('两次输入的新密码不一致');
        return;
      }
      console.log('修改密码:', passwordForm.value);
      alert('密码修改成功');
    };

    // 重置密码表单
    const resetPasswordForm = () => {
      passwordForm.value = {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      };
    };

    // 保存系统设置
    const saveSystemSettings = () => {
      console.log('保存系统设置:', systemForm.value);
      alert('系统设置保存成功');
    };

    // 重置系统设置表单
    const resetSystemForm = () => {
      systemForm.value = {
        systemName: '党员教育与管理系统',
        systemVersion: 'v1.0.0',
        systemDescription: '党员教育与管理系统，提供党员信息管理、党组织管理、学习教育等功能',
        allowRegistration: true,
        emailVerification: true,
        pageSize: '20',
        systemNotice: '欢迎使用党员教育与管理系统！'
      };
    };

    // 显示新增角色对话框
    const showAddRoleDialog = () => {
      roleDialogVisible.value = true;
      roleForm.value = {
        id: null,
        name: '',
        code: '',
        description: ''
      };
    };

    // 编辑角色
    const editRole = (role) => {
      roleDialogVisible.value = true;
      roleForm.value = { ...role };
    };

    // 保存角色
    const saveRole = () => {
      console.log('保存角色:', roleForm.value);
      roleDialogVisible.value = false;
      alert('角色保存成功');
    };

    // 删除角色
    const deleteRole = (id) => {
      console.log('删除角色:', id);
      alert('角色删除成功');
    };

    // 更新角色状态
    const updateRoleStatus = (role) => {
      console.log('更新角色状态:', role);
    };

    // 重置角色表单
    const resetRoleForm = () => {
      roleForm.value = {
        id: null,
        name: '',
        code: '',
        description: ''
      };
    };

    // 保存权限
    const savePermissions = () => {
      console.log('保存权限');
      alert('权限保存成功');
    };

    // 搜索日志
    const searchLogs = () => {
      console.log('搜索日志:', logSearchForm.value);
    };

    // 重置日志搜索
    const resetLogSearch = () => {
      logSearchForm.value = {
        username: '',
        operationType: '',
        operationTime: []
      };
    };

    // 日志分页大小变化
    const handleLogSizeChange = (size) => {
      logPage.value.pageSize = size;
      console.log('每页条数:', size);
    };

    // 日志当前页码变化
    const handleLogCurrentChange = (current) => {
      logPage.value.currentPage = current;
      console.log('当前页码:', current);
    };

    // 获取日志类型标签颜色
    const getLogTypeTagType = (type) => {
      const typeMap = {
        login: 'success',
        view: 'primary',
        add: 'info',
        edit: 'warning',
        delete: 'danger'
      };
      return typeMap[type] || 'info';
    };

    // 获取日志类型文本
    const getLogTypeText = (type) => {
      const typeMap = {
        login: '登录',
        view: '查看',
        add: '新增',
        edit: '编辑',
        delete: '删除'
      };
      return typeMap[type] || type;
    };

    return {
      activeTab,
      profileForm,
      userAvatar,
      passwordForm,
      systemForm,
      roleDialogVisible,
      roleForm,
      rolesData,
      permissionTreeData,
      permissionTreeProps,
      logSearchForm,
      logPage,
      logsData,
      saveProfile,
      resetProfile,
      changePassword,
      resetPasswordForm,
      saveSystemSettings,
      resetSystemForm,
      showAddRoleDialog,
      editRole,
      saveRole,
      deleteRole,
      updateRoleStatus,
      resetRoleForm,
      savePermissions,
      searchLogs,
      resetLogSearch,
      handleLogSizeChange,
      handleLogCurrentChange,
      getLogTypeTagType,
      getLogTypeText
    };
  }
};
</script>

<style scoped>
.system-settings {
  padding: 20px;
  background-color: #f5f7fa;
}

.breadcrumb {
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 20px;
  color: #303133;
}

.settings-modules {
  margin-bottom: 20px;
}

.module-content {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
}

.setting-card {
  margin-bottom: 20px;
}

.card-header {
  font-size: 16px;
  font-weight: bold;
}

/* 个人资料样式 */
.profile-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar-section {
  margin-bottom: 30px;
  text-align: center;
}

.user-avatar {
  margin-bottom: 15px;
}

.avatar-actions .el-button {
  margin: 0 5px;
}

.profile-form {
  width: 100%;
  max-width: 600px;
}

/* 角色管理样式 */
.roles-header {
  margin-bottom: 20px;
  text-align: right;
}

/* 权限管理样式 */
.permission-tree {
  height: 400px;
  overflow-y: auto;
  margin-bottom: 20px;
}

.permission-actions {
  text-align: right;
}

/* 日志管理样式 */
.logs-header {
  margin-bottom: 20px;
}

.log-search-form {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.pagination {
  margin-top: 20px;
  text-align: right;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .system-settings {
    padding: 10px;
  }

  .page-title {
    font-size: 20px;
  }

  .module-content {
    padding: 10px;
  }

  .profile-form {
    width: 100%;
  }

  .log-search-form {
    flex-direction: column;
    align-items: stretch;
  }

  .log-search-form .el-form-item {
    width: 100%;
  }

  .pagination {
    text-align: center;
  }
}
</style>
