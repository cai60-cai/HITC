<template>
  <div class="widget about">
    <h3 class="aside-title widget-title cursor" @click="$router.push({name: 'system-info'})">关于本站</h3>
    <div class="item-rank"></div>
    <div id="feed_widget">
      <div class="feed-about">
        <div class="about-main">
          <div class="about-img">
            <img class="cursor" v-if="sysInfo.sysLogo" @click="$router.push({name: 'system-info'})" src="/server/upload/20240608/48782e8c448143bf89d4dafe336f862920240608212937.png" :alt="sysInfo.sysTitle">
          </div>
          <div class="about-me">
            <div class="about-name cursor" @click="$router.push({name: 'system-info'})"> {{ sysInfo.sysTitle }}</div>
            <div class="about-the">{{ sysInfo.sysGraph }}</div>
          </div>
        </div>
        <div class="clear"></div>
      </div>
    </div>
    <div class="data-info d-flex item-tiling">
      <dl class="text-center" id="fanBox" :title="sysInfo.articleCount">
        <dt><span class="count" id="fan">{{ sysInfo.articleCount }}</span></dt>
        <dd>文章</dd>
      </dl>
      <dl class="text-center" :title="sysInfo.originArticleCount">
        <dt><span class="count">{{ sysInfo.originArticleCount }}</span></dt>
        <dd>原创</dd>
      </dl>
      <dl class="text-center" :title="sysInfo.userCount">
        <dt><span class="count">{{ sysInfo.userCount }}</span></dt>
        <dd>用户</dd>
      </dl>
      <dl class="text-center" :title="sysInfo.tagCount">
        <dt><span class="count">{{ sysInfo.tagCount }}</span></dt>
        <dd>标签</dd>
      </dl>
      <dl class="text-center" :title="sysInfo.typeCount">
        <dt><span class="count">{{ sysInfo.typeCount }}</span></dt>
        <dd>分类</dd>
      </dl>
    </div>
    <div class="clear"></div>
  </div>
</template>

<script>
import {URL_PREFIX} from "@/api/config";
import {mapGetters} from "vuex";

export default {
  name: "AboutSystemSlide",
  data(){
    return {
      baseUrl: URL_PREFIX
    }
  },
  computed:{
    ...mapGetters(['sysInfo'])
  }
}
</script>

<style lang="less" scoped>
ol, ul, li {
  list-style: none;
}
.content {
  width: 1080px;
  margin: 0 auto 10px;
  transform: none;
  padding: 5px 0;
  box-sizing: border-box;

  #primary {
    float: left;
    width: 70.2%;
    min-height: 80vh;
    transition-duration: .5s;
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
