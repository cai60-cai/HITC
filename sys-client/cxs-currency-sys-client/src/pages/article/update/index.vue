<template>
  <div class="site-content clean content" style="transform: none;">
    <MyBreadcrumb :breadcrumb="true"></MyBreadcrumb>
    <div id="primary" class="content-area">
      <el-form :model="articleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm"
               style="padding: 20px 0">
        <el-row>
          <el-col :span="24" style="padding: 0 10px;">
            <el-form-item style="margin-bottom: 20px;" label-width="70px" label="标题" prop="articleTitle">
              <el-input v-model="articleForm.articleTitle" style="width: 400px" :rows="1" type="textarea" maxlength="20"
                        show-word-limit class="article-textarea" autosize placeholder="请输入文章标题,不超过20字"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" style="padding: 0 10px;">
            <el-form-item style="margin-bottom: 20px;" label-width="70px" label="摘要" prop="articleAbstract">
              <el-input v-model="articleForm.articleAbstract" style="width: 600px" :rows="1" maxlength="130"
                        show-word-limit type="textarea" class="article-textarea" autosize
                        placeholder="请输入文章摘要，0-130字"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24" style="padding: 0 10px;">
            <div class="postInfo-container">
              <el-row>
                <el-col :span="12">
                  <el-form-item label-width="70px" label="分类" class="postInfo-container-item" prop="articleType">
                    <el-select v-model="articleForm.articleType" filterable default-first-option placeholder="选择分类"
                               style="width: 400px">
                      <el-option-group
                          v-for="group in teachnologyList"
                          :key="group.id"
                          :label="group.typeName">
                        <el-option
                            v-for="item in group.children"
                            :key="item.id"
                            :label="item.typeName"
                            :value="item.id">
                        </el-option>
                      </el-option-group>
                    </el-select>
                  </el-form-item>
                </el-col>

                <el-col :span="12">
                  <el-form-item label-width="90px" label="是否原创" class="postInfo-container-item">
                    <el-switch
                        style="display: block;margin-left: 0;margin-top: 9px;"
                        v-model="articleForm.articleIsSelf"
                        active-color="#13ce66"
                        :active-value="1"
                        :inactive-value="0"
                        active-text="原创"
                        inactive-text="转载">
                    </el-switch>
                  </el-form-item>
                </el-col>
              </el-row>
            </div>
          </el-col>
        </el-row>

        <el-row v-if="articleForm.articleIsSelf === 0">
          <el-col :span="24" style="padding: 0 10px;">
            <el-form-item style="margin-bottom: 20px;" label-width="70px" label="原文地址">
              <el-input v-model="articleForm.originalLink" style="width: 600px" :rows="1" maxlength="80"
                        show-word-limit type="textarea" class="article-textarea" autosize
                        placeholder="转载请输入原文地址"/>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" style="padding: 0 10px;">
            <el-form-item label-width="50px" label="标签" class="postInfo-container-item">
              <el-tag
                  size="small"
                  style="margin-right: 10px"
                  v-for="tag in articleForm.tags"
                  :key="tag.tagId"
                  @close="handleClose(tag)"
                  closable>
                {{ tag.tagName }}
              </el-tag>
            </el-form-item>
            <el-form-item label-width="50px" class="postInfo-container-item">
              <el-row>
                <el-tag
                    size="small"
                    type="info"
                    class="cursor tag"
                    @click="addTag(tag)"
                    style="margin-right: 10px"
                    v-for="tag in tags"
                    :key="tag.tagId">
                  {{ tag.tagName }}
                </el-tag>
              </el-row>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row>
          <el-col :span="24" style="padding: 0 10px;">
            <el-form-item label-width="1px" prop="articleDetail">
              <v-md-editor
                  v-model="articleForm.articleDetail"
                  height="500px"
                  @upload-image="handleUploadImage"
                  :disabled-menus="[]"
              ></v-md-editor>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" style="padding: 0 10px;">
            <el-form-item label-width="60px" label="依赖包" class="postInfo-container-item" prop="file">
              <el-upload
                  class="upload-demo"
                  action=""
                  ref="upload"
                  :file-list="fileList"
                  :limit="1"
                  :on-change="fileChange"
                  :on-remove="fileRemove"
                  :auto-upload="false">
                <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
                <div slot="tip" class="el-upload__tip">只能上传.zip文件，且不超过20mb</div>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" style="padding: 0 10px;">
            <el-form-item label="封面">
              <div class="block" style="width: 300px; height: 250px; border: 1px solid #ccc">
                <el-image fit="fill" style="width: 100%; height: 100%" v-if="articleForm.articleCover" :src="baseUrl + articleForm.articleCover">
                  <div slot="error" class="image-slot">
                    <i class="el-icon-picture-outline"></i>
                  </div>
                </el-image>
              </div>

              <el-button type="primary" size="small" icon="el-icon-upload" style="position: absolute;bottom: 110px;margin-left: 430px;" @click="imagecropperShow=true">
                选择封面
              </el-button>

              <image-cropper
                  v-show="imagecropperShow"
                  :key="imagecropperKey"
                  :width="300"
                  :height="250"
                  field="file"
                  url="/server/base/file/upload"
                  lang-type="zh"
                  @close="close"
                  @crop-upload-success="cropSuccess"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="24" style="padding: 0 10px;">
            <div v-if="articleForm.updateTime" style="text-align: end;margin-bottom: 10px;margin-right: 10px;">
              上次修改: {{ articleForm.updateTime }}
            </div>
            <el-form-item style="text-align: end">
              <el-button type="primary" @click="updateHandle()">确认修改</el-button>
              <el-button @click="canalHandle()">取消</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <el-dialog
          :title="dialogInfo[dialogFlag].title"
          :visible.sync="dialogVisible"
          width="30%">
        <span>{{dialogInfo[dialogFlag].tip}}</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="confirmHandle()">确 定</el-button>
        </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {getTagList, getTechnologyType} from "@/api/main";
