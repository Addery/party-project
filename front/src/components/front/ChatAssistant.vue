<script setup>
import { ref, nextTick } from 'vue'
import { sendChatMessage } from '@/api/chat'
import { marked } from 'marked'

// 控制聊天窗口的显示与隐藏
const isChatOpen = ref(false)

// 聊天消息列表
const messages = ref([
  { 
    id: 1, 
    content: '您好！我是智能问答助手，有什么可以帮助您的吗？', 
    sender: 'assistant', 
    timestamp: new Date().toLocaleTimeString() 
  }
])

// 输入消息内容
const inputMessage = ref('')

// 加载状态
const isLoading = ref(false)

// 聊天消息列表容器的引用
const chatMessagesRef = ref(null)

// 切换聊天窗口的显示状态
const toggleChat = () => {
  isChatOpen.value = !isChatOpen.value
  // 如果打开聊天窗口，滚动到底部
  if (isChatOpen.value) {
    scrollToBottom()
  }
}

// 滚动到聊天消息列表的底部
const scrollToBottom = () => {
  nextTick(() => {
    if (chatMessagesRef.value) {
      chatMessagesRef.value.scrollTop = chatMessagesRef.value.scrollHeight
    }
  })
}

// 发送消息
const sendMessage = async () => {
  if (inputMessage.value.trim() && !isLoading.value) {
    // 添加用户消息
    messages.value.push({
      id: Date.now(),
      content: inputMessage.value.trim(),
      sender: 'user',
      timestamp: new Date().toLocaleTimeString()
    })
    
    // 滚动到底部，让用户能看到自己发送的消息
    scrollToBottom()

    // 清空输入框
    const messageContent = inputMessage.value.trim()
    inputMessage.value = ''

    // 设置加载状态
    isLoading.value = true

    try {
      // 调用后端API获取回复
      const response = await sendChatMessage(messageContent)
      
      // 处理可能的乱码问题
      let replyContent = response.data.reply
      if (typeof replyContent === 'string') {
        // 尝试手动解码乱码
        try {
          // 使用TextDecoder处理可能的UTF-8编码问题
          const decoder = new TextDecoder('utf-8');
          const encodedData = new TextEncoder().encode(replyContent);
          replyContent = decoder.decode(encodedData);
        } catch (e) {
          console.error('处理乱码失败:', e);
        }
      }
      
      // 添加助手回复
      messages.value.push({
        id: Date.now() + 1,
        content: replyContent,
        sender: 'assistant',
        timestamp: new Date().toLocaleTimeString()
      })
      
      // 滚动到底部
      scrollToBottom()
    } catch (error) {
      console.error('发送消息失败:', error)
      
      // 添加错误消息
      messages.value.push({
        id: Date.now() + 1,
        content: '抱歉，我暂时无法为您提供服务，请稍后再试。',
        sender: 'assistant',
        timestamp: new Date().toLocaleTimeString()
      })
    } finally {
      // 重置加载状态
      isLoading.value = false
    }
  }
}

// 处理键盘事件（按Enter发送消息）
const handleKeyPress = (e) => {
  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault()
    sendMessage()
  }
}
</script>

<template>
  <div class="chat-assistant-container">
    <!-- 聊天按钮 -->
    <div 
      class="chat-button"
      @click="toggleChat"
    >
      <span class="chat-icon">💬</span>
      <span class="chat-button-text" v-if="!isChatOpen">智能问答</span>
    </div>

    <!-- 聊天窗口 -->
    <div 
      class="chat-window"
      :class="{ 'chat-window-open': isChatOpen }"
    >
      <!-- 聊天窗口头部 -->
      <div class="chat-header">
        <h3>智能问答助手</h3>
        <button class="close-button" @click="toggleChat">×</button>
      </div>

      <!-- 聊天消息列表 -->
      <div class="chat-messages" ref="chatMessagesRef">
        <div 
          v-for="message in messages" 
          :key="message.id"
          class="message"
          :class="{ 'user-message': message.sender === 'user', 'assistant-message': message.sender === 'assistant' }"
        >
          <div class="message-content">
            <!-- 用户消息直接显示，助手消息使用Markdown渲染 -->
            <template v-if="message.sender === 'user'">
              {{ message.content }}
            </template>
            <template v-else>
              <div v-html="marked(message.content)"></div>
            </template>
          </div>
          <div class="message-time">{{ message.timestamp }}</div>
        </div>
        
        <!-- 加载状态 -->
        <div v-if="isLoading" class="message assistant-message">
          <div class="message-content">
            <div class="loading-indicator">
              <span class="loading-dot"></span>
              <span class="loading-dot"></span>
              <span class="loading-dot"></span>
            </div>
          </div>
        </div>
      </div>

      <!-- 消息输入区域 -->
      <div class="chat-input-container">
        <textarea 
          v-model="inputMessage"
          class="chat-input"
          placeholder="请输入您的问题..."
          rows="1"
          @keydown="handleKeyPress"
          :disabled="!isChatOpen"
        ></textarea>
        <button 
          class="send-button"
          @click="sendMessage"
          :disabled="!inputMessage.trim()"
        >
          发送
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.chat-assistant-container {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 1000;
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

/* 聊天按钮样式 */
.chat-button {
  display: flex;
  align-items: center;
  gap: 8px;
  background-color: #d93025;
  color: white;
  padding: 12px 16px;
  border-radius: 25px;
  cursor: pointer;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
  transition: all 0.3s ease;
  font-size: 14px;
  font-weight: 500;
}

.chat-button:hover {
  background-color: #b32a1e;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.3);
}

.chat-icon {
  font-size: 18px;
}

/* 聊天窗口样式 */
.chat-window {
  width: 320px;
  height: 400px;
  background-color: white;
  border-radius: 10px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
  display: flex;
  flex-direction: column;
  opacity: 0;
  transform: translateY(20px);
  visibility: hidden;
  transition: all 0.3s ease;
  margin-bottom: 10px;
}

.chat-window-open {
  opacity: 1;
  transform: translateY(0);
  visibility: visible;
}

/* 聊天窗口头部 */
.chat-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background-color: #d93025;
  color: white;
  border-radius: 10px 10px 0 0;
}

