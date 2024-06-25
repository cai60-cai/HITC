<template>
  <div class="app-container">
    <el-button type="primary" @click="handleCreate">创建新菜单</el-button>
    <el-table
      v-loading="listLoading"
      :data="list"
      row-key="menuId"
      border
      fit
      :tree-props="{children: 'children'}"
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column :resizable="false" label="菜单id" prop="menuId" sortable="custom" align="center" width="80">
        <template slot-scope="{row}">
          <span>{{ row.menuId }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" label="菜单标题" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.meta.title }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" label="菜单名称" min-width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" label="菜单图标" width="110px" align="center">
        <template slot-scope="{row}">
          <span>
            <i v-if="row.meta.icon.startsWith('el-icon')" :class="row.meta.icon" />
            <svg-icon v-else :icon-class="row.meta.icon" />
          </span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" label="菜单路径" width="150px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.path }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" label="菜单重定向" width="110px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.redirect }}</span>
        </template>
      </el-table-column>
      <el-table-column :resizable="false" label="开启面包屑" width="110px" align="center">
        <template slot-scope="{row}">
          <el-switch
            :value="row.meta.breadcrumb"
            active-color="#13ce66"
            inactive-color="#ccc"
          />
        </template>
      </el-table-column>
      <el-table-column :resizable="false" label="钉在tags-view" width="110px" align="center">
        <template slot-scope="{row}">
          <el-switch
            :value="row.meta.affix"
            active-color="#13ce66"
            inactive-color="#ccc"
          />
        </template>
      </el-table-column>
      <el-table-column :resizable="false" fixed="right" label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :title="textMap[dialogStatus]" :visible.sync="dialogFormVisible">
      <el-form
        ref="dataForm"
        :rules="rules"
        :model="temp"
        label-position="left"
        label-width="120px"
        style="width: 500px; margin-left:50px;"
      >
        <el-form-item label="菜单标题" prop="title">
          <el-input v-model="temp.meta.title" />
        </el-form-item>
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="temp.name" />
        </el-form-item>
        <el-form-item label="菜单图标">
          <el-input v-model="temp.meta.icon" />
        </el-form-item>
        <el-form-item label="菜单路径" prop="path">
          <el-input v-model="temp.path" />
        </el-form-item>
        <el-form-item label="重定向地址">
          <el-input v-model="temp.redirect" />
        </el-form-item>
        <el-form-item label="父级菜单">
          <el-select v-model="temp.menuParantId" filterable placeholder="请选择" @change="parentMenuChange($event)">
            <el-option
              v-for="item in dataList"
              :key="item.menuId"
              :label="item.meta.title"
              :value="item.menuId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否隐藏">
          <el-switch
            v-model="temp.hidden"
            active-text="隐藏"
            inactive-text="显示"
          />
        </el-form-item>
        <el-form-item label="总是显示">
          <el-switch
            v-model="temp.alwaysShow"
            active-text="显示"
            inactive-text="不显示"
          />
        </el-form-item>
        <el-form-item label="keep-alive缓存">
          <el-switch
            v-model="temp.meta.noCahce"
            active-text="缓存"
            inactive-text="不缓存"
          />
        </el-form-item>
        <el-form-item label="开启面包屑">
          <el-switch
            v-model="temp.meta.breadcrumb"
            active-text="开启"
            inactive-text="关闭"
          />
        </el-form-item>
        <el-form-item label="钉在tags-views">
          <el-switch
            v-model="temp.meta.affix"
            active-text="固定"
            inactive-text="不固定"
          />
        </el-form-item>
        <el-form-item label="激活菜单">
          <el-input v-model="temp.meta.activeMenu" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { createMenu, deleteMenus, getAllMenuTreeList, updateMenu } from '@/api/menu'
import { deepClone } from '@/utils'

const defaultTemp = {
  menuId: undefined,
  name: '',
  redirect: '',
  path: '',
  alwaysShow: false,
  hidden: false,
  menuParantId: -1,
  meta: {
    title: '',
    icon: '',
    noCahce: false,
    breadcrumb: true,
    affix: false,
    activeMenu: ''
  },
  json: ''
}
export default {
  name: 'Menu',
  data() {
    return {
      list: [],
      dataList: [],
      listLoading: true,
      currentMenuParent: [],
      checkedParentNode: undefined,
      temp: {
        menuId: undefined,
        name: '',
        redirect: '',
        path: '',
        alwaysShow: false,
        hidden: false,
        menuParantId: -1,
        meta: {
          title: '',
          icon: '',
          noCahce: false,
          breadcrumb: true,
          affix: false,
          activeMenu: ''
        },
        json: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增'
      },
      rules: {
        name: [{ required: true, message: '菜单名为必填项', trigger: 'change' }],
        path: [{ required: true, message: '菜单路径为必传项', trigger: 'blur' }],
        meta: {
          title: [{ required: true, message: '菜单标题为必传项', trigger: 'change' }]
        }
      },
      downloadLoading: false,
      defaultProps: {
        children: 'children',
        label: function(data, node) {
          return data.meta.title
        }
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parentMenuChange(event) {
      this.currentMenuParent = event
      this.$forceUpdate()
    },
    getList() {
      this.listLoading = true
      getAllMenuTreeList().then(response => {
        this.listLoading = false
        this.list = response.data
        this.dataList = this.handleList(response.data)
      })
    },
    handleList(data) {
      const res = []
      this.toListDF(data, res)
      const parentMenu = {
        menuId: -1,
        name: 'first-menu',
        meta: {
          title: '顶级菜单'
        }
      }
      res.unshift(parentMenu)
      return res
    },
    toListDF(tree, list) {
      for (const node of tree) {
        list.push(node)
        if (node.children.length !== 0) {
          this.toListDF(node.children, list)
        }
      }
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.menuId = undefined
          createMenu(this.temp).then(() => {
            this.list.unshift(this.temp)
            this.dialogFormVisible = false
            this.temp = defaultTemp
            this.getList()
            this.$notify({
              title: 'OK',
              message: '菜单创建成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleCreate() {
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.currentMenuParent = []
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleUpdate(row) {
      // this.temp = Object.assign({}, row) // copy obj
      this.temp = deepClone(row)
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      if (row.parentId !== -1) {
        this.temp.menuParantId = row.parentId
      }

      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateMenu(this.temp).then(() => {
            this.dialogFormVisible = false
            this.temp = defaultTemp
            this.$notify({
              title: 'OK',
              message: '菜单修改成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('确定要删除这个菜单吗?', '删除菜单', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        deleteMenus({ menuIds: [row.menuId] }).then(() => {
          this.$notify({
            title: 'OK',
            message: '删除成功',
            type: 'success',
            duration: 2000
          })
          this.getList()
        })
      }).catch(err => {
        console.error(err)
      })
    }
  }
}
</script>
