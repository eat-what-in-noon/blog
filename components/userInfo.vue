<template>
    <div class="main">
        <div class="user">
            <img class="avator" src="@/assets/qq.png" alt="头像">
            <div style="position:absolute;top:30%;left:50%;transform: translate(-50%);">
                <div class="username" style="font-size:18px">{{ this.username }}</div>
                <div style="position:absolute;top:150%;width:100%">
                    <img src="@/assets/email.png" alt="邮箱" style="position:absolute;width:25px;left:-80%;height: 25px;margin-top: 6px;">
                    <div class="email" style="position:absolute; font-size: 14px; left:-15%;margin-top: 10px;">{{ this.email }}</div>
                </div>
            </div>
        </div>
        <div class="information">
            <div style="position: absolute;top: 60%;left:12%">
                <div >文章</div>
                <div>10</div>
            </div>
            <div style="position: absolute;top: 60%;left:45%">
                <div>标签</div>
                <div>5</div>
            </div>
            <div style="position: absolute;top: 60%;left:78%">
                <div >分类</div>
                <div style="font-size: medium;">2</div>
            </div>
        </div>
        <div>
            <el-button type="success" style="position: absolute;top:80%;width:76%;left:12%;opacity: 1;text-align: center;">关注我！</el-button>
        </div>
    </div>
</template>

<script>
export default{
    data() {
        return{
            username:'',
            //introduction:'',
            passageNum:'',
            email:'',
            tagNum:'',
            categoryNum:''
        }
    },

    created(){
        const storedUser = localStorage.getItem("userInfo");
        const user = JSON.parse(storedUser);
        this.username = user.username;
        //this.introduction = user.introduction;
        this.email = user.email;
    }
}
</script>

<style scoped>
.main{
    position: relative;
    width: 18%;
    height: 45%;
    background: linear-gradient(rgb(198, 174, 209), blue); /* 标准的语法 */
    /*background-color: rgba(240, 248, 255, 0.74);*/
    opacity: 1;
    border-radius:15px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3); /* 设置阴影效果 */
    transition: box-shadow 0.3s ease; /* 添加过渡效果 */
    transition: transform 0.3s ease; /* 添加过渡效果 */
}

.main:hover {
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.6); /* 悬浮时增加阴影效果 */
    transform: scale(1.02); /* 悬浮时微微放大 */
}

.avator{
    position: absolute;
    width:50px;
    height: 50px;
    top: 15%;
    left: 50%; /* 距离左侧50% */
    transform: translate(-50%, -50%); /* 使用负边距将元素居中 */
}

.user{
    width:18%;
}


</style>
