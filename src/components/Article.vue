<template>
    <div class="article">
        <img class="article-image" src="@/assets/test.png" alt="文章图片">
        <div class="article-text">
            <p>{{ article.text }}</p>
        </div>
        <div class="artical-otherInfo">
            <p>{{ article.text }}</p>
        </div>
    </div>
</template>

<script>
export default{
  name: 'Article',
  props: {
    article: Object
  },
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
    width:90%;
    height:35%;
    border-radius: 20px;
    background-color: rgba(241, 241, 241, 0.795);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3); /* 设置阴影效果 */
    overflow: hidden;
    transition: box-shadow 0.3s ease; /* 添加过渡效果 */
}

.article:hover {
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.6); /* 悬浮时增加阴影效果 */
}

.article-image{
    position:absolute; 
    width: 100%;
    height:50%;
    top:0;

}

.article-text{

}

.artical-otherInfo{

}
</style>