// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import 'element-ui/lib/theme-chalk/base.css';

Vue.prototype.$axios = axios
Vue.config.productionTip = false
Vue.use(ElementUI)


new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  async created() {
    // 初始化 Vuex store
    //this.$store.dispatch('initializeStore');
    const token = localStorage.getItem('token');
    if (!token) {
      this.$router.push('/Sign');
    }
  }
})