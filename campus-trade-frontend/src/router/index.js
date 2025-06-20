import { createRouter, createWebHistory } from 'vue-router'
// 不再在这里导入 ElMessage

// 导入所有视图组件
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import Layout from '../layouts/Layout.vue'
import HomeView from '../views/HomeView.vue'
import DetailView from '../views/DetailView.vue'
import PublishView from '../views/PublishView.vue'
import ProfileView from '../views/ProfileView.vue'
import MessageCenterView from '../views/MessageCenterView.vue'
import AdminView from '../views/AdminView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', redirect: '/login' },
    { path: '/login', name: 'login', component: LoginView },
    { path: '/register', name: 'register', component: RegisterView },
    {
      path: '/home',
      component: Layout,
      redirect: '/home/products',
      children: [
        { path: 'products', name: 'home', component: HomeView },
        { path: 'detail/:id', name: 'detail', component: DetailView },
        { path: 'publish', name: 'publish', component: PublishView },
        { path: 'profile', name: 'profile', component: ProfileView },
        { path: 'messages', name: 'messages', component: MessageCenterView }
      ]
    },
    {
      path: '/admin',
      name: 'admin',
      component: AdminView
    }
  ]
})

// 最终版导航守卫 (已移除 ElMessage)
router.beforeEach((to, from, next) => {
  const userStr = localStorage.getItem('user');
  const user = userStr ? JSON.parse(userStr) : null;

  // 1. 访问管理员页面
  if (to.name === 'admin') {
    if (user && user.username === 'admin') {
      next(); // 是管理员，放行
    } else {
      // ElMessage.error('您没有权限访问此页面！'); // 已移除
      next({ name: 'login' }); // 不是管理员，直接跳转
    }
    return;
  }

  // 2. 访问需要登录的页面 (非登录/注册/管理员页)
  if (to.name !== 'login' && to.name !== 'register') {
    if (user) {
      next(); // 已登录，放行
    } else {
      // ElMessage.error('请先登录！'); // 已移除
      next({ name: 'login' }); // 未登录，直接跳转
    }
    return;
  }

  // 3. 访问公共页面 (登录/注册页)，直接放行
  next();
});


export default router
