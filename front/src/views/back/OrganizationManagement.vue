<script setup>
import { ref, computed } from 'vue'
import AdminLayout from '@/components/AdminLayout.vue'

// 模拟组织架构数据
const organizations = ref([
  {
    id: 1,
    name: '西安工业大学党委',
    level: 1,
    parentId: null,
    description: '西安工业大学党的委员会',
    leader: '张书记',
    memberCount: 1500,
    children: [
      {
        id: 2,
        name: '西安工业大学计算机科学与工程学院党委',
        level: 2,
        parentId: 1,
        description: '计算机科学与工程学院党的委员会',
        leader: '杨正强书记',
        memberCount: 300,
        children: [
          {
            id: 3,
            name: '教师第三党支部',
            level: 3,
            parentId: 2,
            description: '计算机科学与工程学院教师第三党支部',
            leader: '惠燕书记',
            memberCount: 50,
            children: [
              {
                id: 4,
                name: '红芯计科学党小组',
                level: 4,
                parentId: 3,
                description: '计算机科学与工程学院教师第三党支部红芯计科学党小组',
                leader: '',
                memberCount: 15,
                children: []
              },
              {
                id: 5,
                name: '炬光软工党小组',
                level: 4,
                parentId: 3,
                description: '计算机科学与工程学院教师第三党支部炬光软工党小组',
                leader: '',
                memberCount: 20,
                children: []
              },
              {
                id: 6,
                name: '数智研发党小组',
                level: 4,
                parentId: 3,
                description: '计算机科学与工程学院教师第三党支部数智研发党小组',
                leader: '',
                memberCount: 15,
                children: []
              }
            ]
          }
        ]
      }
    ]
  }
])

// 展开/折叠状态
const expandedIds = ref([1])

// 切换展开/折叠
const toggleExpand = (id) => {
  const index = expandedIds.value.indexOf(id)
  if (index > -1) {
    expandedIds.value.splice(index, 1)
  } else {
    expandedIds.value.push(id)
  }
}

// 是否展开
const isExpanded = (id) => {
  return expandedIds.value.includes(id)
}

// 选中的组织
const selectedOrganization = ref(null)

// 选择组织
const selectOrganization = (org) => {
  selectedOrganization.value = org
}

// 模态框状态
const showAddModal = ref(false)
const showEditModal = ref(false)
const showDeleteModal = ref(false)
const showDetailModal = ref(false)

// 新组织数据
const newOrg = ref({
  name: '',
  description: '',
  leader: '',
  parentId: null
})

// 编辑组织数据
const editOrg = ref({
  id: null,
  name: '',
  description: '',
  leader: ''
})

// 打开添加模态框
const openAddModal = (parentId = null) => {
  newOrg.value = {
    name: '',
    description: '',
    leader: '',
    parentId
  }
  showAddModal.value = true
}

// 打开编辑模态框
const openEditModal = (org) => {
  editOrg.value = {
    id: org.id,
    name: org.name,
    description: org.description,
    leader: org.leader
  }
  showEditModal.value = true
}

// 打开删除模态框
const openDeleteModal = (org) => {
  selectedOrganization.value = org
  showDeleteModal.value = true
}

// 添加组织
const addOrganization = () => {
  // 生成新ID
  const newId = Math.max(...getAllOrgIds(), 0) + 1
  
  // 构建新组织
  const org = {
    id: newId,
    name: newOrg.value.name,
    description: newOrg.value.description,
    leader: newOrg.value.leader,
    parentId: newOrg.value.parentId,
    memberCount: 0,
    level: newOrg.value.parentId ? getOrgLevel(newOrg.value.parentId) + 1 : 1,
    children: []
  }
  
  // 添加到对应的父组织中
  if (newOrg.value.parentId) {
    const parent = findOrgById(newOrg.value.parentId, organizations.value)
    if (parent) {
      parent.children.push(org)
    }
  } else {
    organizations.value.push(org)
  }
  
  // 关闭模态框
  showAddModal.value = false
}

// 编辑组织
const updateOrganization = () => {
  const org = findOrgById(editOrg.value.id, organizations.value)
  if (org) {
    org.name = editOrg.value.name
    org.description = editOrg.value.description
    org.leader = editOrg.value.leader
  }
  showEditModal.value = false
}

