<template>
  <div class="all">
    <div class="item">
      <span style="font-weight: 600">举报类型: </span>{{reportInfo.reportType}}
    </div>
    <div class="item">
      <span style="font-weight: 600">举报资源类型:</span>
      <span v-if="reportInfo.reportTarget === 1">
        <el-link :underline="false" v-if="reportInfo.reportTargetObj" @click="$router.push({name: 'article-detail', params: {id: reportInfo.reportTargetObj.articleId}})">
          博客-{{reportInfo.reportTargetObj.articleTitle}}
        </el-link>
        <span v-else>
          博客-源文件已删除~
        </span>
      </span>
      <span v-if="reportInfo.reportTarget === 2">
        <el-link :underline="false" v-if="reportInfo.reportTargetObj">
          评论-{{reportInfo.reportTargetObj.commentContent}}
        </el-link>
        <span v-else>
          评论-源评论已删除~
        </span>
      </span>
    </div>
    <div class="item">
      <span style="font-weight: 600">举报内容:</span> {{reportInfo.reportContent}}
    </div>
    <div class="item">
      <span style="font-weight: 600">举报状态:</span>
      <el-tag
          v-if="reportInfo.reportStatus === 1"
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
    <div class="item" v-if="reportInfo.reportImages && reportInfo.reportImages.length > 0">
      <span style="font-weight: 600">举报图片:</span> <br/>
      <el-image
          v-for="item in reportInfo.reportImages" :key="item"
          style="width: 30%; height: 150px;margin-right: 10px"
          :src="baseUrl + item"
          :preview-src-list="[baseUrl + item]">
      </el-image>
    </div>
    <div class="item">
      <span style="font-weight: 600">举报时间:</span> {{reportInfo.reportTime}}
    </div>
    <div class="item" v-if="reportInfo.reportStatus === 1">
      <div>
        <span style="font-weight: 600">处理结果:</span>
        {{reportInfo.reportHandleResult}}</div>
      <div>
        <span style="font-weight: 600">处理结果描述:</span>
        {{reportInfo.reportHandleDesc}}</div>
      <div>
        <span style="font-weight: 600">处理时间:</span>
        {{reportInfo.reportHandleTime}}</div>
    </div>
  </div>
</template>

<script>
import {getReportInfo} from "@/api/user";
import {URL_PREFIX} from "@/api/config";

export default {
  name: "ReportInfo",
  mounted() {
    this.init()
  },
  data(){
    return {
      id:0,
      baseUrl: URL_PREFIX,
      reportInfo:{}
    }
  },
  methods:{
    init(){
      this.id = this.$route.params.id
      getReportInfo(this.$route.params.id).then(res => {
        const {code, data} = res
        if (code === 200) {
          this.reportInfo = data
        }
      })
    }
  }
}
</script>

<style scoped>
.item{
  min-height: 30px;
}
</style>
