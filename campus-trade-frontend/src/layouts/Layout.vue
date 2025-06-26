<template>
  <el-container class="layout-container">
    <el-header height="60px">
      <div class="header-content">

        <router-link to="/home" class="logo-link">
          <div class="logo">校园二手交易系统</div>
        </router-link>

        <div class="user-info">
          <el-button type="primary" plain size="small" @click="goToPublish">发布商品</el-button>
          <el-button type="success" plain size="small" @click="goToProfile">个人中心</el-button>
          <el-button type="warning" plain size="small" @click="goToMessages">消息中心</el-button>
          <span class="welcome-text">欢迎, {{ username }}</span>
          <el-button type="danger" plain size="small" @click="logout">退出登录</el-button>
        </div>
      </div>
    </el-header>
    <el-main>
      <div class="main-content-wrapper">
        <router-view></router-view>
      </div>
    </el-main>
  </el-container>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router'; // 确保引入了 useRouter

const router = useRouter();
const username = ref('');

onMounted(() => {
  const user = JSON.parse(localStorage.getItem('user'));
  if (user && user.username) {
    username.value = user.username;
  }
});

const goToPublish = () => {
  router.push('/home/publish');
};
const goToProfile = () => {
  router.push('/home/profile');
};
const goToMessages = () => {
  router.push('/home/messages');
};
const logout = () => {
  localStorage.removeItem('user');
  router.push('/login');
};
</script>

<style scoped>
.layout-container {
  min-height: 100vh;
  background-color: #f0f2f5;
}

.el-header {
  position: sticky; /* 让头部在滚动时固定在顶部 */
  top: 0;
  z-index: 100;
  background-color: #303133;
  color: #fff;
  display: flex;
  align-items: center;
}

.header-content {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

/* 核心修改：为 router-link 设置样式 */
.logo-link {
  text-decoration: none; /* 去掉链接的下划线 */
  color: inherit; /* 继承父元素的颜色 (白色) */
}

.logo {
  font-size: 20px;
  font-weight: bold;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-info > .el-button {
  margin-right: 10px;
}

.welcome-text {
  margin: 0 15px;
}

.main-content-wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}
</style>
