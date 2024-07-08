<template>
    <div class="main">
        <el-card class="box-card tag_container">
            <div class="clearfix">
                <span class="card-title">标签墙</span>
            </div>
            <div class="tagBox">
                <span @click="handleClike"
                      :style="{ backgroundColor: tag.color, fontSize: tag.fontSize }"
                      class="tag-item hand-style" v-for="(tag, index) in tagList" :key="index">
                    {{ tag.name }}
                </span>
            </div>
        </el-card>
    </div>
</template>

<script>
export default {
    name: 'tags',
    data() {
        return {
            tagList: []
        }
    },
    mounted() {
        this.initializeTags();
    },
    methods: {
        initializeTags() {
            const staticTags = ['Vue', 'JavaScript', 'ElementUI', 'CSS', 'HTML', 'Node.js', 'React', 'Angular','test1','askdj','是打算肯德基','阿松大','扬州实习','第一天','打工人','美食','spring boot开发','武汉理工大学','可恶的蟑螂','哈哈哈哈哈哈'];
            this.tagList = staticTags.map(tag => ({
                name: tag,
                color: this.randomColor(),
                fontSize: this.randomFontSize()
            }));
        },
        randomColor() {
            const letters = '0123456789ABCDEF';
            let color = '#';
            for (let i = 0; i < 6; i++) {
                color += letters[Math.floor(Math.random() * 16)];
            }
            return color;
        },
        randomFontSize() {
            const minSize = 11; // 最小字体大小
            const maxSize = 15; // 最大字体大小
            return `${Math.floor(Math.random() * (maxSize - minSize + 1) + minSize)}px`;
        },
        handleClike() {
            this.$emit('clickTag');
        }
    }
}
</script>

<style scoped>
.main {
    position: relative;
    width: 300px;
    height: auto;
    background-color: rgb(255, 255, 255);
    border-radius: 20px;
    transition: box-shadow 0.3s ease; /* 添加过渡效果 */
    transition: transform 0.3s ease; /* 添加过渡效果 */
}
.main:hover {
    box-shadow: 0 8px 16px rgba(218, 214, 214, 0.6); /* 悬浮时增加阴影效果 */
    transform: scale(1.001); /* 悬浮时微微放大 */
}

.box-card {
    font-size: 16px;
    padding: 12px;
    background-color: var(--background-color);
    color: var(--article-color);
    margin-bottom: 15px;
}

.card-title {
    position: relative;
    padding-left: 40px;
    left: -90px;
    top: -25px;
}

.card-title::before,
.card-title::after {
    position: absolute;
    width: 12px;
    height: 12px;
    top: 4px;
    border-radius: 50%;
    content: '';
}

.card-title::before {
    left: 20px;
    background: #fc625d;
}

.card-title::after {
    left: 5px;
    background-color: #fdbc40;
}

.clearfix {
    position: relative;
    font-size: 16px;
    margin-bottom: 20px;
}

.clearfix::before {
    content: '';
    width: 25px;
    border: 2px solid #333;
    position: absolute;
    bottom: 10px;
    left: -20px;
    transition: width 0.5s;
}

.box-card:hover .clearfix::before {
    width: 70px;
}

.tagBox {
    padding-bottom: 5px; /* 减少底部内边距 */
    height: auto;
    margin-top: 5px;
    width: 100%; /* 设置宽度为100% */
    overflow: hidden; /* 超出部分隐藏 */
}

.tag-item {
    display: inline-block; /* 行内块显示 */
    margin: 3px; /* 减少标签之间的外边距 */
    padding: 5px 10px;
    border-radius: 5px;
    color: #ffffff;
    cursor: pointer;
    white-space: nowrap; /* 不换行 */
    transition: box-shadow 0.3s ease; /* 添加过渡效果 */
    transition: transform 0.3s ease; /* 添加过渡效果 */
}

.tag-item:hover {
    box-shadow: 0 8px 16px rgba(87, 84, 84, 0.6); /* 悬浮时增加阴影效果 */
    transform: scale(1.2); /* 悬浮时微微放大 */
}
</style>
