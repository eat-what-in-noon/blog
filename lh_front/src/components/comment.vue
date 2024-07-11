<template>
    <div class="comment-area">
        <div class="comment-head">
            <i class="el-icon-edit-outline" style="font-weight: bold;font-size: 22px;margin-top: 2px;"></i> 留言
        </div>
        <div class="comment-box" >
            <textarea
                class="comment-textarea"
                v-model="currentContent"
                placeholder="写下点什么..."
                maxlength="1000"/>
                <el-button type="primary" style="position:relative;left:90%" @click="SubmitComment()">提交</el-button>
        </div>

        <div class="commentInfo-title">
          <span style="font-size: 1.15rem">Comments | </span>
          <span>{{ comments.length }} 条留言</span>
        </div>

        <div class="all-commentsInfo" v-if="comments.length > 0">
      <!-- 评论详情 -->
            <div id="comment-content" class="commentInfo-detail"
                v-for="(item, index) in comments"
                :key="index">
        <!-- 头像 -->
              <div @click="goToHome(item)">
            <el-avatar  shape="square" class="commentInfo-avatar" :size="35" :src="item.avatar ? baseURL + item.avatar : 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png' "></el-avatar>
              </div>
                <el-button @click="deleteComment(item)" class="delete-button" v-if="item.authorId === user.id">删除</el-button>
            <div style="flex: 1;padding-left: 12px">
          <!-- 评论信息 -->
                <div style="display: flex;justify-content: space-between">
                    <div @click="goToHome(item)">
                        <span class="commentInfo-username">{{ item.username }}</span>
                        <span class="commentInfo-other">{{ item.createdDate }}</span>
                    </div>
                </div>
          <!-- 评论内容 -->
                <div class="commentInfo-content">
                    {{ item.content }}
                </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
import article from "./Article.vue";

export default{
    name:'comment',
    data(){
        return{
            comments:[],
          articleId:'',
          user:{},
          comment:{
            content:'',
            authorId: '',
            articleId:'',
            createdDate:'',
            username:'',
            avatar:'',
          },
          currentContent: '',
          currentDate:'',
          baseURL:'http://localhost:8090/static/avatar/',
        }
    },
  created(){
    this.axios = axios.create({
      baseURL: 'http://localhost:8090', // 后端 API 的地址
      timeout: 5000 // 请求超时时间
    });
  },
  mounted() {
      this.articleId = sessionStorage.getItem("articleId");
    this.user = JSON.parse(localStorage.getItem('userInfo'));
    this.comment.authorId = this.user.id;
    this.comment.articleId = this.articleId;
    this.comment.username = this.user.username;
    this.comment.avatar = this.user.avatar;
    this.getComments();
  },
  methods:{
      getComments(){
        const url = "/article/getComment"
        this.axios({
          method:"get",
          url:url,
          params:{
            id:this.articleId,
          },
          headers: {
            'Authorization': 'Bearer '+ localStorage.getItem('token')
          }
        }).then((res) => {
          if (res.data.error_message === 'success') {
            this.comments = res.data.data.map((item, index) => {
              return {
                ...item,
                avatar: res.data.users[index].avatar,
                username: res.data.users[index].username,
              };
            });
            console.log("这是评论信息");
            console.log(this.comments);
          } else {
            // 登录失败处理
            this.$message({
              message: res.data.error_message,
              type: "warning",
            });
          }
        }).catch((error) => {
          // 请求失败处理
          console.error('获取评论失败', error);
          this.$message.error('出错误了');
        });
      },
      SubmitComment(){
        if(this.currentContent.trim().length === 0){
          this.$message.warning("评论不能为空");
          return false;
        }
        const url = "comment/addComment";
        this.axios({
          method:"post",
          url:url,
          data:{
            content: this.currentContent,
            authorId: this.comment.authorId,
            articleId: this.comment.articleId,
          },
          headers: {
            'Authorization': 'Bearer '+ localStorage.getItem('token')
          }
        }).then((res) => {
          if (res.data.error_message === 'success') {
            this.$message.success("提交评论成功！");
            const newComment = {
              content: this.currentContent,

              username:this.comment.username,
              avatar:this.comment.avatar,
            }
            this.currentContent = '';
            this.comments.push(newComment);
            console.log(newComment);

          } else {
            // 登录失败处理
            this.$message({
              message: res.data.error_message,
              type: "warning",
            });
          }
        }).catch((error) => {
          // 请求失败处理
          console.error('发送评论失败', error);
          this.$message.error('出错误了');
        });
      },

    getCurrentDate() {
      const now = new Date();
      const year = now.getFullYear(); // 获取年份
      const month = now.getMonth() + 1; // 获取月份（返回值是0-11，所以要加1）
      const day = now.getDate(); // 获取日期

      this.currentDate = `${year}-${this.pad(month)}-${this.pad(day)}`;
    },
    deleteComment(item){
       const url = "/comment/deleteComment";
      this.axios({
        method:"delete",
        url:url,
        data:{
          id:item.id,
        },
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('token')
        }
      }).then((res) => {
        if (res.data.error_message === 'success') {
          this.$message.success("删除评论成功！");
          let index = this.comments.indexOf(item);
          console.log("下标为"+index);
          if(index !== -1){
            this.comments.splice(index, 1);
          }

        } else {
          // 登录失败处理
          this.$message({
            message: res.data.error_message,
            type: "warning",
          });
        }
      }).catch((error) => {
        // 请求失败处理
        console.error('删除评论失败', error);
        this.$message.error('出错误了');
      });
    },
    goToHome(item){
        sessionStorage.setItem("identity", JSON.stringify(item.authorId));
        console.log("go to mainFram");
        this.$router.push('/MainFrame');
    },
  }
}
</script>

<style scoped>

.comment-head{
    display: flex;
    align-items: center;
    font-size: 20px;
    font-weight: bold;
    margin: 20px 0 20px 0;
    user-select: none;
}


.comment-textarea {
    border: solid 1px;
    width: 100%;
    font-size: 14px;
    padding: 15px 0 15px 8px;
    min-height: 180px;
    /* 不改变大小 */
    resize: none;
    border-radius: 14px;
    background-color:aliceblue;
    background-size: contain;
    background-repeat: no-repeat;
    background-position: 100%;
    margin-bottom: 10px;
  }

  .commentInfo-title{
    position: relative;
    color: gray;
    top:30px
  }

  .all-commentsInfo{
    position: relative;
    width: 100%;
    margin-top: 20px;
    top:30px;
  }

  #comment-content {
    border-bottom: 1px solid rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
  }

  .commentInfo-detail {
    display: flex;
  }

  .commentInfo-avatar {
    border-radius: 5px;
  }

  .commentInfo-username {
    color: orange;
    font-size: 16px;
    font-weight: 600;
    margin-right: 5px;
  }


  .commentInfo-other {
    font-size: 12px;
    color:gray;
    user-select: none;
  }


  .commentInfo-content {
    margin: 15px 0 25px;
    padding: 18px 20px;
    background-color: rgb(229, 239, 252);
    border-radius: 12px;
    color: black;
    word-break: break-word;
  }

.delete-button{
  position: absolute;
  width:40px;
  height:30px;
  background-color: orange;
  right:0%;
  display: flex;
  align-items: center;  /* 垂直居中 */
  justify-content: center;  /* 水平居中 */
  text-align: center;
  font-size: 13px;
}


</style>
