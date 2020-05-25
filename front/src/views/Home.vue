<template>
    <!-- 
        v-loading、element-loading-text、element-loading-background、element-loading-spinner 用于定义加载的样式
     -->
    <el-container class="container" v-loading="false" element-loading-text="拼命加载中" element-loading-background="rgba(0, 0, 0, 0.8)"
     element-loading-spinner="el-icon-loading">
        <!-- 侧边栏 -->
        <Aside :foldAside="foldAside" />
        <!-- 
            direction="vertical"  用于垂直布局
        -->
        <el-container direction="vertical">
            <!-- 头部导航栏 -->
            <Header @foldOrOpenAside="foldOrOpen" />
            <!-- 内容 -->
            <Content />
        </el-container>
    </el-container>
</template>

<script>
    import Header from '@/views/home/Header.vue'
    import Aside from '@/views/home/Aside.vue'
    import Content from '@/views/home/Content.vue'

    export default {
        name: 'Home',
        components: {
            Header,
            Aside,
            Content
        },
        data() {
            return {
                foldAside: true
            }
        },
        methods: {
            foldOrOpen(data) {
                this.foldAside = data
            }
        }
    }
</script>
<style>
    .container {
        height: 100%;
    }
</style>