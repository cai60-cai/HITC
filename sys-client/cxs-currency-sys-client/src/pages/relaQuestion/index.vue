<template>
  <div class="site-content clean content" style="transform: none;">
    <MyBreadcrumb :breadcrumb="true"></MyBreadcrumb>
    <div id="primary" class="content-area">
      <h1 style="font-size: 19px; padding: 5px 10px">问题: {{questionInfo.question}}</h1>
      <div>
        <v-md-editor :value="questionInfo.questionAnswer" mode="preview"></v-md-editor>
      </div>
    </div>

    <div id="sidebar" class="widget-area all-sidebar">
      <FallbackSide title="系统相关问题" :list="questionList"></FallbackSide>
    </div>
  </div>
</template>
<script>
import FallbackSide from "@/components/side/fallback-side";
import {URL_PREFIX} from "@/api/config";
import {getToken} from "@/utils/auth";
import {FALLBACK_TYPE} from "@/utils/constant";
import {questionInfo, questionList} from "@/api/main";
import {addFeedback} from "@/api/feedback";
export default {
  name: 'RelaQuestion',
  components: {FallbackSide},
  data() {
    return {
      id:0,
      questionList:[],
      baseUrl: URL_PREFIX,
      questionInfo:{}
    };
  },
  mounted() {
    this.init();
  },
  watch:{
    "$route.params" :{
      handler: function (newsValue, oldValue) {
        this.id = newsValue.id
        this.getQuestionInfo(this.$route.params.id)
      }
    }
  },
  methods: {
    init(){
      questionList().then(res => {
        this.questionList = res.data
      })
      this.id = this.$route.params.id
      this.getQuestionInfo(this.$route.params.id)
    },
    getQuestionInfo(id){
      questionInfo(id).then(res => {
        this.questionInfo = res.data
      })
    }
  }
}
</script>

<style scoped lang="scss">
.sidebar-enter, .sidebar-leave-to {
  margin-right: -100%;
  opacity: 0;
}

.sidebar-enter-to, .sidebar-leave {
  margin-right: 0%;
  opacity: 1;
}

.sidebar-enter-active, .sidebar-leave-avtive {
  transition: all 0.5s linear;
}

.width-full {
  width: 100% !important;
}