// 删除组织
const deleteOrganization = () => {
  if (!selectedOrganization.value) return
  
  const parent = findParentOrg(selectedOrganization.value.id, organizations.value)
  if (parent) {
    parent.children = parent.children.filter(child => child.id !== selectedOrganization.value.id)
  } else {
    organizations.value = organizations.value.filter(org => org.id !== selectedOrganization.value.id)
  }
  
  selectedOrganization.value = null
  showDeleteModal.value = false
}

// 辅助函数：获取所有组织ID
const getAllOrgIds = () => {
  const ids = []
  const collectIds = (orgs) => {
    for (const org of orgs) {
      ids.push(org.id)
      if (org.children && org.children.length > 0) {
        collectIds(org.children)
      }
    }
  }
  collectIds(organizations.value)
  return ids
}

// 辅助函数：通过ID查找组织
const findOrgById = (id, orgs) => {
  for (const org of orgs) {
    if (org.id === id) {
      return org
    }
    if (org.children && org.children.length > 0) {
      const result = findOrgById(id, org.children)
      if (result) {
        return result
      }
    }
  }
  return null
}

// 辅助函数：查找父组织
const findParentOrg = (id, orgs) => {
  for (const org of orgs) {
    if (org.children && org.children.some(child => child.id === id)) {
      return org
    }
    if (org.children && org.children.length > 0) {
      const result = findParentOrg(id, org.children)
      if (result) {
        return result
      }
    }
  }
  return null
}

// 辅助函数：获取组织级别
const getOrgLevel = (id) => {
  const org = findOrgById(id, organizations.value)
  return org ? org.level : 0
}

// 计算属性：当前选中组织的详情
const selectedOrgDetails = computed(() => {
  return selectedOrganization.value
})

// 面包屑导航
const breadcrumb = ref([])

// 更新面包屑 - 去掉顶层校党委
const updateBreadcrumb = (org) => {
  const path = []
  const buildPath = (id) => {
    const org = findOrgById(id, organizations.value)
    if (org) {
      // 如果不是顶层校党委，则添加到面包屑
      if (org.level > 1) {
        path.unshift(org)
      }
      if (org.parentId) {
        buildPath(org.parentId)
      }
    }
  }
  if (org) {
    buildPath(org.id)
  }
  breadcrumb.value = path
}

// 点击组织时更新面包屑
const handleOrgClick = (org) => {
  selectOrganization(org)
  updateBreadcrumb(org)
}

// 打开详情模态框
const openDetailModal = (org) => {
  selectOrganization(org)
  showDetailModal.value = true
}

// 初始化 - 直接显示校党委的子组织
if (organizations.value.length > 0 && organizations.value[0].children.length > 0) {
  // 初始化时设置面包屑为第一个子组织
  selectedOrganization.value = organizations.value[0].children[0]
  updateBreadcrumb(organizations.value[0].children[0])
}

// 模拟API调用：获取组织架构
const fetchOrganizations = () => {
  // 实际项目中这里会调用API获取数据
  console.log('获取组织架构数据')
}

// 初始化数据
fetchOrganizations()
</script>

