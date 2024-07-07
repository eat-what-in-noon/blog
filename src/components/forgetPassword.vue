<template>
    <div class="forgetpassword">
      <h1 style="position: relative;margin-left: -270px; top: 20px;">忘记密码</h1>
      <div v-if="!emailCheck" class="emailform">
            <el-form ref="emailForm" :model="emailForm" label-width="80px">
                <el-form-item prop="email"  >
                    <el-input class="email" placeholder="邮箱" v-model="emailForm.email" readonly onfocus="this.removeAttribute('readonly');" auto-complete="off" style="width: 300px;"></el-input>
                </el-form-item>
                <el-form-item prop="check_num">
                    <el-input class="check_num" placeholder="验证码" v-model="emailForm.check_num" readonly onfocus="this.removeAttribute('readonly');" auto-complete="off" style="width: 300px;"></el-input>
                    <el-button size="mini" @click="$emit('show-password')" style="position: absolute;border:none;background-color: white;outline: none;left:73%;top:7px">验证码</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" style="width: 300px;height: 50px;font-size: 20px;margin-top: 10px;" @click="check()">下一步</el-button>
                </el-form-item>
            </el-form>
      </div>

      <div v-if="emailCheck" class="passwordform">
        <el-form ref="passwordForm" :model="passwordForm" label-width="80px">
            <el-form-item prop="password"  >
                <el-input class="password" placeholder="请输入新密码" v-model="passwordForm.password" readonly onfocus="this.removeAttribute('readonly');" auto-complete="off" style="width: 300px;"></el-input>
            </el-form-item>
            <el-form-item prop="check_password">
                <el-input class="check_password" placeholder="请确认密码" v-model="passwordForm.check_password" readonly onfocus="this.removeAttribute('readonly');" auto-complete="off" style="width: 300px;"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" style="width: 300px;height: 50px;font-size: 20px;margin-top: 10px;">重置密码</el-button>
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
            emailCheck:false
        }
    },
    methods:{
        check(){
            //如果验证码正确
            this.emailCheck = true;
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