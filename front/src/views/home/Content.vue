<template>
    <el-main class="content">
        <Tab v-if="$route.meta.isTab"></Tab>
        <el-card v-else class="card" shadow="hover">
            <keep-alive>
                <router-view />
            </keep-alive>
        </el-card>
    </el-main>
</template>

<script>
    import Tab from '@/views/home/Tab.vue'
    export default {
        name: 'Content',
        components:{
            Tab
        }
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