import {fileUpload} from "@/api/base";
import {URL_PREFIX} from "@/api/config";
import {
  getArticleFileInfo,
  getUpdateArticleInfo,
  saveOrUpdateArticle,
  saveOrUpdateDraft
} from "@/api/article";
import ImageCropper from '@/components/ImageCropper'
const defaultArticle = {
      articleCover: '',
      articleTitle: '',
      articleType: undefined,
      articleAbstract: '',
      articleIsSelf: 0,
      file: {},
      tags: [],
      articleDetail: '',
      updateTime: ''
    }
export default {
  name: "UpdateArticle",
  components: { ImageCropper },
  data() {
    var checkFile = (rule, value, callback) => {
      let file = this.articleForm.file
      if (file && Object.keys(file).length > 0) {
        let fileName = file.name
        if (fileName) {
          const prefix = fileName.substring(fileName.lastIndexOf('.'))
          if ('.zip' === prefix) {
            if (file.size / 1024 /1024 > 20) {
              callback(new Error('只能上传0-20MB的.zip文件,当前大小:' +  Math.round(file.size / 1024 /1024) + 'MB'));
            } else {
              callback();
            }
          } else {
            callback(new Error('只能上传.zip类型的文件,当前文件类型' + prefix));
          }
        } else {
          callback(new Error('文件名不能为空'));
        }
      } else {
        callback();
      }
    };
    return {
      baseUrl: URL_PREFIX,
      imagecropperKey: 0,
      imagecropperShow: false,
      teachnologyList: [],
      articleForm: {
        articleId: null,
        articleCover: '',
        articleTitle: '',
        articleType: undefined,
        articleAbstract: '',
        articleIsSelf: 0,
        file: {},
        tags: [],
        articleDetail: '',
        updateTime: '',
        fileFlag: 0
      },
      rules: {
        articleTitle: [
            {required: true, message: '文章标题为必填项', trigger: 'blur'},
            {min: 0, max: 20, message: '长度在 0 到 20 个字符', trigger: 'blur'}],
        articleAbstract: [
          {required: true, message: '文章摘要为必填项', trigger: 'blur'},
          {min: 0, max: 130, message: '长度在 0 到 130 个字符', trigger: 'blur'}],
        articleType: [
          {required: true, message: '文章所属类型为必选项', trigger: 'blur'}
        ],
        articleDetail: [
          {required: true, message: '文章详情为必填项', trigger: 'blur'}
        ],
        file: [
          {validator: checkFile, trigger: 'blur'}
        ]
      },
      text: '测试',
      tags: [],
      fileList: [],
      dialogVisible: false,
      dialogInfo: {
        update: {
          title: '修改文章',
          tip: '修改后将进入审核状态,确认修改吗?'
        },
        canal: {
          title: '取消',
          tip: '系统不会保存你的输入信息,确认取消吗?'
        }
      },
      dialogFlag: 'update'
    }
  },
  mounted() {
    const id = this.$route.params.id
    if (id) {
      this.articleForm.articleId = id;
    } else {
      this.$router.back()
    }
    this.init()
  },
  methods: {
    init() {
      getTechnologyType().then(res => {
        this.teachnologyList = res.data
      })
      getTagList().then(res => {
        this.tags = res.data
      })
      getUpdateArticleInfo(this.articleForm.articleId).then(res => {
        if (res.data && Object.keys(res.data).length > 0) {
          this.articleForm = res.data
          this.articleForm.fileFlag = 0
          const {tags, type, fileInfo} = res.data
          if (tags) {
            this.articleForm.tags = tags
          }
          if (type) {
            this.articleForm.articleType = type.id
          }
          if (fileInfo) {
            const file = {
              name: fileInfo.fileName,
              size: fileInfo.fileSize
            }
            this.fileList = [file]
          }
        }
      })
    },
    handleUploadImage(event, insertImage, files) {
      // 拿到 files 之后上传到文件服务器，然后向编辑框中插入对应的内容
      fileUpload(files[0]).then(res => {
        insertImage({
          url: URL_PREFIX + res.data,
          desc: '描述',
          width: 'auto',
          height: 'auto',
        });
      })
    },
    addTag(tag) {
      if (this.articleForm.tags.length >= 5) {
        this.$message.warning('标签最多添加5个')
      } else {
        const findTag = this.articleForm.tags.find(t => t.tagId === tag.tagId)
        if (!findTag) {
          this.articleForm.tags.push(tag)
        } else {
          this.$message.warning('该标签已添加')
        }
      }
    },
    handleClose(tag) {
      this.articleForm.tags.splice(this.articleForm.tags.indexOf(tag), 1);
    },
    fileChange(file, fileList) {
      this.articleForm.fileFlag = 1
      this.articleForm.file = file
    },
    fileRemove(file, fileList){
      this.articleForm.fileFlag = 1
      this.articleForm.file = undefined
    },
    canalHandle(){
      this.dialogFlag = 'canal'
      this.dialogVisible = true
    },
    updateHandle(){
      this.$refs.ruleForm.validate(valid => {
        if (valid) {
          this.dialogFlag = 'update'
          this.dialogVisible = true
        } else {
          return false
        }
      })
    },
    confirmHandle(){
      const flag = this.dialogFlag
      if (flag === 'canal') {
        this.dialogVisible = false
        this.$router.back()
      } else if (flag === 'update') {
        this.updateArticle()
      }
    },
    updateArticle(){
      const loading = this.$loading({
        lock: true,
        text: '正在发布文章、请稍后',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      console.log(this.articleForm);
      saveOrUpdateArticle(this.articleForm).then(res => {
        this.$message.success('文章修改成功、进入审核阶段')
        loading.close()
        this.dialogVisible = false
        this.$nextTick(() => {
          this.articleForm = defaultArticle
          this.fileList = []
          this.$refs.ruleForm.resetFields()
          this.$router.back()
        })
      })
      setTimeout(() => {
        loading.close();
        this.dialogVisible = false;
      }, 2000)
    },

    cropSuccess(resData) {
      this.imagecropperShow = false
      this.imagecropperKey = this.imagecropperKey + 1
      this.articleForm.articleCover = resData
    },
    close() {
      this.imagecropperShow = false
    }
  }
}
</script>

<style scoped lang="scss">


.tag:hover {
  color: #409eff;
}

.article-textarea ::v-deep {
  textarea {
    padding-right: 40px;
    resize: none;
    border: none;
    border-radius: 0px;
    border-bottom: 1px solid #bfcbd9;
  }
}

.content {
  width: 1080px;
  margin: 0 auto 10px;
  transform: none;
  padding: 5px 0;
  box-sizing: border-box;

  #primary {
    float: left;
    width: 100%;
    transition-duration: .5s;
    background-color: white;
  }
}

.el-form-item__content {
  margin-left: 0;
}
</style>
