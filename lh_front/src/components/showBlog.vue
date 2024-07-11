<template>
  <div class="background">
    <div class="header">
      <el-page-header class="custom-page-header" @back="goBack" content="博客">
      </el-page-header>
    </div>
    <img class="cover-img" :src="article.cover ? this.baseURL + article.cover : this.baseURL+'test.png' "  alt="文章封面">

      <div class="title"> {{article.title}} </div>
      <div class="other-info">
        <div class="date">
            <img src="@/assets/date.png" class="date-img" alt="日期图标">
            <div class="date-text">发布于{{ article.createdDate }}</div>
        </div>

        <div class="tags">
            <img class="tags-img" src="@/assets/tags.png" alt="标签图标">
            <div class="tags-list">
                <el-tag type="info" v-for="(tag,index) in article.tags" :key="index">{{ tag }}</el-tag>
            </div>
        </div>

        <div class="category">
          <img class="category-img" src="@/assets/article_category.png" alt="类别图标">
          <span class="category-text">{{categorys.at(article.categoryId-1)}}</span>
        </div>
      </div>

    <div class="main">
      <div class="block">
        <el-avatar shape="square" class="avatar" :size="80" src="http://localhost:8090/static/avatar/1bb185c5-8491-4e0b-9629-44afdb61a7ca.jpg"  alt="头像">
          <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"/>
        </el-avatar>
        <p class="user-name">{{author.username}}</p>
      </div>
      <div id="check" style="position: absolute;top:0px;height:300px;width:100%"></div>
      <div class="blog">
       <div class="text">
          <div class="main-text" v-html="article.content" />
          <div class="footer-text">本文章最后更新于 {{ lastUpdated }}</div>
        </div>

        <div class="like">
            <img :src="isLiked ? require('@/assets/after_like.png') : require('@/assets/before_like.png')"
              class="like-img" alt="点赞数图标"
              @click="toggleLike"
              >
            <span class="like-num">{{article.likeCnt}}点赞</span>
        </div>

        <div class="comment">
          <comment/>
        </div>
      </div>
    </div>
</div>

</template>

<script>
import { quillEditor } from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import article from "./Article.vue";
import comment from '../components/comment';
import axios from "axios";
import userInfo from "./userInfo.vue";

export default {
  components: {
    quillEditor,
    comment
  },
  data() {
    return {
      categorys:['欧洲杯', '娱乐', '游戏', '体育','教育','文化', '科技','财经','国际', '生活', '汽车','摄影'],
      article: {
        id: '',
        title: '',
        content: '',
        createdDate:'',
        authorId:'',
        categoryId:'',
        cover:'',
        tags:[],
        likeCnt:'',
        commentCnt:'',
      },
      baseURL:'http://localhost:8090/static/cover/',
      baseURLAvatar:'http://localhost:8090/static/avatar/',
      lastUpdated:'2023-11-05 04:16:22',
      isLiked: false,
      user:{},
      author:{},
    }
  },
  created(){
    this.axios = axios.create({
      baseURL: 'http://localhost:8090', // 后端 API 的地址
      timeout: 5000 // 请求超时时间
    });
  },
  mounted() {
    this.article.id = JSON.parse(sessionStorage.getItem('articleId'));
    this.user = JSON.parse(localStorage.getItem('userInfo'));
    console.log(this.article.id);
    this.axios({
      method:"get",
      url:`/article/getArticleInfoById`,
      params:{
        id:this.article.id,
      },
      headers: {
        'Authorization': 'Bearer '+ localStorage.getItem('token')
      }
    }).then((res) => {
      if (res.data.error_message === 'success') {
        this.$message.success("文章获取成功");
        console.log("文章获取成功");
        this.article = res.data.data;
        this.article.tags = res.data.tagNames;
        this.article.likeCnt = res.data.likeCnt;
        this.article.commentCnt = res.data.commentCnt;
        console.log(this.article);
        this.getIsLiked();
        this.getAuthor();
      } else {
        // 登录失败处理
        this.$message({
          message: res.data.error_message,
          type: "warning",
        });
      }
    }).catch((error) => {
      // 请求失败处理
      console.error('文章数据获取错误', error);
      this.$message.error('出错误了');
    });
    sessionStorage.setItem("articleId", this.article.id);
    console.log(this.article);
  },
  methods: {
    getAuthor(){
      this.axios({
        method:"get",
        url:`/user/userinfo`,
        params:{
          id:this.article.authorId,
        },
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('token')
        }
      }).then((res) => {
        if (res.data.error_message === 'success') {
          console.log("作者信息获取成功");
          this.author = res.data.data;
          console.log(this.author);
        } else {
          // 登录失败处理
          this.$message({
            message: res.data.error_message,
            type: "warning",
          });
        }
      }).catch((error) => {
        // 请求失败处理
        console.error('作者信息获取错误', error);
        this.$message.error('出错误了');
      });
    },
    goBack() {
      console.log('go back');
      this.$router.push('/MainFrame');
    },
    toggleLike() {
      let url, add, method;
      if(!this.isLiked){
        url="/user/like";
        method="post";
        add = 1;
        console.log("现在是点赞")
      }else{
        url='/user/cancelLike';
        method="delete";
        add=-1;
        console.log("现在是取消点赞")
      }
      const like={
        userId:this.user.id,
        articleId:this.article.id,
      };
      this.axios({
        method:method,
        url:url,
        data:like,
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('token')
        }
      }).then((res) => {
        if (res.data.error_message === 'success') {
          this.isLiked = !this.isLiked;
          this.article.likeCnt += add;
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
        console.error('点赞或取消点赞失败', error);
        this.$message.error('出错误了');
      });

    },
    getIsLiked(){
      const url = "/article/checkLike"
      this.axios({
        method:"get",
        url:url,
        params:{
          articleId:this.article.id,
          userId:this.user.id,
        },
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('token')
        }
      }).then((res) => {
        if (res.data) {
          this.isLiked = res.data;
          console.log(res.data);
        } else {
          this.isLiked = res.data;
          console.log(res.data);
        }
      }).catch((error) => {
        // 请求失败处理
        console.error('获取信息错误', error);
        this.$message.error('出错误了');
      });
    }
  }
}
</script>

