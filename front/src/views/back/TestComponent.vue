<template>
  <div class="test-component">
    <h1>测试组件</h1>
    <p>这是一个简单的测试组件，用于测试路由是否正常工作。</p>
  </div>
</template>

<script setup>
console.log('测试组件加载完成')
</script>

<style scoped>
.test-component {
  padding: 20px;
  background-color: white;
  color: black;
}
</style>