<template>
  <AdminLayout>
    <div class="organization-management">
      <!-- 面包屑导航 -->
      <div class="breadcrumb">
        <!-- 始终显示"组织管理"作为面包屑起点 -->
        <span>组织管理</span>
        <span v-if="breadcrumb.length > 0"> / </span>
        <span v-for="(item, index) in breadcrumb" :key="item.id">
          {{ item.name }}
          <span v-if="index < breadcrumb.length - 1"> / </span>
        </span>
      </div>

      <!-- 页面标题区域 -->
      <div class="page-header">
        <h2>组织架构</h2>
        <button class="btn btn-primary" @click="openAddModal">添加组织</button>
      </div>

      <!-- 内容区域 -->
      <div class="content-section">
        <!-- 组织树 -->
        <div class="organization-tree">
          <div class="tree-container">
            <!-- 直接显示校党委的子组织，跳过顶层校党委 -->
            <div v-if="organizations.length > 0">
              <!-- 遍历校党委的子组织 -->
              <div 
                class="tree-node" 
                v-for="org in organizations[0].children" 
                :key="org.id"
              >
                <div 
                  class="node-header" 
                  :class="{ 'node-selected': selectedOrganization?.id === org.id }"
                  @click="handleOrgClick(org)"
                >
                  <span 
                    class="expand-icon" 
                    v-if="org.children && org.children.length > 0"
                    @click.stop="toggleExpand(org.id)"
                  >
                    {{ isExpanded(org.id) ? '▼' : '▶' }}
                  </span>
                  <span class="node-name">{{ org.name }}</span>
                  <span class="node-leader">{{ org.leader }}</span>
                  <div class="node-actions">
                    <button class="btn-action" @click.stop="openDetailModal(org)" title="查看详情">查看详情</button>
                    <button class="btn-action" @click.stop="openAddModal(org.id)" title="添加子组织">添加</button>
                    <button class="btn-action" @click.stop="openEditModal(org)" title="编辑">编辑</button>
                    <button class="btn-action" @click.stop="openDeleteModal(org)" title="删除">删除</button>
                  </div>
                </div>
                <!-- 显示二级子组织 -->
                <div 
                  class="node-children" 
                  v-if="org.children && org.children.length > 0 && isExpanded(org.id)"
                >
                  <div 
                    class="tree-node" 
                    v-for="child in org.children" 
                    :key="child.id"
                    :style="{ marginLeft: '20px' }"
                  >
                    <div 
                      class="node-header" 
                      :class="{ 'node-selected': selectedOrganization?.id === child.id }"
                      @click="handleOrgClick(child)"
                    >
                      <span 
                        class="expand-icon" 
                        v-if="child.children && child.children.length > 0"
                        @click.stop="toggleExpand(child.id)"
                      >
                        {{ isExpanded(child.id) ? '▼' : '▶' }}
                      </span>
                      <span class="node-name">{{ child.name }}</span>
                      <span class="node-leader">{{ child.leader }}</span>
                      <div class="node-actions">
                        <button class="btn-action" @click.stop="openDetailModal(child)" title="查看详情">查看详情</button>
                        <button class="btn-action" @click.stop="openAddModal(child.id)" title="添加子组织">添加</button>
                        <button class="btn-action" @click.stop="openEditModal(child)" title="编辑">编辑</button>
                        <button class="btn-action" @click.stop="openDeleteModal(child)" title="删除">删除</button>
                      </div>
                    </div>
                    <!-- 显示三级子组织 -->
                    <div 
                      class="node-children" 
                      v-if="child.children && child.children.length > 0 && isExpanded(child.id)"
                    >
                      <div 
                        class="tree-node" 
                        v-for="grandchild in child.children" 
                        :key="grandchild.id"
                        :style="{ marginLeft: '20px' }"
                      >
                        <div 
                          class="node-header" 
                          :class="{ 'node-selected': selectedOrganization?.id === grandchild.id }"
                          @click="handleOrgClick(grandchild)"
                        >
                          <span class="node-name">{{ grandchild.name }}</span>
                          <span class="node-leader">{{ grandchild.leader }}</span>
                          <div class="node-actions">
                            <button class="btn-action" @click.stop="openDetailModal(grandchild)" title="查看详情">查看详情</button>
                            <button class="btn-action" @click.stop="openEditModal(grandchild)" title="编辑">编辑</button>
                            <button class="btn-action" @click.stop="openDeleteModal(grandchild)" title="删除">删除</button>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 添加组织模态框 -->
      <div class="modal" v-if="showAddModal">
        <div class="modal-content">
          <div class="modal-header">
            <h3>添加组织</h3>
            <button class="close-btn" @click="showAddModal = false">×</button>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label>组织名称：</label>
              <input type="text" class="form-control" v-model="newOrg.name" required />
            </div>
            <div class="form-group">
              <label>组织描述：</label>
              <textarea class="form-control" v-model="newOrg.description" rows="3"></textarea>
            </div>
            <div class="form-group">
              <label>负责人：</label>
              <input type="text" class="form-control" v-model="newOrg.leader" required />
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showAddModal = false">取消</button>
            <button class="btn btn-primary" @click="addOrganization">添加</button>
          </div>
        </div>
      </div>

      <!-- 组织详情模态框 -->
      <div class="modal" v-if="showDetailModal">
        <div class="modal-content">
          <div class="modal-header">
            <h3>组织详情</h3>
            <button class="close-btn" @click="showDetailModal = false">×</button>
          </div>
          <div class="modal-body">
            <div v-if="selectedOrgDetails" class="details-content">
              <div class="detail-item">
                <label>组织名称：</label>
                <span>{{ selectedOrgDetails.name }}</span>
              </div>
              <div class="detail-item">
                <label>组织级别：</label>
                <span>{{ 
                  selectedOrgDetails.level === 1 ? '校级' : 
                  selectedOrgDetails.level === 2 ? '院系级' : 
                  selectedOrgDetails.level === 3 ? '支部级' : '其他'
                }}</span>
              </div>
              <div class="detail-item">
                <label>负责人：</label>
                <span>{{ selectedOrgDetails.leader }}</span>
              </div>
              <div class="detail-item">
                <label>党员人数：</label>
                <span>{{ selectedOrgDetails.memberCount }}</span>
              </div>
              <div class="detail-item">
                <label>组织描述：</label>
                <span>{{ selectedOrgDetails.description }}</span>
              </div>
              <div class="detail-item">
                <label>子组织数量：</label>
                <span>{{ selectedOrgDetails.children ? selectedOrgDetails.children.length : 0 }}</span>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-primary" @click="showDetailModal = false">关闭</button>
          </div>
        </div>
      </div>

      <!-- 编辑组织模态框 -->
      <div class="modal" v-if="showEditModal">
        <div class="modal-content">
          <div class="modal-header">
            <h3>编辑组织</h3>
            <button class="close-btn" @click="showEditModal = false">×</button>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label>组织名称：</label>
              <input type="text" class="form-control" v-model="editOrg.name" required />
            </div>
            <div class="form-group">
              <label>组织描述：</label>
              <textarea class="form-control" v-model="editOrg.description" rows="3"></textarea>
            </div>
            <div class="form-group">
              <label>负责人：</label>
              <input type="text" class="form-control" v-model="editOrg.leader" required />
            </div>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showEditModal = false">取消</button>
            <button class="btn btn-primary" @click="updateOrganization">保存</button>
          </div>
        </div>
      </div>

      <!-- 删除组织模态框 -->
      <div class="modal" v-if="showDeleteModal">
        <div class="modal-content">
          <div class="modal-header">
            <h3>删除组织</h3>
            <button class="close-btn" @click="showDeleteModal = false">×</button>
          </div>
          <div class="modal-body">
            <p>确定要删除组织 "{{ selectedOrganization?.name }}" 吗？</p>
            <p class="warning-text">此操作将同时删除该组织下的所有子组织，且不可恢复！</p>
          </div>
          <div class="modal-footer">
            <button class="btn btn-secondary" @click="showDeleteModal = false">取消</button>
            <button class="btn btn-danger" @click="deleteOrganization">删除</button>
          </div>
        </div>
      </div>
    </div>
  </AdminLayout>
