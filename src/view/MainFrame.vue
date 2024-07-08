<template>
  <div class="background">
    <div class="header">
      <div class="home">
        <el-button @click="goToMainFrame" style="background-color:#ffffff00;border: none; width: 40px; height:30px;margin-left: -40px;" >
            <img src="@/assets/home.png" alt="首页" style="width:25px;height: 25px;margin-top: 1px;">
            <span style="position: absolute;margin-top: 8px;margin-left: 5px; font-weight: 1000;">首页</span>
        </el-button>
      </div>

      <div class="search">
        <el-button style="background-color:#ffffff00;border: none; width: 40px; height:30px;margin-left: -40px;" >
            <img src="@/assets/search.png" alt="搜索" style="width:25px;height: 25px;margin-top: 1px;">
            <span style="position: absolute;margin-top: 8px;margin-left: 5px; font-weight: 1000;">搜索</span>
          </el-button>
      </div>
      <div class="write">
        <el-button style="background-color:#ffffff00;border: none; width: 40px; height:30px;margin-left: -40px;" >
            <img src="@/assets/write.png" alt="写博客" style="width:25px;height: 25px;margin-top: 1px;">
            <span style="position: absolute;margin-top: 8px;margin-left: 5px; font-weight: 1000;">写博客</span>
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

        <div v-if="showMain" class="welcome">
          <div style="position: absolute;">
              <h1>欢迎来到博客平台</h1>
              <h2>123456879456654654654</h2>
          </div>
          <!--<img style="position:absolute; width: 150px;height:150px;left: 70%;" src="@/assets/reading.png" alt="插画">-->
        </div>

     <userSetting v-if="showUserSetting" class="userSetting" />
     <div v-if="showMain" class="main" @scroll="handleScroll">
      <div id="check" style="position: absolute;top:0;height:60%;width:100%"></div>
        <div  class="container">
          <div class="user-style" ref="userInfo">
            <userInfo/>
          </div>

          <div class="tags-style" ref="tags">
             <tags @clickTag="showFilterArticles"/>
          </div>
          
          <div ref="filter_articles" v-if="showFilter" class="filter-article-list">
            <filterArticle  v-for="filter_article in filter_articles"  :key="filter_article.id" :filter_article="filter_article"/>
          </div>
            <div v-if="showAllarticle" ref="articles" class="article-list">
              <Article v-for="article in articles"  :key="article.id" :article="article" />
            </div>
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
export default{
name:'MainFrame',
components:{
  userInfo,
  userSetting,
  Article,
  filterArticle,
  tags,
},
data(){
    return{
      userData:null,
      showUserSetting:false,
      showAllarticle:true,
      showMain:true,
      showFilter: false, // 控制是否显示 filterArticle 组件
      articles: [
        {
          id:'1',
          title:"test1test1test1test1test1asdasd",
          text:"这是一段测试文字，这里是article组件的内容，内容是测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下",
          tags:["vue开发","测试标签"],
          author:"lh"
        },
        {
          id:'2',
          title:"test2",
          text:"这是一段测试文字，这里是article组件的内容，内容是啊八八八八八八八八吧阿八八八",
          tags:["springboot开发","扬州实习"]
        },
        {
          id:'3',
          title:"test3",
          text:"这是一段测试文字，这里是article组件的内容，内容是测试一下"
        },
        {
          id:'3',
          title:"test3",
          text:"这是一段测试文字，这里是article组件的内容，内容是啊八八八八八八八八吧阿八八八"
        },
        {
          id:'3',
          title:"test3",
          text:"这是一段测试文字，这里是article组件的内容，内容是啊八八八八八八八八吧阿八八八"
        },
        {
          id:'3',
          title:"test3",
          text:"这是一段测试文字，这里是article组件的内容，内容是啊八八八八八八八八吧阿八八八"
        },
        {
          id:'3',
          title:"test3",
          text:"这是一段测试文字，这里是article组件的内容，内容是啊八八八八八八八八吧阿八八八"
        },
        {
          id:'3',
          title:"test3",
          text:"这是一段测试文字，这里是article组件的内容，内容是啊八八八八八八八八吧阿八八八"
        },
        {
          id:'3',
          title:"test3",
          text:"这是一段测试文字，这里是article组件的内容，内容是啊八八八八八八八八吧阿八八八"
        },{
          id:'3',
          title:"test3",
          text:"这是一段测试文字，这里是article组件的内容，内容是啊八八八八八八八八吧阿八八八"
        },{
          id:'3',
          title:"test3",
          text:"这是一段测试文字，这里是article组件的内容，内容是啊八八八八八八八八吧阿八八八"
        },
      ],
      filter_articles:[
        {
          id:'1',
          title:"test1test1test1test1test1",
          text:"这是一段测试文字，这里是article组件的内容，内容是测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下测试一下",
          tags:["vue开发","测试标签"],
          author:"lh"
        },
        {
          id:'2',
          title:"test2",
          text:"这是一段测试文字，这里是article组件的内容，内容是啊八八八八八八八八吧阿八八八",
          tags:["vue开发","扬州实习"]
        },
        {
          id:'3',
          title:"test3",
          text:"这是一段测试文字，这里是article组件的内容，内容是测试一下",
          tags:["vue开发","哈哈哈"],
        },
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
      this.showMain = false;
      this.showUserSetting = true;
      this.showAllarticle = false;
      this.showFilter = false;
    },
    goToMainFrame(){
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
      console.log(checkRect.bottom);
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
    background-color: rgb(47, 48, 48); /* 悬浮时背景颜色加深 */
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
    left:30%;
    top:10%;
    width:60%;
    height:20%;
    z-index: 1;
  }

</style>
