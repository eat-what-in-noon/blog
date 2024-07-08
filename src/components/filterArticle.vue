<template>
    <div class="filter-article">
        <img class="filter-article-image" src="@/assets/test.png" alt="文章图片">
        <div class="filter-article-title">
            <p>{{ truncateTitle(filter_article.title) }}</p>
        </div>
        <!--<div class="article-author">
            <img class="author-img" src="@/assets/author.png" alt="作者图标">
            <p class="author-name">{{ article.author }}</p>
        </div>-->
        
            <div v-if="filter_article.tags" class="tags">
                <img class="tags-img" src="@/assets/tags.png" alt="标签图标">
                <div class="tags-list">
                    <el-tag type="info" v-for="(tag,index) in filter_article.tags" :key="index">{{ tag }}</el-tag>
                </div>
            </div>
            
           
            <div class="filter-artical-otherInfo">
            <div style="position: absolute;left:50%;width:50%;top:0;height:10%"></div>
                <div class="like">
                    <img src="@/assets/after_like.png" class="like-img" alt="点赞数图标">
                    <div class="like-num">205点赞</div>
                </div>

                <div class="comments">
                    <img src="@/assets/comments.png" class="comments-img" alt="评论数图标">
                    <div class="comments-num">600评论</div>
                </div>

                <div class="date">
                    <img src="@/assets/date.png" class="date-img" alt="日期图标">
                <div class="date-text">发布于 2024-7-7</div>
            </div>
            </div>
        </div>
    </div>
</template>

<script>
export default{
  name: 'filterArticle',
  props: {
    filter_article: Object
  },
  methods: {
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
    width: 50%;
    height:15%;
    top:20%;
    left:50%;
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
    height:10%; 
    left:70%;
    top:10px;
}
.date-text{
    position: absolute; 
    width:50%;
    padding-top:10px;
    font-size: smaller;
    font-weight: 700;
    padding-left: 25px;
    color: rgb(117, 121, 124);
    font-size: 12px;
}
.date-img{
    width:28px;
    height: 28px; 
    position: absolute;
    padding-top:2px; 
    left:0%;
}
.like{
    position: absolute; 
    width:30%;
    height: 100%; 
    left:10%;
    top:10px;
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
    padding-left:25px;
    top:10%;
    font-size: smaller;
    font-weight: 700;
    width:80%;
}

.comments{
    position: absolute; 
    width:30%;
    height: 100%; 
    left:40%;
    top:10px;
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
    padding-left:25px;
    top:13%;
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
    width:20px;
    height: 17px; 
    position: absolute; 
    left:8%;
    top:20%;
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