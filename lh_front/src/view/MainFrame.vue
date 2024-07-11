<template>
  <div class="background">
    <div class="header">
      <div class="home">
        <el-button @click="goToMainFrame" style="background-color:#ffffff00;border: none; width: 40px; height:30px;margin-left: -40px;" >
            <img src="@/assets/home.png" alt="首页" style="width:25px;height: 25px;margin-top: 1px;">
            <span style="position: absolute;margin-top: 8px;margin-left: 5px; font-weight: 1000;">首页</span>
        </el-button>
      </div>

      <div class="write">
        <el-button @click="gotoWrite()" style="background-color:#ffffff00;border: none; width: 40px; height:30px;margin-left: -40px;" >
            <img src="@/assets/write.png" alt="写博客" style="width:25px;height: 25px;margin-top: 1px;">
            <span style="position: absolute;margin-top: 8px;margin-left: 5px; font-weight: 1000;">写博客</span>
          </el-button>
      </div>

      <div class="classify">
        <el-button @click="drawer = true" style="background-color:#ffffff00;border: none; width: 40px; height:30px;margin-left: -40px;" >
            <img src="@/assets/classify.png" alt="分类" style="width:23px;height: 20px;margin-top: 2px;">
            <span style="position: absolute;margin-top: 8px;margin-left: 5px; font-weight: 1000;">分类</span>
          </el-button>
      </div>

      <div class="personal-information">
        <el-dropdown trigger="click">
          <el-button style="background-color:#ffffff00;border: none; width: 40px; height:30px;margin-left: -40px;" >
            <img src="@/assets/my.png" alt="我的" style="width:25px;height: 25px;margin-top: 1px;">
            <span style="position: absolute;margin-top: 8px;margin-left: 5px; font-weight: 1000;">我的</span>
          </el-button>
          <el-dropdown-menu>
          <el-dropdown-item @click.native="show_userSetting()">个人信息</el-dropdown-item>
          <el-dropdown-item @click.native="quit()">退出登录</el-dropdown-item>
        </el-dropdown-menu>
        </el-dropdown>
      </div>
     </div>



     <video autoplay muted loop id="background-video">
          <source src="@/assets/background.mp4" type="video/mp4">
          Your browser does not support the video tag.
      </video>

    <el-drawer
      title="分类"
      :visible.sync="drawer"
      :direction="direction"
      :before-close="handleClose"
      custom-class="custom-drawer"
      :modal="false"
      append-to-body>
      <div class="class-contents">
        <el-button v-for="(item, index) in class_contents" :key="index" type="text" @click="getDataByCatagory(item)" class="class-item">
          <img :src="item.img" style="position:absolute; width:30px;height:30px;left:40px;top:17px;" alt="分类图片">
          <span>{{ item.name }}</span>
        </el-button>
      </div>
    </el-drawer>

    <div v-show="showMain" class="search-and-welcome">
      <div class="welcome">
        <h1 style="font-size: 80px;color: #b2cee5">中午吃什么の博客平台</h1>
      </div>
      <div class="search-box">
        <!-- 搜索 -->
        <div style="display: flex">
          <input class="ais-SearchBox-input" type="text"
                 v-model="articleSearch"
                 placeholder="搜索文章" maxlength="32">
          <div class="ais-SearchBox-submit" @click="selectArticle()">
            <img src="@/assets/search.png" style="width: 20px;height: 20px; margin-top:5px;" alt="搜索图标">
          </div>
        </div>
      </div>
    </div>

     <userSetting v-show="showUserSetting" class="userSetting" />
     <div v-show="showMain" class="main" @scroll="handleScroll">
      <div id="check" style="position: absolute;top:0;height:60%;width:100%"></div>

        <div  class="container">
          <div @click="getDataByUser()" class="user-style" ref="userInfo">
            <userInfo :user="user"/>
          </div>

          <div v-show="showMain" class="tags-style" ref="tags">
             <tags @clickTag="getDataByTag()"/>
          </div>

          <transition name="el-fade-in-linear">
          <div ref="filter_articles" v-show="showFilter" class="filter-article-list">
            <div v-if="filter_articles.length > 0">
              <filterArticle  v-for="filter_article in filter_articles"  :key="filter_article.id" :filter_article="filter_article"/>
            </div>
            <div v-else>
              <img src="@/assets/placeholder.png" alt="占位图片" style="position:absolute;top:60px;left:10%; width:680px;height:600px">
              <h2 style="position: absolute;left:38%;top:530px ">还没有文章哦~</h2>
            </div>
          </div>
        </transition>
          <transition name="el-fade-in-linear">
            <div v-show="showAllarticle" ref="articles" class="article-list">
              <Article  v-for="article in articles"  :key="article.id" :article="article" />
            </div>
          </transition>
        </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import userInfo from '../components/userInfo.vue';
