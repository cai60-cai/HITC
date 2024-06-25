<template>
  <div class="all">
    <div class="point-setting">
      <h3>展示积分余额</h3>
      <el-row :gutter="20" class="data-row">
        <el-col :span="12"><div class="grid-content bg-purple">
          <el-switch
              v-model="settingInfo.showPoint"
              @change="settingsPointChangeHandle"
              active-color="#13ce66"
              inactive-color="#cccccc"
              active-text="展示"
              inactive-text="隐藏"
          >
          </el-switch>
        </div></el-col>
      </el-row>
    </div>

    <div class="point-setting">
      <h3>接收邮件通知</h3>
      <el-row :gutter="20" class="data-row">
        <el-col :span="12"><div class="grid-content bg-purple">
          <el-switch
              v-model="settingInfo.receiveEmailNotice"
              @change="settingsEmailChangeHandle"
              active-color="#13ce66"
              inactive-color="#cccccc"
              active-text="接收"
              inactive-text="不接受"
          >
          </el-switch>
        </div></el-col>
      </el-row>
    </div>

    <div class="reward-setting">
      <h3>其他用户转发权限</h3>
      <el-row :gutter="20" style="display: flex; align-items: center;height: 50px">
        <el-col :span="12">
          <div class="grid-content bg-purple">
            <el-switch
                v-model="settingInfo.openReward"
                @change="settingsRewardChangeHandle"
                active-color="#13ce66"
                inactive-color="#cccccc"
                active-text="开启"
                inactive-text="关闭"
            >
            </el-switch>
          </div>
        </el-col>
<!--        <el-col :span="12">-->
<!--          <div class="grid-content bg-purple">-->
<!--            <el-button v-if="settingInfo.openReward" size="small" type="primary" @click="updateRewardInfo">保存</el-button>-->
<!--          </div>-->
<!--        </el-col>-->
      </el-row>
<!--      <el-row :gutter="20">-->
<!--        <el-col v-if="settingInfo.openReward" :span="24">-->
<!--          <div>-->
<!--            <b>注意: </b>图片上传须知,请上传长宽比例1:1的图片，比例失调会导致图片展示异常-->
<!--          </div>-->
<!--        </el-col>-->
<!--        <el-col v-if="settingInfo.openReward" :span="24">-->
<!--          <div class="grid-content bg-purple">-->
<!--            <div class="item">-->
<!--              <el-upload-->
<!--                  class="avatar-uploader"-->
<!--                  :action="baseUrl + '/base/file/upload'"-->
<!--                  :headers="header"-->
<!--                  :show-file-list="false"-->
<!--                  :on-success="weixinHandleAvatarSuccess"-->
<!--                  :before-upload="beforeAvatarUpload">-->
<!--                <img v-if="rewardInfo.weixinImg" :src="baseUrl + rewardInfo.weixinImg" class="avatar">-->
<!--                <i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
<!--              </el-upload>-->
<!--              <h5 class="title">微信收款码</h5>-->
<!--            </div>-->
<!--            <div class="item item-border">-->
<!--              <el-color-picker class="color-check" v-model="rewardInfo.weixinBorderColor"></el-color-picker>-->
<!--              <h5 class="title">微信收款码边框</h5>-->
<!--            </div>-->
<!--            <div class="item">-->
<!--              <el-upload-->
<!--                  class="avatar-uploader"-->
<!--                  :action="baseUrl + '/base/file/upload'"-->
<!--                  :headers="header"-->
<!--                  :show-file-list="false"-->
<!--                  :on-success="zhifubaoHandleAvatarSuccess"-->
<!--                  :before-upload="beforeAvatarUpload">-->
<!--                <img v-if="rewardInfo.zhifubaoImg" :src="baseUrl + rewardInfo.zhifubaoImg" class="avatar">-->
<!--                <i v-else class="el-icon-plus avatar-uploader-icon"></i>-->
<!--              </el-upload>-->
<!--              <h5 class="title">支付宝收款码</h5>-->
<!--            </div>-->
<!--            <div class="item item-border">-->
<!--              <el-color-picker class="color-check" v-model="rewardInfo.zhifubaoBorderColor"></el-color-picker>-->
<!--              <h5 class="title">支付宝收款码边框</h5>-->
<!--            </div>-->
<!--          </div>-->
<!--        </el-col>-->
<!--      </el-row>-->
    </div>
  </div>
