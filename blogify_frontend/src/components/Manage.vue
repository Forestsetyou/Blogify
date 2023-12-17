<template>
    <el-container id="manage_page_body" direction="vertical">
      <manage-header></manage-header>
      <el-container>
        <el-aside style="height: 110%; margin-top: -80px; width: 250px">
          <manage-menu></manage-menu>
        </el-aside>
        <el-main>
          <manage-bread-crumb></manage-bread-crumb>
          <router-view/>
        </el-main>
      </el-container>
    </el-container>
</template>

<script>
import ManageHeader from './Manage/ManageHeader.vue'
import ManageMenu from './Manage/ManageMenu.vue'
import ManageBreadCrumb from './Manage/ManageBreadCrumb.vue'

export default {
  name: 'Manage',
  components: {ManageMenu, ManageHeader, ManageBreadCrumb}
}
</script>

<style>
#manage_page{
  min-width: 1300px;
  min-height: 1300px;
}
body{
  margin: 0 0;
}
#manage_page_body{
  height: 100%;
  padding: 4px 4px;
  background: #f0f2f5;
  min-height: 100vh;
}
</style>
