<template>
  <div class="app-container">
    <el-button type="primary" @click="handleCreate">创建新分类</el-button>
    <el-table
      v-loading="listLoading"
      :data="list"
      row-key="id"
      border
      fit
      :tree-props="{children: 'children'}"
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="分类id" prop="typeId" sortable="custom" align="center" width="120">
        <template slot-scope="{row}">
          <span>{{ row.id }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分类名称" width="250px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.typeName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分类路由" width="230px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.typeRoute }}</span>
        </template>
      </el-table-column>
      <el-table-column label="分类图标" width="200px" align="center">
        <template slot-scope="{row}">
          <span>
            <i :class="row.typeIcon" />
          </span>
        </template>
      </el-table-column>
      <el-table-column label="是否显示" width="180px" align="center">
        <template slot-scope="{row}">
          <el-switch
            :value="row.typeDel"
            active-color="#13ce66"
            inactive-color="#ccc"
            :active-value="0"
            :inactive-value="1"
          />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="300" class-name="small-padding fixed-width">
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
        <el-form-item label="分类名称" prop="typeName">
          <el-input v-model="temp.typeName" />
        </el-form-item>
        <el-form-item label="分类图标">
          <el-input v-model="temp.typeIcon" />
        </el-form-item>
        <el-form-item label="分类路径">
          <el-input v-model="temp.typeRoute" />
        </el-form-item>
        <el-form-item label="父级分类">
          <el-select v-model="temp.typeParentId" filterable placeholder="请选择" @change="parentTypeChange($event)">
            <el-option
              v-for="item in dataList"
              :key="item.id"
              :label="item.typeName"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否显示">
          <el-switch
            v-model="temp.typeDel"
            active-text="隐藏"
            inactive-text="显示"
            :active-value="1"
            :inactive-value="0"
          />
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
import { createOrUpdateType, deleteAdminTechnologyType, getAdminTechnologyTypeList } from '@/api/technology'

const defaultTemp = {
  typeIcon: '',
  typeName: '',
  typeRoute: '',
  typeParentId: -1,
  typeDel: 0
}
export default {
  name: 'TechnologyType',
  data() {
    return {
      list: [],
      dataList: [],
      listLoading: true,
      currentTypeParent: [],
      checkedParentNode: undefined,
      temp: {
        id: 0,
        typeIcon: '',
        typeName: '',
        typeRoute: '',
        typeParentId: -1,
        typeDel: 0
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '编辑',
        create: '新增'
      },
      rules: {
        typeName: [{ required: true, message: '分类名为必填项', trigger: 'change' }]
      },
      downloadLoading: false
    }
  },
  created() {
    this.getList()
  },
  methods: {
    parentTypeChange(event) {
      this.currentTypeParent = event
      this.$forceUpdate()
    },
    getList() {
      this.listLoading = true
      getAdminTechnologyTypeList().then(response => {
        this.listLoading = false
        this.list = response.data
        this.dataList = this.dataListHandle(response.data)
      })
    },
    dataListHandle(data) {
      const tempList = data.map(d => {
        return d
      })
      tempList.unshift({ id: -1, typeName: '顶级分类' })
      return tempList
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          this.temp.menuId = undefined
          createOrUpdateType(this.temp).then(() => {
            this.dialogFormVisible = false
            this.$nextTick(() => {
              this.temp = defaultTemp
              this.$refs['dataForm'].clearValidate()
              this.getList()
            })
            this.$notify({
              title: 'OK',
              message: '分类创建成功',
              type: 'success',
              duration: 2000
            })
          })
        }
      })
    },
    handleCreate() {
      this.dialogStatus = 'create'
      this.temp = defaultTemp
      this.dialogFormVisible = true
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      if (row.parentId !== -1) {
        this.temp.typeParentId = row.typeParentId
      }
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          const { id, typeName, typeIcon, typeParentId, typeRoute, typeDel } = this.temp
          createOrUpdateType({ id, typeName, typeIcon, typeParentId, typeRoute, typeDel }).then(() => {
            this.dialogFormVisible = false
            this.$notify({
              title: 'OK',
              message: '分类修改成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          })
        }
      })
    },
    handleDelete(row, index) {
      this.$confirm('确定要删除这个分类吗?', '删除分类', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(res => {
        deleteAdminTechnologyType(row.id).then(() => {
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
