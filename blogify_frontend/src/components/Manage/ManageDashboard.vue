<template>
  <div>
    <el-row :gutter="32">
      <el-col :xs="12" :sm="6" style="text-align: center;margin-bottom: 32px;">
        <el-card shadow="hover" @click.native="selectCard (0)" style="vertical-align: center">
          <el-col :span="8" style="text-align: center">
            <i class="el-icon-view" style="font-size: 54px; margin: 24px 0 0 22px"/>
          </el-col>
          <el-col :span="16" style="text-align: center">
            <p>总阅读量</p>
            <h2>{{statistics.views}}</h2>
          </el-col>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" style="text-align: center;margin-bottom: 32px;">
        <el-card shadow="hover" @click.native="selectCard (1)">
          <el-col :span="8" style="text-align: center">
            <i class="el-icon-notebook-1" style="font-size: 54px; margin: 24px 0 0 22px"/>
          </el-col>
          <el-col :span="16" style="text-align: center">
            <p>文章总数</p>
            <h2>{{statistics.essays}}</h2>
          </el-col>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" style="text-align: center;margin-bottom: 32px;">
        <el-card shadow="hover" @click.native="selectCard (2)">
          <el-col :span="8" style="text-align: center">
            <i class="el-icon-thumb" style="font-size: 54px; margin: 24px 0 0 22px"/>
          </el-col>
          <el-col :span="16" style="text-align: center">
            <p>点赞数</p>
            <h2>{{statistics.appreciation}}</h2>
          </el-col>
        </el-card>
      </el-col>
      <el-col :xs="12" :sm="6" style="text-align: center;margin-bottom: 32px;">
        <el-card shadow="hover" @click.native="selectCard (3)">
          <el-col :span="8" style="text-align: center">
            <i class="el-icon-chat-dot-square" style="font-size: 54px; margin: 24px 0 0 22px"/>
          </el-col>
          <el-col :span="16" style="text-align: center">
            <p>评论数</p>
            <h2>{{statistics.comment}}</h2>
          </el-col>
        </el-card>
      </el-col>
    </el-row>
    <el-row type="flex" justify="center">
      <el-col :span="24" style="text-align: center;">
        <el-card shadow="hover">
          <div class="com-page">
            <BlogChart style="width: 100%; height: 350px;" :psMsg="selectedCard"></BlogChart>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import BlogChart from './ManageComponents/BlogChart'

export default {
  components: {
    BlogChart
  },
  data () {
    return {
      selectedCard: 0,
      recommendList: [],
      statistics: {
        essays: 0,
        views: 0,
        appreciation: 0,
        comment: 0
      }
    }
  },
  created () {
    this.initStatistics()
    console.log(this.$router)
  },
  methods: {
    async initStatistics () {
      this.$axios.get('/blog/statistics?q=all')
        .then(resp => {
          if (resp.data.code === 200) {
            this.statistics = resp.data.data
          } else {
            this.$message.info('数据加载失败!')
          }
        }).catch(failResp => {})
    },
    selectCard (id) {
      this.selectedCard = id
    }
  }
}
</script>

<style scoped lang="less">
.com-page {
  width: 100%;
  height: 100%;
  overflow: hidden;
  display: flex;
  justify-content: center;
}

.el-card:hover {
  cursor: pointer;
}

.chart-wrapper {
  height: 300px;
  margin-top: 30px;
}

.el-card /deep/ .el-card__body {
  padding: 0;
}
</style>
