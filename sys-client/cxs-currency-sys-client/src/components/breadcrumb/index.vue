<template>
  <nav class="breadcrumb">
    <div id="scrolldiv">
      <div id="site-nav-wrap" class="clear">
        <nav id="site-nav" class="main-nav">
          <div class="menu-pcmenu-container">
            <ul id="menu-pcmenu" class="down-menu nav-menu sf-js-enabled sf-arrows">
              <li>
                <el-link :underline="false" @click.stop="toRoute({name:'page-refresh', query: {to: 'index'}})">
                  <i class="el-icon-s-home"></i>
                  <span class="font-text">首页</span>
                </el-link>
              </li>

              <li v-for="item in typeList" :key="item.id">
                <el-link :underline="false" v-if="item.children && item.children.length > 0">
                  <i :class="item.typeIcon"></i>
                  <span class="font-text"> {{ item.typeName }}</span>
                </el-link>
                <el-link :underline="false" target="_blank"
                         v-else-if="item.typeRoute && item.typeRoute.startsWith('http')" :href="item.typeRoute">
                  <i :class="item.typeIcon"></i>
                  <span class="font-text"> {{ item.typeName }}</span>
                </el-link>
                <el-link :underline="false" v-else
                         @click="toRoute({name: 'search-detail', params: {typeId: item.id}})">
                  <i :class="item.typeIcon"></i>
                  <span class="font-text"> {{ item.typeName }}</span>
                </el-link>
                <ul v-if="item.children && item.children.length > 0" class="sub-menu">
                  <li v-for="child in item.children" :key="child.id">
                    <el-link :underline="false" :icon="child.typeIcon"
                             @click="toRoute({name: 'search-detail', params: {typeId: child.id}})"> {{
                        child.typeName
                      }}
                    </el-link>
                  </li>
                </ul>
              </li>
            </ul>
          </div>
        </nav>
      </div>
      <div class="scrolltext" v-if="!breadcrumb">
        <i class="el-icon-bell"></i>
        <ul class="marquee-list">
          <li class="scrolltext-title cursor" @click="toNotice(notice.val)" @mouseenter="mouseenterhandle()" @mouseleave="mouseleavehandle()">
            <transition class="inner-container2" name="breadcrumb" mode="out-in">
              <span class="text2" v-if="notice.val">{{ notice.val.noticeTitle }}</span>
            </transition>
          </li>
        </ul>
      </div>
      <div class="scrolltext" v-else>
        <el-breadcrumb separator-class="el-icon-arrow-right">
          <el-breadcrumb-item :to="{name:'page-refresh', query: {to: 'index'}}">首页</el-breadcrumb-item>
          <el-breadcrumb-item v-for="(item, index) in breadcrumbs" :key="index">{{ item }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
    </div>
  </nav>
</template>

<script>

  import {mapGetters} from "vuex";

  export default {
    name: "MyBreadcrumb",
    props:{
      breadcrumb: {
        type: Boolean,
        default: true
      }
    },
    computed: {
      ...mapGetters(['breadcrumbAppend','simpleNoticeList','typeList']),
      notice() {
        return {
          id: this.number,
          val: this.simpleNoticeList[this.number]
        }
      },
      breadcrumbs (){
        return this.breadcrumbList.concat(this.breadcrumbAppend)
      }
    },
    data(){
      return {
        number: 0,
        timer: undefined,
        breadcrumbList: []
      }
    },
    mounted() {
      this.init()
      this.startMove()
    },
    methods:{
      init(){
        const matchedRoutes = this.$route.matched
        if (matchedRoutes) {
          const titleList = matchedRoutes.filter(m => m.name).map(m => m.meta.title).filter(r => r)
          if(titleList && titleList.length > 0) {
            this.breadcrumbList = titleList
          }
        }
      },
      startMove() {
        this.timer = setTimeout(() => {
          if (this.number === this.simpleNoticeList.length - 1) {
            this.number = 0;
          } else {
            this.number = this.number + 1;
          }
          this.startMove();
        }, 3000)
      },
      mouseenterhandle(){
        clearInterval(this.timer)
      },
      mouseleavehandle(){
        this.startMove()
      },
      toNotice(val) {
        this.$router.push({name:'notice-detail', query:{id: val.id}})
      },
      toRoute(to) {
        this.$store.dispatch('header/setbreadcrumbAppend', [])
        this.$router.push(to)
      },
    }
  }
</script>

<style scoped lang="less">
.breadcrumb {
  background: #ffffff;
  // margin-top: 5px !important;
  margin-bottom: 5px !important;
  // height: 30px;
  // line-height: 30px;
  margin: 0 auto;
  padding-left: 10px;

  .bull {
    float: left;
    font-size: 14px;
    color: #666;
    margin-right: 8px;
  }

  #scrolldiv {


    #site-nav-wrap {
      max-width: 100%;
      border-bottom: 1px solid #f1f1f1;
      #sidr-close {
        display: none;
      }

      .main-nav {
        margin: 0;
        a {
          color: #999;
          line-height: 25px;
          padding: 0 8px
        }

        .menu-pcmenu-container {
          .nav-menu {
            float: left;
            margin: 0;
            padding: 0;
            list-style: none;

            li {
              display: block;
              float: left;
              height: 50px;
              line-height: 50px;
              position: relative;
              white-space: nowrap;

              a {
                padding: 0 13px;
                color: #444;
                text-align: left;
                transition-duration: .5s;
                display: block;
                position: relative;
                zoom: 1;
                height: 50px;
                line-height: 50px;

                i {
                  margin-right: 1px;
                }
              }

              a:hover {
                color: #fff !important;
                background: #409eff;
              }

              .sub-menu {
                padding-top: 0;
                box-shadow: 0 2px 2px rgb(0 0 0 / 3%);
                position: absolute;
                display: none;
                top: 100%;
                left: 0;
                min-width: 160px;
                z-index: 10001;
                border-bottom: 1px solid #ddd;

                li {
                  line-height: 1.4em;
                  height: auto;
                  float: none;
                  display: block;
                  background: rgba(255, 255, 255, 0.9);
                  border-top: 1px solid #ddd;
                  border-left: 1px solid #ddd;
                  border-right: 1px solid #ddd;

                  a {
                    display: block;
                    height: auto;
                    line-height: 1.7em;
                    padding: 10px 20px;
                    margin: 0px;
                    border: 0px;
                    text-transform: none;
                    font-size: 0.9em;
                  }
                }
              }
            }

            li:hover > .sub-menu {
              display: block;
            }
          }
        }
      }
    }

    .scrolltext-mobile{
      width: 100%;
      height: 30px;
      line-height: 30px;
      overflow: hidden;
      display: flex;
      align-items: center;

      .marquee-list {
        width: 100%;
        height: 30px;
        margin-left: 5px;
        //li:first-child{
        //  animation: show 6s linear infinite;
        //}
      }

      ul {
        li {
          height: 30px;
          text-overflow: ellipsis;
          white-space: nowrap;
          word-wrap: normal;
          overflow: hidden;

          a {
            text-decoration: none;
          }

          a:visited {
            color: #555;
          }
        }
      }
    }


    .scrolltext {
      width: 70%;
      height: 30px;
      line-height: 30px;
      overflow: hidden;
      display: flex;
      align-items: center;

      .marquee-list {
        width: 100%;
        height: 30px;
        margin-left: 5px;
        //li:first-child{
        //  animation: show 6s linear infinite;
        //}
      }

      ul {
        li {
          height: 30px;
          text-overflow: ellipsis;
          white-space: nowrap;
          word-wrap: normal;
          overflow: hidden;

          a {
            text-decoration: none;
          }

          a:visited {
            color: #555;
          }
        }
      }
    }
  }
}
</style>
