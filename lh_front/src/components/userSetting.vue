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
                <el-avatar  class="avatar" fit="contain" :size="180" alt="头像"
                            :src="userForm.avatar ? baseURL + userForm.avatar : 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png'" >
                </el-avatar>
              </el-upload>
            </div>

            <div style="position:absolute;top:40%;left:10%;width: 100%;">
            <el-form ref="userForm" :model="userForm" label-width="80px">
                <el-form-item label="用户名" prop="username">
                    <el-input v-model="userForm.username" readonly onfocus="this.removeAttribute('readonly');" style="width: 300px;margin-left: -30%;"></el-input>
                </el-form-item>
                <el-form-item label="手机号" prop="phonenumber">
                    <el-input v-model="userForm.phoneNumber" readonly onfocus="this.removeAttribute('readonly');" style="width: 300px;margin-left: -30%;"></el-input>
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                    <span style="position:absolute;left:50px;font-size: 18px;">{{ this.userForm.email }}</span>
                    <el-button @click="showDialog()" size="mini" style="position: absolute;left: 65%;top:8px;border-radius: 10px;">修改</el-button>
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                <el-radio v-model="userForm.gender" label="man" style="margin-left: -60%;">男</el-radio>
                <el-radio v-model="userForm.gender" label="woman">女</el-radio>
                </el-form-item>
                <el-form-item>
                    <el-button @click="updateInfo()" type="primary" style="position:absolute;left:0%;width:60%">提交修改</el-button>
                </el-form-item>
            </el-form>
        </div>
      </div>
      <el-dialog :visible.sync="dialogVisible" class="email-dialog" :modal-append-to-body="false">
        <el-form ref="emailForm" :rules="rules" :model="emailForm" label-width="20px">
          <el-form-item>
            <el-input v-model="this.userForm.email"></el-input>
          </el-form-item>
          <el-form-item prop="newEmail">
            <el-input placeholder="请输入新邮箱" v-model="emailForm.newEmail"></el-input>
          </el-form-item>
          <el-form-item prop="checkNum">
            <el-input class="checkNum" placeholder="验证码" v-model="emailForm.checkNum" readonly onfocus="this.removeAttribute('readonly');" auto-complete="off"></el-input>
            <el-button size="mini" @click="getCheckNum" style="position: absolute;border:none;background-color: white;outline: none;left:85%;top:7px">{{buttonText}}</el-button>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="updateEmail" style="width:100%;">提交</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>
      <div class="myTags">
        <div class="tagBox">
        <span
                      :style="{ backgroundColor: tag.color, fontSize: tag.fontSize }"
                      class="tag-item hand-style" v-for="(tag, index) in tagList" :key="index">
                    {{ tag.name }}
        </span>
    </div>
      </div>
    </div>

</template>

