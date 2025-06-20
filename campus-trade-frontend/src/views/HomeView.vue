<template>
  <!--
    我们使用 el-row，但去掉 gutter 属性
    我们给它一个 flex 布局和 gap 属性
  -->
  <el-row class="product-row">
    <!--
      我们只设置 el-col 的宽度，间距由父容器的 gap 控制
    -->
    <el-col :span="6" v-for="product in productList" :key="product.id" class="product-col">
      <el-card class="product-card" @click="goToDetail(product.id)">
        <div class="image-container">
          <img :src="product.imageUrl || 'https://images.unsplash.com/photo-1517842645767-c6f90405774b?q=80&w=2098&auto=format&fit=crop'" class="product-image"/>
        </div>
        <div class="info-container">
          <p class="product-name">{{ product.name }}</p>
          <div class="bottom-area">
            <span class="price">¥ {{ product.price }}</span>
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>

<script setup>
// script 部分保持不变
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getProductsService } from '@/api/product.js'

const productList = ref([])
const router = useRouter()

onMounted(async () => {

  try {
    const response = await getProductsService()
    productList.value = response.data
  } catch (error) {
    console.error('获取商品列表失败', error)
  }
})

const goToDetail = (id) => {
  router.push(`/home/detail/${id}`)
}
</script>

<style scoped>
/*
 * 核心修改：重写 el-row 的样式
 */
.product-row {
  display: flex;
  flex-wrap: wrap;
  /* 使用 gap 属性来创建间距，它在高DPI下计算更精确 */
  gap: 20px;
}

.product-col {
  /*
   * 核心修改：使用 CSS 的 calc() 函数来精确计算宽度
   * (100% - (列数-1) * 间距) / 列数
   * (100% - 3 * 20px) / 4
   */
  width: calc((100% - 60px) / 4);
  /*
   * 我们不再需要 Element Plus 的响应式断点，
   * 因为 Flexbox + gap + calc() 已经能完美处理换行和间距
   */
  max-width: none; /* 覆盖 el-col 可能的 max-width */
  flex-basis: auto; /* 覆盖 el-col 可能的 flex-basis */
  flex-grow: 0;
}


/* --- 以下卡片样式保持不变 --- */
.product-card {
  cursor: pointer;
  transition: all 0.2s ease-in-out;
  width: 100%;
}

.product-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.image-container {
  width: 100%;
  padding-top: 75%;
  position: relative;
  overflow: hidden;
  background-color: #eee;
}

.product-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.info-container {
  padding: 14px;
}

.product-name {
  font-size: 16px;
  font-weight: 500;
  color: #303133;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
  min-height: 44px;
}

.bottom-area {
  margin-top: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 18px;
  font-weight: bold;
  color: #f56c6c;
}
</style>
