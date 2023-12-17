<template>
  <div style="text-align: center">
    <search-bar style="display: inline-block" @inputSearch="inputSearch"></search-bar>
    <el-dialog
        top="5vh"
        title="编辑用户信息"
        :visible.sync="editFormVisible">
      <el-form v-model="formData" style="text-align: left" ref="dataForm">
        <el-row>
          <el-col :span="10">
            <el-form-item label="头像" :label-width="formLabelWidth" prop="avatar">
              <div>
                <el-image style="max-width: 180px" :src="formData.blogFile.publishUrl" />
              </div>
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
      <div slot="footer" class="dialog-footer">
        <el-button @click="editFormCancel">取 消</el-button>
        <el-button type="primary" @click="editFormSubmit">确 定</el-button>
      </div>
    </el-dialog>
    <el-card style="margin: 18px 2%;width: 95%">
      <el-table
          :data="dataList"
          stripe
          border
          style="width: 100%"
          :max-height="tableHeight"
          @selection-change="selectChange">
        <el-table-column
            type="selection"
            width="45">
        </el-table-column>
        <el-table-column
            sortable
            prop="id"
            label="UID"
            show-overflow-tooltip
            width="90">
        </el-table-column>
        <el-table-column
            sortable
            prop="username"
            label="用户名"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            sortable
            prop="nickname"
            label="昵称"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            prop="role.name"
            label="角色"
            show-overflow-tooltip
            width="90">
        </el-table-column>
        <el-table-column
            prop="email"
            label="邮箱"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            sortable
            prop="latestActiveTime"
            label="最近活跃时间"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            width="150px">
          <template slot-scope="scope">
            <el-button
                @click.native.prevent="dataEdit(scope.row)"
                type="primary"
                size="small">
              编辑
            </el-button>
            <el-button
                @click.native.prevent="dataDelete(scope.row)"
                type="danger"
                size="small">
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin: 20px 0 20px 0;float: left">
        <!--        <el-button @click="cancelSelection">取消选择</el-button>-->
        <el-button :disabled="selectionDisabled" type="danger" @click="multiDelete">批量删除</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import SearchBar from './ManageComponents/SearchBar.vue'
export default {
  name: 'ManageUserList',
  components: {SearchBar},
  data () {
    return {
      dataList: [],
      dataToDeleteList: [],
      editFormVisible: false,
      formData: {},
      formLabelWidth: '120px'
    }
  },
  created () {
    this.editFormClear()
    this.initDataList()
  },
  computed: {
    tableHeight () {
      return window.innerHeight - 320
    },
    selectionDisabled () {
      return this.dataToDeleteList.length === 0
    }
  },
  methods: {
    initDataList () {
      this.$axios.get('/user').then(resp => {
        if (resp.data.code === 200) {
          this.dataList = resp.data.data
        } else {
          this.$message.info('数据加载失败!')
        }
      }).catch(failResp => {
        console.log(failResp)
      })
    },
    formDataNullCheck () {
      if (this.formData.blogFile === null) {
        this.formData.blogFile = {}
      }
    },
    selectChange (data) {
      this.dataToDeleteList = data
    },
    multiDelete () {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.post('/user/batch/delete', this.dataToDeleteList).then(resp => {
          if (resp.data.code === 200) {
            this.$message.info('删除成功!')
            this.initDataList()
          } else {
            this.$message.info('删除失败!')
          }
        }).catch(failResp => {
          console.log(failResp)
        })
      }).catch(failResp => {
        this.$message.info('已取消')
      })
    },
    dataDelete (log) {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete('/user/' + log.id).then(resp => {
          if (resp.data.code === 200) {
            this.$message.info('删除成功!')
            this.initDataList()
          } else {
            this.$message.info('删除失败!')
          }
        }).catch(failResp => {
          console.log(failResp)
        })
      }).catch(failResp => {
        this.$message.info('已取消')
      })
    },
    inputSearch (keyword) {
      this.$axios.post('/user/keyword/select', {'keyword': keyword}).then(resp => {
        if (resp.data.code === 200) {
          this.dataList = resp.data.data
        } else {
          this.$message.info('数据加载失败')
        }
      }).catch(failResp => {
        console.log(failResp)
      })
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
            this.initDataList()
            this.editFormCancel()
            this.$message.info('更新成功!')
          } else {
            this.$message.info('更新失败!')
          }
        }).catch(failResp => {
          elLoading.close()
          console.log(failResp)
        })
      }).catch(failResp => {
        console.log(failResp)
      })
    },
    editFormCancel () {
      this.editFormVisible = false
      this.editFormClear()
    },
    editFormClear () {
      this.formData = {
        id: 0,
        username: '',
        nickname: '',
        gender: '',
        email: '',
        roleId: 1,
        introductions: '',
        blogFile: {
          publishUrl: ''
        }
      }
    },
    deepClone (src, dst) {
      for (let key in src) {
        if (src.hasOwnProperty(key)) {
          if (typeof src[key] === 'object') {
            if (!dst[key]) dst[key] = {}
            this.deepClone(src[key], dst[key])
          } else {
            dst[key] = src[key]
          }
        }
      }
    },
    dataEdit (user) {
      this.deepClone(user, this.formData)
      this.formDataNullCheck()
      this.editFormVisible = true
    }
  }
}
</script>

<style scoped>
</style>
