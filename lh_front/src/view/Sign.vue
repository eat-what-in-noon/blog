<template>
<div class="background">
    <video autoplay muted loop id="background-video">
      <source src="@/assets/background.mp4" type="video/mp4">
      Your browser does not support the video tag.
    </video>
    <transition name="el-zoom-in-center">
        <Login v-show="currentComponent === 'Login'" @show-register="showRegister" @show-password="showPassword" />
    </transition>
    <transition name="el-zoom-in-center">
        <Register v-show="currentComponent === 'Register'" @show-login="showLogin" />
    </transition>
    <transition name="el-zoom-in-center">
        <forgetPassword v-show="currentComponent === 'forgetPassword'" @show-login="showLogin" />
    </transition>
</div>
</template>

<script>
import Login from '../components/Login.vue';
import Register from '../components/Register.vue';
import forgetPassword from '../components/forgetPassword';
export default{
    name:'Sign',
    components:{
        Login,
        Register,
        forgetPassword,
    },
    data(){
        return{
            currentComponent:'Login',
        }
    },
    methods:{
        showRegister(){
            this.currentComponent = 'Register';
        },
        showLogin(){
            this.currentComponent = 'Login';
        },
        showPassword(){
            this.currentComponent = 'forgetPassword';
        },
    }
}
</script>

<style scoped>
.background{
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
}

#background-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover; /* 或者使用 contain，根据需要选择 */
}

.login{
    position: absolute;
    left:70%;
}


</style>