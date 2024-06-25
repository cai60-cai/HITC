<template>
  <div>
    <div>
      <!-- 支付按钮，模拟支付操作 -->
      <button type="primary" @click="pay">支付</button>
    </div>
  </div>
</template>

<script>
import {pay} from "@/api/pay";
import vueQr from 'vue-qr';
import {getToken} from "@/utils/auth";
import {getPointRechargeTypeList} from "@/api/point";

export default {
  name: "Test",
  components: {
    vueQr
  },
  data() {
    return {
      selectPointType: 1,
      pointTypeList: [],
      pointObj: {
        "gold": 100,
        "id": 1,
        "money": "10.00",
        "typeDesc": "购买100积分"
      },
      paySucc: false,
      dialogVisible: false,
      text: "",
      socket: null,
      timeout: 10 * 1000, // 45秒一次心跳
      timeoutObj: null, // 心跳心跳倒计时
      serverTimeoutObj: null, // 心跳倒计时
      timeoutnum: null, // 断开 重连倒计时
      lockReconnect: false, // 防止
      websocket: null
    }
  },
  watch:{
    selectPointType: {
      immediate: true,
      handler: function (newValue, oldValue){
        this.startPay()
        if (newValue) {
          let point = null;
          this.pointTypeList.forEach(obj => {
            if (obj.id == newValue) {
              point = obj
            }
          })
          this.pointObj = point
        }
      }
    }
  },
  mounted() {
    this.init()
  },
  methods: {
    init(){
      getPointRechargeTypeList().then(res => {
        this.pointTypeList = res.data
      })
    },
    pay() {
      this.paySucc = false;
      this.dialogVisible = true;
      this.startPay()
      let point = null;
      this.pointTypeList.forEach(obj => {
        if (obj.id == this.selectPointType) {
          point = obj
        }
      })
      this.pointObj = point
    },
    startPay(){
      var token = getToken();
      pay({id: this.selectPointType}).then(resp => {
        this.text = resp.data;
        this.dialogVisible = true;
        const _this = this;
        //使用webSocket发送请求，下面会简单介绍websocket使用
        if ("WebSocket" in window) {
          // 打开一个 web socket
          var ws = new WebSocket(`ws://${location.host}/websocket/bindingRecord/${token}`);
          ws.onopen = function () {
            // Web Socket 已连接上，使用 send() 方法发送数据
            ws.send("data");
          };

          ws.onmessage = function (evt) {
            var received_msg = evt.data;
            if (Boolean(evt.data)) {
              _this.paySucc = true;
              _this.$message.success("交易成功");
              setTimeout(() => {
                _this.dialogVisible = false;
              }, 1 * 1000);
            }
            ws.close();
          };
        } else {
          // 浏览器不支持 WebSocket
          alert("您的浏览器不支持 WebSocket!");
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
