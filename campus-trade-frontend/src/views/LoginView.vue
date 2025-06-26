<template>
  <div class="login-container">
    <el-form :model="form" label-width="80px" class="login-form">
      <h2>用户登录</h2>
      <el-form-item label="用户名">
        <el-input v-model="form.username"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="form.password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onLogin">登录</el-button>
        <el-button @click="goToRegister">去注册</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { loginService } from '@/api/user.js';
import { ElMessage } from 'element-plus';

const router = useRouter();

const form = ref({
  username: '',
  password: ''
});

// 在 onLogin 函数内部
const onLogin = async () => {
  try {
    const response = await loginService(form.value);
    ElMessage.success('登录成功！');

    // 1. 将后端返回的用户信息存储到 localStorage
    // JSON.stringify 是为了把JS对象转成字符串才能存储
    localStorage.setItem('user', JSON.stringify(response.data));


    // 判断是否是管理员
    if (response.data.username === 'admin') {
      router.push('/admin');
    } else {
      router.push('/home');
    }

  } catch (error) {
    ElMessage.error(error.response?.data || '登录失败');
  }
};

const goToRegister = () => {
  router.push('/register');
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}
.login-form {
  width: 400px;
  padding: 30px;
  border: 1px solid #e4e7ed;
  border-radius: 5px;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
h2 {
  text-align: center;
  margin-bottom: 25px;
  color: #303133; /* 新增：设置清晰的字体颜色 */
  font-weight: 600; /* 新增：字体加粗 */
}
</style>
