// src/api/admin.js
import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
});

// 获取汇总统计数据
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
