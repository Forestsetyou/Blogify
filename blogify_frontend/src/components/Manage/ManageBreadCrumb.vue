
<template>
  <el-breadcrumb style="margin-bottom: 20px" separator="/">
    <el-breadcrumb-item v-for="route in currentPath" :key="route.path" :to="route.path">{{route.name}}</el-breadcrumb-item>
  </el-breadcrumb>
</template>

<script>
export default {
  name: 'ManageBreadCrumb',
  data () {
    return {
      currentPath: this.$router.currentRoute.matched
    }
  },
  watch: {
    $route (to, from) {
      this.currentPath = this.$route.matched
    }
  }
}
</script>

<style scoped>

</style>
