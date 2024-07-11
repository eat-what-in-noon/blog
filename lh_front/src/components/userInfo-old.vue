<template>
    <div @click="getDataByUser()" class="main">
        <div class="user">
          <div class="block">
            <el-avatar class="avator" :size="80" :src="this.baseURL+this.avatar" @error="errorHandler" alt="头像">
              <img src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"/>
            </el-avatar>
          </div>
            <div style="position:absolute;top:30%;left:50%;transform: translate(-50%);">
                <div class="username" style="font-size:18px">{{ this.username }}</div>
                <div style="position:absolute;left:-80%;top:150%;width:100%;height:100%">
                    <img src="@/assets/email.png" alt="邮箱" style="position:absolute;width:25px;left:-80%;height: 25px;margin-top: 6px;">
                    <div class="email" style="position:absolute; font-size: 14px; left:-5%;margin-top: 10px;">{{ this.email }}</div>
                </div>
            </div>
        </div>
        <div class="information">
            <div style="position: absolute;top: 70%;left:12%">
                <div >文章</div>
                <div>10</div>
            </div>
            <div style="position: absolute;top: 70%;left:45%">
                <div>标签</div>
                <div>5</div>
            </div>
            <div style="position: absolute;top: 70%;left:78%">
                <div >分类</div>
                <div style="font-size: medium;">2</div>
            </div>
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
            categoryNum:'',
            avatar:'',
            baseURL:'http://localhost:8090/static/avatar/',
        }
    },

    created(){
        const storedUser = localStorage.getItem("userInfo");
        const user = JSON.parse(storedUser);
        this.username = user.username;
        //this.introduction = user.introduction;
        this.email = user.email;
        this.avatar = user.avatar;
    },
  methods:{
    errorHandler() {
      return true
    },
  },
}
</script>

<style scoped>
.main{
    position: relative;
    width: 300px;
    height: 250px;
    background-color: rgb(232, 236, 236);
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
