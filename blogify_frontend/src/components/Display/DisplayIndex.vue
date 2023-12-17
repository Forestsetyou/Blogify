<template>
    <div>
        <el-row style="height: 95vh">
            <el-col :span="24" style="height: 100%">
                <el-card shadow="none" class="welcome">
                    <h1 class="tit" style="font-family: PlayBall-2">
                        {{ pageShow.title }}
                        <div class="border"></div>
                    </h1>

                    <h2 class="intro" style="white-space: pre-wrap;">{{ pageShow.intro }}</h2>
                    <div class="bounce down" @click="startRead">
                        <i class="el-icon-arrow-down" style="color: white"></i>
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <el-container id="index" class="animate__animated animate__fadeInUp" style="width: 60%; margin: 0 auto">
            <el-row :gutter="12">
                <el-col :xs="24" :sm="17">
                    <el-card style="background-color: rgba(255,255,255,0.9); min-height: 85vh" class="left-item">
                        <div slot="header" class="total">
                            <div class="title">
                                <i v-if="selection.selected" class="el-icon-back" @click="refreshSelection"></i>
                                <span>{{ pageShow.selectionStatus }}</span>
                            </div>
                            <span>共 <span style="color: #3a8ee6; font-size: 20px">{{ totalEssayCount }}</span> 篇</span>
                        </div>
                        <el-row type="flex" align="middle" style="flex-wrap: wrap" :gutter="20" shadow="never"
                                class="animate__animated animate__fadeInUp blog-content"
                                v-for="essay in pageStatus.essayList" :key="essay.id">
                            <el-col class="img" :xs="24" :sm="8">
                                <el-image lazy :src="essay.blogFile.publishUrl" style="max-width: 180px; max-height: 120px">
                                  <div slot="error" class="image-slot">
                                    <el-image src="static/default_essay_pic.jpg" fit="contain"/>
                                  </div>
                                </el-image>
                            </el-col>
                            <el-col :xs="24" :sm="16">
                                <div @click="readEssay(essay)">
                                    <h3>{{ essay.title }}</h3>
                                    <p class="description">{{ essay.abstractions }}</p>
                                    <div class="blog-info">
                                        <div class="user-info">
                                            <el-avatar size="small" :src="essay.user.blogFile.publishUrl" @error="avatarLoadError">
                                              <el-avatar icon="el-icon-user-solid" size="large" style="background-color: #666"></el-avatar>
                                            </el-avatar>
                                            <a href="#" class="header">{{ essay.user.nickname }}</a>
                                        </div>
                                        <div class="blog-date">
                                            <i class="el-icon-date"></i>
                                            <span>{{ essay.updateTime }}</span>
                                        </div>
                                        <div>
                                            <i class="el-icon-view"></i>
                                            <span>{{ essay.views }}</span>
                                        </div>
                                        <div class="blog-type">
                                            <el-tag effect="plain">{{ essay.type.name }}</el-tag>
                                        </div>
                                    </div>
                                </div>
                            </el-col>
                        </el-row>
                    </el-card>
                    <el-pagination
                            small
                            background
                            @current-change="curPageChange"
                            :page-size="pageOption.size"
                            :page-count="pageOption.count"
                            :current-page="pageStatus.curPage"
                            layout="prev, pager, next"
                            :total="totalPageCount"
                            style="margin-top: 6px">
                    </el-pagination>
                </el-col>
                <el-col :xs="24" :sm="7">
                    <el-card style="background-color: rgba(255,255,255,0.9)"
                             class="animate__animated animate__fadeInUp right-item">
                        <div slot="header" class="attributes">
                            <b>分类</b>
                        </div>
                        <ul>
                            <li class="animate__animated animate__fadeInUp blog-type" v-for="type in showList.types"
                                :key="type.id"
                                @click="selectType(type)"
                                :class="isTypeSelected(type)">
                                <div style="display: flex;align-items: center">
                                  <i class="el-icon-collection-tag" style="font-size: 28px; margin-right: 10px"/>
