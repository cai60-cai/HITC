<template>
  <div class="app-container">
    <el-button type="primary" @click="handleAddRole">创建新角色</el-button>

    <el-table v-loading="listLoading" :data="rolesList" style="width: 100%;margin-top:30px;" border>
      <el-table-column align="center" label="角色id" width="220">
        <template slot-scope="scope">
          {{ scope.row.roleId }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="角色名称" width="220">
        <template slot-scope="scope">
          {{ scope.row.roleName }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="角色描述" width="220">
        <template slot-scope="scope">
          {{ scope.row.roleDesc }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="操作" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope)">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDelete(scope)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog :visible.sync="dialogVisible" :title="dialogType==='edit'?'编辑角色':'修改角色'">
      <el-form :model="role" label-width="80px" label-position="left">
        <el-form-item label="角色名称">
          <el-input v-model="role.roleName" placeholder="角色名" />
        </el-form-item>

        <el-form-item label="角色描述">
          <el-input v-model="role.roleDesc" placeholder="角色描述" />
        </el-form-item>
        <el-form-item label="菜单权限">
          <el-tree
            ref="tree"
            :check-strictly="checkStrictly"
            :data="routesData"
            :props="defaultProps"
            show-checkbox
            node-key="path"
            class="permission-tree"
          />
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
import path from 'path'
import { deepClone } from '@/utils'
import { getRoutes, getRoles, addRole, deleteRole, updateRole } from '@/api/role'

const defaultRole = {
  roleId: 0,
  roleName: '',
  roleDesc: '',
  routes: []
}

export default {
  name: 'Role',
  data() {
    return {
      role: Object.assign({}, defaultRole),
      routes: [],
      rolesList: [],
      listLoading: true,
      dialogVisible: false,
      dialogType: 'new',
      checkStrictly: false,
      defaultProps: {
        children: 'children',
        label: 'title'
      }
    }
  },
  computed: {
    routesData() {
      return this.routes
    }
  },
  created() {
    // Mock: get all routes and roles list from server
    this.getRoutes()
    this.getRoles()
  },
  methods: {
    async getRoutes() {
      const res = await getRoutes()
      this.serviceRoutes = res.data
      const a = this.generateRoutes(res.data)
      this.routes = a
    },
    getRoles() {
      this.listLoading = true
      getRoles().then(res => {
        this.listLoading = false
        this.rolesList = res.data
      })
    },

    // 重塑路由结构，使其看起来与侧边栏相同
    generateRoutes(routes, basePath = '/') {
      const res = []

      for (let route of routes) {
        // skip some route
        if (route.hidden) {
          continue
        }
        // const onlyOneShowingChild = this.onlyOneShowingChild(route.children, route)

        // if (route.children && onlyOneShowingChild && !route.alwaysShow) {
        //   route = onlyOneShowingChild
        // }

        const data = {
          path: path.resolve(basePath, route.path),
          title: route.meta && route.meta.title
        }

        // recursive child routes
        if (route.children) {
          data.children = this.generateRoutes(route.children, data.path)
        }
        res.push(data)
      }
      return res
    },
    generateArr(routes) {
      let data = []
      routes.forEach(route => {
        data.push(route)
        if (route.children) {
          const temp = this.generateArr(route.children)
          if (temp.length > 0) {
            data = [...data, ...temp]
          }
        }
      })
      return data
    },
    handleAddRole() {
      this.role = Object.assign({}, defaultRole)
      if (this.$refs.tree) {
        this.$refs.tree.setCheckedNodes([])
      }
      this.dialogType = 'new'
      this.dialogVisible = true
    },
    handleEdit(scope) {
      this.dialogType = 'edit'
      this.dialogVisible = true
      this.checkStrictly = true
      this.role = deepClone(scope.row)
      this.$nextTick(() => {
        const routes = this.generateRoutes(this.role.routes)
        const rs = this.generateArr(routes)
        this.$refs.tree.setCheckedNodes(rs)
        // 设置节点的已检查状态不会影响其父节点和子节点
        this.checkStrictly = false
      })
    },
    handleDelete({ $index, row }) {
      this.$confirm('确定要删除这个角色吗?', 'Warning', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(async() => {
          const ids = []
          ids.push(row.roleId)
          const data = {
            ids
          }
          await deleteRole(data)
          this.rolesList.splice($index, 1)
          this.$message({
            type: 'success',
            message: '角色删除成功!'
          })
        })
        .catch(err => {
          console.error(err)
        })
    },
    generateTree(routes, basePath = '/', checkedKeys) {
      const res = []

      for (const route of routes) {
        const routePath = path.resolve(basePath, route.path)

        // recursive child routes
        if (route.children) {
          route.children = this.generateTree(route.children, routePath, checkedKeys)
        }

        if (checkedKeys.includes(routePath) || (route.children && route.children.length >= 1)) {
          res.push(route)
        }
      }
      return res
    },
    async confirmRole() {
      const isEdit = this.dialogType === 'edit'

      const checkedKeys = this.$refs.tree.getCheckedKeys()
      this.role.routes = this.generateTree(deepClone(this.serviceRoutes), '/', checkedKeys)

      if (isEdit) {
        await updateRole(this.role)
        for (let index = 0; index < this.rolesList.length; index++) {
          if (this.rolesList[index].key === this.role.key) {
            this.rolesList.splice(index, 1, Object.assign({}, this.role))
            break
          }
        }
      } else {
        this.role.roleId = undefined
        const { data } = await addRole(this.role)
        this.role.roleId = data.roleId
        this.rolesList.push(this.role)
      }

      const { roleDesc, roleId, roleName } = this.role
      this.dialogVisible = false
      this.$notify({
        title: 'Success',
        dangerouslyUseHTMLString: true,
        message: `
            <div>Role Id: ${roleId}</div>
            <div>Role Name: ${roleName}</div>
            <div>Description: ${roleDesc}</div>
          `,
        type: 'success'
      })
      this.getRoles()
    },
    // reference: src/view/layout/components/Sidebar/SidebarItem.vue
    onlyOneShowingChild(children = [], parent) {
      let onlyOneChild = null
      // 过滤掉hidder为true的
      const showingChildren = children.filter(item => !item.hidden)

      // 当只有一个子路由时，默认情况下显示该子路由
      if (showingChildren.length === 1) {
        onlyOneChild = showingChildren[0]
        onlyOneChild.path = path.resolve(parent.path, onlyOneChild.path)
        return onlyOneChild
      }

      // 如果没有要显示的子路由，则显示父路由
      if (showingChildren.length === 0) {
        onlyOneChild = { ...parent, noShowingChildren: true }
        return onlyOneChild
      }

      return false
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
