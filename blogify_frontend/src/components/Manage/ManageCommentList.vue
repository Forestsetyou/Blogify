<template>
  <div style="text-align: center">
    <search-bar style="display: inline-block" @inputSearch="inputSearch"></search-bar>
    <el-dialog
        top="5vh"
        :title="!formData.checkState ? '审核评论' : '查看评论信息'"
        :visible.sync="checkFormVisible">
      <el-form v-model="formData" style="text-align: left" ref="dataForm">
        <el-form-item label="评论ID" :label-width="formLabelWidth" prop="id">
          <el-input v-model="formData.id" auto-complete="off" :placeholder="formData.id" disabled/>
        </el-form-item>
        <el-form-item v-if="formData.parentEssayId" label="来自博文" :label-width="formLabelWidth">
          <el-input v-model="formParent.title" auto-complete="off" :placeholder="formParent.title" disabled/>
        </el-form-item>
        <el-form-item v-else label="来自评论" :label-width="formLabelWidth">
          <el-input v-model="formParent.content" auto-complete="off" :placeholder="formParent.content" disabled/>
        </el-form-item>
        <el-form-item label="评论者" :label-width="formLabelWidth" prop="user.username">
          <el-input v-model="formData.user.username" auto-complete="off" :placeholder="formData.user.username" disabled/>
        </el-form-item>
        <el-form-item label="评论时间" :label-width="formLabelWidth" prop="create_time">
          <el-input v-model="formData.createTime" auto-complete="off" :placeholder="formData.createTime" disabled/>
        </el-form-item>
        <el-form-item label="评论内容" :label-width="formLabelWidth" prop="content">
          <el-input v-model="formData.content" auto-complete="off" :placeholder="formData.content" disabled/>
        </el-form-item>
      </el-form>
      <div v-if="formData.checkState" slot="footer" class="dialog-footer">
        <el-button v-if="formData.checkState" type="primary" @click="checkFormCancel">确 定</el-button>
      </div>
      <div v-else slot="footer" class="dialog-footer">
        <el-button v-if="!formData.checkState" type="warning" @click="auditFormReject">驳 回</el-button>
        <el-button v-if="!formData.checkState" type="danger" @click="auditFormSubmit">通 过</el-button>
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
            label="评论ID"
            show-overflow-tooltip
            width="90">
        </el-table-column>
        <el-table-column
            prop="user.username"
            label="评论者"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            label="审核状况"
            show-overflow-tooltip
            :filters="filters.audit"
            :filter-method="auditFilterHandler"
            fit>
          <template slot-scope="scope">
            <span v-if="scope.row.checkState === 1">通过</span>
            <span v-if="scope.row.checkState === 2">已驳回</span>
            <span v-if="scope.row.checkState === 0">待审核</span>
          </template>
        </el-table-column>
        <el-table-column
            prop="content"
            label="评论内容"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            label="来自"
            show-overflow-tooltip
            fit>
          <template slot-scope="scope">
            <span> {{ scope.row.parentEssayId ? scope.row.essay.title : scope.row.comment.content }} </span>
          </template>
        </el-table-column>
        <el-table-column
            sortable
            prop="createTime"
            label="评论时间"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            width="150px">
          <template slot-scope="scope">
            <el-button
                v-if="scope.row.checkState"
                @click.native.prevent="checkForm(scope.row)"
                type="primary"
                size="small">
              查看
            </el-button>
            <el-button
                v-else
                @click.native.prevent="auditForm(scope.row)"
                type="warning"
                size="small">
              审核
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
      </div>
    </el-card>
  </div>
</template>

<script>
import SearchBar from './ManageComponents/SearchBar.vue'
export default {
  name: 'ManageCommentList',
  components: {SearchBar},
  data () {
    return {
      dataList: [],
      dataToDeleteList: [],
      checkFormVisible: false,
      formData: {},
      formParent: {},
      formLabelWidth: '120px',
      sourcePath: '/comment',
      filters: {
        audit: [
          {
            text: '待审核',
            value: 0
          },
          {
            text: '已通过',
            value: 1
          },
          {
            text: '已驳回',
            value: 2
          }
        ]
      }
    }
  },
  created () {
    this.checkFormClear()
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
    auditFilterHandler (value, row, column) {
      return value === row.checkState
    },
    initDataList () {
      this.$axios.get(this.sourcePath).then(resp => {
        if (resp.data.code === 200) {
          this.dataList = resp.data.data
          console.log(this)
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
    checkFormCancel () {
      this.checkFormVisible = false
      // this.checkFormClear()
    },
    auditFormReject () {
      this.formData.checkState = 2
      console.log(this.formData)
      this.$confirm('确认驳回?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消'
      }).then(() => {
        let elLoading = this.$loading({
          text: '正在驳回',
          background: 'rgba(0, 0, 0, 0.8)',
          spinner: 'el-icon-loading'
        })
        this.$axios.put(this.sourcePath + '/' + this.formData.id, this.formData).then(resp => {
          elLoading.close()
          if (resp.data.code === 200) {
            this.initDataList()
            this.checkFormCancel()
            this.$message.info('驳回成功!')
          } else {
            this.$message.info('驳回失败!')
          }
        }).catch(failResp => {
          elLoading.close()
          console.log(failResp)
        })
      }).catch(failResp => {
        console.log(failResp)
      })
    },
    auditFormSubmit () {
      this.formData.checkState = 1
      console.log(this.formData)
      this.$confirm('确认通过?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消'
      }).then(() => {
        let elLoading = this.$loading({
          text: '正在通过',
          background: 'rgba(0, 0, 0, 0.8)',
          spinner: 'el-icon-loading'
        })
        this.$axios.put(this.sourcePath + '/' + this.formData.id, this.formData).then(resp => {
          elLoading.close()
          if (resp.data.code === 200) {
            this.initDataList()
            this.checkFormCancel()
            this.$message.info('通过成功!')
          } else {
            this.$message.info('通过失败!')
          }
        }).catch(failResp => {
          elLoading.close()
          console.log(failResp)
        })
      }).catch(failResp => {
        console.log(failResp)
      })
    },
    checkFormClear () {
      this.formParent = {}
      this.formData = {
        id: 0,
        parentEssayId: 0,
        essay: {
          essayTagMappings: []
        },
        parentCommentId: 0,
        comment: {},
        userId: 0,
        user: {},
        checkState: 1,
        content: '',
        createTime: ''
      }
    },
    checkForm (comment) {
      this.deepClone(comment, this.formData)
      this.formParent = this.formData.parentEssayId ? this.formData.essay : this.formData.comment
      this.checkFormVisible = true
    },
    auditForm (comment) {
      this.deepClone(comment, this.formData)
      this.formParent = this.formData.parentEssayId ? this.formData.essay : this.formData.comment
      this.checkFormVisible = true
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
    }
  }
}
</script>

<style scoped>
</style>
