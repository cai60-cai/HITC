import router from './router'
import store from './vuex/store'
import { Message } from 'element-ui'
import { getToken } from '@/utils/auth'
const whiteList = ['/', '/index', '/auth-redirect'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // determine whether the user has logged in
  const hasToken = getToken()
  store.dispatch('auth/setToken', hasToken)
  if (hasToken) {
    store.dispatch('auth/setLoginStatus', true)
    try {
      const res = await store.dispatch('auth/getInfo')
      next()
    } catch (error) {
      await store.dispatch('auth/resetToken')
      if (to.meta.auth) {
        store.dispatch('auth/setLoginFlag', true)
      } else {
        next()
      }
    }
  } else {
    if (to.meta.auth) {
      router.push('/')
      store.dispatch('auth/setLoginFlag', true)
    } else {
      next()
    }
  }
})
