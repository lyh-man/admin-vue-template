<template>
    <el-submenu :index="menu.name">
        <template slot="title">
            <i :class="menu.meta.icon"></i>
            <span>{{$i18n.locale === 'zh' ? menu.meta.name_zh : menu.meta.name_en}}</span>
        </template>
        <el-menu-item v-for="subMenu in menu.children" :key="subMenu.meta.menuId" :index="subMenu.name" @click="$router.push({ name: subMenu.name })">
            <i :class="subMenu.meta.icon"></i>
            <span slot="title">{{$i18n.locale === 'zh' ? subMenu.meta.name_zh : subMenu.meta.name_en}}</span>
        </el-menu-item>
    </el-submenu>
</template>

<script>
    export default {
        name: 'DynamicMenu',
        props: {
            menu: {
                type: Object,
                required: true
            }
        }
    }
</script>

<style>
</style>