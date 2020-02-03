// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import VueResource from 'vue-resource'
import VueWechatTitle from 'vue-wechat-title'
import BaiduMap from 'vue-baidu-map'

Vue.use(VueWechatTitle);
Vue.use(VueResource);
Vue.prototype.axios = axios;    //全局注册，使用方法为:this.$axios
Vue.use(ElementUI);
Vue.config.productionTip = false;

Vue.use(BaiduMap, {
  ak: 'rOsG2lx0to18nZrBCaxAjWdzBYASHGaL'
});


// http request 请求拦截器
axios.interceptors.request.use(request => {
  // 在发送请求之前做些什么
  let url = request.url;
  if (localStorage.getItem('token')) {
    if (!url.match('/auth(.*?)')) {
      request.headers.authorization = 'Bearer ' + localStorage.getItem('token');
    }
  }
  return request;
}, error => {
  // 对请求错误做些什么
  return Promise.reject(error);
});

// http response 响应拦截器
axios.interceptors.response.use(response => {
  return response;
}, error => {
  if (error.response) {
    switch (error.response.status) {
      // 返回403，清除token信息并跳转到登录页面
      case 403:
        router.replace({
          path: '/auth/login'
          //登录成功后跳入浏览的当前页面
          // query: {redirect: router.currentRoute.fullPath}
        })
    }
    // 返回接口返回的错误信息
    return Promise.reject(error.response.data);
  }
});
//路由判断是否登录
router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requireAuth)) {  // 判断该路由是否需要登录权限
    if (localStorage.getItem('token') === null || localStorage.getItem('token') === '') {  // 判断当前的token是否存在 ； 登录存入的token
      next('/auth/login');
    } else {
      console.log('发送登录验证请求');
      axios.post('http://localhost:8080/isLogin', {})
        .then((response) => {
          console.log(response);
          if (response.status === 200) {
            next();
          } else {
            next('/auth/login');
          }

        }).catch((error) => {
        console.log(error);
        next('/auth/login');
      });
    }
  } else {
    next();
  }
});


/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: {App},
  template: '<App/>'
});
