<script src="../../../zrgj-old/src/router/index.js"></script>
<template>
  <el-container class="background">
    <video autoplay muted loop id="background-video">
      <source src="@/assets/background.mp4" type="video/mp4">
      Your browser does not support the video tag.
    </video>


    <el-header class="header">
      <el-page-header @back="goBack" content="文章编写">
      </el-page-header>
    </el-header>


    <el-main class="main">
      <div >
        <el-form :model="article" :rules="rules" ref="articleForm" label-width="100px" class="article">
          <el-form-item label="添加标题" prop="title">
            <el-input class="ui-title" v-model="article.title"></el-input>
          </el-form-item>

          <el-form-item v-if="!isUpdate" label="选择分类" prop="category">
            <el-select class="ui-category" v-model="article.category" placeholder="请选择文章分类">
              <el-option label="欧洲杯" value="1"></el-option>
              <el-option label="娱乐" value="2"></el-option>
              <el-option label="游戏" value="3"></el-option>
              <el-option label="体育" value="4"></el-option>
              <el-option label="教育" value="5"></el-option>
              <el-option label="文化" value="6"></el-option>
              <el-option label="科技" value="7"></el-option>
              <el-option label="财经" value="8"></el-option>
              <el-option label="国际" value="9"></el-option>
              <el-option label="生活" value="10"></el-option>
              <el-option label="汽车" value="11"></el-option>
              <el-option label="摄影" value="12"></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="文章内容" prop="content">
            <quill-editor class="ui-content" v-model="article.content" :options="editorOptions"></quill-editor>
          </el-form-item>

          <el-form-item v-if="!isUpdate" label="添加标签" prop="tags">
            <div class="ui-tags">
              <el-row class="tags">
              <el-tag
                :key="tag"
                v-for="tag in article.tags"
                closable
                :disable-transitions="false"
                @close="handleClose(tag)">
                {{tag}}
              </el-tag>
              <el-input
                class="input-new-tag"
                v-if="inputVisible"
                v-model="inputValue"
                ref="saveTagInput"
                size="small"
                @keyup.enter.native="handleInputConfirm"
                @blur="handleInputConfirm">
              </el-input>
              <el-button v-else class="button-new-tag" size="small" @click="showInput">+ New Tag</el-button>
              </el-row>
              <el-row class="tags">
                <el-tag
                  :key="tag"
                  v-for="tag in modelTags"
                  :disable-transitions="false"
                  @click="handleClick(tag)">
                  {{tag}}
                </el-tag>
              </el-row>
            </div>
          </el-form-item>

          <el-form-item label="文章封面" prop="cover">

            <el-upload
              class="upload-cover"
              drag
              action="http://localhost:8090/article/uploadCover"
              name="cover"
              :headers="headerObj"
              :show-file-list="false"

              list-type="picture"
              :on-change="handleAvatarChangeIcon"
              :on-success="handleAvatarSuccess"
              ref="uploadCover">
              <img v-if="article.cover" :src="baseURL + article.cover" class="cover">
              <div v-else>
                <i  class="el-icon-upload"></i>
                <div  class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              </div>
              <div class="el-upload__tip" slot="tip">只能上传jpg/png文件，且不超过10Mb</div>
            </el-upload>

          </el-form-item>
        </el-form>
      </div>
      <div >
        <el-button @click="save()" type="primary" class="save-button">发布</el-button>
      </div>
    </el-main>

  </el-container>

</template>

<script>
import { quillEditor } from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'
import axios from "axios";
import article from "./Article.vue";