.content {
  width: 1080px;
  margin: 0 auto 10px;
  transform: none;
  padding: 5px 0;
  box-sizing: border-box;

  .demo-ruleForm{
    padding: 10px;
  }

  .content-area {
    float: left;
    width: 70.2%;
    transition-duration: .5s;
    background-color: white;
    padding: 10px;
    min-height: 400px;

    .content-all {

      .content-header {
        height: 50px;
        background-color: white;
        position: relative;

        ol, ul, li {
          list-style: none;
        }

        .back-list {
          position: absolute;
          color: #999;
          top: 12px;
          left: 15px;
          margin: 0;
          width: 60px;
          border: 1px solid #ddd;
          background: white;
          cursor: pointer;
          outline: none;
          display: flex;
          justify-content: center;
          align-items: center;
        }

        .article-type {
          display: flex;
          align-items: center;
          height: 50px;
          line-height: 50px;
          padding: 0 10px;

          .item {
            cursor: pointer;
            margin-right: 5px;
          }
        }

        .back-list:hover {
          background-color: #409eff;
          color: white;
        }

        .single-meta {
          position: absolute;
          top: 15px;
          right: 15px;

          a {
            cursor: pointer;
          }

          a:hover {
            background-color: #409eff;
            color: white;
          }

          .download {
            a:hover {
              background-color: #409eff;
              color: white;
            }
          }

          .slider:hover {
            background-color: #409eff;
            color: white;
          }

          .comment {
            a:hover {
              background-color: #409eff;
              color: white;
            }
          }

          li {
            float: left;

            a {
              color: #999;
              line-height: 26px;
              margin: 0 5px 0 0;
              padding: 0 8px;
              display: block;
              border: 1px solid #ddd;
              border-radius: 2px;
            }
          }

          .views {
            color: #999;
            line-height: 26px;
            margin: 0 5px 0 0;
            padding: 0 8px;
            display: block;
            border: 1px solid #ddd;
            border-radius: 2px;
            cursor: pointer;
          }
        }
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

      .content-footer {

        .article-file {
          background-color: #f8f8f8;
          border-left: 5px solid #409eff;
          border-right: 5px solid #409eff;
          line-height: 50px;
          padding: 0 10px;

          .file-info {
            height: 50px;
            display: flex;
            justify-content: space-between;
            align-items: center;
          }
        }

        .reward {
          position: relative;
          margin: 50px auto;

          a {
            cursor: pointer;
          }

          .i-active {
            color: #409eff;
          }

          .social-main {
            position: relative;
            margin: 0 auto;
            width: 243px;

            span {
              float: left;
            }

            .like {
              a {
                background: #fff;
                width: 120px;
                display: block;
                border: 1px solid #ddd;
                float: left;
                padding-left: 15px;
                height: 37.6px;
                line-height: 37.6px;
              }

              a:hover {
                background-color: #409eff;
                color: white;
              }
            }

            .shang-p {
              a {
                position: absolute;
                background: #fff;
                left: 96px;
                top: -5px;
                width: 48px;
                height: 48px;
                font-size: 16px;
                line-height: 45px;
                display: block;
                border: 1px solid #ddd;
                border-radius: 40px;
              }

              .shang-empty {
                position: absolute;
                left: 90px;
                top: 0px;
                width: 62px;
                height: 38px;
                overflow: hidden;

                span {
                  background: #fff;
                  width: 60px;
                  height: 60px;
                  display: block;
                  margin: -10px 0 0 0;
                  border-radius: 60px;
                  border: 1px solid #ddd;
                }
              }

              .shang-s {
                height: 37px;

                a {
                  text-align: center;
                }

                a:hover {
                  background-color: #409eff;
                  color: white;
                }
              }
            }

            .share-sd {

              .share-s {

                a {
                  background: #fff;
                  width: 120px;
                  display: block;
                  border: 1px solid #ddd;
                  float: left;
                  padding-left: 40px;
                  height: 37.6px;
                  line-height: 37.6px;
                  margin-top: -25px;
                }

                a:hover {
                  background-color: #409eff;
                  color: white;
                }
              }

              #share {
                position: absolute;
                top: -60px;
                right: -29px;
                width: 302px;
                height: 68px;
                display: none;
                z-index: 999;
              }
            }
          }
        }
      }

      .article-extra {
        margin-top: 20px;
        padding: 0 10px;

        .article-type {
          height: 50px;
          line-height: 50px;
        }
        .article-tag {
          height: 50px;
          line-height: 50px;
          background-color: white;

          .cursor:first-child {
            margin-left: 10px;
          }
        }
      }

      .divider {
        height: 15px;
        background-color: #f1f1f1;
      }

      .share-notice{
        .authorbio{
          background: #fff;
          padding: 20px;
          border: 1px solid #ddd;
          box-shadow: 0 1px 1px rgb(0 0 0 / 4%);
          border-radius: 2px;

          ol, ul, li {
            list-style: none;
          }

          .avatar {
            float: left;
            width: 40px;
            height: 40px;
            margin: 5px 10px 0 0;
            padding: 0;
          }
        }
      }
    }
  }

  #sidebar {
    float: right;
    width: 28.8%;
    position: relative;
    overflow: visible;
    box-sizing: border-box;

    h3 {
      background: #f8f8f8;
      height: 40px;
      line-height: 40px;
      border-bottom: 1px solid #ddd;
    }

    .widget {
      background: #fff;
      margin: 0 0 10px 0;
      border: 1px solid #ddd;
      border-radius: 2px;
      box-shadow: 0 1px 1px rgb(0 0 0 / 4%);

      h3 {
        background: #f8f8f8;
        height: 40px;
        line-height: 40px;
        border-bottom: 1px solid #ddd;

        i {
          float: left;
          width: 40px;
          height: 40px;
          font-size: 18px;
          color: #409eff;
          line-height: 40px;
          text-align: center;
          margin: 0 15px 0 0;
          padding: 1px 0;
          border-right: 1px solid #ddd;
          border-radius: 2px 0 0 0;
        }
      }

      #feed_widget {
        .feed-about {
          position: relative;
          font-size: 18px;
          display: block;

          .about-main {
            font-size: 14px;
            padding: 10px 15px 0 15px;

            .about-img {
              img {
                float: left;
                width: 120px;
                height: auto;
                margin: 5px 10px 0 0;
                padding: 2px;
                border-radius: 2px;
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
          width: 28.8%;
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

      .searchbar {
        width: 90%;
        margin: 10px auto 0;

        span {
          input {
            float: left;
            width: 70.2%;
            height: 37px;
            line-height: 37px;
            font: 14px "Microsoft YaHei", Helvetica;
            padding: 2px 10px;
            background: #ebebeb;
            border: 1px solid #ebebeb;
            border-radius: 2px 0 0 2px;
            -webkit-appearance: none;
          }

          button {
            overflow: visible;
            position: relative;
            border: 0;
            cursor: pointer;
            height: 37px;
            width: 28.8%;
            color: #fff;
            background: #409eff;
            border-radius: 0 2px 2px 0;
          }
        }
      }
    }

    .php_text .widget-text {
      padding: 3px;
    }

    .tagcloud {
      padding: 5px 0 5px 2px;

      a {
        float: left;
        margin: 4px;
        padding: 0 7px;
        line-height: 26px;
        text-align: center;
        border: 1px solid #ddd;
        border-radius: 2px;
        box-shadow: 0 1px 1px rgb(0 0 0 / 4%);
      }

      a:hover {
        background: #409eff;
        color: #fff !important;
        border: 1px solid #409eff;
        transition: all 0.1s ease-in 0s;
      }

      a:visited {
        color: #555;
      }
    }
  }

  .reward-title {
    text-align: center;
  }

  .reward-model {
    .reward-img {
      margin-top: 30px;

      .img-boder {
        border: 9.02px solid rgb(60, 175, 54);
        border-radius: 29.97px;
        width: 250.89px;
        height: 250.89px;
        padding: 24.05px;
        margin: 0 auto;

        img {
          margin: 0 auto;
          width: 185px;
        }
      }

      .reward-tip {
        width: 48.13px;
        position: relative;
        margin: 0 auto;
        font-size: 12px;
        font-weight: 700;
        background: #fff;
        height: 15px;
        line-height: 15px;
        margin-top: -12px;
        color: #3caf36;
        text-align: center;
      }
    }
  }
}

.avatar-uploader, .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader, .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}
.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>

