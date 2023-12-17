<template>
  <div>
    <el-row :gutter="5">
      <el-col :span="21">
        <el-input placeholder="请输入文章标题" v-model="formData.title"></el-input>
      </el-col>
      <el-col :span="3">
        <el-button v-if="!this.inEdit" style="border-radius: 20px" type="danger" @click="createEssayForm">发布文章</el-button>
        <el-button v-else style="border-radius: 20px" type="danger" @click="createEssayForm">保存文章</el-button>
      </el-col>
    </el-row>
    <el-row>
      <mavon-editor v-model="formData.content" style="margin: 10px auto;min-height: 70vh"/>
    </el-row>
    <el-dialog
        top="5vh"
        title="新建标签"
        :visible.sync="formVisible.tag">
      <el-form v-model="extraFormData.tag" style="text-align: center" inline>
        <el-form-item label="新建标签名称" :label-width="formLabelWidth" prop="name">
          <el-input v-model="extraFormData.tag.name" auto-complete="off" :placeholder="extraFormData.tag.name"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createTagFormCancel">取 消</el-button>
        <el-button type="primary" @click="createTagFormSubmit">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog
        top="5vh"
        title="新建分类"
        :visible.sync="formVisible.type">
      <el-form v-model="extraFormData.type" style="text-align: center" inline>
        <el-form-item label="新建分类名称" :label-width="formLabelWidth" prop="name">
          <el-input v-model="extraFormData.type.name" auto-complete="off" :placeholder="extraFormData.type.name"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createTypeFormCancel">取 消</el-button>
        <el-button type="primary" @click="createTypeFormSubmit">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog top="7vh" class="publish_dialog" :title="this.inEdit ? '保存文章' : '发布文章'" :visible.sync="formVisible.essay">
      <el-form style="text-align: left" :model="formData" :rules="formRules"
               class="publish_form">
        <el-row>
          <el-col :span="12">
            <el-form-item style="height: 200px" label="文章首图" :label-width="formLabelWidth">
              <div>
                <el-image style="max-width: 180px; max-height: 180px" :src="formData.blogFile.publishUrl">
                  <div slot="error" class="image-slot">
                    <i style="font-size: 48px" class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="首图 URL" :label-width="formLabelWidth" prop="blogFile.publishUrl">
              <el-input v-model="formData.blogFile.publishUrl" autocomplete="off" :placeholder="formData.blogFile.publishUrl"></el-input>
            </el-form-item>
            <el-form-item label="是否公开" :label-width="formLabelWidth" prop="isPublic">
              <el-radio-group v-model="formData.isPublic">
                <el-radio-button :label="true" border>公开</el-radio-button>
                <el-radio-button :label="false" border>隐藏</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="文章分类" :label-width="formLabelWidth" prop="type">
              <el-select size="small" v-model="formData.typeId" placeholder="请选择文章分类" style="margin-right: 10px">
                <el-option v-for="type in dataList.types" :label="type.name" :value="type.id" :key="type.id"></el-option>
              </el-select>
              <el-button @click="createTypeForm" size="small">+ 新建分类</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label-width="formLabelWidth" label="文章标签">
          <el-card shadow="never" style="height: 120px; overflow: auto">
            <el-checkbox-group v-model="checkGroupTags.fromForm">
              <el-checkbox v-for="tagId in checkGroupTags.fromData" :label="tagId" :key="tagId">{{ checkGroupTags.nameList[tagId] }}</el-checkbox>
            </el-checkbox-group>
            <el-button @click="createTagForm" size="mini">+ 新建标签</el-button>
          </el-card>
        </el-form-item>
        <el-form-item label="博文概要" :label-width="formLabelWidth" prop="abstractions">
          <el-input v-model="formData.abstractions" autocomplete="off"
                    :placeholder="formData.abstractions" type="textarea" rows="4"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createEssayFormCancel">取消</el-button>
        <el-button v-if="inEdit" type="primary" @click="updateEssayFormSubmit">保存文章</el-button>
        <el-button v-else type="primary" @click="createEssayFormSubmit">发布文章</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data () {
    return {
      formData: {},
      extraFormData: {},
      inEdit: false,
      formVisible: {
        essay: false,
        tag: false,
        type: false
      },
      formLabelWidth: '120px',
      sourcePath: '/essay',
      dataSource: [
        {
          name: 'types',
          path: '/type'
        },
        {
          name: 'tags',
          path: '/tag/pure'
        }
      ],
      dataList: {
        types: [],
        tags: []
      },
      checkGroupTags: {
        fromData: [],
        fromForm: [],
        nameList: []
      },
      formRules: {
        type: [
          {
            required: true,
            message: '请选择类型',
            trigger: 'blur'}
        ],
        tags: [
          {
            required: true,
            message: '请选择至少一种标签',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  created () {
    this.pageDataInit()
  },
  methods: {
    createEssayFormClear () {
      this.formData = {
        id: null,
        typeId: null,
        userId: 1, // 默认管理员
        backgroundPicId: null,
        title: null,
        content: null,
        abstractions: null,
        isPublic: true,
        type: {},
        user: {},
        essayTagMappings: [],
        blogFile: {}
      }
    },
    checkGroupTagClear () {
      this.checkGroupTags = {
        fromData: [],
        fromForm: [],
        nameList: []
      }
    },
    transeferEssayTagMappings () {
      console.log(this.checkGroupTags)
      if (this.inEdit) {
        this.formData.essayTagMappings = this.dataList.tags.filter(tag => this.checkGroupTags.fromForm.indexOf(tag.id) !== -1).map(tag => {
          return {
            essayId: this.formData.id,
            tagId: tag.id
          }
        })
      } else {
        this.formData.essayTagMappings = this.dataList.tags.filter(tag => this.checkGroupTags.fromForm.indexOf(tag.id) !== -1).map(tag => {
          return {
            tagId: tag.id
          }
        })
      }
    },
    initEssayTagMappings () {
      this.checkGroupTags.fromForm = this.formData.essayTagMappings.map(essayTagMapping => essayTagMapping.tag.id)
    },
    extraFormClear () {
      this.extraFormData = {
        type: {
          name: ''
        },
        tag: {
          name: ''
        }
      }
    },
    formDataNullCheck () {
      if (this.formData.blogFile === null) {
        this.formData.blogFile = {}
      }
      if (this.formData.essayTagMappings === null) {
        this.formData.essayTagMappings = []
      }
    },
    pageDataInit () {
      this.checkGroupTagClear()
      this.initDataList()
      this.extraFormClear()
      this.createEssayFormClear()
      if (this.$blogify.status.editEssay) {
        this.formData = this.$blogify.status.editEssay
        this.formDataNullCheck()
        this.initEssayTagMappings()
        this.$blogify.statusClear()
        this.inEdit = true
      } else {
        this.inEdit = false
      }
    },
    initDataList () {
      for (let src of this.dataSource) {
        this.$axios.get(src.path).then(resp => {
          if (resp.data.code === 200) {
            this.dataList[src['name']] = resp.data.data
            if (src['name'] === 'tags') {
              this.checkGroupTags.fromData = this.dataList.tags.map(tag => tag.id)
              for (let tag of this.dataList.tags) {
                this.checkGroupTags.nameList[tag.id] = tag.name
              }
            }
          } else {
            this.$message.info('数据加载失败!')
          }
        }).catch(failResp => {
          console.log(failResp)
        })
      }
    },
    createEssayFormSubmit () {
      this.$confirm('确认提交?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消'
      }).then(() => {
        let elLoading = this.$loading({
          text: '正在创建',
          background: 'rgba(0, 0, 0, 0.8)',
          spinner: 'el-icon-loading'
        })
        this.transeferEssayTagMappings()
        this.$axios.post(this.sourcePath, this.formData).then(resp => {
          elLoading.close()
          if (resp.data.code === 200) {
            this.essayCreateSuccess()
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
    createEssayForm () {
      console.log(this.formData)
      this.formVisible.essay = true
    },
    createEssayFormCancel () {
      this.formVisible.essay = false
    },
    createTagFormSubmit () {
      this.$confirm('确认创建?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消'
      }).then(() => {
        let elLoading = this.$loading({
          text: '正在创建',
          background: 'rgba(0, 0, 0, 0.8)',
          spinner: 'el-icon-loading'
        })
        this.$axios.post('/tag', this.extraFormData.tag).then(resp => {
          elLoading.close()
          if (resp.data.code === 200) {
            this.initDataList()
            this.createTagFormCancel()
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
    createTagForm () {
      this.extraFormClear()
      this.formVisible.tag = true
    },
    createTagFormCancel () {
      this.formVisible.tag = false
      this.extraFormClear()
    },
    createTypeFormSubmit () {
      this.$confirm('确认创建?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消'
      }).then(() => {
        let elLoading = this.$loading({
          text: '正在创建',
          background: 'rgba(0, 0, 0, 0.8)',
          spinner: 'el-icon-loading'
        })
        this.$axios.post('/type', this.extraFormData.type).then(resp => {
          elLoading.close()
          if (resp.data.code === 200) {
            this.initDataList()
            this.createTypeFormCancel()
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
    createTypeForm () {
      this.extraFormClear()
      this.formVisible.type = true
    },
    createTypeFormCancel () {
      this.formVisible.type = false
      this.extraFormClear()
    },
    updateEssayFormSubmit () {
      this.$confirm('确认保存?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消'
      }).then(() => {
        let elLoading = this.$loading({
          text: '正在保存',
          background: 'rgba(0, 0, 0, 0.8)',
          spinner: 'el-icon-loading'
        })
        this.transeferEssayTagMappings()
        this.$axios.put(this.sourcePath + '/' + this.formData.id, this.formData).then(resp => {
          elLoading.close()
          if (resp.data.code === 200) {
            this.essayCreateSuccess()
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
    essayCreateSuccess () {
      this.formVisible.essay = false
      this.$confirm('前往查看博文?', '提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消'
      }).then(() => {
        this.$store.commit('readEssay', this.formData)
        this.$router.replace('/display/read')
      }).catch(failResp => {
        console.log(failResp)
      })
    }
  }
}
</script>

<style scoped lang="less">
.el-form /deep/ .el-form-item__content {
  line-height: 23px;
}

.el-card /deep/ .el-card__body {
  padding: 10px;
}

</style>
