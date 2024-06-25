<template>
  <div class="comment-area">
    <div class="avatar-box">
      <el-avatar :size="!isReply ? 'large' : 'small'" v-if="user.avatar" :src="baseUrl + user.avatar" style="border: 1px solid #e8e8ed;"></el-avatar>
      <el-avatar :size="!isReply ? 'large' : 'small'" v-else :src="defaultAvatar" style="border: 1px solid #e8e8ed;"></el-avatar>
    </div>
    <div class="comment-box">
      <textarea id="position-commentAxea" class="input-comment"
                :placeholder="placeholder"
                v-model="commentContent"></textarea>
      <div class="comment-submit">
        <span class="comment-tip">还能输入 <span style="color: #222226;margin: 0 4px;font-style: normal;">{{
            commentNumLimit
          }}</span> 个字符</span>
        <el-button type="primary" style="border-radius: 15px;height: 25px; line-height: 8px" size="small"
                   @click="commentClick">
          {{ isReply ? '回复' : '评论' }}
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import defaultAvatar from "@/assets/default-avatar.png";
import {URL_PREFIX} from "@/api/config";
import {mapGetters} from "vuex";

export default {
  name: "CommentInputArea",
  props: {
    placeholder: {
      type: String,
      default: '请输入高质量的评论'
    },
    commentHandle: {
      type: Function,
      default: () => {}
    },
    isReply: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      defaultAvatar: defaultAvatar,
      baseUrl: URL_PREFIX,
      commentContent: '',
      commentOldContent: '',
      commentNumLimit: 1000
    }
  },
  methods: {
    commentClick(){
      this.commentHandle(this.commentContent)
    },
    operaValue(value){
      this.commentContent = value
    }
  },
  // 组件挂载完绑定事件，指定函数接收
  mounted(){
    this.$bus.$on('opera-value',this.operaValue)
  },
  // 组件销毁前关闭事件
  beforeDestroy(){
    this.$bus.$off('opera-value');
  },
  watch:{
    commentContent: {
      immediate: true,
      handler: function (newsValue, oldValue) {
        this.commentOldContent = oldValue
        if (newsValue) {
          const length = newsValue.length
          if (length > 1000) {
            this.commentContent = this.commentOldContent
            this.commentNumLimit = 0
          } else {
            this.commentNumLimit = 1000 - length
          }
        } else {
          this.commentNumLimit = 1000
        }
      }
    }
  },
  computed: {
    ...mapGetters(['user'])
  }
}
</script>

<style lang="less" scoped>
.comment-area {
  display: flex;
  padding: 5px;
  box-sizing: border-box;
  margin-bottom: 5px;

  .avatar-box {
    width: 7%;
  }

  .comment-box {
    width: 93%;
    background: rgba(245, 246, 247, 0.8);
    padding: 10px;
    border-radius: 5px;

    .input-comment {
      width: 100%;
      height: 100px;
      border: none;
      resize: none;
      background: none;
      overflow-x: hidden;
    }

    .input-comment::-webkit-scrollbar {
      width: 2px; /* 纵向滚动条 宽度 */
      background: #cccccc; /* 整体背景 */
      border-radius: 10px; /* 整体 圆角 */
    }

    .input-comment::-webkit-scrollbar-thumb {
      border-radius: 3px;
      height: 10px; /* 滚动条滑块长度 */
      -webkit-box-shadow: inset005pxrgba(0, 0, 0, 0.2);
      background: rgba(0, 0, 0, 0.2);
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
</style>
