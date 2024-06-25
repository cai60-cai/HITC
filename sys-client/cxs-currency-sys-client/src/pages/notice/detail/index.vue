<template>
  <div class="site-content clean content" style="transform: none;">
    <MyBreadcrumb :breadcrumb="isBread"></MyBreadcrumb>
    <div id="primary" class="content-area" v-if="noticeInfo">
      <h2 class="title">{{ noticeInfo.noticeTitle }}</h2>
      <v-md-editor class="md-content v-md-editor" :value="noticeInfo.noticeContent" mode="preview"></v-md-editor>
      <div class="end-desc" v-if="noticeInfo.user">
        由<span class="strong"> {{noticeInfo.user.nickName}} </span>
        <span v-if="noticeInfo.updateTime">
          修改于 {{noticeInfo.updateTime}}
        </span>
        <span v-else>
          发布于 {{noticeInfo.publishTime}}
        </span>
      </div>
    </div>
    <div id="sidebar" class="widget-area all-sidebar">
      <SysNoticeSide title="系统公告" :list="noticeList"></SysNoticeSide>
    </div>
  </div>
</template>

<script>
import SysNoticeSide from '@/components/side/sys-notice-side'
import {getNoticeInfo, getPushNoticeList} from "@/api/main";
export default {
  name: "NoticeDetail",
  components:{
    SysNoticeSide
  },
  data(){
    return {
      isBread: true,
      noticeInfo: {},
      noticeList: []
    }
  },
  watch: {
    "$route.query": {
      handler: function (val, oldval) {
        getNoticeInfo(val.id).then(res => {
          this.noticeInfo = res.data
        })
      }
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init(){
      const id = this.$route.query.id
      if (id) {
        getNoticeInfo(id).then(res => {
          this.noticeInfo = res.data
        })
        getPushNoticeList().then(res => {
          this.noticeList = res.data
        })
      } else {
        this.$router.back()
      }
    },
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
    width: 70.2%;
    min-height: 60vh;
    transition-duration: .5s;
    background-color: white;

    .v-md-editor ol li {
      list-style: decimal;
      margin: 0 20px;
    }
    .v-md-editor ul li {
      list-style: disc;
      margin: 0 20px;
    }
    .v-md-editor ul li{
      margin: 0px!important;
    }

    .title {
      text-align: center;
      padding: 10px 0;
      box-sizing: border-box;
      font-size: 18px;
      display: flex;
      justify-content: center;
      align-items: center;
      background-color: #f8f8f8;
      border-left: 5px solid #409eff;
      border-right: 5px solid #409eff;

      .self {
        color: #409eff;
        background: rgb(81 107 250 / 10%);
        text-align: center;
        width: 34px;
        height: 20px;
        line-height: 20px;
        border-radius: 2px;
        font-size: 13px;
        margin-right: 8px;
      }
    }

    .end-desc{
      text-align: end;
      margin-right: 25px;
    }
  }

  #sidebar {
    float: right;
    width: 28.8%;
    position: relative;
    overflow: visible;
    box-sizing: border-box;

    h3.aside-title {
      background-color: white;
      color: #3d3d3d;
      padding: 0 16px;
      height: 38px;
      line-height: 38px;
      position: relative;
      font-size: 14px;
      font-weight: bold;
    }

    .item-rank {
      height: 1px;
      background-color: #f5f6f7;
      width: 268px;
      margin: auto;
    }

    .item-tiling > * {
      width: 100%;
    }

    .data-info {
      padding: 9px 10px;

      .text-center {
        text-align: center;
      }
    }

    .data-info.item-tiling .count {
      color: #4a4d52;
      font-size: 14px;
      font-weight: 500;
      line-height: 22px;
    }

    .data-info.item-tiling dd {
      color: #999aaa;
      font-size: 14px;
      line-height: 22px;
      padding: 3px 0;
    }

    .d-flex {
      display: flex !important;
    }

    .justify-content-center {
      -ms-flex-pack: center !important;
      justify-content: center !important;
    }

    .flex-column {
      -webkit-box-orient: vertical !important;
      -webkit-box-direction: normal !important;
      -ms-flex-direction: column !important;
      flex-direction: column !important;
    }

    .widget {
      background-color: white;
      margin: 0 0 10px 0;
      border: 1px solid #ddd;
      border-radius: 2px;
      box-shadow: 0 1px 1px rgb(0 0 0 / 4%);

      #feed_widget {
        .feed-about {
          position: relative;
          font-size: 18px;
          display: block;

          .about-main {
            font-size: 14px;
            padding: 10px 15px 0 15px;
            display: flex;
            align-items: center;

            .about-img {
              img {
                float: left;
                width: 75px;
                height: 75px;
                border-radius: 50%;
                margin: 5px 10px 0 0;
                padding: 2px;
                border: 1px solid #ddd;
              }
            }

            .about-me {
              padding: 5px 0;

              .about-name {
                font-size: 15px;
                font-weight: 700;
                margin-bottom: 5px;
              }
            }
          }

          .clear {
            clear: both;
            display: block;
          }
        }

        a {
          color: #999;
          width: 40px;
          height: 40px;
          display: block;
          text-align: center;
          margin: 0 auto;
          border-radius: 4px;
          border: 1px solid #ddd;
          cursor: pointer;
        }

        a:hover {
          color: #fff;
        }

        span {
          display: block;
        }

        .weixin a:hover {
          background: #409eff;
          border: 1px solid #409eff;
        }

        .tqq a:hover {
          background: #4e91d1;
          border: 1px solid #4e91d1;
        }

        .tsina a:hover {
          background: #c40000;
          border: 1px solid #c40000;
        }

        .feed a:hover {
          background: #d28300;
          border: 1px solid #d28300;
        }

        li {
          float: left;
          width: 25%;
          height: 40px;
          line-height: 38px;
        }

        ul {
          margin: 0 10px;
          overflow: hidden;
          padding: 15px;

          li {
            white-space: nowrap;
            word-wrap: normal;
            text-overflow: ellipsis;
            overflow: hidden;
          }
        }

        .about-inf {
          text-align: center;
          background: #f8f8f8;
          float: left;
          font-size: 14px;
          width: 100%;
          padding: 0 10px;
          border-top: 1px solid #ddd;

          span {
            float: left;
            width: 50%;
            padding: 10px 0 10px 0;
            height: 40px;
          }

          .about-pn {
            border-right: 1px solid #ddd;
          }
        }

      }

      ul {
        padding: 15px;

        li {
          width: 99%;
          line-height: 28px;
          white-space: nowrap;
          word-wrap: normal;
          text-overflow: ellipsis;
          overflow: hidden;
        }
      }
    }
  }
}
</style>
