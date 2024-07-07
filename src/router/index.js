import Vue from 'vue'
import Router from 'vue-router'
import MainFrame from '@/view/MainFrame'
import Sign from '@/view/Sign'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path:'/',
      redirect: '/MainFrame'
    },
    {
      path: '/MainFrame',
      name: 'MainFrame',
      component: MainFrame,
    },
    {
      path:'/Sign',
      name:'Sign',
      component:Sign,
    }
  ]
})
