<template>
  <el-menu
    :default-active="currentPath"
    class="manage_menu"
    router
    mode="vertical"
    background-color="#545c64"
    text-color="#fff"
    active-text-color="#ffd04b"
    :collapse="isCollapse">
    <div style="height: 80px; text-align: left"></div>
      <el-menu-item v-for="item in manageMenu" :key="item.path" :index="item.path" style="font-size: 18px; text-align: left">
        <i :class="item.iconClass" style="font-size: 30px; margin-left: 15px"></i>
        <span style="margin-left: 15px">
          {{item.zhName}}
        </span>
      </el-menu-item>
  </el-menu>
</template>

<script>
export default {
  name: 'ManageMenu',
  data () {
    return {
      isCollapse: false
    }
  },
  computed: {
    manageMenu () {
      return this.$store.state.manageMenu
    },
    currentPath () {
      return this.$route.path
    }
  }
}
</script>

<style scoped>
.manage_menu {
  border-radius: 5px;
  height: 100%;
  min-height: 98vh;
}
</style>
