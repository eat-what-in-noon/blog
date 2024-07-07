<template>
    <div  class="login">
      <h1 style="position: relative;margin-left: -270px; top: 20px;">登录</h1>
      <el-tabs v-model="activeTab" @tab-click="handleTabClick" class="custom-tabs">
        <el-tab-pane label="密码登录" name="password"></el-tab-pane>
        <el-tab-pane label="第三方登录" name="provider"></el-tab-pane>
      </el-tabs>
      <div class="login-form" v-if="activeTab === 'password'">
        <el-form ref="login1Form" :model="login1Form" :rules="rules" label-width="80px">
          <el-form-item prop="loginName">
              <el-input id="username" placeholder="用户名/邮箱" v-model="login1Form.loginName" readonly onfocus="this.removeAttribute('readonly');" auto-complete="off" style="width: 300px;"></el-input>
          </el-form-item>
          <el-form-item prop="password">
              <el-input id="password" placeholder="密码" type="password" readonly onfocus="this.removeAttribute('readonly');" v-model="login1Form.password" auto-complete="off" style="width: 300px;"></el-input>
              <el-button size="mini" @click="$emit('show-password')" style="position: absolute;border:none;background-color: white;outline: none;left:73%;top:7px">忘记密码</el-button>
            </el-form-item>
          <el-form-item>
              <el-button type="primary"  size="mini" style="width: 300px;height: 50px;font-size: 20px;margin-top: 10px;"  @click="onlogin()">登录</el-button>
          </el-form-item>
        </el-form>
    </div>

    <div class="provider-form" v-if="activeTab === 'provider'">
        <el-form ref="login2Form" :model="login2Form" label-width="80px">
          <el-form-item>
            <el-button class="qq_login" @click="quickLogin('QQ登录成功')">
              <img id="qq_icon" class="qq_icon" src="@/assets/qq.png" alt="qq_login">
              <span id="description_qq_login">QQ登录</span>
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button class="wechat_login" @click="quickLogin('微信登录成功')">
              <img id="wechat_icon" class="vx_icon" src="@/assets/vx.png" alt="wechat_login">
              <span id="description_wechat_login">微信登录</span>
            </el-button>
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
                  loginName: '', //用户登录时使用的账号，可以为username也可以为邮箱
                  password: ''  //密码
              },
              activeTab:'password',//当前显示哪种登录方式
              //验证规则
                rules: {
                  loginName: [
                        { required: true, message: '请输入账号', trigger: 'blur' }
                    ],
                    password: [
                        { required: true, message: '请输入密码', trigger: 'blur' }
                    ]
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
    onlogin(){
      const user = {
        username:this.login1Form.loginName, 
        password:this.login1Form.password,
      }
      this.axios({
        method:"post",
        url:"/user/login",
        data:JSON.stringify(user),
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

    handleTabClick(tab){
    if (tab.name === 'provider') {
      this.$refs.login1Form.clearValidate(); // 清除密码登录表单的验证信息
    } else if (tab.name === 'password') {
      this.$nextTick(() => {
        this.$refs.login1Form.validate(); // 在切换到密码登录页时重新触发表单验证
      });
    }
      this.activeTab = tab.name; // 更新当前活动标签页
    }
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

.qq_login,.wechat_login{
    position: relative;
    text-align: center;
    width: 300px;
    height: 50px;
    margin-left: -100px;
    margin-top:20px;
    border: none;
    border-radius: 10px;
    overflow:hidden;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
  }

  .qq_icon,.vx_icon{
    position:absolute;
    left:5px;
    top:10px;
    height: 33px;
    width:35px;
  }
</style>