.chat-header h3 {
  font-size: 16px;
  font-weight: 600;
  margin: 0;
}

.close-button {
  background: none;
  border: none;
  color: white;
  font-size: 24px;
  cursor: pointer;
  padding: 0;
  line-height: 1;
}

/* 聊天消息列表 */
.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 15px;
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.chat-messages::-webkit-scrollbar {
  width: 4px;
}

.chat-messages::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 2px;
}

.chat-messages::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 2px;
}

.chat-messages::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

/* 消息样式 */
.message {
  display: flex;
  flex-direction: column;
  max-width: 85%;
  animation: messageSlide 0.3s ease;
}

@keyframes messageSlide {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

/* 用户消息 */
.user-message {
  align-self: flex-end;
}

.user-message .message-content {
  background-color: #d93025;
  color: white;
  padding: 12px 16px;
  border-radius: 18px 18px 4px 18px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

/* 助手消息 */
.assistant-message {
  align-self: flex-start;
}

.assistant-message .message-content {
  background-color: #f0f0f0;
  color: #333;
  padding: 12px 16px;
  border-radius: 18px 18px 18px 4px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.1);
}

/* Markdown样式 */
.assistant-message .message-content h1 {
  font-size: 18px;
  margin: 10px 0;
  font-weight: 600;
}

.assistant-message .message-content h2 {
  font-size: 16px;
  margin: 8px 0;
  font-weight: 600;
}

.assistant-message .message-content h3 {
  font-size: 14px;
  margin: 6px 0;
  font-weight: 600;
}

.assistant-message .message-content p {
  margin: 6px 0;
  line-height: 1.5;
}

.assistant-message .message-content ul,
.assistant-message .message-content ol {
  margin: 6px 0;
  padding-left: 20px;
}

.assistant-message .message-content li {
  margin: 4px 0;
}

.assistant-message .message-content a {
  color: #d93025;
  text-decoration: none;
}

.assistant-message .message-content a:hover {
  text-decoration: underline;
}

.assistant-message .message-content code {
  background-color: #e8e8e8;
  padding: 2px 4px;
  border-radius: 4px;
  font-family: monospace;
  font-size: 12px;
}

.assistant-message .message-content pre {
  background-color: #e8e8e8;
  padding: 10px;
  border-radius: 6px;
  overflow-x: auto;
  font-family: monospace;
  font-size: 12px;
  margin: 6px 0;
}

.assistant-message .message-content blockquote {
  border-left: 3px solid #d93025;
  padding-left: 10px;
  margin: 6px 0;
  color: #666;
  font-style: italic;
}

/* 消息时间 */
.message-time {
  font-size: 11px;
  color: #999;
  margin-top: 4px;
  align-self: flex-end;
}

/* 加载状态 */
.loading-indicator {
  display: flex;
  align-items: center;
  gap: 6px;
}

.loading-dot {
  width: 8px;
  height: 8px;
  background-color: #999;
  border-radius: 50%;
  animation: loadingPulse 1.4s infinite ease-in-out both;
}

.loading-dot:nth-child(1) {
  animation-delay: -0.32s;
}

.loading-dot:nth-child(2) {
  animation-delay: -0.16s;
}

@keyframes loadingPulse {
  0%, 80%, 100% {
    transform: scale(0);
    opacity: 0.7;
  }
  40% {
    transform: scale(1);
    opacity: 1;
  }
}

/* 输入区域 */
.chat-input-container {
  display: flex;
  gap: 10px;
  padding: 15px;
  border-top: 1px solid #f0f0f0;
}

.chat-input {
  flex: 1;
  padding: 10px 12px;
  border: 1px solid #ddd;
  border-radius: 20px;
  resize: none;
  font-size: 14px;
  outline: none;
  transition: border-color 0.3s ease;
}

.chat-input:focus {
  border-color: #d93025;
}

.send-button {
  background-color: #d93025;
  color: white;
  border: none;
  padding: 0 18px;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  font-weight: 500;
  transition: background-color 0.3s ease;
  align-self: flex-end;
}

.send-button:hover:not(:disabled) {
  background-color: #b32a1e;
}

.send-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

/* 响应式设计 */
@media (max-width: 480px) {
  .chat-assistant-container {
    bottom: 10px;
    left: 10px;
    right: 10px;
  }
  
  .chat-window {
    width: 100%;
    height: 350px;
  }
  
  .message {
    max-width: 90%;
  }
}
</style>