<!--                                    <el-image lazy :src="type.picUrl"-->
<!--                                              style="width: 28px;height: 28px; border-radius: 50%; margin-right: 10px"></el-image>-->
                                    {{type.name}}
                                </div>
                                <div>{{type.essays.length}}</div>
                            </li>
                        </ul>
                        <div class="more" @click="toggleMoreType">
                            <i v-if="!moreType" class="el-icon-arrow-down"></i>
                            <i v-else class="el-icon-arrow-up"></i>
                        </div>
                    </el-card>
                    <el-card style="background-color: rgba(255,255,255,0.9)"
                             class="animate__animated animate__fadeInUp right-item">
                        <div slot="header" class="attributes">
                            <b>标签</b>
                        </div>
                        <div class="tags">
                            <div class="animate__animated animate__fadeInUp tag-item" v-for="tag in showList.tags"
                                 :key="tag.id"
                                 @click="selectTag(tag)"
                                 :class="isTagSelected(tag)">
                                <div class="sjx-outer">
                                    <div class="sjx-inner"></div>
                                </div>
                                <div class="tag">
                                    {{ tag.name }}
                                    {{ tag.essayTagMappings.length }}
                                </div>
                            </div>
                        </div>
                        <div class="more" @click="toggleMoreTag">
                            <i v-if="!moreTag" class="el-icon-arrow-down"></i>
                            <i v-else class="el-icon-arrow-up"></i>
                        </div>
                    </el-card>
                    <el-card style="background-color: rgba(255,255,255,0.9)"
                             class="animate__animated animate__fadeInUp right-item">
                        <div slot="header" class="attributes">
                            <span>最新推荐</span>
                        </div>
                        <el-link class="animate__animated animate__fadeInUp recommend-blog l-text" v-for="essay in showList.latestEssays"
                             :key="essay.id"
                             @click="readEssay(essay)">
                            <a>{{ essay.title }}</a>
                        </el-link>
                    </el-card>
                </el-col>
            </el-row>
        </el-container>
    </div>
</template>

<script>
import '../../assets/font/my_font.css'

