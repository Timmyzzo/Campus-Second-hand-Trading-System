<template>
  <div class="admin-container">
    <h1>后台管理面板</h1>
    <el-tabs v-model="activeTab" @tab-click="handleTabClick" class="admin-tabs">

      <!-- ==================== 汇总统计 ==================== -->
      <el-tab-pane label="汇总统计" name="summary">
        <el-card class="query-card">
          <h3>汇总查询</h3>
          <el-form :model="summaryParams" inline>
            <el-form-item label="关键词">
              <el-input v-model="summaryParams.keyword" placeholder="商品名称" clearable style="width: 150px;" />
            </el-form-item>
            <el-form-item label="分类">
              <el-select v-model="summaryParams.category" placeholder="所有分类" clearable style="width: 130px;">
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
              <el-input-number v-model="summaryParams.minPrice" :min="0" :precision="2" controls-position="right" placeholder="最低价" style="width: 110px;"></el-input-number>
              <span style="margin: 0 5px;">-</span>
              <el-input-number v-model="summaryParams.maxPrice" :min="0" :precision="2" controls-position="right" placeholder="最高价" style="width: 110px;"></el-input-number>
            </el-form-item>
            <el-form-item label="时间范围">
              <el-date-picker
                v-model="summaryParams.dateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="YYYY-MM-DD"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="fetchSummary">查询</el-button>
              <el-button @click="resetSummaryQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-divider><h3>挂牌信息汇总</h3></el-divider>
        <el-table :data="summary.productsSummary" border stripe>
          <el-table-column prop="category" label="物品种类"></el-table-column>
          <el-table-column prop="total_count" label="挂牌总数"></el-table-column>
          <el-table-column prop="on_sale_count" label="在售数量"></el-table-column>
          <el-table-column prop="sold_count" label="已售数量"></el-table-column>
        </el-table>

        <el-divider><h3>成交情况汇总</h3></el-divider>
        <el-table :data="summary.salesSummary" border stripe>
          <el-table-column prop="category" label="物品种类"></el-table-column>
          <el-table-column prop="transaction_count" label="成交笔数"></el-table-column>
          <el-table-column prop="total_turnover" label="成交总额"></el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- ==================== 商品管理 ==================== -->
      <el-tab-pane label="商品管理" name="products">
        <el-table :data="allProducts" stripe border>
          <el-table-column prop="id" label="ID" width="80"></el-table-column>
          <el-table-column prop="name" label="商品名称" show-overflow-tooltip></el-table-column>
          <el-table-column prop="sellerId" label="卖家ID" width="100"></el-table-column>
          <el-table-column prop="price" label="价格" width="100"></el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="row.status === '在售' ? 'success' : 'info'">{{ row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120" fixed="right">
            <template #default="{ row }">
              <el-popconfirm title="确定要删除这个商品吗？这将一并删除相关订单和留言，且不可恢复！" @confirm="deleteProduct(row.id)">
                <template #reference>
                  <el-button type="danger" size="small">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- ==================== 用户管理 ==================== -->
      <el-tab-pane label="用户管理" name="users">
        <el-card class="query-card">
          <h3>用户查询</h3>
          <el-form :model="userParams" inline>
            <el-form-item label="用户名">
              <el-input v-model="userParams.username" placeholder="用户名" clearable></el-input>
            </el-form-item>
            <el-form-item label="学号">
              <el-input v-model="userParams.studentId" placeholder="学号" clearable></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="fetchUsers">查询</el-button>
              <el-button @click="resetUserQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-table :data="userList" stripe border>
          <el-table-column prop="id" label="ID" width="80"></el-table-column>
          <el-table-column prop="username" label="用户名"></el-table-column>
          <el-table-column prop="gender" label="性别" width="80"></el-table-column>
          <el-table-column prop="studentId" label="学号"></el-table-column>
        </el-table>
      </el-tab-pane>

    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getSummaryService, deleteProductService, getAllUsersService } from '@/api/admin.js';
import { getProductsService } from '@/api/product.js';
import { ElMessage } from 'element-plus';

const activeTab = ref('summary');

// --- 汇总统计 ---
const defaultSummaryParams = { keyword: '', category: '', minPrice: undefined, maxPrice: undefined, dateRange: [] };
const summaryParams = ref({ ...defaultSummaryParams });
const summary = ref({ productsSummary: [], salesSummary: [] });

/**
 * 【最终版】构建汇总查询的参数，确保所有有效值都被包含
 */
const buildSummaryParams = () => {
  const params = {};
  const { keyword, category, minPrice, maxPrice, dateRange } = summaryParams.value;

  if (keyword) params.keyword = keyword;
  if (category) params.category = category;
  // 只有当 minPrice 是一个数字（包括0）时才添加
  if (typeof minPrice === 'number') params.minPrice = minPrice;
  if (typeof maxPrice === 'number') params.maxPrice = maxPrice;
  if (dateRange && dateRange.length === 2) {
    params.startTime = dateRange[0] + ' 00:00:00';
    params.endTime = dateRange[1] + ' 23:59:59';
  }
  return params;
};

const fetchSummary = async () => {
  try {
    const params = buildSummaryParams();
    console.log("发送给后端的汇总查询参数:", params); // 保留日志用于调试
    const response = await getSummaryService(params);
    summary.value = response.data;
  } catch (error) {
    console.error("获取汇总数据失败:", error);
    ElMessage.error('获取汇总数据失败');
  }
};

const resetSummaryQuery = () => {
  summaryParams.value = { ...defaultSummaryParams };
  fetchSummary();
};

// --- 商品管理 ---
const allProducts = ref([]);
const fetchAllProducts = async () => {
  try {
    const response = await getProductsService();
    allProducts.value = response.data;
  } catch (error) {
    ElMessage.error('获取商品列表失败');
  }
};
const deleteProduct = async (id) => {
  try {
    await deleteProductService(id);
    ElMessage.success('删除成功！');
    fetchAllProducts();
  } catch (error) {
    ElMessage.error('删除失败');
  }
};

// --- 用户管理 ---
const defaultUserParams = { username: '', studentId: '' };
const userParams = ref({ ...defaultUserParams });
const userList = ref([]);
const fetchUsers = async () => {
  try {
    const params = {};
    if (userParams.value.username) params.username = userParams.value.username;
    if (userParams.value.studentId) params.studentId = userParams.value.studentId;
    const response = await getAllUsersService(params);
    userList.value = response.data;
  } catch (error) {
    ElMessage.error('获取用户列表失败');
  }
};
const resetUserQuery = () => {
  userParams.value = { ...defaultUserParams };
  fetchUsers();
};

// --- Tab 切换 ---
const handleTabClick = (tab) => {
  if (tab.props.name === 'summary') fetchSummary();
  else if (tab.props.name === 'products') fetchAllProducts();
  else if (tab.props.name === 'users') fetchUsers();
};

onMounted(() => {
  fetchSummary();
});
</script>

<style scoped>
.admin-container {
  padding: 20px;
}

h1, h3 {
  color: #303133;
  font-weight: 600;
}

h1 {
  text-align: center;
  margin-bottom: 20px;
}

.admin-tabs {
  background-color: #fff;
  padding: 20px;
  border-radius: 5px;
}

.query-card {
  margin-bottom: 20px;
  background-color: #fcfcfc;
  border: 1px solid #e4e7ed;
}

.el-divider {
  margin: 30px 0;
}

:deep(.el-table) {
  color: #606266;
}

:deep(.el-table th) {
  color: #303133;
}
</style>
