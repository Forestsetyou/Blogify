<template>
  <el-card>
    <el-form v-model="formData" style="text-align: left" ref="dataForm">
      <el-row>
        <el-col :span="10">
          <el-form-item label="头像" :label-width="formLabelWidth" prop="avatar">
            <div>
              <el-image style="max-width: 180px" :src="formData.blogFile.publishUrl" />
            </div>
          </el-form-item>
          <el-form-item label="头像URL" :label-width="formLabelWidth" prop="id">
            <el-input v-model="formData.blogFile.publishUrl" autocomplete="off" :placeholder="formData.blogFile.publishUrl"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="14">
          <el-form-item label="UID" :label-width="formLabelWidth" prop="id">
            <el-input v-model="formData.id" autocomplete="off" :placeholder="formData.id" disabled></el-input>
          </el-form-item>
          <el-form-item label="用户名" :label-width="formLabelWidth" prop="username">
            <el-input v-model="formData.username" autocomplete="off" :placeholder="formData.username"></el-input>
          </el-form-item>
          <el-form-item label="昵称" :label-width="formLabelWidth" prop="nickname">
            <el-input v-model="formData.nickname" autocomplete="off" :placeholder="formData.nickname"></el-input>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="性别" :label-width="formLabelWidth" prop="gender">
            <el-input v-model="formData.gender" autocomplete="off" :placeholder="formData.gender"></el-input>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="角色" :label-width="formLabelWidth" prop="roleId">
            <el-radio-group v-model="formData.roleId">
              <el-radio-button :label="1" border>管理员</el-radio-button>
              <el-radio-button :label="2" border>普通用户</el-radio-button>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item label="邮箱" :label-width="formLabelWidth" prop="email">
        <el-input v-model="formData.email" autocomplete="off" :placeholder="formData.email"></el-input>
      </el-form-item>
      <el-form-item label="个人简介" :label-width="formLabelWidth" prop="introductions">
        <el-input v-model="formData.introductions" autocomplete="off"
                  :placeholder="formData.introductions" type="textarea" rows="4"></el-input>
      </el-form-item>
    </el-form>
    <el-button type="primary" @click="editFormSubmit">提交</el-button>
  </el-card>
</template>

<script>
export default {
  name: 'ManageUserInfo',
  data () {
    return {
      formData: {
        blogFile: {}
      },
      formLabelWidth: '120px'
    }
  },
  created () {
    this.initFromData()
  },
  methods: {
    initFromData () {
      this.formData = this.$store.state.user
    },
    editFormSubmit () {
      this.$confirm('确认提交?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消'
      }).then(() => {
        let elLoading = this.$loading({
          text: '正在更新',
          background: 'rgba(0, 0, 0, 0.8)',
          spinner: 'el-icon-loading'
        })
        this.$axios.put('/user/' + this.formData.id, this.formData).then(resp => {
          elLoading.close()
          if (resp.data.code === 200) {
            this.$message.success('更新成功!')
          } else {
            this.$message.error('更新失败!')
          }
        }).catch(failResp => {
          elLoading.close()
          console.log(failResp)
        })
      }).catch(failResp => {
        console.log(failResp)
      })
    }
  }
}
</script>

<style>
</style>