export default {
  data () {
    return {
      pageShow: {
        title: 'Welcome To Blogify',
        intro: '',
        selectionStatus: '全部博客',
        fullIntro: ''
      },
      essayList: [],
      pageStatus: {
        curPage: 1,
        essayList: []
      },
      dataList: {
        tags: [],
        types: [],
        essays: []
      },
      showList: {
        tags: [],
        types: [],
        latestEssays: []
      },
      selection: {
        selected: false,
        type: null,
        tag: null
      },
      pageOption: {
        size: 5,
        count: 6
      },
      moreType: false,
      moreTag: false,
      introAnimationTimer: null
    }
  },
  computed: {
    totalEssayCount () {
      return this.essayList.length
    },
    totalPageCount () {
      return Math.ceil(this.totalEssayCount / this.pageOption.size)
    }
    // screenWidth () { // 实时屏幕宽度
    //   return document.documentElement.clientWidth
    // },
    // 计算分页栏样式
    // pageSmall () {
    //   return this.screenWidth <= 768
    // },
    // pageLayout () {
    //   if (this.screenWidth < 768) {
    //     return 'prev, pager, next'
    //   } else {
    //     return 'total, prev, pager, next, jumper'
    //   }
    // }
  },
  created () {
    // window.addEventListener('resize', this.screenAdapter)
    this.initDataList()
    this.refreshSelection()
  },
  mounted () {
    this.introAnimationTimer = setTimeout(this.introAnimationWithYiyan, 1000)
    this.$store.commit('removeEssay')
  },
  methods: {
    avatarLoadError (error) {
      console.log(error)
      return true
    },
    async introDelay (second) {
      return new Promise((resolve, reject) => setTimeout(resolve, second * 1000))
    },
    async introAnimationWithYiyan () {
      let resp = await this.$yiyan.get('?c=d&c=h&c=i')
      this.pageShow.intro = ''
      this.pageShow.fullIntro = '『' + resp.data.hitokoto + '』\n——《' + resp.data.from + '》'
      for (let i = 0; i < this.pageShow.fullIntro.length; i++) {
        this.pageShow.intro += this.pageShow.fullIntro[i]
        await this.introDelay(0.1)
      }
      this.introAnimationTimer = setTimeout(this.introAnimationWithYiyan, 5000)
    },
    async initDataList () {
      try {
        let resp = await this.$axios.get('/display/types')
        if (resp.data.code === 200) {
          this.dataList.types = resp.data.data
        } else {
          this.$message.info('分类数据加载失败')
        }
      } catch (e) {
        console.log(e)
      }
      try {
        let resp = await this.$axios.get('/display/tags')
        if (resp.data.code === 200) {
          this.dataList.tags = resp.data.data
        } else {
          this.$message.info('标签数据加载失败')
        }
      } catch (e) {
        console.log(e)
      }
      try {
        let resp = await this.$axios.get('/display/essays')
        if (resp.data.code === 200) {
          this.dataList.essays = resp.data.data
        } else {
          this.$message.info('博文数据加载失败')
        }
      } catch (e) {
        console.log(e)
      }
      this.initShowList()
    },
    initShowList () {
      if (!this.moreTag) { // 默认显示 8 个 Tag
        this.showList.tags = this.dataList.tags.slice(0, 8)
      } else {
        this.showList.tags = this.dataList.tags
      }
      if (!this.moreType) { // 默认显示 5 个 Type
        this.showList.types = this.dataList.types.slice(0, 5)
      } else {
        this.showList.types = this.dataList.types
      }
      this.showList.latestEssays = this.dataList.essays.slice(0, 3) // 默认推荐三篇
    },
    initEssayList () { // 根据条件获取博文
      let options = {
        q: true
      }
      if (this.selection.selected) {
        if (this.selection.type !== null) {
          options.type = this.selection.type.name
        } else {
          options.tag = this.selection.tag.name
        }
      }
      this.$axios.post('/display/essays', options).then(resp => {
        if (resp.data.code === 200) {
          this.essayList = resp.data.data
          this.essayListNullCheck()
          this.curPageChange(1)
        } else {
          this.$message.info('博文数据加载失败')
        }
      }).catch(failResp => {
        console.log(failResp)
      })
    },
    essayListNullCheck () {
      for (let i = 0; i < this.essayList.length; i++) {
        if (this.essayList[i].blogFile === null) {
          this.essayList[i].blogFile = {}
        }
        if (this.essayList[i].type === null) {
          this.essayList[i].type = {}
        }
        if (this.essayList[i].user.blogFile === null) {
          this.essayList[i].user.blogFile = {}
        }
      }
    },
    curPageChange (newPageNum) {
      this.pageStatus.curPage = newPageNum
      let cur = newPageNum
      this.pageStatus.essayList = this.essayList.slice((cur - 1) * this.pageOption.size, cur * this.pageOption.size)
    },
    // 下移至主页
    startRead () {
      this.$nextTick(() => {
        document.getElementById('index').scrollIntoView({
          behavior: 'smooth',
          block: 'start'
          // inline: 'nearest'
        })
      })
    },
    isTypeSelected (type) {
      let nowType = this.selection.type
      if (nowType === null || nowType.id !== type.id) {
        return ''
      }
      return 'activeType'
    },
    isTagSelected (tag) {
      let nowTag = this.selection.tag
      if (nowTag === null || nowTag.id !== tag.id) {
        return ''
      }
      return 'activeTag'
    },
    readEssay (essay) {
      this.$store.commit('readEssay', essay)
      this.$router.push('/display/read')
    },
    toggleMoreType () {
      this.moreType = !this.moreType
      this.initShowList()
    },
    toggleMoreTag () {
      this.moreTag = !this.moreTag
      this.initShowList()
    },
    selectType (type) {
      this.selection.selected = true
      this.selection.tag = null
      this.selection.type = type
      this.initEssayList()
      this.pageShow.selectionStatus = '分类: ' + this.selection.type.name
    },
    selectTag (tag) {
      this.selection.selected = true
      this.selection.tag = tag
      this.selection.type = null
      this.initEssayList()
      this.pageShow.selectionStatus = '标签: ' + this.selection.tag.name
    },
    refreshSelection () {
      this.selection.selected = false
      this.selection.tag = null
      this.selection.type = null
      this.initEssayList()
      this.pageShow.selectionStatus = '全部博客'
    },
    // 修改当前页码
    handleCurrentChange (newSize) {
      this.queryInfo.pagenum = newSize
      this.getBlogList()
    }
    // 屏幕尺寸变化的监听函数
    // screenAdapter () {
    //   this.screenWidth = document.documentElement.clientWidth
    // }
  }
}
</script>

