<template>
  <div v-if="product" class="detail-container">
    <el-page-header @back="goBack" :content="`商品详情: ${product.name}`" />

    <el-row :gutter="20" class="content-row">
      <el-col :span="10">
        <img :src="product.imageUrl || 'https://images.unsplash.com/photo-1517842645767-c6f90405774b?q=80&w=2098&auto=format&fit=crop'" class="product-image-large"/>
      </el-col>

      <el-col :span="14">
        <h1>{{ product.name }}</h1>
        <p class="description">{{ product.description }}</p>
        <el-divider />
        <p><strong>分类:</strong> {{ product.category }}</p>
        <p><strong>状态:</strong>
          <el-tag :type="product.status === '在售' ? 'success' : 'info'">{{ product.status }}</el-tag>
        </p>
        <p class="price-large">价格: ¥ {{ product.price }}</p>
        <el-divider />
        <el-button type="danger" plain @click="purchase">立即购买</el-button>
        <el-button type="primary" @click="startChat">留言咨询</el-button>
      </el-col>
    </el-row>
  </div>

  <div v-else>
    <p>商品加载中或不存在...</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { getProductByIdService } from '@/api/product.js';
import { purchaseService } from '@/api/order.js';
import { sendMessageService } from '@/api/message.js'; // 注意：这里只需要引入发送消息的API
import { ElMessage, ElMessageBox } from 'element-plus';

const route = useRoute();
const router = useRouter();
const product = ref(null);
const currentUser = ref(null);

onMounted(async () => {
  const userStr = localStorage.getItem('user');
  if (userStr) {
    currentUser.value = JSON.parse(userStr);
  }

  const productId = route.params.id;
  try {
    const response = await getProductByIdService(productId);
    product.value = response.data;
  } catch (error) {
    console.error("获取商品详情失败", error);
    ElMessage.error("获取商品详情失败");
  }
});

const goBack = () => {
  router.back();
};

const purchase = async () => {
  if (!product.value || !currentUser.value) {
    ElMessage.error('信息错误或未登录！');
    return;
  }
  if (product.value.status !== '在售') {
    ElMessage.warning('商品已售出或下架。');
    return;
  }
  if (currentUser.value.id === product.value.sellerId) {
    ElMessage.warning('不能购买自己发布的商品！');
    return;
  }
  ElMessageBox.confirm(
    `确定要以 ¥ ${product.value.price} 的价格购买 “${product.value.name}” 吗？`,
    '确认购买',
    { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }
  ).then(async () => {
    try {
      const payload = { productId: product.value.id, buyerId: currentUser.value.id };
      await purchaseService(payload);
      ElMessage.success('购买成功！');
      product.value.status = '已售';
    } catch (error) {
      const errorMessage = error.response?.data?.message || error.response?.data || '购买失败';
      ElMessage.error(errorMessage);
    }
  }).catch(() => {
    ElMessage.info('已取消购买');
  });
};

/**
 * 【新】发起聊天并跳转到消息中心的函数
 */
const startChat = async () => {
  if (!currentUser.value || !product.value) {
    ElMessage.error('请先登录或商品信息有误！');
    return;
  }
  // 在 startChat 函数的开头
  if (currentUser.value.id === product.value.sellerId) {
    ElMessage.info('这是您自己发布的商品，无需留言。');
    return;
  }

  // 直接跳转到消息中心。
  // 我们可以在消息中心页面去判断是否需要创建新对话。
  // 为了简化，这里也可以先发送一条默认消息来“创建”对话。
  try {
    const messageData = {
      productId: product.value.id,
      senderId: currentUser.value.id,
      receiverId: product.value.sellerId,
      content: `你好，我对你的商品 “${product.value.name}” 很感兴趣。`
    };
    await sendMessageService(messageData);
    ElMessage.success('已发起对话，正在跳转到消息中心...');
    router.push('/home/messages');
  } catch (error) {
    ElMessage.error('发起对话失败，请稍后重试。');
  }
};
</script>

<style scoped>
/* 样式代码保持不变，这里省略以保持简洁，请保留你原来的样式代码 */
.detail-container { background-color: #fff; padding: 20px; border-radius: 5px; }
.content-row { margin-top: 20px; }
.product-image-large { display: block; width: 100%; height: 400px; object-fit: contain; background-color: #f5f7fa; border-radius: 5px; border: 1px solid #ebeef5; }
h1 { margin-top: 0; color: #303133; font-weight: 600; }
p { color: #606266; line-height: 1.6; }
p > strong { color: #303133; }
.description { margin: 20px 0; }
.price-large { font-size: 24px; font-weight: bold; color: #f56c6c; margin-bottom: 20px; }
:deep(.el-page-header__title),
:deep(.el-page-header__content) { color: #303133 !important; }
:deep(.el-divider--horizontal) { margin: 20px 0; }
</style>
