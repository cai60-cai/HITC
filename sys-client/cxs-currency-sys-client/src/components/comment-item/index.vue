<template>
  <div class="content-item">
    <div class="comment-content-avatar-box" v-if="comment.commentFrom">
      <el-avatar :title="comment.commentFrom.nickName" :size="avatarSize == 1 ? 'large' : 'small'"
                 v-if="comment.commentFrom.avatar" :src="baseUrl + comment.commentFrom.avatar"
                 style="border: 1px solid #e8e8ed;"></el-avatar>
      <el-avatar :title="comment.commentFrom.nickName" :size="avatarSize == 1 ? 'large' : 'small'" v-else
                 :src="defaultAvatar" style="border: 1px solid #e8e8ed;"></el-avatar>
    </div>
    <div class="comment-content-info">
      <div @mouseenter="isEnter = true" @mouseleave="isEnter = false">
        <div class="comment-user-info">
          <span class="comment-nickname" v-if="comment.commentFrom && !reply">
             {{ comment.commentFrom.nickName }}
        </span>
          <span class="comment-nickname" v-if="comment.commentFrom && reply">
             {{ comment.commentFrom.nickName }} 回复 {{ comment.commentTo.nickName }}
        </span>
          <span class="comment-pushtime">
                    {{ comment.commentTimeFormat }}
                  </span>
          <span class="comment-more" v-show="isEnter">
            <el-link v-if="comment.commentFrom && user.userId === comment.commentFrom.userId" class="click-more"
                     style="margin-right: 10px" type="info" :underline="false" @click.native="deleteHandle(comment)">
              删除
            </el-link>
            <el-link v-if="comment.commentFrom && user.userId !== comment.commentFrom.userId" type="info"
                     style="margin-right: 10px" :underline="false" icon="el-icon-s-promotion"
                     @click.native="reportHandle(comment)">
              举报
            </el-link>
            <el-link v-if="comment.commentFrom && user.userId !== comment.commentFrom.userId" type="info"
                     style="margin-right: 10px" :underline="false" icon="el-icon-s-promotion"
                     @click.native="replyHandle(comment)">
              {{ isReply ? '收起' : '回复' }}
            </el-link>
        </span>
          <span class="comment-like">
                    <a @click.stop="likeCommentHandle(comment)" data-action="ding" data-id="1" title="点赞"
                       class="favorite cursor">
                      <i class="count">{{ comment.likeCount }}</i>
                      <i class="iconfont icon-dianzan" :class="{'i-active' : comment.likeStatus}"></i>
                    </a>
                  </span>
        </div>
        <div class="comment-content-text">
          {{ comment.commentContent }}
        </div>
        <div class="comment-reply-area" v-if="isReply">
          <CommentInputArea :is-reply="true" :placeholder="placeholder" :comment-handle="replyCommentHandle"></CommentInputArea>
        </div>
      </div>
      <div v-if="comment.children && comment.children.length > 0">
        <CommentItem v-for="item in comment.children" :key="item.commentId" :comment="item"
                     :avatarSize="2" :replyFunc="replyFunc" :reply="true" :replySuccess="replySuccess" :operaSuccess="operaSuccess"></CommentItem>
      </div>

      <el-dialog title="评论举报" :modal="false" :visible.sync="reportDialogVisible" class="reward-model" width="50%">
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="举报类型" prop="reportType">
            <el-radio-group v-model="ruleForm.reportType">
              <el-radio :label="item" :key="index" v-for="(item, index) in reportTypeList">{{item}}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="举报内容" prop="reportContent">
            <el-input
                type="textarea"
                :rows="7"
                resize="none"
                placeholder="请输入举报内容"
                show-word-limit
                autocomplete="off"
                maxlength="1000"
                v-model="ruleForm.reportContent">
            </el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="canalReport">取 消</el-button>
          <el-button type="primary" size="small" @click="submitForm('ruleForm')">提交</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import defaultAvatar from '@/assets/default-avatar.png'