<style scoped lang="less">

body {
  width: 100%;
}

.welcome {
  background-color: rgba(0, 0, 0, 0.1);
  border: none;
  height: 90%;
  position: relative;

  .border {
    width: 812px;
    height: 112px;
    position: absolute;
    top: -6px;
    left: -6px;
    border: 3px solid white;
    box-sizing: border-box;
    animation: clipMe 5s linear infinite;
  }

  .tit {
    box-sizing: border-box;
    position: relative;
    width: 800px;
    height: 100px;
    line-height: 100px;
    box-shadow: inset 0 0 0 1px white;
    margin: 40px auto;
    margin-top: 80px;
    color: white;
    text-align: center;
    font-size: 50px;
    font-weight: normal;
    letter-spacing: 10px;
  }

  .intro {
    letter-spacing: 5px;
    line-height: 50px;
    width: 80%;
    margin: 0 auto;
    text-align: center;
    font-weight: normal;
    color: white;
  }

  .down {
    animation: bounce 2s infinite;
    animation-duration: 3s;
    font-size: 25px;
    position: absolute;
    bottom: 5px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    justify-content: center;
    align-items: center;
    width: 50px;
    height: 50px;
    border-radius: 50%;
    border: 2px solid #fff;
  }

  .down:hover {
    animation: none;
    cursor: pointer;
    box-shadow: 0 0 20px 0 white;
    transition: all .2s;
  }
}

@keyframes clipMe {

  0%,
  100% {
    clip: rect(0px, 806px, 6px, 0px);
  }

  25% {
    clip: rect(0px, 6px, 112px, 0px);
  }

  50% {
    clip: rect(112px, 812px, 112px, 0px);
  }

  75% {
    clip: rect(0px, 812px, 112px, 806px);
  }
}

@keyframes bounce {
  0%, 20%, 50%, 80%, 100% {
    transform: translate(-50%, 0);
  }
  40% {
    transform: translate(-50%, -30px);
  }
  60% {
    transform: translate(-50%, -15px);
  }
}

ul {
  padding-left: 10px;
  padding-right: 10px;
  margin-bottom: 0;
  border-radius: 5px;
}

.el-pagination {
  padding-bottom: 20px;
}

.el-card /deep/ .el-card__body {
  padding: 0;
}

