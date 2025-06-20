// src/api/user.js
import axios from 'axios';

// 创建axios实例，配置基础路径
const request = axios.create({
  baseURL: '/api', // 所有请求都会带上/api前缀，然后被代理转发
  timeout: 5000
});

// 注册接口
export const registerService = (userData) => {
  return request.post('/users/register', userData);
}

// 登录接口
export const loginService = (credentials) => {
  return request.post('/users/login', credentials);
}
