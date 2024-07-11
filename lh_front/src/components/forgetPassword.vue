<template>
    <div class="forgetpassword">
      <h1 style="position: relative;margin-left: -270px; top: 20px;">忘记密码</h1>
      <div v-if="!emailCheck" class="emailform">
            <el-form ref="emailForm" :model="emailForm" :rules="rules" label-width="80px">
                <el-form-item prop="email"  >
                    <el-input class="email" placeholder="邮箱" v-model="emailForm.email" readonly onfocus="this.removeAttribute('readonly');" auto-complete="off" style="width: 300px;"></el-input>
                </el-form-item>
                <el-form-item prop="check_num">
                    <el-input class="check_num" placeholder="验证码" v-model="emailForm.check_num" readonly onfocus="this.removeAttribute('readonly');" auto-complete="off" style="width: 300px;"></el-input>
                    <el-button size="mini" @click="getCheckCode()" style="position: absolute;border:none;background-color: white;outline: none;left:67%;top:7px">{{ buttonText }}</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" style="width: 300px;height: 50px;font-size: 20px;margin-top: 10px;" @click="check()">下一步</el-button>
                </el-form-item>
            </el-form>
      </div>

      <div v-if="emailCheck" class="passwordform">
        <el-form ref="passwordForm" :model="passwordForm" :rules="rules" label-width="80px">
            <el-form-item prop="password"  >
                <el-input class="password" placeholder="请输入新密码" v-model="passwordForm.password" readonly onfocus="this.removeAttribute('readonly');" auto-complete="off" style="width: 300px;"></el-input>
            </el-form-item >
            <el-form-item prop="check_password">
                <el-input class="check_password" placeholder="请确认密码" v-model="passwordForm.check_password" readonly onfocus="this.removeAttribute('readonly');" auto-complete="off" style="width: 300px;"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button @click="updatePassword()" type="primary" style="width: 300px;height: 50px;font-size: 20px;margin-top: 10px;">重置密码</el-button>
            </el-form-item>
        </el-form>
      </div>

      <div class="footer">
            <span>></span>
            <el-button type="text" @click="$emit('show-login')">返回登录</el-button>
        </div>
    </div>
</template>

<script>
import axios from 'axios';
export default{
    data(){
      return{
        emailForm:{
          email:'',
          check_num:''
        },
        passwordForm:{
          password:'',
          check_password:''
        },
        emailCheck:false,
        isButtonDisabled: false,
        buttonText:  '获取验证码',
        countdown: 60,
        rules: {
          email: [
            { required: true, message: '请输入邮箱地址', trigger: 'blur' },
            { type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change'] }
          ],
          check_num: [
            { required: true, message: '请输入六位数验证码', trigger: 'blur' },
            { validator: this.validateCheckNum, trigger: 'blur' }
          ],
          password: [
            { required: true, message: 'Please input the password', trigger: 'blur' }
          ],
          check_password: [
            { required: true, message: 'Please confirm the password', trigger: 'blur' },
            { validator: this.validatePass, trigger: 'blur' }
          ]
        }
      }

    },
  created(){
    this.axios = axios.create({
      baseURL: 'http://localhost:8090', // 后端 API 的地址
      timeout: 5000 // 请求超时时间
    });
  },
    methods:{
      //向邮箱发送验证码
      getCheckCode(){

        const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if(!emailPattern.test(this.emailForm.email)){
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
            console.log(this.emailForm.email);
            // 发送获取验证码的请求
            this.axios({
              method: "get",
              url: "/mail/getCheckCode",
              params: {
                email:this.emailForm.email,
              },
              headers: {
                'Content-Type': 'application/json'
              }
            }).then((res) => {
              if (res.data.error_message === 'success') {
                console.log(res.data);
                this.$message.success("验证码发送成功，请注意查收");
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

      //检查验证码是否正确
      check(){
        this.$refs.emailForm.validate((valid) => {
          if (valid) {
            this.axios({
              method: "get",
              url: "/mail/CheckCode",
              params: {
                email: this.emailForm.email,
                code: this.emailForm.check_num,
              },
              headers: {
                'Content-Type': 'application/json'
              }
            }).then((res) => {
              if (res.data.error_message === 'success') {
                console.log(res.data);
                this.emailCheck = true;
              } else {
                // 登录失败处理
                this.$message({
                  message: res.data.error_message,
                  type: "warning",
                });
                this.emailCheck = false;
              }
            }).catch((error) => {
              // 请求失败处理
              console.error('判断验证码出错', error);
              this.$message.error('出错误了');
              this.emailCheck = false;
            });
          } else {
            console.log('验证失败');
            this.$message.warning("输入格式不正确")
          }
        });
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

      validatePass(rule, value, callback) {
        if (value === '') {
          callback(new Error('请确认密码'));
        } else if (value !== this.passwordForm.password) {
          callback(new Error('密码不一致'));
        } else {
          callback();
        }
      },

      updatePassword(){
        this.$refs.passwordForm.validate((valid) => {
          if (valid) {
            this.axios({
              method:"put",
              url:"/user/forgetPassword",
              data:{
                email:this.emailForm.email,
                newPassword: this.passwordForm.check_password,
                checkCode: this.emailForm.check_num,
              },
              headers: {
                'Content-Type': 'application/json'
              }
            }).then((res) => {
              if (res.data.error_message === 'success') {
                console.log(res.data);
                this.$message.success("密码更改成功！");
                window.location.reload();
              } else {
                // 登录失败处理
                this.$message({
                  message: res.data.error_message,
                  type: "warning",
                });
              }
            }).catch((error) => {
              // 请求失败处理
              console.error('更改密码出错', error);
              this.$message.error('出错误了');
            });
          } else {
            console.log('两次密码输入不同');
            this.$message.warning("两次密码输入不同");
          }
        });

      }
    }
}

</script>

<style scoped>
.forgetpassword{
  position: absolute;
  width: 460px;
  height: 500px;
  border:solid 3px;
  border-radius: 20px;
  left:60%;
  top:120px;
  background-color: rgba(255, 255, 255, 0.838);
}

.emailform, .passwordform{
  position: absolute;
  top:30%
}

.footer{
  position: absolute;
  top: 380px;
  left: 85px;
}
</style>
