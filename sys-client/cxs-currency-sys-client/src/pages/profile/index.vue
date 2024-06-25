<template>
  <div class="site-content clean content" style="transform: none;">
    <MyBreadcrumb v-if="refresh" :breadcrumb="isBread"></MyBreadcrumb>
    <div id="primary" class="content-area">
      <div class="left-nav">
        <div class="left-nav-top" v-if="userInfo">
          <div class="avatar">
            <el-avatar v-if="user.avatar" style="margin-right: 10px;border: 1px solid #cccccc" :size="100" :src="baseURL + user.avatar"></el-avatar>
            <el-avatar v-else style="margin-right: 10px;border: 1px solid #cccccc" :size="100" :src="user.avatar"></el-avatar>
          </div>
          <div class="avatar-info">
            <div class="nickname" :title="userInfo.nickName"><i class="iconfont icon-xingming"></i>{{ userInfo.nickName }}</div>
            <div class="point" title="积分余额"><i class="iconfont icon-jifen"></i>{{ userInfo.point }}</div>
<!--            <div class="gs-address" title="归属地">-->
<!--              <i class="iconfont icon-dizhi"></i>{{ userInfo.address }}-->
<!--            </div>-->
          </div>
        </div>
        <div class="left-nav-main">
          <router-link class="link-item" to="/profile/personal">个人资料</router-link>
          <router-link class="link-item" to="/profile/setting">个性化设置</router-link>
          <router-link class="link-item" to="/profile/updatepwd">修改密码</router-link>
          <router-link class="link-item" to="/profile/loginlog">登录日志查看</router-link>
          <router-link class="link-item" to="/profile/pushinfo">我的发布</router-link>
          <router-link class="link-item" to="/profile/collinfo">我的收藏</router-link>
          <router-link class="link-item" to="/profile/commentinfo">我的评论</router-link>
          <router-link class="link-item" to="/profile/tradeflow">积分交易流水</router-link>
          <router-link class="link-item" to="/profile/feedback">我的反馈</router-link>
          <router-link class="link-item" to="/profile/report">我的举报</router-link>
<!--          <router-link class="link-item" to="/profile/order">我的订单</router-link>-->
        </div>
      </div>
      <div class="right-view">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script>
  import AboutSystem from "@/components/side/about-system";
  import SysNoticeSide from "@/components/side/sys-notice-side";
  import {mapGetters} from "vuex";
  import {getPersonal} from "@/api/user";
  import { URL_PREFIX } from '@/api/config'

  export default {
    name: "Profile",
    components:{
      AboutSystem,
      SysNoticeSide
    },
    data(){
      return {
        baseURL: URL_PREFIX,
        isBread: true,
        refresh: true,
        userInfo:{}
      }
    },
    watch:{
      '$route.path':{
        immediate: true,
        handler: function (old, newValue) {
          this.refresh = false
          this.$store.dispatch('header/setbreadcrumbAppend', [])
          this.$nextTick(() => {
            this.refresh = true
          })
        }
      }
    },
    mounted() {
      this.init();
    },
    methods:{
      init(){
        getPersonal().then(res => {
          this.userInfo = res.data
        })
      }
    },
    computed:{
      ...mapGetters(['user'])
    }
  }
</script>

<style lang="less" scoped>
.content {
  width: 1080px;
  margin: 0 auto 10px;
  transform: none;
  padding: 5px 0;
  box-sizing: border-box;

  #primary {
    float: left;
    width: 100%;
    min-height: 80vh;
    transition-duration: .5s;
    display: flex;
    background-color: white;


    .left-nav{
      width: 25%;
      padding: 10px;
      border-right: 1px solid #cccccc;
      box-sizing: border-box;

      .left-nav-top{
        display: flex;
        align-items: center;
        padding: 5px 10px;
        padding-right: 0px;

        .avatar{
          width: 100px;
          height: 100px;
          margin-right: 10px;
        }
        .avatar-info{
          .nickname{
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            width: 145px;
          }
          div{
            height: 25px;
            margin-bottom: 5px;
            padding: 2px;
            line-height: 25px;
          }
          i{
            margin-right: 2px;
          }
        }
      }

      .left-nav-main{
        margin-top: 20px;
        padding-left: 20px;
        .link-item{
          display: block;

        }

      }
    }

    .right-view{
      width: 75%;
      padding: 20px;
    }
  }
}
</style>
