<template>
  <div>
    <!-- 【核心】这里是完整的查询表单HTML代码 -->
    <el-card class="query-card">
      <h3>{{ activeTab === 'published' ? '我发布的商品查询' : '我买到的订单查询' }}</h3>
      <el-form :model="queryParams" inline>
        <el-form-item label="关键词">
          <el-input v-model="queryParams.keyword" placeholder="商品名称" clearable @clear="handleQuery" style="width: 150px;" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="queryParams.category" placeholder="所有分类" clearable @change="handleQuery" style="width: 130px;">
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
        <el-form-item label="价格区间">
          <el-input-number v-model="queryParams.minPrice" :min="0" controls-position="right" placeholder="最低价" style="width: 110px;"></el-input-number>
          <span style="margin: 0 5px;">-</span>
          <el-input-number v-model="queryParams.maxPrice" :min="0" controls-position="right" placeholder="最高价" style="width: 110px;"></el-input-number>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="queryParams.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-tabs v-model="activeTab" @tab-click="handleTabClick" class="profile-tabs">
      <el-tab-pane label="我发布的" name="published">
        <el-table :data="myProducts" stripe style="width: 100%">
          <el-table-column prop="name" label="商品名称" width="250"></el-table-column>
          <el-table-column prop="price" label="价格" width="100"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === '在售' ? 'success' : 'info'">{{ row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="发布时间">
            <template #default="{ row }">
              <span>{{ formatDateTime(row.createdAt) }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="我买到的" name="purchased">
        <el-table :data="myOrders" stripe style="width: 100%">
          <el-table-column prop="product_name" label="商品名称" width="250"></el-table-column>
          <el-table-column prop="product_category" label="分类" width="150"></el-table-column>
          <el-table-column prop="order_price" label="成交价" width="100"></el-table-column>
          <el-table-column label="购买时间">
            <template #default="{ row }">
              <span>{{ formatDateTime(row.created_at) }}</span>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getMyProductsService } from '@/api/product.js';
import { getMyOrdersService } from '@/api/order.js';
import { ElMessage } from 'element-plus';

const activeTab = ref('published');
const myProducts = ref([]);
const myOrders = ref([]);
const currentUser = ref(null);

const defaultQueryParams = {
  keyword: '',
  category: '',
  minPrice: undefined,
  maxPrice: undefined,
  dateRange: []
};
const queryParams = ref({ ...defaultQueryParams });

const buildParams = () => {
  const params = {};
  if (queryParams.value.keyword) params.keyword = queryParams.value.keyword;
  if (queryParams.value.category) params.category = queryParams.value.category;
  if (queryParams.value.minPrice) params.minPrice = queryParams.value.minPrice;
  if (queryParams.value.maxPrice) params.maxPrice = queryParams.value.maxPrice;
  if (queryParams.value.dateRange && queryParams.value.dateRange.length) {
    params.startTime = queryParams.value.dateRange[0] + ' 00:00:00'; // 加上时分秒，查询更精确
    params.endTime = queryParams.value.dateRange[1] + ' 23:59:59';
  }
  return params;
};

const handleQuery = () => {
  if (activeTab.value === 'published') {
    fetchMyProducts();
  } else {
    fetchMyOrders();
  }
};

const resetQuery = () => {
  queryParams.value = { ...defaultQueryParams };
  handleQuery();
};

const fetchMyProducts = async () => {
  if (currentUser.value && currentUser.value.id) {
    try {
      const params = buildParams();
      const response = await getMyProductsService(currentUser.value.id, params);
      myProducts.value = response.data;
    } catch (error) {
      console.error("获取已发布商品失败:", error);
      ElMessage.error("获取已发布商品失败，请稍后重试。");
    }
  }
};

const fetchMyOrders = async () => {
  if (currentUser.value && currentUser.value.id) {
    try {
      const params = buildParams();
      const response = await getMyOrdersService(currentUser.value.id, params);
      myOrders.value = response.data;
    } catch (error) {
      console.error("获取已购买订单失败:", error);
      ElMessage.error("获取已购买订单失败，请稍后重试。");
    }
  }
};

onMounted(() => {
  const userStr = localStorage.getItem('user');
  if (userStr) {
    currentUser.value = JSON.parse(userStr);
    handleQuery();
  }
});

const handleTabClick = () => {
  resetQuery(); // 切换tab时，重置查询条件并重新加载数据
};

// ProfileView.vue
const formatDateTime = (dateTimeString) => {
  if (!dateTimeString) return 'N/A';
  // 此时的 dateTimeString 是 "2024-06-21 15:30:00"
  const date = new Date(dateTimeString); // new Date("2024-06-21 15:30:00") 可以被正确解析
  return date.toLocaleString('zh-CN', { hour12: false }).replace(/\//g, '-');
};
</script>

<style scoped>
.query-card {
  margin-bottom: 20px;
  background-color: #fff;
  padding: 10px 20px 0 20px;
  border-radius: 5px;
}

.profile-tabs {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
}

:deep(.el-table) {
  color: #606266;
}

:deep(.el-table th) {
  color: #303133;
}
</style>
