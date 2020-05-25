<template>
    <div class="error-wrapper">
        <h2 class="not-found-title">404</h2>
        <p class="not-found-desc">抱歉！您访问的页面<em>失联</em>啦 ...</p>
        <el-button @click="$router.go(-1)">返回上一页</el-button>
        <el-button type="primary" class="not-found-btn-gohome" @click="$router.push({ name: 'Home' })">进入首页</el-button>
    </div>
</template>

<script>
    export default {}
</script>

<style>
    .error-wrapper {
        position: absolute;
        top: 0;
        right: 0;
        bottom: 0;
        left: 0;
        overflow: hidden;
    }

    .not-found-title {
        margin: 20px 0 15px;
        font-size: 10em;
        font-weight: 400;
        color: rgb(55, 71, 79);
    }

    .not-found-desc {
        margin: 0 0 30px;
        font-size: 26px;
        color: rgb(118, 131, 143);
    }

    .not-found-desc>em {
        font-style: normal;
        color: #ee8145;
    }

    .not-found-btn-gohome {
        margin-left: 30px;
    }
</style>