<template>
  <div class="all">
    <div class="item">
      <span style="font-weight: 600">反馈类型: </span>{{ feedbackInfo.feedbackType }}
    </div>
    <div class="item">
      <span style="font-weight: 600">反馈内容:</span> <br/> <span v-html="feedbackInfo.feedbackContent"></span>
    </div>
    <div class="item">
      <span style="font-weight: 600">反馈状态:</span>
      <el-tag
          v-if="feedbackInfo.feedbackStatus === 1"
          size="small"
          effect="dark">
        已处理
      </el-tag>
      <el-tag
          v-else
          type="warning"
          size="small"
          effect="dark">
        待处理
      </el-tag>
    </div>
    <div class="item" v-if="feedbackInfo.feedbackImages && feedbackInfo.feedbackImages.length > 0">
      <span style="font-weight: 600">举报图片:</span> <br/>
      <el-image
          v-for="item in feedbackInfo.feedbackImages" :key="item"
          style="width: 30%; height: 150px;margin-right: 10px"
          :src="baseUrl + item"
          :preview-src-list="[baseUrl + item]">
      </el-image>
    </div>
    <div class="item">
      <span style="font-weight: 600">反馈时间:</span> {{ feedbackInfo.feedbackTime }}
    </div>
    <div class="item" v-if="feedbackInfo.feedbackStatus === 1">
      <div>
        <span style="font-weight: 600">处理结果:</span>
        {{ feedbackInfo.replyContent }}
      </div>
      <div>
        <span style="font-weight: 600">处理时间:</span>
        {{ feedbackInfo.replyTime }}
      </div>
    </div>
  </div>
</template>

<script>
import {getFeedbackInfo} from "@/api/user";
import {URL_PREFIX} from "@/api/config";

export default {
  name: "FeedBackInfo",
  mounted() {
    this.init()
  },
  data() {
    return {
      id: 0,
      baseUrl: URL_PREFIX,
      feedbackInfo: {}
    }
  },
  methods: {
    init() {
      this.id = this.$route.params.id
      getFeedbackInfo(this.$route.params.id).then(res => {
        const {code, data} = res
        if (code === 200) {
          this.feedbackInfo = data
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
