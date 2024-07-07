<template>
  <div class="background">
    <video autoplay muted loop id="background-video">
      <source src="@/assets/background.mp4" type="video/mp4">
      Your browser does not support the video tag.
    </video>
     <div class="header">
      <div class="personal-information">  
        <el-dropdown trigger="click">
          <el-button style="background-color:#75a694;border: none; width: 40px; height:30px;margin-left: -40px;" >
            <img src="@/assets/my.png" alt="我的" style="width:25px;height: 25px;margin-top: 1px;">
            <span style="position: absolute;margin-top: 8px;margin-left: 5px; font-weight: 1000;">我的</span>
          </el-button>
          <el-dropdown-menu>
          <el-dropdown-item @click.native="show_userSetting()">个人信息</el-dropdown-item>
          <el-dropdown-item @click.native="quit()">退出登录</el-dropdown-item>
        </el-dropdown-menu>
        </el-dropdown>
      </div>
      <div class="home">
        <el-button @click="goToMainFrame" style="background-color:#75a694;border: none; width: 40px; height:30px;margin-left: -40px;" >
            <img src="@/assets/home.png" alt="首页" style="width:25px;height: 25px;margin-top: 1px;">
            <span style="position: absolute;margin-top: 8px;margin-left: 5px; font-weight: 1000;">首页</span>
        </el-button>
      </div>

      <div class="search">
        <el-button style="background-color:#75a694;border: none; width: 40px; height:30px;margin-left: -40px;" >
            <img src="@/assets/search.png" alt="搜索" style="width:25px;height: 25px;margin-top: 1px;">
            <span style="position: absolute;margin-top: 8px;margin-left: 5px; font-weight: 1000;">搜索</span>
          </el-button>
      </div>
      <div class="write">
        <el-button style="background-color:#75a694;border: none; width: 40px; height:30px;margin-left: -40px;" >
            <img src="@/assets/write.png" alt="写博客" style="width:25px;height: 25px;margin-top: 1px;">
            <span style="position: absolute;margin-top: 8px;margin-left: 5px; font-weight: 1000;">写博客</span>
          </el-button>
      </div>
     </div>   
     
     <userInfo v-if="userHidden" class="user"/>
     <userSetting v-if="!userHidden" class="userSetting" />
      <div class="welcome">
          <div style="position: absolute;">
              <h1>欢迎来到博客平台</h1>
              <h2>123456879456654654654</h2>
          </div>
          <!--<img style="position:absolute; width: 150px;height:150px;left: 70%;" src="@/assets/reading.png" alt="插画">-->
      </div>
      <div class="artical-list">
        <Article v-for="article in articles" :key="article.id" :article="article" />
      </div>
  </div>
</template>

<script>
import axios from 'axios';
import userInfo from '../components/userInfo.vue';
import userSetting from '../components/userSetting.vue';
import Article from '../components/Article';
export default{
name:'MainFrame',
components:{
  userInfo,
  userSetting,
  Article
},
data(){
    return{
      userData:null,
      userHidden:'true',
      articles: [
        {
          id:'1',
          text:"test1",
        },
        {
          id:'2',
          text:"test2",
        },
        {
          id:'3',
          text:"test3",
        }
      ]
    }
},
created(){
    this.axios = axios.create({
          baseURL: 'http://localhost:8090', // 后端 API 的地址
          timeout: 5000 // 请求超时时间
    });
  
},
methods:{
    quit(){
      const storedUser = localStorage.getItem("userInfo");
      const user = JSON.parse(storedUser);
      
      this.axios({
        method:"post",
        url:"/user/logout",
        data:user,
        headers: {
          'Authorization':'Bearer '+localStorage.getItem('token')
        }
      }).then((res) => {
        if (res.data.error_message === 'success') {
          console.log("清除缓存成功");
          localStorage.clear();
          this.$router.push('/Sign');
          this.$message({
            message: "退出登录成功",
            type: "success",
          });
        } 
      }).catch((error) => {
        // 请求失败处理
        console.error("用户id:"+user.id);
        console.error('清除缓存失败:', error);
      });
    },
    show_userSetting(){
      this.userHidden = false;
    },
    goToMainFrame(){
      this.userHidden = true;
    }
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
.header{
    position: absolute;
    background-color: #75a694;
    height: 6%;
    width: 100%;
    opacity: 1;
    top: 0px;
    left: 0px;
  }

  .user{
    position: absolute;
    left:2%;
    top:10%;
  }

  .userSetting{
    position: absolute;
    left:5%;
    top:5%;
  }

  .personal-information{
    position: absolute;
    left:92%;
    top:-1px;
  }

  .home{
    position: absolute;
    left:85%;
    top:-1px;
  }
  .search{
    position: absolute;
    left:78%;
    top:-1px;
  }

  .write{
    position: absolute;
    left:71%;
    top:-1px;
  }

  .artical-list{
    position: absolute;
    top: 40%;
    left:30%;
    width: 70%;
    height: 100%;
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
    grid-gap: 20px; /* 设置组件之间的间隔 */
  }

  .welcome{
    position: absolute;
    background-color: blue;
    left:30%;
    top:10%;
    width:60%;
    height:20%
  }

</style>
