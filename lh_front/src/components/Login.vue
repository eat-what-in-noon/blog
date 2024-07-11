<template>
    <div  class="login">
      <h1 style="position: relative;margin-left: -270px; top: 20px;">登录</h1>
      <el-tabs v-model="activeTab" @tab-click="handleTabClick" class="custom-tabs">
        <el-tab-pane label="密码登录" name="password"></el-tab-pane>
        <el-tab-pane label="邮箱登录" name="provider"></el-tab-pane>
      </el-tabs>
      <div class="login-form" v-if="activeTab === 'password'">
        <el-form ref="login1Form" :model="login1Form" :rules="rules1" label-width="80px">
          <el-form-item prop="username" :rules="rules1.username">
              <el-input id="username" placeholder="用户名/邮箱" v-model="login1Form.username" readonly onfocus="this.removeAttribute('readonly');" auto-complete="off" style="width: 300px;"></el-input>
          </el-form-item>
          <el-form-item prop="password" :rules="rules1.password">
              <el-input id="password" placeholder="密码" type="password" readonly onfocus="this.removeAttribute('readonly');" v-model="login1Form.password" auto-complete="off" style="width: 300px;"></el-input>
              <el-button size="mini" @click="$emit('show-password')" style="position: absolute;border:none;background-color: white;outline: none;left:73%;top:7px">忘记密码</el-button>
            </el-form-item>
          <el-form-item>
              <el-button type="primary"  size="mini" style="width: 300px;height: 50px;font-size: 20px;margin-top: 10px;"  @click="onlogin1()">登录</el-button>
          </el-form-item>
        </el-form>
    </div>

    <div class="provider-form" v-if="activeTab === 'provider'">
        <el-form ref="login2Form" :model="login2Form" :rules="rules2" label-width="80px">
          <el-form-item prop="email" :rules="rules2.email">
            <el-input id="email" placeholder="邮箱" v-model="login2Form.email" readonly onfocus="this.removeAttribute('readonly');" auto-complete="off" style="width: 300px;"></el-input>
          </el-form-item>
          <el-form-item prop="check_num" :rules="rules2.check_num">
            <el-input id="checkNum" placeholder="验证码" readonly onfocus="this.removeAttribute('readonly');" v-model="login2Form.check_num" auto-complete="off" style="width: 300px;"></el-input>
            <el-button size="mini" @click="getCheckCode()" style="position: absolute;border:none;background-color: white;outline: none;left:67%;top:7px">{{ buttonText }}</el-button>
          </el-form-item>
          <el-form-item>
              <el-button type="primary"  size="mini" style="width: 300px;height: 50px;font-size: 20px;margin-top: 10px;"  @click="onlogin2()">登录</el-button>
          </el-form-item>
        </el-form>
    </div>

    <div class="footer">
      <span>没有账号？</span>
      <el-button type="text" @click="$emit('show-register')">立即注册</el-button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'Login',
  data() {
          return {
              login1Form: {
                  username: '', //用户登录时使用的账号，可以为username也可以为邮箱
                  password: ''  //密码
              },
              login2Form:{
                email:'',
                check_num:''
              },
              activeTab:'password',//当前显示哪种登录方式
              isButtonDisabled: false,
              buttonText:  '获取验证码',
              countdown: 60,
              //验证规则
                rules1: {
                  username: [
                        { required: true, message: '请输入账号', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ],
                },
                rules2: {
                  email: [
                    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
                    { type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change'] }
                  ],
                  check_num: [
                    { required: true, message: '请输入六位数验证码', trigger: 'blur' },
                    { validator: this.validateCheckNum, trigger: 'blur' }
                  ],
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
    功能：登录逻辑，使用axios向后端以json形式传送一个user对象，包括用户名username与密码password
         若登录成功则跳转到主界面处，并显示登录成功提示，否则显示登陆失败提示
    */
    onlogin1(){
      const user = {
        username:this.login1Form.username,
        password:this.login1Form.password,
      }
      this.axios({
        method:"post",
        url:"/user/login",
        data:user,
        headers: {
          'Content-Type': 'application/json'
        }
      }).then((res) => {
        if (res.data.error_message === 'success') {
          // 登录成功处理
          localStorage.setItem("userInfo", JSON.stringify(res.data.data));
          console.log(localStorage.getItem("userInfo"));
          localStorage.setItem("token",res.data.token)
          this.$router.push('/MainFrame');
          this.$message({
            message: "登录成功",
            type: "success",
          });
        } else {
          // 登录失败处理
          this.$message({
            message: res.data.error_message,
            type: "warning",
          });
        }
      }).catch((error) => {
        // 请求失败处理
        console.error('登录请求失败:', error);
        this.$message.error('登录失败，请稍后重试');
      });
    },

    handleTabClick(tab) {
      if (tab.name === 'provider') {
        // 清除密码登录表单的验证信息
        this.$refs.login1Form.clearValidate();
        // 使用 $nextTick 确保在 DOM 更新完成后再触发验证
        this.$nextTick(() => {
          // 在切换到邮箱登录页时重新触发表单验证
          this.$refs.login2Form.validate();
        });
      } else if (tab.name === 'password') {
        // 清除邮箱登录表单的验证信息
        this.$refs.login2Form.clearValidate();
        // 使用 $nextTick 确保在 DOM 更新完成后再触发验证
        this.$nextTick(() => {
          // 在切换到密码登录页时重新触发表单验证
          this.$refs.login1Form.validate();
        });
      }
      // 更新当前活动标签页
      this.activeTab = tab.name;
    },
    validateCheckNum(rule, value, callback) {
      const checkNumPattern = /^\d{6}$/;
      if (!value) {
        callback(new Error('请输入验证码'));
      } else if (!checkNumPattern.test(value)) {
        callback(new Error('请输入六位验证码'));
      } else {
        callback();
      }
    },
    getCheckCode() {

      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailPattern.test(this.login2Form.email)) {
        this.$message.warning("邮箱格式不正确");
        return false;
      }
      if (this.isButtonDisabled) return;

      // 发送验证码逻辑
      this.isButtonDisabled = true;
      this.buttonText = `${this.countdown}s后可重发`;
      const timer = setInterval(() => {
        this.countdown--;
        this.buttonText = ` ${this.countdown}s后可重发`;
        if (this.countdown <= 0) {
          clearInterval(timer);
          this.isButtonDisabled = false;
          this.buttonText = '获取验证码';
          this.countdown = 60;
        }
      }, 1000);
      console.log(this.login2Form.email);
      // 发送获取验证码的请求
      this.axios({
        method: "get",
        url: "/mail/getCheckCode",
        params: {
          email: this.login2Form.email,
        },
        headers: {
          'Content-Type': 'application/json'
        }
      }).then((res) => {
        if (res.data.error_message === 'success') {
          console.log(res.data);
        } else {
          // 登录失败处理
          this.$message({
            message: res.data.error_message,
            type: "warning",
          });
        }
      }).catch((error) => {
        // 请求失败处理
        console.error('获取验证码出错', error);
        this.$message.error('出错误了');
      });
    },
    onlogin2(){
      this.$refs.login2Form.validate((valid) => {
        if (valid) {
          const user = {
            email:this.login2Form.email,
            checkCode:this.login2Form.check_num,
          }

          this.axios({
            method:"post",
            url:"/user/loginByEmail",
            data:user,
            headers: {
              'Content-Type': 'application/json'
            }
          }).then((res) => {
            if (res.data.error_message === 'success') {
              // 登录成功处理
              localStorage.setItem("userInfo", JSON.stringify(res.data.data));
              console.log(localStorage.getItem("userInfo"));
              localStorage.setItem("token",res.data.token)
              this.$router.push('/MainFrame');
              this.$message({
                message: "登录成功",
                type: "success",
              });
            } else {
              // 登录失败处理
              this.$message({
                message: res.data.error_message,
                type: "warning",
              });
            }
          }).catch((error) => {
            // 请求失败处理
            console.error('登录请求失败:', error);
            this.$message.error('登录失败，请稍后重试');
          });
        } else {
          console.log('验证失败');
          this.$message.warning("输入格式不正确")
        }
      });
    },
  },

}
</script>


<style scoped>

.footer{
  position: absolute;
  top: 380px;
  left: 85px;
}

.login{
  position: absolute;
  width: 460px;
  height: 500px;
  border:solid 3px;
  border-radius: 20px;
  left:60%;
  top:120px;
  background-color: rgba(255, 255, 255, 0.838);
}

.login-form{
  position: absolute;
  margin-top:20px;
}

.provider-form{
  position: absolute;
  margin-top:20px;
}

>>> .el-tabs__nav-wrap::after {
  position: static !important;
}

>>> .el-tabs__item{
  font-size: 20px!important;
  font-weight: 800;
}

.custom-tabs{
  margin-top: 50px;
  margin-left:80px;
}

</style>
