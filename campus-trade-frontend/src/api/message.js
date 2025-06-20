import axios from 'axios';

const request = axios.create({
  baseURL: '/api',
  timeout: 5000
});

/**
 * 获取某个商品下，两个用户之间的对话列表
 * @param {number} productId 商品ID
 * @param {number} userId1 用户1的ID
 * @param {number} userId2 用户2的ID
 */
export const getMessagesService = (productId, userId1, userId2) => {
  return request.get('/messages', { params: { productId, userId1, userId2 } });
}

/**
 * 发送一条新留言
 * @param {object} messageData 包含 productId, senderId, receiverId, content 的对象
 */
export const sendMessageService = (messageData) => {
  return request.post('/messages', messageData);
}

/**
 * 【新增】获取我的所有对话列表
 * @param {number} userId 当前登录用户的ID
 */
export const getMyConversationsService = (userId) => {
  return request.get('/messages/my-conversations', { params: { userId } });
}
