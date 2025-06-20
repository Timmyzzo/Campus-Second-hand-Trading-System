<template>
  <div class="register-container">
    <el-form :model="form" label-width="80px" class="register-form">
      <h2>用户注册</h2>
      <el-form-item label="用户名">
        <el-input v-model="form.username"></el-input>
      </el-form-item>
      <el-form-item label="密码">
        <el-input type="password" v-model="form.password"></el-input>
      </el-form-item>
      <el-form-item label="学号">
        <el-input v-model="form.studentId"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-radio-group v-model="form.gender">
          <el-radio label="男"></el-radio>
          <el-radio label="女"></el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onRegister">注册</el-button>
        <el-button @click="goToLogin">返回登录</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { registerService } from '@/api/user.js';
import { ElMessage } from 'element-plus';

const router = useRouter();

const form = ref({
  username: '',
  password: '',
  studentId: '',
  gender: '男'
});

const onRegister = async () => {
  try {
    await registerService(form.value);
    ElMessage.success('注册成功！将跳转到登录页。');
    router.push('/login');
  } catch (error) {
    ElMessage.error(error.response?.data || '注册失败');
  }
};

const goToLogin = () => {
  router.push('/login');
};
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5; /* 更柔和的背景色 */
}
.register-form {
  width: 400px;
  padding: 30px;
  border: 1px solid #e4e7ed;
  border-radius: 5px;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); /* 新增：添加阴影效果，让表单浮起来 */
}
h2 {
  text-align: center;
  margin-bottom: 25px;
  color: #303133; /* 新增：设置清晰的字体颜色 */
  font-weight: 600; /* 新增：字体加粗 */
}
</style>
