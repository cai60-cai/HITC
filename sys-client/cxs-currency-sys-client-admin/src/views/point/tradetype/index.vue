<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAddType">创建新类型</el-button>

    <el-table v-loading="listLoading" :data="pointRechargeTypeList" style="width: 100%;margin-top:30px;" border>
      <el-table-column align="center" label="类型id" width="100">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="充值积分" width="200">
        <template slot-scope="scope">
          {{ scope.row.gold }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="原价" width="200">
        <template slot-scope="scope">
          {{ scope.row.money }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="折扣" width="150">
        <template slot-scope="scope">
          {{ scope.row.discount === 0 ? undefined : scope.row.discount }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="是否展示" width="200">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.shows"
            disabled
            active-color="#13ce66"
            inactive-color="#cccccc"
          />
        </template>
      </el-table-column>
      <el-table-column align="center" label="描述" width="200">
        <template slot-scope="scope">
          {{ scope.row.typeDesc ? scope.row.typeDesc.length > 10 ? scope.row.typeDesc.slice(0, 10) + '...' : scope.row.typeDesc : '' }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogVisible" :title="dialogType==='edit'?'编辑类型':'修改类型'">
      <el-form :model="type" label-width="80px" label-position="left">

        <el-form-item label="充值积分">
          <el-input v-model="type.gold" type="number" placeholder="充值积分" />
        </el-form-item>

        <el-form-item label="原价">
          <el-input-number v-model="type.money" :precision="2" :controls="false" placeholder="原价" />
        </el-form-item>

        <el-form-item label="折扣">
          <el-input-number v-model="type.discount" :precision="2" :controls="false" :min="0.1" :max="10.0" placeholder="折扣,折扣价=原价*折扣/10" />
        </el-form-item>

        <el-form-item label="折扣价">
          <el-input v-model="qian" disabled />
        </el-form-item>

        <el-form-item label="是否展示">
          <el-switch
            v-model="type.shows"
            active-color="#13ce66"
            inactive-color="#cccccc"
            active-text="展示"
            inactive-text="不展示"
          />
        </el-form-item>

        <el-form-item label="描述">
          <el-input v-model="type.typeDesc" type="textarea" :rows="2" resize="none" placeholder="描述" />
        </el-form-item>
      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmType">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { deepClone } from '@/utils'
import { getPointRechargeTypeList, removePointRechargeType, saveOrPointRechargeType } from '@/api/point'

const defaultType = {
  id: null,
  gold: 0,
  money: 0.0,
  discount: 10.0,
  shows: true,
  typeDesc: ''
}

export default {
  name: 'TradeType',
  data() {
    return {
      pointRechargeTypeList: [],
      listLoading: true,
      dialogVisible: false,
      dialogType: 'new',
      type: Object.assign({}, defaultType),
      qian: 0
    }
  },
  watch: {
    type: {
      deep: true,
      immediate: true,
      handler: function(newValue, oldVal) {
        if (newValue) {
          const { money, discount } = newValue
          if (money && discount) {
            this.qian = money * discount / 10
          } else {
            this.qian = money
          }
        }
      }
    }
  },
  created() {
    this.init()
  },
  methods: {
    init() {
      getPointRechargeTypeList().then(res => {
        this.listLoading = false
        this.pointRechargeTypeList = res.data
      })
    },

    handleAddType() {
      this.type = Object.assign({}, defaultType)
      this.dialogType = 'new'
      this.dialogVisible = true
    },

    handleEdit(scope) {
      this.dialogType = 'edit'
      this.dialogVisible = true
      this.type = deepClone(scope.row)
      if (this.type.discount === 0) {
        this.type.discount = undefined
      }
    },
    handleDelete({ $index, row }) {
      this.$confirm('确定要删除这个类型吗?', 'Warning', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          await removePointRechargeType(row.id)
          this.pointRechargeTypeList.splice($index, 1)
          this.$message({
            type: 'success',
            message: '类型删除成功!'
          })
        })
        .catch(err => {
          console.error(err)
        })
    },
    async confirmType() {
      const isEdit = this.dialogType === 'edit'
      if (isEdit) {
        await saveOrPointRechargeType(this.type)
        this.$message.success('类型修改成功')
        this.dialogVisible = false
        this.type = deepClone(defaultType)
        this.init()
      } else {
        this.type.id = undefined
        const { data } = await saveOrPointRechargeType(this.type)
        this.pointRechargeTypeList.push(data)
        this.$message.success('类型添加成功')
        this.dialogVisible = false
        this.type = deepClone(defaultType)
        this.init()
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.app-container {
  .roles-table {
    margin-top: 30px;
  }

  .permission-tree {
    margin-bottom: 30px;
  }
}
</style>
