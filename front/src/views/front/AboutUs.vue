<template>
  <div class="about-us">
    <!-- 导航栏 -->
    <Navbar />
    
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>红芯溯源・炬立初心</h2>
    </div>

    <!-- 内容容器 -->
    <div class="content-container">
      <!-- 部门介绍 -->
      <div class="section">
        <h3 class="section-title">部门介绍</h3>
        <div class="section-content">
          <p>红芯软炬党建教育平台是为了加强党员队伍建设，提高党员素质，推进党建工作信息化、规范化而开发的综合管理平台。</p>
          <p>系统旨在通过现代化信息技术手段，实现党员信息管理、学习教育、组织活动、考核评价等工作的数字化管理，提高党建工作效率和质量。</p>
          <p>我们的使命是：为党组织提供高效、便捷的管理工具，为党员提供优质的学习教育资源，促进党建工作创新发展。</p>
        </div>
      </div>

      <!-- 组织架构 -->
      <div class="section">
        <h3 class="section-title">组织架构</h3>
        <div class="organization-chart">
          <div class="org-node top-node">
            <div class="node-content">西安工业大学党委</div>
          </div>
          <div class="org-level">
            <div class="org-node second-node">
              <div class="node-content">计算机科学与工程学院党委</div>
            </div>
          </div>
          <div class="org-level">
            <div class="org-node third-node">
              <div class="node-content">教师第三党支部</div>
            </div>
          </div>
          <div class="org-level">
            <div class="org-node fourth-node">
              <div class="node-content">红芯计科学党小组</div>
            </div>
            <div class="org-node fourth-node">
              <div class="node-content">炬光软工党小组</div>
            </div>
            <div class="org-node fourth-node">
              <div class="node-content">数智研发党小组</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 联系我们 -->
      <div class="section">
        <h3 class="section-title">联系我们</h3>
        <div class="contact-info">
          <div class="contact-item">
            <i class="contact-icon">📍</i>
            <div class="contact-text">
              <div class="contact-label">办公地址</div>
              <div class="contact-value">陕西省西安市未央区学府中路二号</div>
            </div>
          </div>
          <div class="contact-item">
            <i class="contact-icon">📞</i>
            <div class="contact-text">
              <div class="contact-label">联系电话</div>
              <div class="contact-value">029-86173020</div>
            </div>
          </div>
          <div class="contact-item">
            <i class="contact-icon">✉️</i>
            <div class="contact-text">
              <div class="contact-label">电子邮箱</div>
              <div class="contact-value">addery8023@163.com</div>
            </div>
          </div>
          <div class="contact-item">
            <i class="contact-icon">🕒</i>
            <div class="contact-text">
              <div class="contact-label">办公时间</div>
              <div class="contact-value">周一至周五 8:30-17:30</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    
    <!-- 智能助手 -->
    <ChatAssistant />
  </div>
</template>

<script setup>
// 关于我们页面组件
import Navbar from '@/components/front/Navbar.vue'
import ChatAssistant from '@/components/front/ChatAssistant.vue'
</script>

<style scoped>
.about-us {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.page-header {
  background-color: #c72c41;
  color: white;
  padding: 40px 0;
  text-align: center;
  width: 100%;
}

.page-header h2 {
  font-size: 28px;
  font-weight: bold;
  margin: 0;
  color: white;
}

.content-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  background-color: white;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  max-width: 1200px;
  margin: 30px auto;
}

.section {
  margin-bottom: 40px;
  background-color: white;
  padding: 20px;
}

.section:last-child {
  margin-bottom: 0;
}

.section-title {
  font-size: 22px;
  color: #2c3e50;
  margin: 0 0 20px 0;
  padding-bottom: 10px;
  border-bottom: 2px solid #d93025;
  display: inline-block;
}

.section-content {
  color: #666;
  line-height: 1.8;
}

.section-content p {
  margin: 0 0 15px 0;
}

/* 组织架构 */
.organization-chart {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
}

.org-level {
  display: flex;
  gap: 30px;
  justify-content: center;
}

.org-node {
  min-width: 150px;
}

.node-content {
  background-color: white;
  padding: 15px;
  border-radius: 8px;
  text-align: center;
  font-weight: 500;
  color: #2c3e50;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.top-node .node-content {
  background-color: #c72c41;
  color: white;
  font-size: 18px;
  padding: 20px;
  min-width: 250px;
}

.second-node .node-content {
  background-color: #3b82f6;
  color: white;
  padding: 18px;
  min-width: 220px;
}

.third-node .node-content {
  border: 2px solid #c72c41;
  background-color: white;
  color: #2c3e50;
  padding: 18px;
  min-width: 180px;
}

.fourth-node .node-content {
  border: 2px solid #3b82f6;
  background-color: white;
  color: #2c3e50;
  padding: 15px;
  min-width: 150px;
}

/* 联系我们 */
.contact-info {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.contact-item {
  display: flex;
  gap: 20px;
  align-items: flex-start;
}

.contact-icon {
  font-size: 24px;
  color: #d93025;
  margin-top: 5px;
}

.contact-text {
  flex: 1;
}

.contact-label {
  font-weight: 500;
  color: #2c3e50;
  margin-bottom: 5px;
}

.contact-value {
  color: #666;
}
</style>