</template>

<style scoped>
/* 页面容器 */
.organization-management {
  width: 100%;
  min-height: 100vh;
  margin: 0;
  padding: 24px;
  background-color: #ffffff;
  border-radius: 0;
  box-shadow: none;
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
  box-sizing: border-box;
}

/* 页面头部 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.breadcrumb {
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
  font-size: 14px;
  color: #6c757d;
  opacity: 0.8;
  flex: 1;
}

.page-header h2 {
  margin: 0;
  color: #212529;
  font-size: 24px;
  font-weight: 600;
}

/* 按钮样式 */
.btn {
  padding: 10px 20px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: all 0.2s ease;
  outline: none;
}

.btn-primary {
  background-color: #c72c41;
  color: white;
  border-color: #c72c41;
}

.btn-primary:hover {
  background-color: #a82336;
  border-color: #a82336;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(199, 44, 65, 0.2);
}

.btn-secondary {
  background-color: #f8f9fa;
  color: #495057;
  border: 1px solid #dee2e6;
}

.btn-secondary:hover {
  background-color: #e9ecef;
  border-color: #adb5bd;
}

.btn-danger {
  background-color: #c72c41;
  color: white;
  border-color: #c72c41;
}

.btn-danger:hover {
  background-color: #a82336;
  border-color: #a82336;
}

