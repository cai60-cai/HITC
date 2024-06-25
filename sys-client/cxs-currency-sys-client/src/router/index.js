import Vue from 'vue'   // 引入vue
import VueRouter from 'vue-router'   //引入vue-router插件
import Layout from '@/pages/layout'
Vue.use(VueRouter)  // 应用插件

// 修改VueRouter原型的push
const originPush = VueRouter.prototype.push
const originReplace = VueRouter.prototype.replace
VueRouter.prototype.push = function (location,onResolved,onRejectesd){
    if(onResolved === undefined && onRejectesd === undefined){
        return originPush.call(this,location).catch(()=>{});
    }else{
        // 调用时传递了成功或失败的回调
        return originPush.call(this,location,onResolved,onRejectesd);
    }
}
VueRouter.prototype.replace = function (location,onResolved,onRejectesd){
    if(onResolved === undefined && onRejectesd === undefined){
        return originReplace.call(this,location).catch(()=>{});
    }else{
        // 调用时传递了成功或失败的回调
        return originReplace.call(this,location,onResolved,onRejectesd);
    }
}

// 创建路由器
const router = new VueRouter({
    mode: 'history',
    // 路由配置对象
    routes:[
        {
            path:'/',
            component: Layout,
            redirect: '/index',
            children:[
                {
                    path: 'index',
                    component: () => import('@/pages/Index'),
                    name: 'index'
                },
                {
                    path: 'resource/detail',
                    component: () => import('@/pages/resource/detail'),
                    name: 'resource-detail'
                },
                {
                    path: 'search/detail/:typeId?',
                    component: () => import('@/pages/search/detail'),
                    name: 'search-detail',
                    meta: {
                        title: '搜索'
                    }
                },
                {
                    path: 'notice/detail',
                    component: () => import('@/pages/notice/detail'),
                    name: 'notice-detail',
                    meta: {
                        title: '公告详情'
                    }
                },
                {
                    path: 'article/publish',
                    component: () => import('@/pages/article/publish'),
                    name: 'article-publish',
                    meta: {
                        title: '发布文章',
                        auth: true
                    }
                },
                {
                    path: 'article/update/:id?',
                    component: () => import('@/pages/article/update'),
                    name: 'article-update',
                    meta: {
                        title: '修改文章',
                        auth: true
                    }
                },
                {
                    path: 'article/detail/:id?',
                    component: () => import('@/pages/article/detail'),
                    name: 'article-detail',
                    meta: {
                        title: '文章详情'
                    }
                },
                {
                    path: 'refresh',
                    component: () => import('@/pages/refresh'),
                    name: 'page-refresh'
                },
                {
                    path: 'system/info',
                    component: () => import('@/pages/system/info'),
                    name: 'system-info',
                    meta: {
                        title: '关于本站'
                    }
                },
                {
                    path: 'profile',
                    component: () => import('@/pages/profile'),
                    name: 'profile',
                    meta: {
                        title: '个人中心',
                        auth: true
                    },
                    redirect: '/profile/personal',
                    children:[
                        {
                            path: '/profile/personal',
                            component: () => import('@/pages/profile/personal'),
                            name: 'personal',
                            meta: {
                                title: '个人资料',
                                auth: true
                            }
                        },
                        {
                            path: '/profile/setting',
                            component: () => import('@/pages/profile/setting'),
                            name: 'setting',
                            meta: {
                                title: '个性化设置',
                                auth: true
                            }
                        },
                        {
                            path: '/profile/collinfo',
                            component: () => import('@/pages/profile/collinfo'),
                            name: 'collinfo',
                            meta: {
                                title: '我的收藏',
                                auth: true
                            }
                        },
                        {
                            path: '/profile/commentinfo',
                            component: () => import('@/pages/profile/commentinfo'),
                            name: 'commentinfo',
                            meta: {
                                title: '我的评论',
                                auth: true
                            }
                        },
                        {
                            path: '/profile/loginlog',
                            component: () => import('@/pages/profile/loginlog'),
                            name: 'loginlog',
                            meta: {
                                title: '登录日志',
                                auth: true
                            }
                        },
                        {
                            path: '/profile/pushinfo',
                            component: () => import('@/pages/profile/pushinfo'),
                            name: 'pushinfo',
                            meta: {
                                title: '我的发布',
                                auth: true
                            }
                        },
                        {
                            path: '/profile/updatepwd',
                            component: () => import('@/pages/profile/updatepwd'),
                            name: 'updatepwd',
                            meta: {
                                title: '修改密码',
                                auth: true
                            }
                        },
                        {
                            path: '/profile/tradeflow',
                            component: () => import('@/pages/profile/tradeflow'),
                            name: 'tradeflow',
                            meta: {
                                title: '积分交易流水',
                                auth: true
                            }
                        },
                        {
                            path: '/profile/feedback',
                            component: () => import('@/pages/profile/feedback'),
                            name: 'feedback',
                            meta: {
                                title: '我的反馈',
                                auth: true
                            }
                        },
                        {
                            path: '/profile/report',
                            component: () => import('@/pages/profile/report'),
                            name: 'report',
                            meta: {
                                title: '我的举报',
                                auth: true
                            }
                        },
                        {
                            path: '/profile/reportInfo/:id?',
                            component: () => import('@/pages/profile/report/info'),
                            name: 'reportInfo',
                            meta: {
                                title: '举报详情',
                                auth: true
                            }
                        },
                        {
                            path: '/profile/feedbackInfo/:id?',
                            component: () => import('@/pages/profile/feedback/info'),
                            name: 'feedbackInfo',
                            meta: {
                                title: '反馈详情',
                                auth: true
                            }
                        },
                        {
                            path: '/profile/test',
                            component: () => import('@/pages/test'),
                            name: 'test',
                            meta: {
                                title: '测试'
                            }
                        },
                        {
                            path: '/profile/order',
                            component: () => import('@/pages/profile/order'),
                            name: 'order',
                            meta: {
                                title: '我的订单',
                                auth: true
                            }
                        }
                    ]
                },
                {
                    path: 'sysmsg',
                    component: () => import('@/pages/sysmsg'),
                    name: 'Sysmsg',
                    meta: {
                        title: '系统消息',
                        auth: true
                    }
                },
                {
                    path: 'fallback',
                    component: () => import('@/pages/fallback'),
                    name: 'fallback',
                    meta: {
                        title: '用户反馈'
                    }
                },
                {
                    path: 'relaQuestion/:id?',
                    component: () => import('@/pages/relaQuestion'),
                    name: 'relaQuestion',
                    meta: {
                        title: '相关问题'
                    }
                }
            ]
        },
        {
            path: '/mobileMsg',
            component: () => import('@/pages/message/mobile-message'),
            name: 'MobileMessage'
        },
    ]
})
export default router;
