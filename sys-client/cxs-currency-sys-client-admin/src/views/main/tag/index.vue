<template>
  <div class="app-container">
    <el-input
      v-if="inputVisible"
      ref="saveTagInput"
      v-model="inputValue"
      class="input-new-tag"
      size="small"
      @keyup.enter.native="createNewTag"
      @blur="resetInput"
    />
    <el-button v-else type="primary" class="button-new-tag" @click="showInput">创建新标签</el-button><br>
    <br>
    <el-tag
      v-for="tag in tagList"
      :key="tag.tagId"
      class="cursor"
      style="margin: 10px 10px 10px 0px"
      closable
      :disable-transitions="false"
      @click="clickHandle(tag)"
      @close="deleteTag(tag)"
    >
      {{ tag.tagName }}
    </el-tag>
    <el-dialog :visible.sync="dialogVisible" title="编辑标签">
      <el-form :model="tag" label-width="80px" label-position="left">
        <el-form-item label="标签名称">
          <el-input v-model="tag.tagName" placeholder="标签名称" />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmRole">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { deleteTag, getTagList, saveOrUpdateTag } from '@/api/tag'

export default {
  name: 'Tag',
  data() {
    return {
      tagList: [],
      inputVisible: false,
      inputValue: '',
      dialogVisible: false,
      tag: {}
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      this.getList()
    },
    getList() {
      getTagList().then(res => {
        this.tagList = res.data
      })
    },
    deleteTag(tag) {
      this.$confirm('确定要删除这个标签吗?', 'Warning', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          await deleteTag(tag.tagId)
          this.tagList.splice(this.tagList.indexOf(tag), 1)
          this.$message({
            type: 'success',
            message: '标签删除成功!'
          })
        })
        .catch(err => {
          console.error(err)
        })
    },

    showInput() {
      this.inputVisible = true
      this.$nextTick(_ => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },

    createNewTag() {
      saveOrUpdateTag({ tagName: this.inputValue }).then(() => {
        this.$message.success('标签创建成功')
        this.inputVisible = false
        this.inputValue = ''
        this.getList()
      })
    },
    resetInput() {
      this.inputVisible = false
      this.inputValue = ''
    },
    clickHandle(tag) {
      this.tag = Object.assign({}, tag)
      this.dialogVisible = true
    },
    confirmRole() {
      saveOrUpdateTag(this.tag).then(() => {
        this.$message.success('标签修改成功')
        this.dialogVisible = false
        this.tag = {}
        this.getList()
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  background-color: white;
  .roles-table {
    margin-top: 30px;
  }

  .permission-tree {
    margin-bottom: 30px;
  }

  .el-tag + .el-tag {
    margin-left: 10px;
  }
  .button-new-tag {
    margin-bottom: 10px;
  }
  .input-new-tag {
    width: 90px;
    margin-bottom: 10px;
    vertical-align: bottom;
  }
}
</style>
