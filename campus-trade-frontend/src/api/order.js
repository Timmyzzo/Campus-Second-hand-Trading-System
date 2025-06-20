import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
});

/**
 * 购买商品接口
 * @param {object} payload - 包含 productId 和 buyerId 的对象
 */
export const purchaseService = (payload) => {
  return request.post('/orders/purchase', payload);
}

/**
 * 获取我的订单列表
 * @param {number} buyerId - 当前登录用户的ID
 */
export const getMyOrdersService = (buyerId, params) => {
  return request.get('/orders/my', { params: { buyerId, ...params } });
}
