<template>
  <div>
    <div class="articles-area">
      <el-row style="height: 95vh">
      </el-row>
      <el-card id="index" style="text-align: left;width: 75%; margin: 35px auto 0 auto">
        <div>
          <span style="font-size: 20px"><strong>{{ essay.title }}</strong></span>
          <el-divider content-position="left">{{ essay.updateTime}}</el-divider>
          <div class="markdown-body">
            <vue-markdown :source="essay.content" v-highlight/>
          </div>
          <el-button type="primary" icon="el-icon-thumb" @click.native="praiseEssay" circle></el-button>
        </div>
      </el-card>
    </div>
    <display-comment-board></display-comment-board>
  </div>
</template>

<script>
// import '../../assets/style/markdown.css'
import VueMarkdown from 'vue-markdown'
import DisplayCommentBoard from './DisplayCommentBoard.vue'
export default {
  name: 'DisplayRead',
  components: {
    VueMarkdown,
    DisplayCommentBoard
  },
  data () {
    return {
      essay: {
        title: '',
        content: ''
      },
      comments: []
    }
  },
  created () {
    this.initDataList()
  },
  mounted () {
    this.$nextTick(() => {
      document.getElementById('index').scrollIntoView({
        behavior: 'smooth',
        block: 'start'
        // inline: 'nearest'
      })
    })
  },
  methods: {
    async initDataList () {
      let essay = this.$store.state.readEssay
      if (essay === null) {
        this.$message.info('博文信息读取错误!')
      } else {
        try {
          let resp = await this.$axios.get('/display/essays/' + essay.id)
          if (resp.data.code === 200) {
            this.essay = resp.data.data
          } else {
            this.$message.info('博文信息加载错误!')
          }
          try {
            resp = await this.$axios.get('/display/comments/' + this.essay.id)
            if (resp.data.code === 200) {
              this.comments = resp.data.data // 加载完毕
            } else {
              this.$message.info('评论信息加载错误!')
            }
          } catch (e) {
            console.log(e)
          }
        } catch (e) {
          console.log(e)
        }
      }
    },
    praiseEssay () {
      this.$axios.get('/display/essays/praise/' + this.essay.id).then(resp => {
        if (resp.data.code === 200) {
          this.$message.success('点赞成功!')
        } else {
          this.$message.warning('点赞失败!')
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
