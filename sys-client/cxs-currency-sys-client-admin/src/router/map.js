import Layout from '@/layout'

export const menuMap = {
  '/user': Layout,
  '/user/add': () => import('@/views/user/add'),
  '/user/update': () => import('@/views/user/update'),
  '/user/delete': () => import('@/views/user/delete'),
  '/system': Layout,
  '/system/role': () => import('@/views/system/role'),
  '/system/user': () => import('@/views/system/user'),
  '/system/menu': () => import('@/views/system/menu'),
  '/system/task': () => import('@/views/system/task'),
  '/system/info': () => import('@/views/system/info'),
  '/dashboard': () => import('@/views/dashboard/index'),
  '/main': Layout,
  '/main/nav': () => import('@/views/main/nav'),
  '/main/friend_link': () => import('@/views/main/friend_link'),
  '/main/tag': () => import('@/views/main/tag'),
  '/main/technology': () => import('@/views/main/technology'),
  '/blog': Layout,
  '/blog/examine': () => import('@/views/blog/examine'),
  '/blog/notice': () => import('@/views/blog/notice'),
  '/blog/editNotice/:id?': () => import('@/views/blog/notice/edit'),
  '/blog/manage': () => import('@/views/blog/manage'),
  '/other': Layout,
  '/other/question': () => import('@/views/other/question'),
  '/other/feedback': () => import('@/views/other/feedback'),
  '/other/report': () => import('@/views/other/report'),
  '/log': Layout,
  '/log/operalog': () => import('@/views/system/log'),
  // 积分交易
  '/point': Layout,
  '/point/trade-type': () => import('@/views/point/tradetype'),
  '/point/order': () => import('@/views/point/order')
}