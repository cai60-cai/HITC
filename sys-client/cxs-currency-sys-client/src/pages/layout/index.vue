<template>
  <div className="all">
    <MainHeader></MainHeader>
    <MainContent></MainContent>
    <MainFooter></MainFooter>
  </div>
</template>

<script>
import MainHeader from '../../components/main-header'
import MainContent from '../../components/main-content'
import MainFooter from '../../components/main-footer'
import {mapGetters} from "vuex";
import {URL_PREFIX} from "@/api/config";

export default {
  name: "Layout",
  components: {
    MainHeader,
    MainContent,
    MainFooter
  },
  data() {
    return {
      baseUrl: URL_PREFIX,
      wxIsShow: false
    }
  },
  mounted() {
    this.init()
  },
  computed: {
    ...mapGetters(['sysInfo', 'socketConnFlag', 'isLogin', 'user'])
  },
  watch: {
    socketConnFlag: {
      handler: function (newValue, oldValue) {
        if (this.isLogin && newValue) {
          this.initWebSocketMessage()
        }
      }
    }
  },
  methods: {
    init() {
      this.$store.dispatch('main/setSysInfo')
      this.$store.dispatch('main/setTagList')
      this.$store.dispatch('main/setTypeList')
      this.$store.dispatch('main/setHotSearchArticleList')
      this.$store.dispatch('main/setSystemRecommendArticleList')
      this.$store.dispatch('main/setSimpleNoticeList')
      this.checkMobile()
      if (this.isLogin) {
        this.$store.dispatch('main/setSysMessageList')
        this.$store.dispatch('main/setSocketConnFlag', true)
      }
    },
    checkMobile() {
      if (this.$device.mobile) {
        // this.$router.replace('/mobileMsg')
      }
    },
    initWebSocketMessage() {
      const _that = this;
      this.$nextTick(() => {
        var token = this.user.userId;
        if ("WebSocket" in window && token && token.length > 0) {
          // 打开一个 web socket
          var ws = new WebSocket(`${process.env.VUE_APP_Websocket}://${location.host}${URL_PREFIX}/bindingSysMessage/${token}`);
          ws.onopen = function () {
            // Web Socket 已连接上，使用 send() 方法发送数据
            ws.send(token + "连接成功");
          };

          ws.onmessage = function (evt) {
            let received_msg = evt.data;
            if (received_msg && received_msg.length > 0) {
              _that.$store.dispatch('main/setAddUnReadMessageNum')
            }
          };
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.all {
  position: relative;
}
</style>