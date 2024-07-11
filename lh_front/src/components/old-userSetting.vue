<template>
    <div class="main">
        <div class="userform">
          <div class="block">
            <el-upload
              class="avatar-uploader"
              action="http://localhost:8090/user/uploadAvatar"
              :headers="headerObj"
              name="avatar"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
              :on-change="handleAvatarChangeIcon">
              <el-avatar  class="avator" fit="contain" :size="180" alt="头像"
                         :src="userForm.avatar ? userForm.baseURL + userForm.avatar : 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" >
              </el-avatar>
            </el-upload>
          </div>
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
                <el-button type="primary" style="position:absolute;left:0%;width:60%">提交修改</el-button>
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
          headerObj:{
            'Authorization': 'Bearer '+ localStorage.getItem('token')
          },
            userForm: {
                username: '',
                password: '',
                phonenumber: '',
                email: '',
                gender:'',
                avatar:'',
                baseURL:'http://localhost:8090/static/avatar/',
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
      this.userForm.email = user.email;
    this.userForm.avatar = user.avatar;
  },

  methods:{
    handleAvatarChangeIcon(file) {
      const isLt10M = file.size / 1024 / 1024 < 10;
      if (!isLt10M) {
        this.$message.error('上传头像图片大小不能超过 10MB!');
        return false;
      }
      return true;
    },

    handleAvatarSuccess(res, file) {
      this.userForm.avatar = res.data;
      console.log(res.data);
    },
  }
}

</script>

<style scoped>
.main{
    position: relative;
    width:90%;
    height:85%;
    background-color: rgb(212, 185, 185);
    opacity: 0.9;
    border-radius:15px;
    z-index: 100;
}
.userform{
    position: absolute;
    width:50%;
    height:100%;
    background-color: rgb(241, 239, 239);
    border-radius:15px;
}
.block{
  position: absolute;
  top:5%;
left:40%;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