import userSetting from '../components/userSetting.vue';
import Article from '../components/Article';
import tags from '../components/tags'
import filterArticle from '../components/filterArticle'
import {data} from "autoprefixer";
import article from "../components/Article.vue";
export default {
  name: 'MainFrame',
  components: {
    userInfo,
    userSetting,
    Article,
    filterArticle,
    tags,
  },
  data() {
    return {
      isArticleOpShow: true,
      direction: 'ttb',
      userData: null,
      showUserSetting: false,
      showAllarticle: true,
      articleSearch: '',
      showMain: true,
      showFilter: false, // 控制是否显示 filterArticle 组件
      drawer: false,
      class_contents: [
        {name: '生活', img: require('@/assets/life.png')},
        {name: '欧洲杯', img: require('@/assets/football.png')},
        {name: '娱乐', img: require('@/assets/entertainment.png')},
        {name: '游戏', img: require('@/assets/game.png')},
        {name: '体育', img: require('@/assets/sports.png')},
        {name: '教育', img: require('@/assets/education.png')},
        {name: '文化', img: require('@/assets/culture.png')},
        {name: '科技', img: require('@/assets/tech.png')},
        {name: '财经', img: require('@/assets/economy.png')},
        {name: '国际', img: require('@/assets/world.png')},
        {name: '汽车', img: require('@/assets/car.png')},
        {name: '摄影', img: require('@/assets/photograph.png')}
      ],
      articles: [],
      filter_articles: [],
      user: {},
    }
  },
  created() {
    this.axios = axios.create({
      baseURL: 'http://localhost:8090', // 后端 API 的地址
      timeout: 5000 // 请求超时时间
    });
    this.turnOwner();
  },

  mounted() {
    //获取主页面信息
    this.axios({
      method: "get",
      url: "/article/getAllArticle",
      data: '',
      headers: {
        'Authorization': 'Bearer ' + localStorage.getItem('token')
      }
    }).then((res) => {
      if (res.data.error_message === 'success') {
        this.articles = res.data.data.map((item, index) => {
          return {
            ...item,
            likeCnt: res.data.likeCnt[index],
            commentCnt: res.data.commentCnt[index],
            tagNames: res.data.tagNames[index]
          };
        });
        console.log(this.articles);
      } else {
        // 登录失败处理
        this.$message({
          message: res.data.error_message,
          type: "warning",
        });
      }
    }).catch((error) => {
      // 请求失败处理
      console.error('主页数据获取错误', error);
      this.$message.error('出错误了');
    });



    let identity = JSON.parse(sessionStorage.getItem("identity"));
    // 删除 'updateArticle' item
    sessionStorage.removeItem("identity");
    // 检查是否存在 'updateArticle' item
    if (identity !== null) {
      //更改user信息
      this.axios({
        method: "get",
        url: "/user/userinfo",
        params: {
          id: identity,
        },
        headers: {
          'Authorization': 'Bearer ' + localStorage.getItem('token')
        }
      }).then((res) => {
        if (res.data.error_message === 'success') {
          console.log("identity用户信息为");
          console.log(res.data);
          this.user = res.data.data;
          //模拟点击身份卡片效果
          this.getDataByUser();
        } else {
          // 登录失败处理
          this.$message({
            message: res.data.error_message,
            type: "warning",
          });
        }
      }).catch((error) => {
        // 请求失败处理
        console.error('主页信息获取错误', error);
        this.$message.error('出错误了');
      });

    }

  },


methods:{
  turnOwner(){
    this.user = JSON.parse(localStorage.getItem("userInfo"));
  },
  getItemIcon(img){
    console.log("@/assets/"+img);
    return '@/assets/'+img;
  },
  getDataByCatagory(item) {
    this.turnOwner();
      // 处理点击按钮事件，可以根据需要添加逻辑
    console.log(item);
    this.isArticleOpShow = false;

    this.axios({
      method:"get",
      url:"/article/getArticleInfoByCategory",
      params:{
        categoryName: item.name,
      },
      headers: {
        'Authorization': 'Bearer '+ localStorage.getItem('token')
      }
    }).then((res) => {
      if (res.data.error_message === 'success') {
        this.filter_articles = res.data.data.map((item, index) => {
          return {
            ...item,
            likeCnt: res.data.likeCnt[index],
            commentCnt: res.data.commentCnt[index],
            tagNames: res.data.tagNames[index]
          };
        });
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
      console.error('主页数据获取错误', error);
      this.$message.error('出错误了');
    })
      console.log('Clicked item:', item);
      this.drawer = false
      this.showFilterArticles();

  },
  getDataByTag(){
    this.turnOwner();
    const tag =  sessionStorage.getItem('tag');
    this.isArticleOpShow = false;
    console.log(tag);
    this.axios({
      method:"get",
      url:"/article/getArticleInfoByTag",
      params:{
        tagName: tag
      },
      headers: {
        'Authorization': 'Bearer '+ localStorage.getItem('token')
      }
    }).then((res) => {
      if (res.data.error_message === 'success') {
        this.filter_articles = res.data.data.map((item, index) => {
          return {
            ...item,
            likeCnt: res.data.likeCnt[index],
            commentCnt: res.data.commentCnt[index],
            tagNames: res.data.tagNames[index]
          };
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
      console.error('主页数据获取错误', error);
      this.$message.error('出错误了');
    });
    this.showFilterArticles();
  },
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
      }else{
        this.$message.error(res.data.error_message);
        this.$router.push('/Sign');
        localStorage.clear();
      }
    }).catch((error) => {
      // 请求失败处理
      console.error("用户id:"+user.id);
      console.error('清除缓存失败:', error);
      this.$router.push('/Sign');
      localStorage.clear();
      this.$message({
        message: "退出登录错误！",
        type: "error",
      });
    });
  },
    handleClose(done) {
        done();
      },
    show_userSetting(){
      this.showMain = false;
      this.showUserSetting = true;
      this.showAllarticle = false;
      this.showFilter = false;
    },
    goToMainFrame(){
      this.turnOwner();
      this.showMain = true;
      this.showAllarticle = true;
      this.showFilter = false;
      this.showUserSetting = false;
    },
    showArticles() {
      this.showAllarticle = true;
      this.showFilter = false; // 确保显示全部文章时隐藏 filterArticle
    },

    showFilterArticles() {

      //this.filter_articles = this.articles.filter(article => article.tags.includes(tag));
      this.showFilter = true; // 显示 filterArticle 组件
      this.showAllarticle = false;
    },
    handleScroll() {
      const checkElement = document.getElementById('check');
      const checkRect = checkElement.getBoundingClientRect();
      const userInfo = this.$refs.userInfo;
      const tags = this.$refs.tags;
      const articleList = this.$refs.articleList;

      if (checkRect.bottom < 0) {
        // 当 check 不可见时，固定 userInfo，滚动 articleList
        userInfo.style.position = 'fixed';
        tags.style.position = 'fixed';
        //articleList.style.marginTop = '20%'; // 确保 articleList 不会被 userInfo 覆盖
      } else {
        // 当 check 可见时，恢复 userInfo 原位置
        userInfo.style.position = 'absolute';
        userInfo.style.top = '50px';
        tags.style.position = 'absolute';
        tags.style.top = '320px';
      }
    },
    gotoWrite(){
      this.$router.push('/textEditor');
    },

  getDataByUser(){
    this.isArticleOpShow = true;
    console.log(this.user);
    this.axios({
      method:"get",
      url:"/user/getAllArticle",
      params:{
        id: this.user.id,
      },
      headers: {
        'Authorization': 'Bearer '+ localStorage.getItem('token')
      }
    }).then((res) => {
      if (res.data.error_message === 'success') {
        this.filter_articles = res.data.data.map((item, index) => {
          return {
            ...item,
            likeCnt: res.data.likeCnt[index],
            commentCnt: res.data.commentCnt[index],
            tagNames: res.data.tagNames[index]
          };
        });
      } else {

        this.$message({
          message: res.data.error_message,
          type: "warning",
        });
      }
    }).catch((error) => {
      // 请求失败处理
      console.error('主页数据获取错误', error);
      this.$message.error('出错误了');
    });

    this.drawer = false
    this.showFilterArticles();
  },


  selectArticle(){
    this.axios({
      method:"get",
      url:"/article/findArticle",
      params:{
        title: this.articleSearch,
      },
      headers: {
        'Authorization': 'Bearer '+ localStorage.getItem('token')
      }
    }).then((res) => {
      if (res.data.error_message === 'success') {
        this.filter_articles = res.data.data.map((item, index) => {
          return {
            ...item,
            likeCnt: res.data.likeCnt[index],
            commentCnt: res.data.commentCnt[index],
            tagNames: res.data.tagNames[index]
          };
        });
      } else {

        this.$message({
          message: res.data.error_message,
          type: "warning",
        });
      }
    }).catch((error) => {
      // 请求失败处理
      console.error('主页数据获取错误', error);
      this.$message.error('出错误了');
    });

    this.drawer = false
    this.showFilterArticles();
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

.user-style{
  position: absolute;
  left:1%;
  top:50px;
  width:100%;
  height: 100%;
}

.tags-style{
  position: absolute;
  left:1%;
  top:320px;
}

.main{
  position: absolute;
  width:100%;
  height: 100%;
  overflow-y: auto;
  -ms-overflow-style: none;
  background-color: #dbcece;
  top:0;
}
.main::-webkit-scrollbar {
  display: none;  /* 适用于 Chrome, Safari 和 Opera */
}

.container{
  position: relative;
  width:100%;
  /*height:100%;*/
  min-height: 100%;
  top:60%;
  left:0;
  background-color: rgb(228, 216, 216);
  z-index: 2;
}
.background::-webkit-scrollbar {
  display: none;  /* Chrome, Safari, and Opera */
}


#background-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover; /* 或者使用 contain，根据需要选择 */
  z-index: 1;
}
.header{
  position: absolute;
  background-color: #75a69400;
  height: 6%;
  width: 100%;
  opacity: 1;
  top: 0px;
  left: 0px;
  z-index: 1000; /* 确保 header 在页面顶部 */
  transition: background-color 0.3s ease; /* 添加过渡效果 */
}

.header:hover {
  background-color: rgb(37, 39, 41); /* 悬浮时背景颜色加深 */
}

.userSetting{
  position: absolute;
  left:5%;
  top:10%;
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

.write{
  position: absolute;
  left:77%;
  top:-1px;
}
.classify{
  position: absolute;
  left:70%;
  top:1px;
}

.article-list{
  position: relative;
  padding-top: 50px;
  padding-bottom:20px;
  left:25%;
  width: 75%;
  height: auto;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  grid-gap: 30px;
  grid-auto-rows: 320px;
}

.filter-article-list{
  position: relative;
  padding-top: 50px;
  left:32%;
  width: 70%;
  height: auto;
  padding-bottom:20px;
}

.welcome{
  position: absolute;
  background-color: rgba(255, 255, 255, 0);
  left:20%;
  top:10%;
  width:60%;
  height:20%;
  z-index: 1;
}

.search-box{
  position: absolute;
  top:380px;
  width:40%;
  left:30%;
  z-index: 1;
  border-radius: 40px 0 0 40px;
}

.ais-SearchBox-input {
  padding: 0 14px;
  height: 30px;
  width: calc(100% - 50px);
  outline: 0;
  border:none;
  border-right: 0;
  border-radius: 40px 0 0 40px;
  background:rgba(29, 28, 28, 0.555);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}

.ais-SearchBox-submit {
  height: 30px;
  width: 50px;
  border: none;
  border-left: 0;
  border-radius: 0 40px 40px 0;
  cursor: pointer;
  background:rgba(29, 28, 28, 0.555);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
}

>>> .el-drawer{
  background-color: transparent !important;
}

.class-contents{
  display: flex;
  position: relative;
  top:0px;
  flex-wrap: wrap;
  justify-content: center;
  height: 100%;
  background-color: transparent !important;
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  overflow: hidden;
}

.class-item{
  position: relative;
  margin: 0 5px 5px 0; /* 统一按钮的间距，确保每个按钮都有右侧和下侧的间距 */
  width: calc((100% - 10px) / 7);
  height:calc((100% - 10px) / 2);
  min-width: calc((100% - 10px) / 7);
  max-width: calc((100% - 10px) / 7);
  /*border:solid 3px;*/
  font-size: 18px;
  transition: transform 0.3s ease; /* 添加过渡效果 */
}


.class-item:hover {
  transform: scale(1.05); /* 悬浮时微微放大 */
}

.class-item:nth-child(6n) {
  /* 去除第3n个的margin-right */
  margin-right: 0;
}

</style>