</template>

<script>
  import {URL_PREFIX} from "@/api/config";
  import {getToken} from "@/utils/auth";
  import {
    getSettingInfo,
    operaPointSetting,
    operaReceiveEmailNoticeSetting,
    operaRewardSetting,
    updateRewardImgInfo
  } from "@/api/user";

  export default {
    name: "Setting",
    data(){
      return {
        value: 1,
        baseUrl: URL_PREFIX,
        settingInfo:{

        },
        rewardInfo:{
          weixinImg: '',
          weixinBorderColor: '',
          zhifubaoImg: '',
          zhifubaoBorderColor: ''
        }
      }
    },
    computed:{
      header(){
        return {
          'access_token': getToken()
        }
      }
    },
    mounted() {
      this.init()
    },
    methods:{
      init(){
        getSettingInfo().then(res => {
          const { data } = res;
          this.settingInfo = data
          if (data) {
            this.rewardInfo = data.rewardInfo
          }
        })
      },
      updateRewardInfo(){
        updateRewardImgInfo(this.rewardInfo).then(res => {
          this.$message.success('打赏信息保存成功')
        })
      },
      operationPointSetting(){
        operaPointSetting(this.settingInfo).then(res => {
          this.init()
        })
      },
      operationReceiveEmailNoticeSetting(){
        operaReceiveEmailNoticeSetting(this.settingInfo).then(res => {
          this.init()
        })
      },
      operationRewardSetting(){
        operaRewardSetting(this.settingInfo).then(res => {
          this.init()
        })
      },
      getRewardInfo(){
        getSettingInfo().then(res => {
          const { data } = res;
          if (data) {
            this.rewardInfo = data.rewardInfo
          }
        })
      },
      weixinHandleAvatarSuccess(res, file) {
        this.rewardInfo.weixinImg = res.data;
        this.$forceUpdate();
      },
      zhifubaoHandleAvatarSuccess(res, file) {
        this.rewardInfo.zhifubaoImg = res.data;
        this.$forceUpdate();
      },
      // 上传之前
      beforeAvatarUpload(file) {
        const isJPG = file.type === 'image/jpeg';
        const isPNG = file.type === 'image/png';
        const isLt2M = file.size / 1024 / 1024 < 2;

        if (!isJPG && !isPNG) {
          this.$message.error('上传头像图片只能是 jpg或png 格式!');
        }
        if (!isLt2M) {
          this.$message.error('上传头像图片大小不能超过 2MB!');
        }
        return (isJPG && isLt2M) || (isPNG && isLt2M) ;
      },
      settingsPointChangeHandle(){
        this.operationPointSetting()
      },
      settingsEmailChangeHandle(){
        this.operationReceiveEmailNoticeSetting()
      },
      settingsRewardChangeHandle(){
        this.operationRewardSetting()
        this.getRewardInfo()
      }
    }
  }
</script>

<style scoped lang="less">
.all{
  &>div{
    padding: 10px 0;
    margin-bottom: 10px;
    border-bottom: 1px solid #cccccc;
  }

  .reward-setting{
    .bg-purple{
      margin-top: 10px;
      display: flex;
      justify-content: space-between;
      .item-border{
        display: flex;
        justify-content: center;
        align-items: end;
        flex-wrap: wrap;
        .color-check{
          width: 200px;
          text-align: center;
        }
        .title{
          width: 200px;
        }
      }
      .item{
        width: 178px;
        height: 200px;
        .avatar-uploader, .el-upload{
          border: 1px dashed #d9d9d9;
          border-radius: 6px;
          cursor: pointer;
          position: relative;
          overflow: hidden;
          width: 178px;
          height: 178px;
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
        .title{
          text-align: center;
        }
      }
    }
  }


}
</style>