export default {
  components: {
    quillEditor
  },

  data() {
    return {
      headerObj:{
        'Authorization': 'Bearer '+ localStorage.getItem('token')
      },
      article: {
        id:'',
        title: '',
        content: '',
        category: '',
        tags: [],
        cover:'',
      },
      //是否更改旧文章
      isUpdate: false,

      baseURL: 'http://localhost:8090/static/cover/',
      editorHeight: '600px',
      inputVisible: false,
      inputValue: '',
      modelTags:[],
      editorOptions: {
        theme: 'snow',
        language: 'zh', // 设置编辑器语言为中文
        toolbar: [
          ['bold', 'italic', 'underline', 'strike'],
          [{ 'list': 'ordered'}, { 'list': 'bullet' }],
          ['link', 'image'],
          ['clean']
        ],
      },
      rules: {
        title: [
          { required: true, message: '请输入文章名称', trigger: 'blur' },
          { min: 5, max: 50, message: '长度在 5 到 50 个字符', trigger: 'blur' }
        ],
        category: [
          { required: true, message: '请选择文章种类', trigger: 'change' }
        ],
      }
    }
  },
  created(){
    this.axios = axios.create({
      baseURL: 'http://localhost:8090', // 后端 API 的地址
      timeout: 5000 // 请求超时时间
    });
  },
  mounted() {
    let updateArticle = JSON.parse(sessionStorage.getItem("updateArticle"));
    // 检查是否存在 'updateArticle' item
    if (updateArticle !== null) {
      console.log("'updateArticle' item 存在");
      // 如果存在，可以在这里添加你的逻辑
      this.isUpdate = true;
      this.article.id = updateArticle.id;
      this.getArticle();
    } else {
      console.log("'updateArticle' item does not exist");
      this.isUpdate = false;
    }
// 删除 'updateArticle' item
    sessionStorage.removeItem("updateArticle");



    this.axios({
      method:"get",
      url:"/tag/getTag",
      data:'',
      headers: {
        'Authorization': 'Bearer '+ localStorage.getItem('token')
      }
    }).then((res) => {
      if (res.data.error_message === 'success') {
        this.modelTags = res.data.data;
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
      console.error('请求标签出错', error);
      this.$message.error('出错误了');
    });

  },
  methods: {
    getArticle(){
      this.axios({
        method:"get",
        url:"/article/getArticleInfoById",
        params:{
          id:this.article.id,
        },
        headers: {
          'Authorization': 'Bearer '+ localStorage.getItem('token')
        }
      }).then((res) => {
        if (res.data.error_message === 'success') {
          this.article = res.data.data;
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
        console.error('获取文章出错', error);
        this.$message.error('出错误了');
      });
    },
    goBack() {
      console.log('go back');
      this.$router.push('/MainFrame');
    },
    adjustEditorHeight() {
      // 获取编辑器内容的实际高度
      const editor = this.$refs.quillEditor.quill;
      const editorHeight = editor.container.scrollHeight + 'px';

      // 设置编辑器的高度
      this.editorHeight = editorHeight;
    },
    handleClose(tag) {
      this.article.tags.splice(this.article.tags.indexOf(tag), 1);
      if(this.modelTags.indexOf(tag) === -1){
        this.modelTags.push(tag);
      }
    },

    showInput() {
      this.inputVisible = true;
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus();
      });
    },

    handleInputConfirm() {
      let inputValue = this.inputValue;
      if (inputValue && this.article.tags.indexOf(inputValue) === -1) {
        this.article.tags.push(inputValue);
        if(this.modelTags.indexOf(inputValue) !== -1)
          this.modelTags.splice(this.modelTags.indexOf(inputValue), 1);
      }else if(this.article.tags.indexOf(inputValue) !== -1){
        this.$message.warning("标签添加重复");
      }
      this.inputVisible = false;
      this.inputValue = '';
    },

    handleClick(tag){
      let index = this.modelTags.indexOf(tag);
      this.modelTags.splice(index, 1);
      if(this.article.tags.indexOf(tag) === -1) {
        this.article.tags.push(tag);
      }
    },
    save(){
      if(this.isUpdate){
        if(!this.article.title || !this.article.content){
          console.error('表单验证失败');
          this.$message.error("请完成格式要求");
          return false;
        }
        console.log("this.article为");
        console.log(this.article);
        this.axios({
          method: "put",
          url: "/article/changeArticle",
          data: {
            id: this.article.id,
            title: this.article.title,
            content: this.article.content,
            cover: this.article.cover,
          },
          headers: {
            'Authorization': 'Bearer ' + localStorage.getItem('token')
          }
        }).then((res) => {
          if (res.data.error_message === 'success') {
            // 更改成功处理
            this.$message({
              message: "文章更改成功",
              type: "success",
            });

          } else {
            // 登录失败处理
            this.$message({
              message: res.data.error_message,
              type: "warning",
            });
            return false;
          }
        }).catch((error) => {
          // 请求失败处理
          console.error('发布请求失败:', error);
          this.$message.error('发布失败，请稍后重试');
          return false;
        });
        this.$router.push('/MainFrame');
      }else{
        this.$refs.articleForm.validate((valid) => {
          if(!valid){
            console.error('表单验证失败');
            this.$message.error("请完成格式要求");
            return false;
          }
          console.log(this.article);
          const user = JSON.parse(localStorage.getItem('userInfo'));
          const article = {
            title: this.article.title,
            content: this.article.content,
            authorId: user.id,
            categoryId: this.article.category,
            cover: this.article.cover,
          }
          let articleId = 0;
          console.log(localStorage.getItem('token'));
          this.axios({
            method: "post",
            url: "/article/addArticle",
            data: article,
            headers: {
              'Authorization': 'Bearer ' + localStorage.getItem('token')
            }
          }).then((res) => {
            if (res.data.error_message === 'success') {
              articleId = res.data.data;
              this.article.tags.forEach((tag, index) => {
                const tagAdd = {
                  tagName: tag,
                  articleId: articleId,
                };
                this.axios({
                  method: 'post',
                  url: '/article/addTagToArticle',
                  data: tagAdd,
                  headers: {
                    'Authorization': 'Bearer ' + localStorage.getItem('token')
                  }
                })
                  .then((res) => {
                    if (res.data.error_message === 'success') {
                      console.log("标签添加成功");
                    } else {
                      this.$message.error(res.data.error_message);
                    }
                  })
                  .catch((error) => {
                    console.log("标签添加失败")
                    this.$message.error("标签添加失败");
                  });
              });
              // 上传成功处理
              this.$message({
                message: "发布成功",
                type: "success",
              });

            } else {
              // 登录失败处理
              this.$message({
                message: res.data.error_message,
                type: "warning",
              });
              return false;
            }
          }).catch((error) => {
            // 请求失败处理
            console.error('发布请求失败:', error);
            this.$message.error('发布失败，请稍后重试');
            return false;
          });
          this.$router.push('/MainFrame');
        })
      }

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
      this.imageUrl = URL.createObjectURL(file.raw);
      this.article.cover = res.data;
      console.log(this.article.cover);
    },
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
}

#background-video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover; /* 确保视频覆盖整个背景 */
  z-index: -1;
}

