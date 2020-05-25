<template>
  <div id="app">
    <router-view/>
  </div>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
/* 解决子组件中 height: 100% 不生效问题 */
html,body,#app{
    height: 100%;
    margin: 0;
    padding: 0;
    overflow: hidden;
}
</style>