<template>
    <div class="main">
        <div class="userform">
            <div style="position:absolute;top:40%;left:10%;width: 100%;">
        <el-form ref="userForm" :model="userForm" :rules="rules" label-width="80px">
            <el-form-item label="用户名" prop="username">
                <el-input v-model="userForm.username" readonly onfocus="this.removeAttribute('readonly');" style="width: 300px;margin-left: -30%;"></el-input>
            </el-form-item>
             <!--<el-form-item label="姓名" prop="name">
                <el-input v-model="userForm.name" readonly onfocus="this.removeAttribute('readonly');" style="width: 300px;margin-left: -30%;"></el-input>
            </el-form-item>-->
            <!--<el-form-item label="密码" prop="password">
                <el-input v-model="userForm.password" readonly onfocus="this.removeAttribute('readonly');" style="width: 300px;"></el-input>
            </el-form-item>-->
            <el-form-item label="手机号" prop="phonenumber">
                <el-input v-model="userForm.phonenumber" readonly onfocus="this.removeAttribute('readonly');" style="width: 300px;margin-left: -30%;"></el-input>
            </el-form-item>
            <el-form-item label="邮箱" prop="email">
                <span style="margin-left: -50%;font-size: 18px;">{{ this.userForm.email }}</span>
                <el-button size="mini" style="position: absolute;left: 45%;top:8px;border-radius: 10px;">修改</el-button>
            </el-form-item>
            <el-form-item label="性别" prop="gender">
              <el-radio v-model="userForm.gender" label="man" style="margin-left: -60%;">男</el-radio>
              <el-radio v-model="userForm.gender" label="woman">女</el-radio>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" style="position:absolute;left:20%">提交修改</el-button>
            </el-form-item>
        </el-form>
    </div>
      </div>
    </div>
</template>

<script>
import axios from 'axios';
export default {
  name: 'Register',
  data() {
        return {
            userForm: {
                username: '',
                password: '',
                phonenumber: '',
                email: '',
                gender:'',
                baseURL:''
            },
            /*rules: {
                username: [
                    { required: true, message: '请输入账号', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' }
                ],
                email: [ { pattern: /^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/, required: true, message: "请输入正确的邮箱", trigger: "blur" }]
            }*/
        };
    },
  created(){
      this.axios = axios.create({
          baseURL: 'http://localhost:8090', // 后端 API 的地址
          timeout: 5000 // 请求超时时间
      });

      const storedUser = localStorage.getItem("userInfo");
      const user = JSON.parse(storedUser);
      this.userForm.username = user.username;
      this.userForm.name = user.name;
      this.userForm.phonenumber = user.phone_number;
      this.userForm.email = user.email
  },

  methods:{

  }
}

</script>

<style scoped>
.main{
    position: relative;
    width:90%;
    height:85%;
    background-color: rgba(240, 248, 255, 0.74);
    opacity: 1;
    border-radius:15px;
}
.userform{
    position: absolute;
    width:50%;
    height:100%;

    background-color: rgba(240, 248, 255, 0.74);
    border-radius:15px;
}
</style>
