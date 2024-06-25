import { asyncRoutes, constantRoutes } from '@/router'
import { getMenusTree } from '@/api/role'
import { menuMap } from '@/router/map'

/**
 * Use meta.role to determine if the current user has permission
 * @param roles
 * @param route
 */
function hasPermission(roles, route) {
  if (route.meta && route.meta.roles) {
    return roles.some(role => route.meta.roles.includes(role))
  } else {
    return true
  }
}

function menuRouteHandle(menus) {
  if (!menus || menus.length < 1) {
    return
  } else {
    menus.forEach(menu => {
      menu['component'] = menuMap[menu['path']]
      if (menu.children && menu.children.length > 0) {
        menuRouteHandle(menu.children)
      }
    })
  }
}

/**
 * Filter asynchronous routing tables by recursion
 * @param routes asyncRoutes
 * @param roles
 */
export function filterAsyncRoutes(routes, roles) {
  const res = []

  routes.forEach(route => {
    const tmp = { ...route }
    if (hasPermission(roles, tmp)) {
      if (tmp.children) {
        tmp.children = filterAsyncRoutes(tmp.children, roles)
      }
      res.push(tmp)
    }
  })

  return res
}

const state = {
  routes: [],
  addRoutes: []
}

const mutations = {
  SET_ROUTES: (state, routes) => {
    state.addRoutes = routes
    state.routes = constantRoutes.concat(routes)
  }
}

const actions = {
  generateRoutes({ commit }, roles) {
    return new Promise((resolve, reject) => {
      let accessedRoutes
      getMenusTree().then(res => {
        const { data } = res
        menuRouteHandle(data)
        data.push(...asyncRoutes)
        if (roles.includes('super_admin')) {
          accessedRoutes = data || []
        } else {
          accessedRoutes = filterAsyncRoutes(data, roles)
        }
        commit('SET_ROUTES', accessedRoutes)
        resolve(accessedRoutes)
      }).catch(err => {
        reject(err)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
