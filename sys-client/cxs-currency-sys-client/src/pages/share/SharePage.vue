<template>
  <el-dialog title="分享" :visible.sync="localVisible" class="share-dialog" width="400px" @close="handleClose">
    <div class="share-icons">
      <div v-for="icon in icons" :key="icon.name" class="icon-item" @click="handleClick(icon)">
        <img :src="icon.src" :alt="icon.name" />
        <p>{{ icon.name }}</p>
      </div>
    </div>
    <span slot="footer" class="dialog-footer">
      <el-button @click="closeDialog">取消</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  props: {
    visible: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      localVisible: this.visible, // 使用本地数据管理可见性
      icons: [
        { name: '朋友圈', src: '/images/social-icons/moments.png' },
        { name: '微信', src: '/images/social-icons/weixin.png' },
        { name: '微博', src: '/images/social-icons/weibo.png' },
        { name: 'QQ', src: '/images/social-icons/qq.png' },
        { name: 'QQ空间', src: '/images/social-icons/qzone.png' }
      ]
    };
  },
  watch: {
    visible(newVal) {
      this.localVisible = newVal;
    },
    localVisible(newVal) {
      this.$emit('update:visible', newVal);
    }
  },
  methods: {
    handleClick(icon) {
      console.log(`点击了${icon.name}`);
      // 在此处处理点击事件，例如调用接口
    },
    closeDialog() {
      this.localVisible = false;
    },
    handleClose() {
      this.localVisible = false;
    }
  }
}
</script>

<style scoped>
.share-dialog {
  text-align: center;
}

.share-icons {
  display: flex;
  justify-content: space-around;
  margin: 20px 0;
}

.icon-item {
  text-align: center;
  cursor: pointer;
}

.icon-item img {
  width: 50px;
  height: 50px;
  display: block;
  margin: 0 auto 10px;
}

.icon-item p {
  margin: 0;
  font-size: 14px;
  color: #666;
}
</style>
