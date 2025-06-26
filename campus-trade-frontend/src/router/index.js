import { createRouter, createWebHistory } from 'vue-router'

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
  history: createWebHistory(import.meta.env.BASE_URL),// 1. 使用HTML5 History模式
  routes: [// 2. 路由规则数组
    { path: '/', redirect: '/login' },// 3. 根路径重定向
    { path: '/login', name: 'login', component: LoginView },// 4. 登录页规则
    { path: '/register', name: 'register', component: RegisterView },
    {
      path: '/home',// 5. 嵌套路由
      component: Layout,// 6. 父路由组件
      redirect: '/home/products',
      children: [// 7. 子路由规则
        { path: 'products', name: 'home', component: HomeView },
        { path: 'detail/:id', name: 'detail', component: DetailView },// 8. 带参数的路由
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

// 导航守卫
router.beforeEach((to, from, next) => {
  const userStr = localStorage.getItem('user');// 10. 从localStorage获取用户信息
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
