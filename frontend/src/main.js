// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import App from './App';
import router from './router';
import axios from 'axios';
import qs from 'qs';

Vue.use(ElementUI);
Vue.config.productionTip = false;
Vue.prototype.axios = axios;
// 设置axios默认参数 
axios.defaults.baseURL = 'http://114.116.77.118:8888';
//axios.defaults.baseURL = 'http://localhost:8888';
// 开启cookie
axios.defaults.withCredentials=true;
// 默认发送表单数据
axios.defaults.transformRequest = [function(data) {
  return qs.stringify(data)
}];

// 访问限制
router.beforeEach((to, from, next) => {
  var logged = JSON.parse(sessionStorage.getItem('logged'));
  if (to.path === '/login' || to.path === '/register') {
    next();
  }
  if (!logged) {
    next({ path: '/login' });
  } else {
    next();
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  render: h => h(App),
  components: { App },
  template: '<App/>'
})
