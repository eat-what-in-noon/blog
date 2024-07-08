<template>
    <div class="article">
        <img class="article-image" src="@/assets/test.png" alt="文章图片">
        <div class="article-title">
            <p>{{ truncateTitle(article.title) }}</p>
        </div>
        <!--<div class="article-author">
            <img class="author-img" src="@/assets/author.png" alt="作者图标">
            <p class="author-name">{{ article.author }}</p>
        </div>-->
        <div class="article-text">
            <p>{{ truncateText(article.text) }}</p>
        </div>
        <div class="artical-otherInfo">
            <div v-if="article.tags" class="tags">
                <img class="tags-img" src="@/assets/tags.png" alt="标签图标">
                <div class="tags-list">
                    <el-tag type="info" v-for="(tag,index) in article.tags" :key="index">{{ tag }}</el-tag>
                </div>
            </div>
            <div style="position: absolute;width:100%;top:50%;height:50%">
                <div class="date">
                    <img src="@/assets/date.png" class="date-img" alt="日期图标">
                    <div class="date-text">2024-7-7</div>
                </div>
                <div class="like">
                    <img src="@/assets/after_like.png" class="like-img" alt="点赞数图标">
                    <div class="like-num">205</div>
                </div>

                <div class="comments">
                    <img src="@/assets/comments.png" class="comments-img" alt="评论数图标">
                    <div class="comments-num">600</div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default{
  name: 'Article',
  props: {
    article: Object
  },
  methods: {
    truncateText(text) {
            const maxLength = 40; // 设置最大显示字数
            if (text.length > maxLength) {
                return text.slice(0, maxLength) + '...'; // 超过最大字数时显示省略号
            } else {
                return text;
            }
        },
        truncateTitle(text) {
            const maxLength = 15; // 设置最大显示字数
            if (text.length > maxLength) {
                return text.slice(0, maxLength) + '...'; // 超过最大字数时显示省略号
            } else {
                return text;
            }
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
    background-color: rgba(219, 181, 181, 0.795);
    overflow: hidden;
    margin-bottom: -100px; /* 垂直间距，根据需要调整 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3); /* 设置阴影效果 */
    transition: box-shadow 0.3s ease; /* 添加过渡效果 */
    transition: transform 0.3s ease; /* 添加过渡效果 */
}

.article:hover {
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.6); /* 悬浮时增加阴影效果 */
    transform: scale(1.02); /* 悬浮时微微放大 */
}

.article-image{
    position:absolute; 
    width: 100%;
    height:45%;
    top:0;
}

.article-title{
    position:absolute; 
    width: 100%;
    height:15%;
    top:42%;
    font-weight: 800;
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
.article-text{
    position:absolute; 
    width: 85%;
    height:20%;
    top:54%;
    font-weight: 200;
    font-size: smaller;
    overflow: hidden;
    text-overflow: ellipsis;
    display: -webkit-box;
    -webkit-line-clamp: 3; /* 控制显示行数 */
    -webkit-box-orient: vertical;
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
    left:30%;
    top:9%;
    font-size: smaller;
    font-weight: 700;
}
.date-img{
    width:25px;
    height: 25px; 
    position: absolute; 
    left:5%;
}
.like{
    position: absolute; 
    width:20%;
    height: 100%; 
    left:45%;
}
.like-img{
    width:25px;
    height: 25px; 
    position: absolute; 
    left:5%;
    top:0%;
}
.like-num{
    position: absolute; 
    left:60%;
    top:15%;
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
    width:30px;
    height: 25px; 
    position: absolute; 
    left:5%;
    top:8%;
}
.comments-num{
    position: absolute; 
    left:60%;
    top:15%;
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
    height: 20px; 
    position: absolute; 
    left:8%;
    top:11%;
}
.tags-list{
    position: absolute; 
    display: flex;
    left:20%;
    font-size: smaller;
    font-weight: 700;
    gap: 5px;
}


</style>