.right-item {
  margin-bottom: 20px;

  li:first-child {
    border-top: 1px solid rgba(179, 216, 255, 0.5);
  }

  li {
    border-bottom: 1px solid rgba(179, 216, 255, 0.5);
  }

  .more {
    text-align: center;
    color: #3a8ee6;
    padding: 8px;
  }

  .more:hover {
    cursor: pointer;
    color: #3a8ee6;
  }

  .blog-type:hover, .activeType {
    background-color: rgba(58, 142, 230, 0.3);
    cursor: pointer;
  }

  .tags {
    display: flex;
    flex-wrap: wrap;
    align-items: center;
    margin: 15px 13px 0;
    border-bottom: 1px solid rgba(179, 216, 255, 0.5);

  }

  .tag-item {
    display: flex;
    justify-content: space-around;
    align-items: center;
    margin-left: 5px;
    margin-right: 5px;
    margin-bottom: 10px;
    box-sizing: border-box;
    .tag {
      background-color: #ecf5ff;
      box-sizing: border-box;
      display: inline-block;
      height: 22px;
      padding: 0 10px;
      line-height: 22px;
      font-size: 10px;
      color: #409eff;
      border-radius: 4px;
      white-space: nowrap;
      border: 1px solid #409eff;
      transition: .2s;
    }

    .sjx-outer {
      width: 0;
      height: 0;
      border-top: 6px solid transparent;
      border-bottom: 6px solid transparent;
      border-right: 6px solid #409eff;
      position: relative;
      transition: .2s;
    }

    .sjx-inner {
      border-top: 6px solid transparent;
      border-bottom: 6px solid transparent;
      border-right: 6px solid #ecf5ff;
      top: -6px;
      left: 1px;
      position: absolute;
      transition: .2s;
    }
  }
  .tag-item:hover, .activeTag {
    box-sizing: border-box;
    .tag {
      color: white;
      background-color: #409eff;
      cursor: pointer;
    }

    .sjx-inner {
      border-right: 6px solid #409eff;
    }
  }
}

.blog-type {
  display: flex;
  justify-content: space-between;
  align-items: center;
  line-height: 40px;
}

.recommend-blog {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  padding-left: 20px;
  padding-right: 20px;

  a {
    border-bottom: 1px solid rgba(34, 36, 38, .15);
    line-height: 40px;
    display: block;
    text-decoration: none;
    color: black;
  }

  a:hover {
    color: #3a8ee6;
  }
}

.total {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: larger;
  font-weight: bold;

  .title {
    display: flex;
    align-items: center;

    .el-icon-back {
      font-weight: bolder;
      color: #3a8ee6;
      margin-right: 10px;
    }

    .el-icon-back:hover {
      cursor: pointer;
    }
  }
}

.blog-content:hover {
  /*border-left: 5px solid #3a8ee6;*/
  /*border-right: 5px solid #3a8ee6;*/
  background-color: rgba(58, 142, 230, 0.3);
  cursor: pointer;

  .el-tag {
    color: white;
    background-color: #3a8ee6;
  }
}

.blog-content {
  padding: 10px;
  height: auto;
  border-bottom: 1px solid rgb(199, 163, 92);
  /*border-bottom: 1px solid rgba(34, 36, 38, .15);*/
  transition: .3s;

  .el-image {
    border-radius: 5px;
    box-sizing: border-box;
    flex-shrink: 0;
  }

  .blog-info {
    display: flex;
    align-items: center;
    color: rgba(0, 0, 0, .4);
    font-size: 10px;

    .user-info {
      display: flex;
      justify-content: space-around;
      align-items: center;
      margin-right: 15px;

      .el-avatar {
        margin-right: 5px;
        width: 22px;
        height: 22px;
      }

      .header {
        text-decoration: none;
        color: #3a8ee6;
        font-weight: bold;
      }
    }

    .blog-date {
      margin-right: 15px;
    }

    .blog-type {
      margin-left: auto;
    }
  }
}

.description {
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  font: 300 1em/1.8 PingFang SC, Lantinghei SC, Microsoft Yahei, Hiragino Sans GB, Microsoft Sans Serif, WenQuanYi Micro Hei, sans-serif;
}

@media screen and (max-width: 768px) {
  .blog-date {
    display: none;
  }

  .welcome {
    width: 100%;

    .border {
      display: none;
    }

    .tit {
      font-size: 2rem;
      width: 100%;
      line-height: 50px;
      letter-spacing: 2px;
      height: auto;
    }

    .intro {
      font-size: 1rem;
      line-height: 30px;
    }
  }

  .el-pagination {
    width: 100%;
  }
}

</style>
