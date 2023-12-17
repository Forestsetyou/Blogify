<template>
  <div style="text-align: center">
    <search-bar style="display: inline-block" @inputSearch="inputSearch"></search-bar>
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
            prop="user.username"
            label="操作者"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            prop="remarks"
            label="操作记录"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            sortable
            prop="operationTime"
            label="操作时间"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            fixed="right"
            label="操作"
            show-overflow-tooltip
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
    </el-card>
  </div>
</template>

<script>
import SearchBar from './ManageComponents/SearchBar.vue'
export default {
  name: 'ManageOperationLogs',
  components: {SearchBar},
  data () {
    return {
      dataList: [],
      dataToDeleteList: []
    }
  },
  created () {
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
      this.$axios.get('/log').then(resp => {
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
        this.$axios.post('/log/batch/delete', this.dataToDeleteList).then(resp => {
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
      console.log(log)
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete('/log/' + log.id).then(resp => {
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
      this.$axios.post('/log/keyword/select', {'keyword': keyword}).then(resp => {
        console.log(resp)
        if (resp.data.code === 200) {
          this.dataList = resp.data.data
        } else {
          this.$message.info('数据加载失败')
        }
      }).catch(failResp => {
        console.log(failResp)
      })
    }
  }
}
</script>

<style scoped>
</style>