<style scoped>
.background {
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  overflow: hidden; /* 确保背景视频不会超出视口 */
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.header{
    position: absolute;
    background-color: #75a69400;
    height: 6%;
    width: 100%;
    opacity: 1;
    color: aliceblue;
    top: 0px;
    padding-top:20px;
    padding-left:10px;
    left: 0px;
    z-index: 1000; /* 确保 header 在页面顶部 */
    transition: background-color 0.3s ease; /* 添加过渡效果 */
  }
  .header:hover {
    background-color: rgb(47, 48, 48); /* 悬浮时背景颜色加深 */
  }

  .blog {
    display: flex;
    justify-content: center; /* 水平居中 */
    align-items: flex-start; /* 垂直顶部对齐 */
    flex-direction: column;
    width: 100%;
    padding-left: 20px;
    text-align: left;
    background-color: rgb(255, 255, 255);
    margin-top: 300px; /* 确保与上方间距一致 */
  }

  .check{
    position: absolute;
    top:0px;
    width:100%;
    height: 320px;
    opacity: 0;
  }

.main {
  position: absolute;
  background-color: rgba(243, 237, 237, 0); /* 透明背景色 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  align-items: center;
  justify-content: center;
  width:100%;
  height:100%;
  top:0px;
  -ms-overflow-style: none;
  overflow-y: auto;
}

.main::-webkit-scrollbar {
  display: none;  /* 适用于 Chrome, Safari 和 Opera */
}

.cover-above{
  position: absolute;
  width:60%;
  height:300px;
  top:0px;
  left:20%;
}

.cover-img{
  position: fixed;
  width: 100%;
  height:300px;
  top:0px;
  left:0px;
  object-fit: cover; /* 保持图片比例，裁剪超出部分 */
}

.text{
  width:64%;
  margin: 0 auto;
  border-radius: 10px;
  padding-left: 50px;
  margin-top:50px;
  background-color: #ebf1fd;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3); /* 设置阴影效果 */
  margin-bottom: 50px;
}

.main-text{
  display: block;
  width: 100%;
}

.footer-text {
  display: block;
  width: 100%;
  font-size: 12px; /* 调整字体大小 */
  color: #888; /* 调整字体颜色 */
  text-align: left;
  margin-bottom: 50px;
}

.comment{
  width:68%;
  margin: 0 auto;
}

>>> .custom-page-header .el-page-header__content {
    color: #ffffff; /* 设置你想要的颜色 */
}

.title{
  position: absolute;
  top:100px;
  font-size: 30px;
  font-weight: 500;
  color: rgb(255, 255, 255);
}

.other-info{
  position:absolute;
  top:205px;
  height:95px;
  width:40%;
  left: 60%;
}

.date{
    position: absolute;
    width:30%;
    left:25%;
    top:60px;
    color: white;
}
.date-text{
    position: absolute;
    width:100%;
    margin-top:6px;
    margin-left: 25px;

    font-size: 12px;
    font-weight: 700;
}
.date-img{
    width:25px;
    height: 25px;
    position: absolute;
    left:0%;
}
.like{
  display: flex;
  align-items: center;
    width:200px;
    margin: 0 auto;
    color: rgb(134, 132, 132);
}
.like-img{
    width:55px;
    height: 55px;
    position: relative;
    left:5%;
    top:0%;
    cursor: pointer;
    transition: transform 0.3s ease, opacity 0.3s ease;
}

.like-img:hover {
  transform: scale(1.1);
  opacity: 0.8;
}

.like-num{
    font-size: 30px;
    font-weight: 700;
    margin-left: 30px;
}

.tags{
  position: absolute;
  left:25%;
  top:19px;
  width:100%;
  height: 100%;
}
.tags-img{
  width:28px;
  height: 28px;
  position: absolute;
  left:0%;
}
.tags-list{
    position: absolute;
    display: flex;
    margin-left:38px;
    font-size: smaller;
    font-weight: 700;
    gap: 5px;
}

.category{
  position: absolute;
  left:55%;
  margin-left: 30px;
  width:15%;
  height: 100%;
  color: white;
  top:60px
}
.category-img{
  width:25px;
  height: 25px;
  position: absolute;
  left:0%;
}
.category-text{
  position: absolute;
  left:25px;
  margin-left:10px;
  margin-top:5px;
  font-size: 14px;
  font-weight: 700;
}


.avatar{
  position: absolute;
  left:0;
  margin-left: 80px;
  top:260px;
}

.user-name{
  position: absolute;
  left:0;
  margin-left: 180px;
  top:230px;
  color: #ffffff;
  font-size: 30px;
  font-weight: 1000;
}

</style>
