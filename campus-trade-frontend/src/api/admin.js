// src/api/admin.js (最终完整版)
import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
});

// 获取汇总统计数据 (这个函数本身设计得很好，无需修改)
export const getSummaryService = (params) => {
  return request.get('/admin/summary', { params });
}

// 删除商品
export const deleteProductService = (id) => {
  return request.delete(`/admin/products/${id}`);
}

// 获取所有用户列表
export const getAllUsersService = (params) => {
  return request.get('/admin/users', { params });
}
