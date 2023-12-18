<template>
  <div class="animate__animated animate__fadeInUp footer-wrap">
    <!--    center aligned居中-->
    <el-row :gutter="20" class=" footer-info">
      <el-col class="ewm" :xs="24" :sm="4" >
        <el-image :src="footerImage" alt="图片加载失败"
                  class="ui rounded image" style="width: 110px"/>
      </el-col>
      <el-col class="new-blog" :xs="24" :sm="7">
        <h4 class="ui inverted header m-text-spaced ">最新博客</h4>
        <div id="newblog-container">
          <div class="recommend-blog l-text list" v-for="essay in essayList" :key="essay.id">
            <a class="item" @click="readEssay(essay)" target="_blank">{{ essay.title }}</a>
          </div>
        </div>
      </el-col>
      <el-col class="connect" :xs="24" :sm="5">
        <h4 class="ui inverted header m-text-spaced ">联系我</h4>
        <div class="ui inverted link list">
          <p class="item">Email：forestsetyou@163.com</p>
          <p class="item">QQ: 0x2E1DC62B</p>
        </div>
      </el-col>
      <el-col class="intro" :xs="24" :sm="8">
        <div class="seven wide column">
          <h4 class="ui inverted header m-text-spaced">博客简介</h4>
          <p class="m-text-thin m-text-spaced m-opacity-mini" >
            抛得开手里玩具，先懂得好好进睡</p>
        </div>
      </el-col>
    </el-row>
    <el-row>
      <div class="author">
        <p class="m-text-thin m-text-spaced m-opacity-mini">Copright©2023-2103 forestsetyou Designed by
          forestsetyou</p>
      </div>
    </el-row>
  </div>
</template>

<script>
export default {
  data () {
    return {
      essayList: [],
      essaySource: '/display/essays',
      footerImage: this.$blogify.default_footer
    }
  },
  created () {
    this.initDataList()
  },
  methods: {
    initDataList () {
      this.$axios.get(this.essaySource).then(resp => {
        if (resp.data.code === 200) {
          this.essayList = resp.data.data.slice(0, 3)
        } else {
          this.$message.info('数据加载失败')
        }
      })
    },
    readEssay (essay) {
      this.$store.commit('readEssay', {
        essay: essay,
        axios: this.$axios
      })
      this.$router.push('/display/read')
    }
  }
}
</script>

<style scoped lang="less">
*:hover{
  cursor: pointer;
}
.footer-wrap {
  bottom: 0 !important;
  line-height: 2;
  position: relative;
  padding: 40px 20px;
  color: #eee;
  font-size: 14px;
  text-align: center;
  background-color: #545c64;
  transition: .2s;
}
.footer-info{
  line-height: 15px;
  color: rgba(255,255,255,.5);
  a{
    text-decoration: none;
    color: rgba(255,255,255,.5);
  }
  h4{
    color: white;
  }

  .list .item{
    line-height: 20px;
  }

  .list .item:hover{
    color: white;
  }
  .el-image{
    margin: 0 auto;
    opacity: 0.8;
  }
  .el-image:hover{
    opacity: 1;
  }
}
.author{
  color: rgba(255,255,255,.5);
}

@media only screen and (max-width: 480px) {
  .ewm,.new-blog,.intro,.connect{
    display: none;
  }
}
</style>
