<template>
  <div class="publish-container">
    <h2>发布我的二手商品</h2>
    <el-form :model="form" label-width="120px" style="max-width: 600px; margin: 0 auto;">
      <el-form-item label="商品名称">
        <el-input v-model="form.name" placeholder="请输入商品名称"></el-input>
      </el-form-item>
      <el-form-item label="商品分类">
        <el-select v-model="form.category" placeholder="请选择分类">
          <el-option label="书籍教材" value="书籍教材"></el-option>
          <el-option label="电子产品" value="电子产品"></el-option>
          <el-option label="生活用品" value="生活用品"></el-option>
          <el-option label="服饰鞋包" value="服饰鞋包"></el-option>
          <el-option label="运动健身" value="运动健身"></el-option>
          <el-option label="乐器相关" value="乐器相关"></el-option>
          <el-option label="美妆护肤" value="美妆护肤"></el-option>
          <el-option label="其他" value="其他"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="商品价格">
        <el-input-number v-model="form.price" :precision="2" :step="1" :min="0.01"></el-input-number>
      </el-form-item>
      <el-form-item label="商品描述">
        <el-input v-model="form.description" type="textarea" :rows="4" placeholder="详细描述一下你的宝贝吧..."></el-input>
      </el-form-item>
      <el-form-item label="图片链接">
        <el-input v-model="form.imageUrl" placeholder="请输入图片URL (仅为演示)"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">确认发布</el-button>
        <el-button @click="goBack">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { publishProductService } from '@/api/product.js'; // 需要在api/product.js中添加
import { ElMessage } from 'element-plus';

const router = useRouter();

const form = ref({
  name: '',
  category: '书籍教材', // 默认值
  price: 0.01,
  description: '',
  imageUrl: '',
  sellerId: null // 稍后从localStorage获取
});

const onSubmit = async () => {
  const user = JSON.parse(localStorage.getItem('user'));
  if (!user) {
    ElMessage.error('请先登录！');
    router.push('/login');
    return;
  }
  form.value.sellerId = user.id;

  try {
    await publishProductService(form.value);
    ElMessage.success('商品发布成功！');
    router.push('/home'); // 发布成功后跳转回主页
  } catch (error) {
    ElMessage.error('发布失败，请检查输入信息。');
  }
};

const goBack = () => {
  router.back();
};
</script>

<style scoped>
.publish-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
}
/* 新增：设置标题颜色 */
h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #303133;
  font-weight: 600;
}
</style>
