import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/vuex/store'
import { getToken } from '@/utils/auth'
import router from "@/router";

// create an axios instance
const service = axios.create({
  // baseURL: '/server', // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 200000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['access_token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const { status } = response
    if (status === 200) {
      const res = response.data
      const { code } = res
      if (code) {
        if (res.code !== 200) {
          Message({
            message: res.msg || 'Error',
            type: 'error',
            duration: 5 * 1000
          })

          // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
          if (res.code === 401) {
            store.dispatch('auth/resetToken')
            store.dispatch('auth/setLoginStatus', false)
            router.replace(router.currentRoute.path)
            store.dispatch('auth/setLoginFlag', true)
          }
          if (res.code === 201) {
            return Promise.reject(new Error(res.message || 'Error'))
          }
          return new Promise(() => {})
        } else {
          return res
        }
      } else {
        return response
      }
    } else if (status === 401) {
      store.dispatch('auth/resetToken')
      store.dispatch('auth/setLoginStatus', false)
      store.dispatch('auth/setLoginFlag', true)
    } else {
      console.log(response)
      // return Promise.reject(new Error(res.message || 'Error'))
    }
    // if the custom code is not 20000, it is judged as an error.
  },
  error => {
    // console.log('err' + error) // for debug
    // Message({
    //   message: error.message,
    //   type: 'error',
    //   duration: 5 * 1000
    // })
    return Promise.reject(error)
  }
)
export default service
