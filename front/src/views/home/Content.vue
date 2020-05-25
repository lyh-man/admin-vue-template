<template>
    <el-main class="content">
        <el-card class="card" shadow="hover">
            <keep-alive>
                <router-view />
            </keep-alive>
        </el-card>
    </el-main>
</template>

<script>
    export default {
        name: 'Content'
    }
</script>

<style>
    .content {
        background-color: #f1f4f5;
    }
    .card {
        height: 100%;
    }
</style>