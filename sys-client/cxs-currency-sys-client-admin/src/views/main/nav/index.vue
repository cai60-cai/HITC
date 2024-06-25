<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAddNav">创建新链接</el-button>

    <el-table v-loading="listLoading" :data="navLinkList" style="width: 100%;margin-top:30px;" border>
      <el-table-column align="center" label="序号" width="80">
        <template slot-scope="{scope, $index}">
          {{ $index + 1 }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="链接名称" width="120">
        <template slot-scope="scope">
          {{ scope.row.navName }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="链接描述" width="150">
        <template slot-scope="scope">
          {{ scope.row.navDesc }}
        </template>
      </el-table-column>
      <el-table-column align="center" :show-overflow-tooltip="true" label="链接地址" width="200">
        <template slot-scope="scope">
          {{ scope.row.navLink }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="链接图标" width="100">
        <template slot-scope="scope">
          <span>
            <svg-icon :icon-class="scope.row.navIcon" />
          </span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="是否登录显示" width="120">
        <template slot-scope="scope">
          <el-switch
            :value="scope.row.navMustLogin === 1"
            active-color="#13ce66"
            inactive-color="#ccc"
          />
        </template>
      </el-table-column>

      <el-table-column align="center" label="链接类型" width="120">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.navType === 1" type="success" effect="dark">
            路由
          </el-tag>
          <el-tag v-else type="success">
            外部链接
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column align="center" label="操作" width="400px" class-name="small-padding fixed-width" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope)">编辑</el-button>
          <el-button type="primary" size="small" @click="unMoveHandle(scope)">上移</el-button>
          <el-button type="primary" size="small" @click="downMoveHandle(scope)">下移</el-button>
          <el-button type="primary" size="small" @click="topMoveHandle(scope)">置顶</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogVisible" :title="dialogType==='edit'?'编辑链接':'新增链接'">
      <el-form :model="nav" label-width="80px" label-position="left">
        <el-form-item label="链接名称">
          <el-input v-model="nav.navName" placeholder="链接名" />
        </el-form-item>

        <el-form-item label="链接描述">
          <el-input v-model="nav.navDesc" placeholder="链接描述" />
        </el-form-item>

        <el-form-item label="链接图标">
          <el-input v-model="nav.navIcon" placeholder="链接图标" />
        </el-form-item>

        <el-form-item label="链接类型">
          <el-select v-model="nav.navType" filterable placeholder="请选择链接类型" @change="navTypeChange($event)">
            <el-option
              label="路由"
              :value="1"
            />
            <el-option
              label="外部链接"
              :value="2"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="链接地址">
          <el-input v-model="nav.navLink" placeholder="链接地址" />
        </el-form-item>

        <el-form-item label="是否登录">
          <el-switch
            v-model="nav.navMustLogin"
            active-text="是"
            inactive-text="否"
            :active-value="1"
            :inactive-value="0"
          />
        </el-form-item>

      </el-form>
      <div style="text-align:right;">
        <el-button type="danger" @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="confirmNav">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { deepClone } from '@/utils'
import { createOrUpdateNavLink, getLinkList, navLinkOrderMoveOrToppingOrDel } from '@/api/nav'

const defaultNav = {
  navId: undefined,
  navName: '',
  navDesc: '',
  navIcon: '',
  navLink: '',
  navMustLogin: 0,
  navRemark: '',
  navType: 1
}

export default {
  name: 'Nav',
  data() {
    return {
      nav: Object.assign({}, defaultNav),
      navLinkList: [],
      listLoading: true,
      dialogVisible: false,
      dialogType: 'new',
      defaultProps: {
        children: 'children',
        label: 'title'
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    navTypeChange(event) {
      this.$forceUpdate()
    },
    getList() {
      this.listLoading = true
      getLinkList().then(res => {
        this.listLoading = false
        this.navLinkList = res.data
      })
    },
    handleAddNav() {
      this.nav = Object.assign({}, defaultNav)
      this.dialogType = 'new'
      this.dialogVisible = true
    },
    handleEdit(scope) {
      this.dialogType = 'edit'
      this.dialogVisible = true
      this.nav = deepClone(scope.row)
    },
    handleDelete({ $index, row }) {
      this.$confirm('确定要删除这个链接吗?', 'Warning', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          const data = {
            navId: row.navId,
            operaType: 4
          }
          await navLinkOrderMoveOrToppingOrDel(data)
          this.$message({
            type: 'success',
            message: '链接删除成功!'
          })
          this.getList()
        })
        .catch(err => {
          console.error(err)
        })
    },
    unMoveHandle(row) {
      const data = {
        navId: row.row.navId,
        operaType: 2
      }
      navLinkOrderMoveOrToppingOrDel(data).then(() => {
        this.$message({
          type: 'success',
          message: '链接上移成功!'
        })
        this.getList()
      })
    },
    downMoveHandle(row) {
      const data = {
        navId: row.row.navId,
        operaType: 3
      }
      navLinkOrderMoveOrToppingOrDel(data).then(() => {
        this.$message({
          type: 'success',
          message: '链接下移成功!'
        })
        this.getList()
      })
    },
    topMoveHandle(row) {
      const data = {
        navId: row.row.navId,
        operaType: 1
      }
      navLinkOrderMoveOrToppingOrDel(data).then(() => {
        this.$message({
          type: 'success',
          message: '链接置顶成功!'
        })
        this.getList()
      })
    },
    async confirmNav() {
      const isEdit = this.dialogType === 'edit'
      if (isEdit) {
        await createOrUpdateNavLink(this.nav)
      } else {
        this.nav.roleId = undefined
        const { data } = await createOrUpdateNavLink(this.nav)
        this.nav.navId = data.navId
        this.navLinkList.push(this.nav)
      }

      const { navDesc, navId, navName } = this.nav
      this.dialogVisible = false
      this.$notify({
        title: 'Success',
        dangerouslyUseHTMLString: true,
        message: `
            <div>nav Id: ${navId}</div>
            <div>nav Name: ${navName}</div>
            <div>Description: ${navDesc}</div>
          `,
        type: 'success'
      })
      this.getList()
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