import {URL_PREFIX} from "@/api/config";
import CommentItem from '@/components/comment-item'
import CommentInputArea from '@/components/comment/comment-input-area'
import {deleteComment, likeComment, publishComment, unLikeComment} from "@/api/comment";
import {ARTICLE_REPORT_TYPE, COMMENT_REPORT_TYPE} from "@/utils/constant";
import {getToken} from "@/utils/auth";
import {addReport} from "@/api/report";
export default {
  name: "CommentItem",
  components: {
    CommentItem,
    CommentInputArea
  },
  props: {
    comment: {
      type: Object,
      default: () => {
      }
    },
    avatarSize: {
      type: Number,
      default: 1
    },
    replyFunc: {
      type: Function,
      default: (item) => {
      }
    },
    replySuccess: {
      type: Function,
      default: (item) => {
      }
    },
    operaSuccess: {
      type: Function,
      default: (comment, type) => {
      }
    },
    reply: {
      type: Boolean,
      default: false
    }
  },
  data() {
    var validatereportType = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('举报类型为必选项'));
      }
      callback()
    };
    var validatereportContent = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('举报内容为必填项'));
      } else {
        if (value.length > 1000) {
          callback(new Error('举报内容不能大于1000字符'));
        }
        callback();
      }
    };
    return {
      defaultAvatar: defaultAvatar,
      baseUrl: URL_PREFIX,
      isEnter: false,
      toUser: '',
      isReply: false,

      reportDialogVisible: false,
      reportTypeList: COMMENT_REPORT_TYPE,
      ruleForm: {
        reportType: '',
        reportContent: '',
        reportTarget: 2,
        reportTargetId: null
      },

      rules: {
        reportType: [
          {validator: validatereportType, trigger: 'blur'}
        ],
        reportContent: [
          {validator: validatereportContent, trigger: 'blur'}
        ]
      }
    }
  },
  computed: {
    ...mapGetters(['user', 'isLogin']),
    placeholder(){
      return "回复: " + this.toUser
    },
    header(){
      return {
        'access_token': getToken()
      }
    }
  },
  methods: {
    likeCommentHandle(item) {
      if (this.isLogin) {
        const {likeStatus, commentId } = item
        if (likeStatus) {
          unLikeComment({commentId}).then(res => {
            this.$message.success('取消点赞成功');
            this.operaSuccess(item, 0)
          })
        } else{
          likeComment({commentId}).then(res => {
            this.$message.success('点赞成功');
            this.operaSuccess(item, 1)
          })
        }
      } else {
        this.$store.dispatch('auth/setLoginFlag', true)
      }
    },
    replyHandle(item) {
      if (this.isReply) {
        this.isReply = false
      } else {
        this.isReply = true
        this.toUser = item.commentFrom.nickName
      }
    },
    reportHandle(item){
      if (this.user && this.user.userId && this.user.userId.length > 0) {
        this.reportDialogVisible = true
        this.ruleForm.reportTarget = 2
        this.ruleForm.reportTargetId = item.commentId
      } else {
        this.$store.dispatch('auth/setLoginFlag', true)
      }
    },
    deleteHandle(item) {
      deleteComment(this.comment.commentId).then(res => {
        this.$message.success('删除成功');
        this.operaSuccess(item, 3)
      })
    },
    replyCommentHandle(value){
      if (this.user && this.user.userId && this.user.userId.length > 0) {
        const { articleId, commentFrom, parentCommentId, commentId} = this.comment
        const replyObj = {
          commentContent: value,
          articleId: articleId,
          commentTo: commentFrom.userId,
          parentCommentId: (parentCommentId && parentCommentId !== 0) ? parentCommentId : commentId
        }
        publishComment(replyObj).then(res => {
          this.$message.success('回复成功');
          this.$bus.$emit('opera-value', '')
          this.replySuccess()
        })
      } else {
        this.$store.dispatch('auth/setLoginFlag', true)
      }
    },
    canalReport(){
      this.reportDialogVisible = false
      this.resetForm('ruleForm')
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          addReport(this.ruleForm).then(res => {
            this.$message.success(res.msg);
            this.ruleForm = {}
            this.reportDialogVisible = false
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  }
}
</script>

<style lang="less" scoped>
.i-active{
  color: #4e91d1;
}
.content-item {
  display: flex;
  padding: 5px;
  box-sizing: border-box;
  margin-bottom: 10px;

  .comment-content-avatar-box {
    width: 7%;
  }

  .comment-content-info {
    width: 93%;
    padding: 0 10px;

    .comment-user-info {
      position: relative;
      margin-bottom: 5px;

      .comment-more {
        float: right;
        margin-right: 50px;

        .click-more {
          position: relative;
          margin-right: 20px;

          .click-more-all {
            position: absolute;
            left: -18px;
            width: 60px;

            li {
              text-align: center;
            }

            li:hover {
              background: #f7f7fc;
            }
          }
        }

        .comment-box {
          width: 100%;
          background: rgba(245, 246, 247, 0.8);
          padding: 10px;
          border-radius: 5px;

          .input-comment {
            width: 100%;
            height: 100px;
            border: none;
            resize: none;
            background: none;
          }

          .input-comment::-webkit-scrollbar {
            width: 2px; /* 纵向滚动条 宽度 */
            height: 15px; /* 横向滚动条 高度 */
            background: #cccccc; /* 整体背景 */
            border-radius: 10px; /* 整体 圆角 */
          }

          .comment-submit {
            height: 30px;
            display: flex;
            justify-content: space-between;

            .comment-tip {
              font-size: 12px;
              color: #999aaa;
              line-height: 17px;
            }
          }
        }
      }

      .comment-like {
        position: absolute;
        right: 0;
      }

    }

    .comment-content-info{
      table-layout:fixed;
      word-break:break-all;
      overflow:hidden;
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
