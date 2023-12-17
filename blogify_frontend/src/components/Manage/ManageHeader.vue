<template>
  <el-header style="z-index: 1; height: 80px; margin-left: -20px">
    <el-card class="manage_header_card">
      <router-link to="/index">
        <img src="../../assets/img/Mini/Blogify-Logo.png" alt="Blogify-Logo" width="65px" style="float: left; margin-top: -13px" />
      </router-link>
      <span style="font-size: 32px; font-weight: bold; position: absolute; left: 120px">Blogify</span>
      <i class="el-icon-switch-button" @click="logout" style="font-size: 40px; float: right; cursor: pointer; outline: 0;" />
    </el-card>
  </el-header>
</template>

<script>
// import {createRoute} from 'vue-router/src/util/route'

export default {
  name: 'ManageHeader',
  methods: {
    logout () {
      this.$confirm('确认退出登录?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.get('/user/logout').then(resp => {
          if (resp && resp.data.code === 200) {
            this.$store.commit('logout')
            this.$store.commit('refreshMenu')
            // **
          }
          this.$router.replace('/index')
          this.$message.info('已退出登录')
        }).catch(failResp => {
          this.$message.info('已取消')
        })
      })
    }
  }
}

</script>

<style scoped>
  .manage_header_card {
    height: 80px;
    opacity: 0.85;
    line-height: 40px;
    min-width: 900px;
  }
</style>
