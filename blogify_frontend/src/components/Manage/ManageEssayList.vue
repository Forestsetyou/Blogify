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
            sortable
            prop="id"
            label="博文ID"
            show-overflow-tooltip
            width="90">
        </el-table-column>
        <el-table-column
            label="标题"
            show-overflow-tooltip
            fit>
          <template slot-scope="scope">
            <el-link @click.native="readEssay(scope.row)">
              <span>
                {{ scope.row.title }}
              </span>
            </el-link>
          </template>
        </el-table-column>
        <el-table-column
            prop="user.username"
            label="作者"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            sortable
            prop="views"
            label="浏览量"
            show-overflow-tooltip
            fit>
        </el-table-column>
        <el-table-column
            sortable
            prop="praise"
            label="点赞数"
            show-overflow-tooltip
            width="90">
        </el-table-column>
        <el-table-column
            sortable
            prop="createTime"
            label="发布时间"
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
                type="warning"
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
        <el-button :disabled="selectionDisabled" type="danger" @click="multiDelete">批量删除</el-button>
      </div>
      <div style="margin: 20px 0 20px 0;float: left">
        <el-button :disabled="false" style="margin-left: 10px" type="primary" @click="createForm">新增博文</el-button>
      </div>
    </el-card>
  </div>
</template>

<script>
import SearchBar from './ManageComponents/SearchBar.vue'
export default {
  name: 'ManageEssayList',
  components: {SearchBar},
  data () {
    return {
      dataList: [],
      formData: {},
      dataToDeleteList: [],
      editFormVisible: false,
      formLabelWidth: '120px'
    }
  },
  created () {
    this.pageDataInit()
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
    readEssay (essay) {
      this.$store.commit('readEssay', essay)
      this.$router.push('/display/read')
    },
    pageDataInit () {
      if (this.$blogify.status.searchType) {
        this.inputSearch(this.$blogify.status.searchType.name)
        this.$blogify.statusClear()
      } else if (this.$blogify.status.searchTag) {
        this.inputSearch(this.$blogify.status.searchTag.name)
        this.$blogify.statusClear()
      } else {
        this.initDataList()
      }
    },
    initDataList () {
      this.$axios.get('/essay').then(resp => {
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
        this.$axios.post('/essay/batch/delete', this.dataToDeleteList).then(resp => {
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
    dataDelete (essay) {
      this.$confirm('确认删除?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$axios.delete('/essay/' + essay.id).then(resp => {
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
      this.$axios.post('/essay/keyword/select', {'keyword': keyword}).then(resp => {
        if (resp.data.code === 200) {
          this.dataList = resp.data.data
        } else {
          this.$message.info('数据加载失败')
        }
      }).catch(failResp => {
        console.log(failResp)
      })
    },
    dataEdit (essay) {
      this.$confirm('前往编辑?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$blogify.status.editEssay = essay
        this.$router.push('/manage/essayWrite')
      }).catch(failResp => {
        this.$message.info('已取消')
      })
    },
    createForm () {
      this.$confirm('新建博文?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$blogify.status.editEssay = null
        this.$router.push('/manage/essayWrite')
      }).catch(failResp => {
        this.$message.info('已取消')
      })
    }
  }
}
</script>

<style scoped>
</style>
