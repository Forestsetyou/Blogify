<template>
  <div>
    <el-upload
        class="picture-upload"
        drag
        :with-credentials="true"
        action="http://localhost:7969/api/v1/file/image"
        multiple>
      <i class="el-icon-upload"></i>
      <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
      <div class="el-upload__tip" slot="tip">只能上传 jpg/jpeg/png/gif/bmp 文件</div>
    </el-upload>
    <waterfall id="waterfall_box" :width="itemWidth" :gutterWidth="gutterWidth" :col="columnNumber" :data="pictureList">
      <template>
        <div v-for="pic in pictureList" class="waterfall_item" :key="pic.id">
          <el-card class="manage-picture-cards">
            <div style="width: 100%; height: 100%">
              <img class="images" v-if="pic.id" :lazy-src="pic.publishUrl" @click="showImg(pic)"
                   @mouseout="refreshButtonIndex"/>
              <span>上传时间: {{pic.uploadTime}}</span>
              <el-button size="mini" type="danger" circle @click="deletePic(pic)">
                <i class="el-icon-delete"></i>
              </el-button>
              <el-button style="top: 40px" size="mini"
                         :type="buttonStatus[buttonIndex].type"
                         circle
                         @click="copyPic(pic)">
                <i :class="buttonStatus[buttonIndex].cls"></i>
              </el-button>
            </div>
          </el-card>
        </div>
      </template>
      <el-dialog :visible.sync="dialogVisible">
          <img width="60%" :src="dialogImageUrl" style="opacity: 100" class="el-card" alt="showImage">
      </el-dialog>
    </waterfall>
  </div>

</template>

<script>
export default {
  data () {
    return {
      pictureList: [],
      renderList: [],
      dialogVisible: false,
      dialogImageUrl: '',
      buttonIndex: 0,
      buttonStatus: [
        {
          type: 'primary',
          cls: 'el-icon-copy-document'
        },
        {
          type: 'success',
          cls: 'el-icon-success'
        }
      ]
    }
  },
  created () {
    this.initPicList()
  },
  computed: {
    itemWidth () {
      return 270 // #rem布局 计算宽度
    },
    gutterWidth () {
      return (18 * 0.5 * (document.documentElement.clientWidth / 270)) // #rem布局 计算x轴方向margin(y轴方向的margin自定义在css中即可)
    },
    columnNumber () {
      return Math.floor(document.documentElement.clientWidth / 270)
    }
  },
  methods: {
    initPicList () {
      this.$axios.get('/file/image').then(resp => {
        if (resp.data.code === 200) {
          this.pictureList = resp.data.data
          console.log(this.pictureList)
        } else {
          this.$message.info('图片加载失败!')
        }
      }).catch(failResp => {
        console.log(failResp)
      })
    },
    showImg (pic) {
      this.dialogImageUrl = pic.publishUrl
      this.dialogVisible = true
    },
    deletePic (pic) {
      this.$confirm(
        '确定要删除该图片吗',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
      ).then(() => {
        this.$axios.delete('/file/image/' + pic.id).then(resp => {
          if (resp.data.code === 200) {
            let pictureList = this.pictureList
            this.pictureList = pictureList.filter(picture => picture.id !== pic.id)
            this.$message.info('删除成功!')
          } else {
            this.$message.info('删除失败!')
          }
        })
      }).catch(failResp => {
        this.$message.info('已取消删除')
      })
    },
    copyPic (pic) {
      navigator.clipboard.writeText(pic.publishUrl).then(resp => {
        this.buttonIndex = 1 - this.buttonIndex
        this.$message.info('复制成功!')
      }).catch(failResp => {
        this.$message.info('复制失败!')
      })
    },
    refreshButtonIndex () {
      this.buttonIndex = 0
    }
  }
}
</script>

<style scoped lang="less">
.el-card /deep/ .el-card__body{
  padding: 0;
}
#waterfall_box {
  width: 100%;
  position: relative;
  margin: 20px auto;
  .waterfall_item {
    .manage-picture-cards {
      position: relative;
      margin-bottom: 20px;
      img {
        width: 100%;
      }
      span {
        position: absolute;
        bottom: 0;
        left: 0;
        line-height: 18px;
        font-size: small;
        color: #eee;
        background-color: rgba(0, 0, 0, 0.3);
        visibility: hidden;
      }
      button {
        position: absolute;
        top: 0;
        right: 0;
        visibility: hidden;
      }
    }
  }

  .waterfall_item:hover {
    cursor: pointer;
    overflow: hidden;
    span {
      color: white;
      transition: all .3s;
      visibility: visible;
    }
    button {
      visibility: visible;
    }
    img{
      transform: scale(1.1);
      width: 100%;
      transition: .6s;
    }
  }
}

</style>
