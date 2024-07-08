<template>
    <div class="register">
      <h1>注册</h1>
      <div class="register-form">
        <el-form ref="registerForm" :model="registerForm" :rules="rules" label-width="80px">
            <el-form-item label="用户名" prop="username">
                <el-input v-model="registerForm.username" readonly onfocus="this.removeAttribute('readonly');" style="width: 300px;"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password">
                <el-input v-model="registerForm.password" readonly onfocus="this.removeAttribute('readonly');" style="width: 300px;"></el-input>
            </el-form-item>
            <el-form-item label="手机号" prop="phonenumber">
                <el-input v-model="registerForm.phonenumber" readonly onfocus="this.removeAttribute('readonly');" style="width: 300px;"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <el-input v-model="registerForm.email" readonly onfocus="this.removeAttribute('readonly');"  style="width: 300px;"></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-radio v-model="registerForm.gender" label="man" style="margin-left: -100px;">男</el-radio>
              <el-radio v-model="registerForm.gender" label="woman">女</el-radio>
            </el-form-item>
        </el-form>
      </div>
      <el-button type="primary" size="mini" style="position:absolute;width: 300px;height: 50px;font-size: 20px;left:18%"  @click="onRegister">注册</el-button>
      <div class="footer">
        <span>已有账号？</span>
        <el-button type="text" @click="$emit('show-login')">立即登录</el-button>
      </div>
    </div>
</template>
    
<script>
import axios from 'axios';
export default {
  name: 'Register',
  data() {
        return {
            registerForm: {
                username: '',
                password: '',
                phonenumber: '',
                email: '',
                gender:'',
                baseURL:''
            },
            rules: {
                username: [
                    { required: true, message: '请输入账号', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' }
                ],
                email: [ { pattern: /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/, required: true, message: "请输入正确的邮箱", trigger: "blur" }]
            }
        };
    },
  created(){
      this.axios = axios.create({
          baseURL: 'http://localhost:8090', // 后端 API 的地址
          timeout: 5000 // 请求超时时间
      });
  },

  methods:{

    /*
    作者：lh
    时间：2024.7.5
    功能：注册逻辑，使用axios向后端以json形式传送一个user对象，包括用户名username与密码password
         若注册成功则跳转到登录处
    */
    onRegister(){
      const user={
        username:this.registerForm.username, 
        password:this.registerForm.password,
        email:this.registerForm.email,
        phone_number:this.registerForm.phonenumber,
        gender:this.registerForm.gender
      }
      this.axios({
        method:"post",
        url:"/user/register",
        data:JSON.stringify(user),
        headers: {
          'Content-Type': 'application/json'
        }
      }).then((res) => {
        if (res.data.error_message === 'success') {
          // 注册成功处理
          sessionStorage.setItem("userInfo", JSON.stringify(res.data.data));
          this.$router.push('/Sign');
          this.$emit('show-login')
          this.$message({
            message: "注册成功",
            type: "success",
          });
        } else {
          // 注册失败处理
          this.$message({
            message: res.data.error_message,
            type: "warning",
          });
        }
      }).catch((error) => {
        // 请求失败处理
        console.error('注册请求失败:', error);
        this.$message.error('注册失败，请稍后重试');
      });
    },

  }
}

</script>

<style scoped>
.register{
  position: absolute;
  top:5%;
  left: 60%;
  width: 460px;
  height: 80%;
  border:solid 3px;
  border-radius: 20px;
  background-color: rgba(255, 255, 255, 0.838);
}

.footer{
  position: absolute;
  top:87%;
  left:30%
}

.register-form{
  margin-top: 30px;
}
</style>
    