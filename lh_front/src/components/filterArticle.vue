<template>
    <div @click="showDetail()" class="filter-article">
      <div class="article-operation">
      <el-dropdown trigger="click">
        <el-button  @click.stop  style="background-color: rgba(255,255,255,0);border:none;width:40px;height:40px" v-if="showOperation">
          <img src="@/assets/articleOp.png" alt="文章操作" style="position:absolute;width:100%;height: 100%;left:0;top:0">
        </el-button>
        <el-dropdown-menu>
          <el-dropdown-item @click.native="updateArticle()">修改文章</el-dropdown-item>
          <el-dropdown-item @click.native="deleteArticle()">删除文章</el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
      </div>

      <img class="filter-article-image" :src="filter_article.cover ? this.baseURL + filter_article.cover : this.baseURL+'test.png' " alt="文章图片">
      <div class="filter-article-title">
            <p>{{ truncateTitle(filter_article.title) }}</p>
        </div>
        <!--<div class="article-author">
            <img class="author-img" src="@/assets/author.png" alt="作者图标">
            <p class="author-name">{{ article.author }}</p>
        </div>-->

            <div v-if="filter_article.tagNames" class="tags">
                <img class="tags-img" src="@/assets/tags.png" alt="标签图标">
                <div class="tags-list">
                    <el-tag type="info" v-for="(tag,index) in filter_article.tagNames" :key="index">{{ tag }}</el-tag>
                </div>
            </div>


            <div class="filter-artical-otherInfo">
            <div style="position: absolute;left:50%;width:50%;top:0;height:10%"></div>
                <div class="like">
                    <img src="@/assets/after_like.png" class="like-img" alt="点赞数图标">
                    <div class="like-num">{{filter_article.likeCnt}}</div>
                </div>

                <div class="comments">
                    <img src="@/assets/comments.png" class="comments-img" alt="评论数图标">
                    <div class="comments-num">{{filter_article.commentCnt}}</div>
                </div>

                <div class="date">
                    <img src="@/assets/date.png" class="date-img" alt="日期图标">
                <div class="date-text">{{ filter_article.createdDate }}</div>
            </div>
            </div>
    </div>
</template>

<script>
import showBlog from "./showBlog.vue";
import axios from "axios";

export default{
  name: 'filterArticle',
  computed: {
    showBlog() {
      return showBlog
    }
  },
  props: {
    filter_article: Object,
    showOperation: Boolean,
  },



  data() {
    return {
      baseURL: 'http://localhost:8090/static/cover/',
    }
  },
  created(){
    this.axios = axios.create({
      baseURL: 'http://localhost:8090', // 后端 API 的地址
      timeout: 5000 // 请求超时时间
    });
  },
  methods: {
        truncateTitle(text) {
            const maxLength = 40; // 设置最大显示字数
            if (text.length > maxLength) {
                return text.slice(0, maxLength) + '...'; // 超过最大字数时显示省略号
            } else {
                return text;
            }
        },
      showDetail(){
        sessionStorage.setItem('articleId', JSON.stringify(this.filter_article.id));
        this.$router.push('/showBlog');
      },
    deleteArticle(){
      this.axios({
        method: "delete",
        url: "/article/deleteArticle",
        data: {
          id:this.filter_article.id,
        },
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('token')
        }
      }).then((res) => {
        if (res.data.error_message === 'success') {
          console.log(res.data);
          window.location.reload();
          this.$message.success("文章删除成功");
        } else {
          // 登录失败处理
          this.$message({
            message: res.data.error_message,
            type: "warning",
          });
        }
      }).catch((error) => {
        // 请求失败处理
        console.error('文章删除出错', error);
        this.$message.error('出错误了');
      });
    },
    updateArticle(){
          sessionStorage.setItem("updateArticle", JSON.stringify(this.filter_article));
          this.$router.push('/textEditor');
    },

  }
}
</script>

<style scoped>
.filter-article{
    cursor: pointer;
    position: relative;
    width:800px;
    height:280px;
    border-radius: 10px;
    background-color: rgba(240, 240, 240, 0.795);
    overflow: hidden;
    margin-bottom: 50px; /* 垂直间距，根据需要调整 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3); /* 设置阴影效果 */
    transition: box-shadow 0.3s ease; /* 添加过渡效果 */
    transition: transform 0.3s ease; /* 添加过渡效果 */
}

.filter-article:hover {
    box-shadow: 0 8px 16px rgba(136, 133, 133, 0.6);; /* 悬浮时增加阴影效果 */
    transform: scale(1.02); /* 悬浮时微微放大 */
}

.filter-article-image{
    position:absolute;
    width: 50%;
    height:100%;
    top:0;
    left:0
}

.filter-article-title{
    position:absolute;
    width: 46%;
    height:15%;
    top:20px;
    left:52%;
    font-weight: 800;
    font-size: 25px;

}
/*.article-author{
    position:absolute;
    top:45%;
    height:15%;
    width:30%;
    left:76%;
}
.author-name{
    position:absolute;
    left:25%;
    top:12%;
    font-weight: 500;
    font-family: 'Courier New', Courier, monospace;
}
.author-img{
    position: absolute;
    width:15px;
    height:15px;
    left:0%;
    top:46%
}*/

.filter-artical-otherInfo{
    position:absolute;
    width: 50%;
    height:24%;
    left:48%;
    top:76%;
}
.date{
    position: absolute;
    width:50%;
  height:100%;
    left:70%;
}
.date-text{
    position: absolute;
    width:50%;
    top:20px;
    font-size: smaller;
    font-weight: 700;
    margin-left: 25px;
    color: rgb(117, 121, 124);
    font-size: 12px;
}
.date-img{
    width:28px;
    height: 28px;
    position: absolute;
    top:20px;
    left:0%;
}
.like{
    position: absolute;
    width:30%;
    height:100%;
    left:10%;
}
.like-img{
    width:30px;
    height: 30px;
    position: absolute;
    left:5%;
    top:20px;
}
.like-num{
    position: absolute;
    margin-left:5px;
    top:27px;
    font-size: smaller;
    font-weight: 700;
    width:80%;
}

.comments{
    position: absolute;
    width:30%;
  height:100%;
    left:40%;

}
.comments-img{
    width:32px;
    height: 32px;
    position: absolute;
    left:5%;
  top:18px;
}
.comments-num{
    position: absolute;
    margin-left:8px;
    top:26px;
    font-size: smaller;
    font-weight: 700;
    width:80%;
}

.tags{
    position: absolute;
    width:50%;
    height: 20%;
    left:50%;
    top:60%;
}
.tags-img{
    width:28px;
    height: 28px;
    position: absolute;
    left:8%;
    top:0px;
}
.tags-list{
    position: absolute;
    display: flex;
    left:16%;
    font-size: smaller;
    font-weight: 700;
    gap: 5px;
}

.article-operation{
  position:absolute;
  left:50%;
  width: 40px;
  height:40px;
  margin-left:360px;
  background-color: rgba(255, 255, 255, 0);
  border:none;
}


</style>
