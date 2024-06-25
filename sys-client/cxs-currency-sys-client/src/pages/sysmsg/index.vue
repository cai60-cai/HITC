<template>
  <div class="site-content clean content" style="transform: none;">
    <MyBreadcrumb v-if="refresh" :breadcrumb="isBread"></MyBreadcrumb>
    <div id="primary" class="content-area">
      <div class="title">
        <el-link @click="sendMessageRead(2)">全部已读</el-link>
      </div>

      <div class="message-all">
        <el-row :gutter="20" class="data-row">
          <el-col :span="2">
            <div class="grid-content bg-purple">id</div>
          </el-col>
          <el-col :span="14">
            <div class="grid-content bg-purple">消息内容</div>
          </el-col>
          <el-col :span="4">
            <div class="grid-content bg-purple">推送时间</div>
          </el-col>
          <el-col :span="4">
            <div class="grid-content bg-purple">操作</div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="data-item" v-if="sysMessageBean" v-for="(item, index) in sysMessageList" :key="item.id">
          <el-col :span="2"><div class="grid-content bg-purple">{{item.id}}</div></el-col>
          <el-col :span="14"><div class="grid-content bg-purple content-msg">{{item.messageContent}}</div></el-col>
          <el-col :span="4"><div class="grid-content bg-purple">{{item.createTime}}</div></el-col>
          <el-col :span="4">
            <div class="grid-content bg-purple" v-if="item.isRead">已读</div>
            <div class="grid-content bg-purple" v-else>
              <el-link :underline="false" type="primary" @click="sendMessageRead(1, item.id)">设为已读</el-link>
            </div>
          </el-col>
        </el-row>
        <el-pagination
            v-if="sysMessageBean.pages && sysMessageBean.pages > 1"
            class="page"
            layout="prev, pager, next"
            @current-change="handleCurrentChange"
            :page-size="sysMessageBean.pageSize"
            :total="sysMessageBean.total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {readSysMessage} from "@/api/user";

export default {
  name: "Sysmsg",
  data(){
    return {
      isBread: true,
      refresh: true,
      params:{
        pageNum: 1,
        pageSize: 10
      }
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
    this.init()
  },
  computed:{
    ...mapGetters(['sysMessageBean', 'sysMessageList'])
  },
  methods:{
    init(){
      this.$store.dispatch('main/setSysMessageList', this.params)
    },
    handleCurrentChange(val){
      this.params.pageNum = val
      this.init()
    },
    sendMessageRead(type, id){
      const data = {
        messageIdList: []
      }
      if (type == 1) {
        data.messageIdList.push(id)
      } else {
        this.sysMessageList.forEach(r => {
          if (!r.isRead) {
            data.messageIdList.push(r.id)
          }
        })
      }
      if (data.messageIdList.length > 0) {
        readSysMessage(data).then(res => {
          this.$store.dispatch('main/setSysMessageList', this.params)
        })
      }
    }
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
    width: 100%;
    min-height: 80vh;
    transition-duration: .5s;
    background-color: white;

    .title{
      text-align: right;
      height: 50px;
      line-height: 50px;
      padding-right: 75px;
    }

    .message-all{
      padding: 10px 20px;

      .grid-content{
        background-color: white;
        text-align: center;
      }

      .data-row {
        border-bottom: 1px solid #DCDFE6;
      }

      .data-item{
        display: flex;
        align-items: center;
        border-bottom: 1px solid #DCDFE6;
        min-height: 50px;

        .content-msg{
          text-align: left;
        }
      }

      .page{
        text-align: right;
        margin-top: 10px;
      }
    }
  }
}
</style>