.header {
  background-color: #75a694;
  height: 6%;
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: flex-start; /* 左对齐 */
  padding-left: 10px; /* 通过 padding 来向右偏移一点点 */
  font-size: 24px;
  font-weight: bold;
  z-index: 1; /* 确保在视频上层 */
}


.main {
  background-color: rgba(255, 255, 255, 0.8); /* 透明背景色 */
  width: 100%;
  height: 94%;
  padding: 20px;
  -ms-overflow-style: none;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 1; /* 确保在视频上层 */
}

.main::-webkit-scrollbar {
  display: none;  /* 适用于 Chrome, Safari 和 Opera */
}

.article {
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* 左对齐 */
}

.ui-title {
  width: 100%;
  min-width: 1200px;
  max-width: 1200px;
}

.ui-category {
  width: 100%;
  min-width: 1200px;
  max-width: 1200px;
}

.ui-content {
  min-width: 1200px;
  max-width: 1600px;
  min-height: 400px;
  padding: 10px; /* 添加内边距 */
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
}
.tags{
  display: flex;
  justify-content: flex-start; /* 子组件靠左对齐 */
  align-items: flex-start; /* 子组件靠顶部对齐（可选） */
  /* 如果需要让子组件在换行时也靠左对齐，可以添加以下属性 */
  flex-wrap: wrap;
  padding: 10px;
}

>>> .el-tag{
  margin-bottom: 10px;
}

.ui-tags{

}

::v-deep .ql-container {
  width: 100%; /* 设置编辑器容器宽度为100% */
  background-color: #ffffff; /* 设置编辑器容器背景颜色为白色 */
}

::v-deep .ql-editor {
  min-height: 350px; /* 设置编辑器内容的最小高度 */
}

.el-tag + .el-tag {
  margin-left: 10px;
}
.button-new-tag {
  margin-left: 10px;
  height: 32px;
  line-height: 30px;
  padding-top: 0;
  padding-bottom: 0;
}
.input-new-tag {
  width: 90px;
  margin-left: 10px;
  vertical-align: bottom;
}


.save-button {
  background-color: #75a694;
  width: 400px;

  color: #fff; /* 设置按钮文字颜色为白色 */
}

.cover{
  width:100%;
  height: 100%;

}

</style>
