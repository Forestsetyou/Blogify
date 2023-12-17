<template>
  <div style="text-align: center">
    <search-bar style="display: inline-block" @inputSearch="inputSearch"></search-bar>
    <el-dialog
        top="15vh"
        title="新建通知信息"
        :visible.sync="createFormVisible">
      <el-form v-model="formData" style="text-align: left" ref="dataForm">
        <el-form-item label="被通知用户" :label-width="formLabelWidth" prop="noticeeId">
          <el-select v-model="formData.noticeeId" placeholder="请选择接受通知的用户">
            <el-option v-for="user in noticees" :key="user.id" :label="user.username" :value="user.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="通知信息" :label-width="formLabelWidth" prop="content">
          <el-input v-model="formData.content" autocomplete="off"
                    :placeholder="'请输入通知内容'" type="textarea" rows="4"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createFormCancel">取 消</el-button>
        <el-button type="primary" @click="createFormSubmit">确 定</el-button>
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
            label="通知ID"
            show-overflow-tooltip
            width="100">
        </el-table-column>
        <el-table-column
            prop="notifier.username"
            label="通知者"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            prop="noticee.username"
            label="被通知者"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            sortable
            prop="noticeTime"
            label="通知时间"
            show-overflow-tooltip
            width="200px">
        </el-table-column>
        <el-table-column
            prop="content"
            label="内容"
            show-overflow-tooltip
            width="300px">
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            width="80px">
          <template slot-scope="scope">
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
      <div style="margin: 20px 0 20px 0;float: left">
        <!--        <el-button @click="cancelSelection">取消选择</el-button>-->
        <el-button :disabled="false" style="margin-left: 10px" type="primary" @click="createForm">新增通知</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import SearchBar from './ManageComponents/SearchBar.vue'
export default {
  name: 'ManageUserNotice',
  components: {SearchBar},
  data () {
    return {
      dataList: [],
      dataToDeleteList: [],
      createFormVisible: false,
      formData: {},
      formLabelWidth: '120px',
      noticees: []
    }
  },
  created () {
    this.createFormClear()
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
      this.$axios.get('/notice').then(resp => {
        if (resp.data.code === 200) {
          this.dataList = resp.data.data
        } else {
          this.$message.info('数据加载失败!')
        }
      }).catch(failResp => {
        console.log(failResp)
      })
      this.$axios.get('/user').then(resp => {
        if (resp.data.code === 200) {
          this.noticees = resp.data.data
        } else {
          this.$message.info('数据加载失败!')
        }
      }).catch(failResp => {
        console.log(failResp)
      })
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
        this.$axios.post('/notice/batch/delete', this.dataToDeleteList).then(resp => {
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
        this.$axios.delete('/notice/' + log.id).then(resp => {
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
      this.$axios.post('/notice/keyword/select', {'keyword': keyword}).then(resp => {
        if (resp.data.code === 200) {
          this.dataList = resp.data.data
        } else {
          this.$message.info('数据加载失败')
        }
      }).catch(failResp => {
        console.log(failResp)
      })
    },
    createFormSubmit () {
      this.$confirm('确认提交?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消'
      }).then(() => {
        let elLoading = this.$loading({
          text: '正在新建通知',
          background: 'rgba(0, 0, 0, 0.8)',
          spinner: 'el-icon-loading'
        })
        this.$axios.post('/notice', this.formData).then(resp => {
          elLoading.close()
          if (resp.data.code === 200) {
            this.initDataList()
            this.createFormCancel()
            this.$message.info('新建成功!')
          } else {
            this.$message.info('新建失败!')
          }
        }).catch(failResp => {
          elLoading.close()
          console.log(failResp)
        })
      }).catch(failResp => {
        console.log(failResp)
      })
    },
    createFormCancel () {
      this.createFormVisible = false
      this.createFormClear()
    },
    createFormClear () {
      this.formData = {
        id: 0,
        notifierId: this.$store.state.user.id,
        content: '',
        noticeeId: 1
      }
    },
    createForm () {
      this.createFormClear()
      this.createFormVisible = true
    }
  }
}
</script>

<style scoped>
</style>
