import Vue from 'vue'
import Router from 'vue-router'
import MainFrame from '@/view/MainFrame'
import Sign from '@/view/Sign'
import textEditor from '@/components/TextEditor'
import showBlog from '@/components/showBlog'

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
    },
    {
      path:'/textEditor',
      name:'textEditor',
      component:textEditor,
    },
    {
      path:'/showBlog',
      name:'showBlog',
      component:showBlog,
    }
  ]
})
