<template>
  <div style="text-align: center">
    <search-bar style="display: inline-block" @inputSearch="inputSearch"></search-bar>
    <el-dialog
        top="5vh"
        title="新建标签"
        :visible.sync="createFormVisible">
      <el-form v-model="formData" style="text-align: center" ref="dataForm" inline>
        <el-form-item label="新建标签名称" :label-width="formLabelWidth" prop="name">
          <el-input v-model="formData.name" auto-complete="off" :placeholder="formData.name"/>
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
            label="标签ID"
            show-overflow-tooltip
            width="90">
        </el-table-column>
        <el-table-column
            prop="name"
            label="标签名称"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            sortable
            prop="essayTagMappings.length"
            label="博文数量"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            width="150px">
          <template slot-scope="scope">
            <el-button
                @click.native.prevent="dataCheck(scope.row)"
                type="primary"
                size="small">
              查看
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
        <el-button :disabled="selectionDisabled" type="danger" @click="multiDelete">批量删除</el-button>
        <el-button :disabled="false" type="danger" @click="createForm">新增标签</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import SearchBar from './ManageComponents/SearchBar.vue'
export default {
  name: 'ManageTagList',
  components: {SearchBar},
  data () {
    return {
      dataList: [],
      dataToDeleteList: [],
      createFormVisible: false,
      formData: {},
      formLabelWidth: '120px',
      sourcePath: '/tag'
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
      this.$axios.get(this.sourcePath).then(resp => {
        if (resp.data.code === 200) {
          this.dataList = resp.data.data
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
        this.$axios.post(this.sourcePath + '/batch/delete', this.dataToDeleteList).then(resp => {
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
        this.$axios.delete(this.sourcePath + '/' + log.id).then(resp => {
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
      this.$axios.post(this.sourcePath + '/keyword/select', {'keyword': keyword}).then(resp => {
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
          text: '正在创建',
          background: 'rgba(0, 0, 0, 0.8)',
          spinner: 'el-icon-loading'
        })
        this.$axios.post(this.sourcePath, this.formData).then(resp => {
          elLoading.close()
          if (resp.data.code === 200) {
            this.initDataList()
            this.createFormCancel()
            this.$message.info('创建成功!')
          } else {
            this.$message.info('创建失败!')
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
        id: null,
        name: ''
      }
    },
    createForm () {
      this.createFormClear()
      this.createFormVisible = true
    },
    dataCheck (tag) {
      this.$confirm('确认查看?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$blogify.status.searchTag = tag
        this.$router.push('/manage/essayList')
      }).catch(failResp => {
        this.$message.info('已取消')
      })
    }
  }
}
</script>

<style scoped>
</style>
