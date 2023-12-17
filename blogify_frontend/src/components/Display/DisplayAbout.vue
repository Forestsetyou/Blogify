<template>
  <el-container style="opacity: 0.9; width: 75%; margin: 35px auto 0 auto" class="message">
    <el-card class="animate__animated animate__fadeInLeft publish">
      <div class="author">
        <el-avatar :src="authUser.blogFile.publishUrl" @error="avatarLoadError" size="large">
          <el-avatar icon="el-icon-user-solid" size="large" style="background-color: #666"></el-avatar>
        </el-avatar>
        <div>
          <div class="nkname">
            <span class="name" v-if="!authState">匿名用户</span>
            <span class="name" v-else>{{ authUser.username }} </span>
          </div>
        </div>
      </div>
      <el-form :model="formData.comment" :rules="commentRules" ref="messageFormRef">
        <el-form-item prop="content">
          <el-input :rows="5" v-model="formData.comment.content" type="textarea" placeholder="请输入你的评论" :disabled="!authState"></el-input>
        </el-form-item>
        <el-form-item style="text-align: right">
          <el-button type="primary" @click="commentFormSubmit" :disabled="!authState">点击发表</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </el-container>
</template>

<script>
export default {
  data () {
    return {
      essay: {
        user: {
          blogFile: {}
        }
      },
      formData: {
        comment: {
          parentEssayId: 0
        }
      },
      dataList: {
        comments: []
      },
      commentRules: {
        content: [
          {required: true, message: '评论内容不能为空！'},
          {min: 0, max: 100, message: '评论内容不超过100字！'}
        ]
      }
    }
  },
  created () {
    this.essay = this.$store.state.readEssay
    this.initFormData()
    this.initDataList()
  },
  computed: {
    authUser () {
      return this.$store.state.user
    },
    authState () {
      return this.$store.state.user.id != null
    }
  },
  methods: {
    initFormData () {
      this.formData.comment = {
        parentEssayId: this.essay.id
      }
    },
    avatarLoadError (error) {
      console.log(this.dataList.comments)
      console.log(error)
      return true
    },
    async initDataList () {
      try {
        let resp = await this.$axios.get('/display/comments/' + this.essay.id)
        if (resp.data.code === 200) {
          this.dataList.comments = resp.data.data
          this.commentsNullCheck()
        } else {
          this.$message.info('评论加载失败')
        }
      } catch (e) {
        console.log(e)
      }
    },
    commentsNullCheck () {
      for (let i = 0; i < this.dataList.comments.length; i++) {
        if (this.dataList.comments[i].user.blogFile === null) {
          this.dataList.comments[i].user.blogFile = {}
        }
      }
    },
    commentFormSubmit () {
      this.$confirm('确认提交评论?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      }).then(() => {
        if (!this.authState) {
          this.$alert('请先登录!', '提示')
        } else {
          this.$axios.post('/comment', this.formData.comment).then(resp => {
            if (resp.data.code === 200) {
              this.initFormData()
              this.initDataList()
              this.$message.info('评论提交成功!')
            } else {
              this.$message.info('评论提交失败!')
            }
          }).catch(failResp => {
            console.log(failResp)
          })
        }
      }).catch(failResp => {
        console.log(failResp)
      })
    }
  }
}
</script>

<style scoped lang="less">
.el-container{
  display: block;
}

.publish{
  margin-bottom: 20px;
}

.author {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  width: 100%;
  margin-bottom: 20px;
  .nkname{
    margin: 10px;
  }
}

.img {
  width: 300px;
  margin: 20px;
}

.comment {
  border-bottom: 1px dashed #ccc;
  margin: 30px 0;
  display: flex;
}

/*.el-avatar {*/
/*    width: 35px;*/
/*    height: 35px;*/
/*    border: 2px solid white;*/
/*    box-shadow: 0 0 10px 2px rgba(0, 0, 0, .06);*/
/*    flex-shrink: 0;*/
/*}*/

.content {
  text-align: left;
  font-size: 14px;
  flex-grow: 1;

  .nkname {
    margin-left: 10px;
    max-width: 530px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;

    .rp, .date {
      color: #999;
      margin-left: 10px;
    }

    .blog {
      color: #349edf;
      margin-left: 10px;
    }
  }

  .reply {
    margin-left: 10px;
  }

  .op {
    color: #ddd;
    font-weight: lighter;

    .rep {
      color: #349edf;
    }

    .del {
      color: red;
    }
  }

  .op:hover {
    cursor: pointer;
  }
}
</style>