/* 主内容区域 */
.content-section {
  width: 100%;
  max-width: 100%;
  margin: 0 auto;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

/* 组织树容器 */
.organization-tree {
  padding: 24px;
  width: 100%;
  box-sizing: border-box;
}

.organization-tree h3 {
  margin-top: 0;
  margin-bottom: 24px;
  color: #212529;
  font-size: 20px;
  font-weight: 600;
  padding-bottom: 16px;
  border-bottom: 1px solid #e9ecef;
}

/* 树结构容器 */
.tree-container {
  max-height: 650px;
  overflow-y: auto;
  padding-right: 10px;
}

/* 自定义滚动条 */
.tree-container::-webkit-scrollbar {
  width: 6px;
}

.tree-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.tree-container::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

.tree-container::-webkit-scrollbar-thumb:hover {
  background: #999;
}

/* 树节点 */
.tree-node {
  margin-bottom: 6px;
  width: 100%;
  box-sizing: border-box;
}

.node-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s ease;
  background-color: white;
  border: 1px solid #f0f0f0;
  position: relative;
  width: 100%;
  box-sizing: border-box;
  flex-wrap: wrap;
  gap: 10px;
}

.node-header:hover {
  background-color: #f8f9fa;
  border-color: #e9ecef;
}

.node-selected {
  background-color: #f8d7da;
  border-color: #f5c6cb;
}

/* 展开/折叠图标 */
.expand-icon {
  font-size: 14px;
  margin-right: 12px;
  color: #6c757d;
  cursor: pointer;
  width: 16px;
  text-align: center;
}

/* 节点名称 */
.node-name {
  flex: 2;
  min-width: 150px;
  font-weight: 500;
  color: #212529;
  font-size: 15px;
}

/* 节点负责人 */
.node-leader {
  flex: 1;
  min-width: 100px;
  font-size: 13px;
  color: #6c757d;
  font-weight: 400;
}

/* 操作按钮组 */
.node-actions {
  display: flex;
  gap: 8px;
  opacity: 0.7;
  flex-shrink: 0;
}

.btn-action {
  background: none;
  border: none;
  cursor: pointer;
  font-size: 14px;
  padding: 6px 10px;
  border-radius: 4px;
  transition: all 0.2s ease;
  color: #6c757d;
  font-weight: 500;
}

.btn-action:hover {
  background-color: #f8f9fa;
  color: #495057;
}

/* 子节点容器 */
.node-children {
  margin-left: 24px;
  padding-left: 12px;
  border-left: 1px solid #e9ecef;
}

/* 模态框样式 */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  width: 550px;
  max-width: 90%;
  max-height: 90vh;
  overflow-y: auto;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 25px;
  border-bottom: 1px solid #e9ecef;
}

.modal-header h3 {
  margin: 0;
  color: #212529;
  font-size: 20px;
  font-weight: 600;
}

.close-btn {
  background: none;
  border: none;
  font-size: 28px;
  cursor: pointer;
  color: #adb5bd;
  padding: 0;
  width: 30px;
  height: 30px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 4px;
  transition: all 0.2s ease;
}

.close-btn:hover {
  color: #6c757d;
  background-color: #f8f9fa;
}

.modal-body {
  padding: 25px;
}

/* 表单样式 */
.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  font-weight: 500;
  color: #495057;
  font-size: 14px;
}

.form-control {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid #ced4da;
  border-radius: 6px;
  font-size: 14px;
  transition: border-color 0.2s ease;
  font-family: inherit;
}

.form-control:focus {
  outline: none;
  border-color: #dc3545;
  box-shadow: 0 0 0 3px rgba(220, 53, 69, 0.1);
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 20px 25px;
  border-top: 1px solid #e9ecef;
  background-color: #f8f9fa;
}

/* 警告文本 */
.warning-text {
  color: #dc3545;
  font-weight: 500;
  margin-top: 15px;
  display: block;
}

/* 响应式设计 */
@media (max-width: 1200px) {
  .organization-tree {
    padding: 20px;
  }
}

@media (max-width: 992px) {
  .node-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 8px;
  }
  
  .node-name,
  .node-leader {
    width: 100%;
  }
  
  .node-actions {
    width: 100%;
    justify-content: flex-end;
  }
}

@media (max-width: 768px) {
  .organization-management {
    padding: 20px 15px;
  }
  
  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 10px;
  }
  
  .page-header h2 {
    font-size: 24px;
  }
  
  .organization-tree {
    padding: 15px;
  }
  
  .modal-content {
    width: 95%;
  }
  
  .modal-header,
  .modal-body,
  .modal-footer {
    padding: 20px;
  }
}

@media (max-width: 480px) {
  .node-actions {
    flex-wrap: wrap;
    gap: 4px;
    justify-content: space-between;
  }
  
  .btn-action {
    flex: 1;
    min-width: calc(50% - 4px);
    padding: 4px 8px;
    font-size: 12px;
    text-align: center;
  }
}
</style>