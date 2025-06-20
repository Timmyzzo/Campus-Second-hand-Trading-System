// src/api/product.js
import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
});

// 获取所有在售商品
export const getProductsService = () => {
  return request.get('/products');
}

// 在 product.js 中新增
// 发布新商品
export const publishProductService = (productData) => {
  return request.post('/products/publish', productData);
}

// 在 product.js 中新增
// 根据ID获取商品详情
export const getProductByIdService = (id) => {
  // 这里的URL `/products/${id}` 会匹配到我们后端写的 @GetMapping("/{id}")
  return request.get(`/products/${id}`);
}

// 获取我发布的商品（支持复杂查询）
export const getMyProductsService = (sellerId, params) => {
  return request.get('/products/my', { params: { sellerId, ...params } });
}
