<template>
    <div @click="showDetail()" class="article">
        <img class="article-image" :src="article.cover ? this.baseURL + article.cover : this.baseURL+'test.png' " alt="文章图片">
        <div class="article-title">
            <p>{{ truncateTitle(article.title) }}</p>
        </div>
        <!--<div class="article-author">
            <img class="author-img" src="@/assets/author.png" alt="作者图标">
            <p class="author-name">{{ article.author }}</p>
        </div>-->
        <div class="artical-otherInfo">
            <div v-if="article.tagNames" class="tags">
                <img class="tags-img" src="@/assets/tags.png" alt="标签图标">
                <div class="tags-list">
                    <el-tag type="info" v-for="(tag,index) in article.tagNames.slice(0,3)" :key="index">{{ tag }}</el-tag>
                </div>
            </div>
            <div style="position: absolute;width:100%;top:50%;height:50%">
                <div class="date">
                    <img src="@/assets/date.png" class="date-img" alt="日期图标">
                    <div class="date-text">{{ article.createdDate }}</div>
                </div>
                <div class="like">
                    <img src="@/assets/after_like.png" class="like-img" alt="点赞数图标">
                    <div class="like-num"> {{article.likeCnt}}</div>
                </div>

                <div class="comments">
                    <img src="@/assets/comments.png" class="comments-img" alt="评论数图标">
                    <div class="comments-num">{{article.commentCnt}}</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default{
  name: 'Article',
  props: {
    article: Object,
  },
  data() {
    return {
      baseURL: 'http://localhost:8090/static/cover/',
    }
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
            sessionStorage.setItem('articleId', JSON.stringify(this.article.id));
            this.$router.push('/showBlog');
        }
    }
}
</script>

<style scoped>
.article{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    position: relative;
    width:310px;
    height:280px;
    border-radius: 20px;
    background-color: rgba(240, 240, 240, 0.795);
    overflow: hidden;
    margin-bottom: -100px; /* 垂直间距，根据需要调整 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3); /* 设置阴影效果 */
    transition: box-shadow 0.3s ease; /* 添加过渡效果 */
    transition: transform 0.3s ease; /* 添加过渡效果 */
}

.article:hover {
    box-shadow: 0 8px 16px rgba(136, 133, 133, 0.6);; /* 悬浮时增加阴影效果 */
    transform: scale(1.02); /* 悬浮时微微放大 */
}

.article-image{
    position:absolute;
    width: 100%;
    height:50%;
    top:0;
}

.article-title{
    position:absolute;
    width: 100%;
    height:15%;
    top:48%;
    font-weight: 800;
    font-size: 18px;
}
.artical-otherInfo{
    position:absolute;
    width: 100%;
    height:24%;
    top:76%;
}
.date{
    position: absolute;
    width:40%;
    height: 100%;
    left:5%;
    top:8%;
}
.date-text{
    position: absolute;
    width:100%;
    margin-left: 25px;
    padding-top:6px;
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
    position: absolute;
    width:20%;
    height: 100%;
    left:57%;
}
.like-img{
    width:25px;
    height: 25px;
    position: absolute;
    left:5%;
    margin-top:2px;
}
.like-num{
    position: absolute;
    padding-left: 35px;
    padding-top:8px;
    font-size: smaller;
    font-weight: 700;
}

.comments{
    position: absolute;
    width:20%;
    height: 100%;
    left:75%;
}
.comments-img{
    width:28px;
    height: 28px;
    position: absolute;
    left:5%;
    top:0%;
  margin-top:1px;
}
.comments-num{
    position: absolute;
    margin-left: 40px;
    margin-top:8px;
    font-size: smaller;
    font-weight: 700;
}

.tags{
    position: absolute;
    width:100%;
    height: 50%;
    top:-5%;
}
.tags-img{
    width:25px;
    height: 25px;
    position: absolute;
    left:5%;
    top:5px;
}
.tags-list{
    position: absolute;
    display: flex;
    left:16%;
    font-size: smaller;
    font-weight: 700;
    gap: 5px;
}


</style>