<script>
import axios from 'axios';
import tags from './tags.vue';
import {constructArgumentList} from "node-notifier/lib/utils";
export default {
  name: 'Register',
  components:{
    tags,
},
  data() {
        return {
          headerObj:{
            'Authorization': 'Bearer '+ localStorage.getItem('token')
          },
            userForm: {
                id:'',
                username: '',
                password: '',
                phoneNumber: '',
                email: '',
                gender:'',
                avatar:'',

            },
            baseURL:'http://localhost:8090/static/avatar/',
            emailForm:{
              oldEmail:'',
              newEmail:'',
              checkNum:'',
            },
            dialogVisible:false,
            tagList:[],
            tagNames:[],
            maxFontSize:30,
            minFontSize:20,
          emailCheck : false,
          //验证码获取按钮显示倒计时
          isButtonDisabled: false,
          buttonText:  '获取验证码',
          countdown: 60,
          rules:{
            newEmail: [
              { required: true, message: '请输入邮箱地址', trigger: 'blur' },
              { type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change'] }
            ],
            checkNum: [
              { required: true, message: '请输入六位数验证码', trigger: 'blur' },
              { validator: this.validateCheckNum, trigger: 'blur' }
            ],
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
    mounted() {
        this.initializeTags();

    },
  created(){
      this.axios = axios.create({
          baseURL: 'http://localhost:8090', // 后端 API 的地址
          timeout: 5000 // 请求超时时间
      });

    const storedUser = localStorage.getItem("userInfo");
    let user = JSON.parse(storedUser);
    const url="/user/userinfo";
    this.axios({
      method:"get",
      url:url,
      params:{
        id:user.id,
      },
      headers: {
        'Authorization': 'Bearer '+ localStorage.getItem('token')
      }
    }).then((res) => {
      if (res.data.error_message === 'success') {
        console.log(res.data);
        user = res.data.data;
        localStorage.setItem("userInfo", JSON.stringify(user));
      } else {
        // 登录失败处理
        this.$message({
          message: res.data.error_message,
          type: "warning",
        });
      }
    }).catch((error) => {
      // 请求失败处理
      console.error('获取用户信息出错', error);
      this.$message.error('出错误了');
    });

      this.userForm.id = user.id;
      this.userForm.username = user.username;
      this.userForm.phoneNumber = user.phoneNumber;
      this.userForm.email = user.email;
      this.userForm.gender = user.gender;
      this.userForm.avatar = user.avatar;
      console.log("用户信息");
      console.log(user);
      console.log(this.userForm);
  },

  methods:{

    initializeTags() {
      const url="/user/getAllTag";
      this.axios({
        method:"get",
        url:url,
        params:{
          id:this.userForm.id,
        },
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('token')
        }
      }).then((res) => {
        if (res.data.error_message === 'success') {
          console.log("这是用户标签信息")
          console.log(res.data);
          this.tagNames = res.data.data;
          console.log(this.tagNames);
          this.tagList = this.tagNames.map(tag => ({
            name: tag,
            color: this.randomColor(),
            fontSize: this.randomFontSize()
          }));
          console.log("这是tagList数据");
          console.log(this.tagList);
        } else {
          // 登录失败处理
          this.$message({
            message: res.data.error_message,
            type: "warning",
          });
        }
      }).catch((error) => {
        // 请求失败处理
        console.error('获取用户标签出错', error);
        this.$message.error('出错误了');
      });

    },
        randomColor() {
            const letters = '0123456789ABCDEF';
            let color = '#';
            for (let i = 0; i < 6; i++) {
                color += letters[Math.floor(Math.random() * 16)];
            }
            return color;
        },
        randomFontSize() {
            return `${Math.floor(Math.random() * (this.maxFontSize - this.minFontSize + 1) + this.minFontSize)}px`;
        },

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

    updateInfo(){
      const url="/user/changeUserInfo";
      this.axios({
        method:"put",
        url:url,
        data:this.userForm,
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('token')
        }
      }).then((res) => {
        if (res.data.error_message === 'success') {
          console.log(res.data);
          this.$message.success("更改成功！！");
        } else {
          // 登录失败处理
          this.$message({
            message: res.data.error_message,
            type: "warning",
          });
        }
      }).catch((error) => {
        // 请求失败处理
        console.error('更改用户信息出错', error);
        this.$message.error('出错误了');
      });
    },
    showDialog(){
      console.log("showDialog方法被调用！");
      this.dialogVisible = true;
      console.log(this.dialogVisible)
    },
    getCheckNum(){
      const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if(!emailPattern.test(this.emailForm.newEmail)){
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
      // 发送获取验证码的请求
      this.axios({
        method: "get",
        url: "/mail/getCheckCode",
        params: {
          email:this.userForm.email,
        },
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('token')
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

    //
    updateEmail() {
      this.$refs.emailForm.validate((valid) => {
        console.log("valid值为"+valid);
        if (valid) {
          this.axios({
            method: "put",
            url: "/user/changeUserEmail",
            data: {
              oldEmail: this.userForm.email,
              newEmail: this.emailForm.newEmail,
              checkCode: this.emailForm.checkNum,
            },
            headers: {
              'Authorization': 'Bearer ' + localStorage.getItem('token')
            }
          }).then((res) => {
            if (res.data.error_message === 'success') {
              console.log(res.data);
              this.$message.success("邮箱更改成功！");
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
            console.error('更改邮箱出错', error);
            this.$message.error('出错误了');
          });
        } else {
          console.log('请确认输入格式是否正确');
          this.$message.warning("请确认输入格式是否正确");
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
  }
}

</script>

<style scoped>
.main{
    position: relative;
    width:90%;
    height:85%;
    background-color: rgba(255, 255, 255, 0);
    opacity: 0.9;
    border-radius:15px;
    z-index: 100;
}
.userform{
    position: absolute;
    width:50%;
    height:100%;
    background-color: rgb(241, 239, 239);
    border-radius:15px 0 0 15px;
}
.myTags{
    position: absolute;
    width:50%;
    height: 100%;
    left:50%;
    background-color: rgb(206, 198, 198);
}
.tagBox {
    position: relative;
    padding-bottom: 5px; /* 减少底部内边距 */
    height: auto;
    margin-top: 60px;
    width: 80%; /* 设置宽度为100% */
    left:10%;
    overflow: hidden; /* 超出部分隐藏 */
}

.tag-item {
    display: inline-block; /* 行内块显示 */
    margin: 8px; /* 减少标签之间的外边距 */
    padding: 5px 10px;
    border-radius: 5px;
    color: #ffffff;
    cursor: pointer;
    white-space: nowrap; /* 不换行 */
    transition: box-shadow 0.3s ease; /* 添加过渡效果 */
    transition: transform 0.3s ease; /* 添加过渡效果 */
}

.tag-item:hover {
    box-shadow: 0 8px 16px rgba(87, 84, 84, 0.6); /* 悬浮时增加阴影效果 */
    transform: scale(1.2); /* 悬浮时微微放大 */
}

.block{
  position: absolute;
  top:5%;
  left:35%;
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
