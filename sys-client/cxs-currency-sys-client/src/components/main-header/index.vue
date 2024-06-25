<template>
  <div class="header-all">
    <div class="header-top">
      <div class="top-nav">
        <div class="menu-topmenu-container">
          <ul id="menu-topmenu" class="top-menu">
            <li class="menu-item" v-for="item in publicNavLinkList" :key="item.navId">
              <el-link v-if="item.navType === 1" :icon="item.navIcon" @click="toRoute(item.navLink)"> {{
                  item.navName
                }}
              </el-link>
              <el-link v-else :icon="item.navIcon" :href="item.navLink"> {{ item.navName }}</el-link>
            </li>
            <li v-if="isLogin" class="menu-item" v-for="item in loginNavLinkList" :key="item.navId">
              <el-link :icon="item.navIcon" @click="toRoute(item.navLink)"> {{ item.navName }}</el-link>
            </li>
<!--            <li v-if="isLogin" class="menu-item">-->
<!--              <el-link @click="sign" icon="el-icon-edit-outline"> 签到11 </el-link>-->
<!--            </li>-->
<!--            <li v-if="isLogin" class="menu-item">-->
<!--              <el-link @click="pay"> 购买 </el-link>-->
<!--            </li>-->
            <li class="menu-item" v-if="!isLogin">
              <el-link icon="el-icon-user" @click="toLogin()">登录</el-link>
            </li>
            <li class="menu-item" v-else>
              <el-dropdown trigger="click" size="medium">


                <span class="el-dropdown-link">
                  <el-badge v-if="sysMessageBean && unReadMessageNum > 0" :value="unReadMessageNum" class="item">
                    <span>
                      <el-avatar v-if="user.avatar" class="cursor" :src="baseUrl + user.avatar" size="small"></el-avatar>
                      <el-avatar v-else class="cursor" :src="defaultUserImg" size="small"></el-avatar>
                    </span>
                  </el-badge>

                  <span v-else>
                    <el-avatar v-if="user.avatar" class="cursor" :src="baseUrl + user.avatar" size="small"></el-avatar>
                    <el-avatar v-else class="cursor" :src="defaultUserImg" size="small"></el-avatar>
                  </span>
                </span>

                <el-dropdown-menu slot="dropdown" style="width: 110px">
                  <el-dropdown-item @click.native="$router.push('/profile')">个人中心</el-dropdown-item>
                  <el-dropdown-item class="clearfix" @click.native="$router.push('/sysmsg')">
                    系统消息
                    <el-badge v-if="sysMessageBean && unReadMessageNum > 0" is-dot class="item"/>
                  </el-dropdown-item>
                  <el-dropdown-item @click.native="$router.push('/profile/updatepwd')">修改密码</el-dropdown-item>
                  <el-dropdown-item @click.native="logoutHandle">退出</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <div class="header-main">
      <div id="top-menu" class="top-menu">
        <div class="logo-search clear">
          <div class="logo-site">
            <h1 class="site-title" v-if="sysInfo">
              <router-link :to="{name:'page-refresh', query: {to: 'index'}}" :title="sysInfo.sysTitle">{{
                  sysInfo.sysTitle
                }}
              </router-link>
            </h1>
            <div class="site-description">{{ sysInfo.sysLogoTitle }}</div>
          </div><!-- .logo-site -->
          <div class="search-main">
            <el-autocomplete
                class="search-input"
                v-model="searchKeyWord"
                :fetch-suggestions="querySearchAsync"
                placeholder="输入搜索内容">
              <i class="el-icon-search el-input__icon" slot="suffix"></i>
            </el-autocomplete>
            <el-button type="primary" icon="el-icon-search" @click="searchSubmit()">搜索</el-button>
          </div><!--.search-main-->
        </div>
      </div>
    </div>

    <el-dialog :visible.sync="loginFlag" width="500px" :close-on-click-modal="false" :show-close="true">
      <el-form ref="loginForm" v-show="login" :model="userInfo" :rules="rules" class="login-form" autocomplete="on"
               label-position="left">
        <div class="title-container">
          <h3 class="title" style="text-align: center;font-size: 20px;margin-bottom: 15px;">
            {{ sysInfo.sysTitle }}登录</h3>
        </div>

        <el-form-item prop="username">
          <el-input
              prefix-icon="el-icon-user"
              ref="username"
              v-model="userInfo.username"
              placeholder="用户名"
              name="username"
              type="text"
              tabindex="1"
              autocomplete="on"
          />
        </el-form-item>

        <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>
          <el-form-item prop="password">
            <el-input
                show-password
                prefix-icon="el-icon-lock"
                ref="password"
                v-model="userInfo.password"
                placeholder="账户密码"
                name="password"
                tabindex="2"
                autocomplete="on"
                @blur="capsTooltip = false"
            />
            <span class="show-pwd">
          </span>
          </el-form-item>
        </el-tooltip>

        <el-form-item prop="code">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-input
                  ref="code"
                  type="text"
                  prefix-icon="el-icon-key"
                  v-model="userInfo.code"
                  placeholder="验证码"
                  name="code"
                  tabindex="2"
                  autocomplete="on"
                  @keyup.enter.native="loginHandle"
              />
            </el-col>
            <el-col :span="6" :offset="3">
              <canvas ref="canvas" :width="codeInfo.codeWidth" :height="codeInfo.codeHeight" @click="reloadCode"></canvas>
<!--              <el-image-->
<!--                  class="img-code"-->
<!--                  v-if="refresh"-->
<!--                  @click="changeCode()"-->
<!--                  style="width: 120px; height: 38px"-->
<!--                  title="点击图片切换验证码"-->
<!--                  :src="codeUrl"></el-image>-->
            </el-col>
          </el-row>
        </el-form-item>

        <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;"
                   @click.native.prevent="loginHandle">登录
        </el-button>
        <div style="text-align: right">
          <el-link type="primary" :underline="false" @click.native="nativeToRegister">注册新用户>></el-link>
        </div>
      </el-form>

      <el-form ref="registerForm" v-show="!login" :model="registerInfo" :rules="registerRules" class="register-form"
               autocomplete="on"
               label-position="left">
        <div class="title-container">
          <h3 class="title" style="text-align: center;font-size: 20px;margin-bottom: 15px;">
            {{ sysInfo.sysTitle }}注册</h3>
        </div>

        <el-form-item prop="userName">
          <el-input
              prefix-icon="el-icon-user"
              ref="username"
              v-model="registerInfo.userName"
              placeholder="请输入用户名"
              name="userName"
              type="text"
              tabindex="1"
              autocomplete="on"
          />
        </el-form-item>

        <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>
          <el-form-item prop="password">
            <el-input
                show-password
                prefix-icon="el-icon-lock"
                ref="password"
                v-model="registerInfo.password"
                placeholder="请输入账户密码"
                name="password"
                tabindex="2"
                autocomplete="on"
                @blur="capsTooltip = false"
            />
            <span class="show-pwd">
          </span>
          </el-form-item>
        </el-tooltip>

        <el-tooltip v-model="capsTooltip" content="Caps lock is On" placement="right" manual>
          <el-form-item prop="password2">
            <el-input
                show-password
                prefix-icon="el-icon-lock"
                ref="password2"
                v-model="registerInfo.password2"
                placeholder="请确认密码"
                name="password2"
                tabindex="2"
                autocomplete="on"
                @blur="capsTooltip = false"
            />
            <span class="show-pwd">
          </span>
          </el-form-item>
        </el-tooltip>

        <el-form-item prop="email">
          <el-input
              prefix-icon="el-icon-message"
              ref="email"
              v-model="registerInfo.email"
              placeholder="请输入邮箱地址,每个邮箱最多可绑定3个账号"
              name="email"
              type="text"
              tabindex="1"
              autocomplete="on"
          />
        </el-form-item>

        <el-form-item prop="code">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-input
                  ref="code"
                  type="text"
                  prefix-icon="el-icon-key"
                  v-model="registerInfo.code"
                  placeholder="请输入邮箱验证码"
                  name="code"
                  tabindex="2"
                  autocomplete="on"
                  @keyup.enter.native="registerHandle"
              />
            </el-col>
            <el-col :span="6" :offset="3">
              <el-button type="primary" size="small" :disabled="pause" @click="sendEmailCode">获取验证码 <span v-if="pause">{{ time }}</span>
              </el-button>
            </el-col>
          </el-row>
        </el-form-item>

        <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;"
                   @click.native.prevent="registerHandle">注册
        </el-button>

        <div style="text-align: right">
          <el-link type="primary" :underline="false" @click.native="nativeToLogin">已有账号,去登录>></el-link>
        </div>
      </el-form>
    </el-dialog>

    <el-dialog
        title="新用户提示"
        :visible.sync="dialogVisible"
        width="30%">
      <span>系统检测到您是新用户,请及时更新信息</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="toUpdateSelfInfo">去更新</el-button>
      </span>
    </el-dialog>


    <el-dialog :width="$device.windows ? '40%':'300px'" :title="paySucc?'支付成功':'扫码支付'" :visible.sync="tradeDialogVisible" :append-to-body="true">
      <div style="text-align: center;min-height: 300px">
        <div class="reward-title">
          <el-radio-group v-model="selectPointType" v-if="$device.windows">
            <el-radio :label="item.id" v-for="item in pointTypeList" :key="item.id">
              {{item.gold}}
            </el-radio>
          </el-radio-group>
          <el-select v-model="selectPointType" v-else>
            <el-option :label="item.gold" :value="item.id" v-for="item in pointTypeList" :key="item.id"/>
          </el-select>
        </div>
        <!-- 生成二维码图片 -->
        <vueQr :text="text" :size="200" v-if="!paySucc"></vueQr>
        <!-- 使用websocket监控是否扫描，扫描成功显示成功并退出界面 -->
        <el-result icon="success" title="交易成功" subTitle="请刷新页面查看" v-else></el-result>
        <div v-if="pointObj && !paySucc">
          应付金额: {{pointObj.money}}元, 所得积分：{{pointObj.gold}}
        </div>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {mapGetters} from "vuex";
import SvnIcon from '@/components/SvgIcon'
import {getLinkList, sendEmailCode, register, getPointRechargeTypeList} from "@/api/main";
import {getCache, setCache} from "@/utils/cache";
import {HEADER_NAV_LINK_LIST_KEY} from "@/utils/cache_constent";
import {URL_PREFIX} from "@/api/config";
import defaultUserImg from '@/assets/logo.png'
import {checkEmailBindStatus, checkUserNameExist, getValidateCode, userSign} from "@/api/user";
import {getToken} from "@/utils/auth";
import {pay} from "@/api/pay";
import vueQr from 'vue-qr';
import AES from "@/utils/AES";

const defaultRegisterInfo = {
  userName: '',
  password: '',
  password2: '',
  email: '',
  code: ''
}
export default {
  name: "main-header",
  components: {
    'svg-icon': SvnIcon,
    vueQr
  },
  computed: {
    loginFlag: {
      set(value) {
        this.$store.dispatch('auth/setLoginFlag', value)
      },
      get() {
        return this.$store.getters.loginFlag
      }
    },
    loginNavLinkList() {
      return this.navLinkList.filter(nav => nav.navMustLogin === 1 && nav.navType === 1)
    },
    publicNavLinkList() {
      return this.navLinkList.filter(nav => nav.navMustLogin === 0)
    },
    ...mapGetters(['isLogin', 'user', 'typeList', 'sysInfo', 'hotSearchArticleList', 'sysMessageBean', 'unReadMessageNum'])
  },
  mounted() {
    this.init()
    this.tradeInit()
  },
  data() {
    var checkUserName = (rule, value, callback) => {
      const pattern = /^[A-Za-z0-9-_]+$/;
      if (!value) {
        return callback(new Error('登录名不能为空'));
      }
      if (pattern.test(value) && value.length <= 16) {
        checkUserNameExist({userName: value}).then(res => {
          if (res.data) {
            callback(new Error('登录名已存在'));
          } else {
            callback();
          }
        })
      } else {
        callback(new Error('登录名须由数字、英文字母、-、下划线(不包含、)组成,不大于16位'));
      }
    };
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.registerInfo.password !== '') {
          this.$refs.registerForm.validateField('password2');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.registerInfo.password) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    var validateEmail = (rule, value, callback) => {
      const pattern = /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/;
      if (value && value !== '') {
        if (pattern.test(value)) {
          checkEmailBindStatus({email: value}).then(res => {
            if (res.data) {
              callback();
            } else {
              callback(new Error('当前邮箱绑定用户已达到上限'));
            }
          })
        } else {
          callback(new Error('邮箱格式不正确'));
        }
      } else {
        callback(new Error('邮箱为必传项'));
      }
    };
    return {
      defaultUserImg: defaultUserImg,
      refresh: true,
      baseUrl: URL_PREFIX,
      codeUrl: URL_PREFIX + '/auth/getCaptcha',
      // 搜索框
      searchFlag: false,
      searchKeyWord: '',
      capsTooltip: false,
      // 登录
      codeInfo:{
        codeWidth: 120,
        codeHeight: 38,
        backgroundColor: 'rgb(0,0,0,0.2)'
      },
      userInfo: {
        username: '',
        password: '',
        code: '',
        codeKey: ''
      },
      rules: {
        username: [{required: true, trigger: 'blur', message: '用户名为必填项'}],
        password: [{required: true, trigger: 'blur', message: '密码为必填项'}],
        code: [{required: true, trigger: 'blur', message: '验证码为必填项'}]
      },
      loading: false,
      navLinkList: [],
      login: true,
      registerInfo: {
        userName: '',
        password: '',
        password2: '',
        email: '',
        code: ''
      },
      time: 30,
      pause: false,
      registerRules: {
        userName: [{validator: checkUserName, trigger: 'blur'}],
        password: [{validator: validatePass, trigger: 'blur'}],
        password2: [{validator: validatePass2, trigger: 'blur'}],
        email: [{validator: validateEmail, trigger: 'blur'}],
        code: [{required: true, trigger: 'blur', message: '验证码为必填项'}]
      },
      dialogVisible: false,

      // 积分交易
      selectPointType: 1,
      pointTypeList: [],
      pointObj: {
        "gold": 100,
        "id": 1,
        "money": "10.00",
        "typeDesc": "购买100积分"
      },
      paySucc: false,
      tradeDialogVisible: false,
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
      // immediate: true,
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
    },
    loginFlag: {
      handler: function (newValue, oldValue){
        this.$nextTick(() => {
          if (newValue) {
            this.createdCode()
          } else {
            this.clearCode()
          }
        })
      }
    }
  },
  methods: {
    async createdCode() {
      const res = await getValidateCode()
      const {code, data, msg} = res
      this.userInfo.codeKey = data.key
      const identifyCode = data.code
      const codeList = identifyCode.split("");
      const canvas = this.$refs.canvas;// 获取 canvas 元素
      const ctx = canvas.getContext('2d');
      ctx.textBaseline = 'bottom';
      if (this.backgroundColor != '' && this.backgroundColor != null) {// 绘制画布背景颜色
        ctx.fillStyle = this.backgroundColor;
      } else {
        ctx.fillStyle = this.randomColor(255, 255);
      }
      ctx.fillRect(0, 0, this.contentWidth, this.contentHeight);
      codeList.forEach((code, i) => {// 绘制验证码字符
        this.drawText(ctx, code, i + 1, identifyCode.length);
      })
      this.drawLine(ctx);// 绘制干扰线
      this.drawDot(ctx);// 绘制干扰点
    },
    randomNum(min, max) {// 生成指定范围内的随机整数
      return Math.floor(Math.random() * (max - min) + min);
    },
    randomColor(min, max) {// 生成指定范围内的随机颜色
      const r = this.randomNum(min, max);
      const g = this.randomNum(min, max);
      const b = this.randomNum(min, max);
      return `rgb(${r},${g},${b})`;
    },
    drawText(ctx, txt, i, len) {// 绘制验证码字符
      ctx.fillStyle = this.randomColor(0, 160);
      ctx.font = `${this.randomNum(25, 30)}px SimHei`;
      const x = (i / (len + 1)) * 120;
      const y = this.randomNum(30, 35);
      const deg = this.randomNum(-45, 45);
      ctx.translate(x, y);
      ctx.rotate((deg * Math.PI) / 180);
      ctx.fillText(txt, 0, 0);
      ctx.rotate((-deg * Math.PI) / 180);
      ctx.translate(-x, -y);
    },
    drawLine(ctx) {// 绘制干扰线
      for (let i = 0; i < 5; i++) {
        ctx.strokeStyle = this.randomColor(100, 255);
        ctx.beginPath();
        ctx.moveTo(this.randomNum(0, 120), this.randomNum(0, 40));
        ctx.lineTo(this.randomNum(0, 120), this.randomNum(0, 40));
        ctx.stroke();
      }
    },
    drawDot(ctx) {// 绘制干扰点
      for (let i = 0; i < 80; i++) {
        ctx.fillStyle = this.randomColor(0, 255);
        ctx.beginPath();
        ctx.arc(this.randomNum(0, 120), this.randomNum(0, 40), 1, 0, 2 * Math.PI);
        ctx.fill();
      }
    },
    reloadCode() {// 点击按钮时清除画布并重新生成验证码
      const canvas = this.$refs.canvas
      const ctx = canvas.getContext('2d')
      ctx.clearRect(0, 0, canvas.width, canvas.height)
      this.createdCode()
    },
    clearCode(){
      const canvas = this.$refs.canvas
      const ctx = canvas.getContext('2d')
      ctx.clearRect(0, 0, canvas.width, canvas.height)
    },
    sign(){
      userSign().then(res => {
        const {code, msg} = res
        if (code === 200) {
          this.$message.success("签到成功")
        } else {
          this.$message.error(msg)
        }
      }).catch(err => {})
    },
    searchSubmit() {
      if (this.searchKeyWord.length > 0) {
        this.$router.push({name: 'search-detail', query: {keyword: this.searchKeyWord}})
        this.searchKeyWord = ''
        this.searchFlag = false
      }
    },
    toRoute(to) {
      this.$store.dispatch('header/setbreadcrumbAppend', [])
      this.$router.push(to)
    },
    loginHandle() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          const useInfo = {...this.userInfo, password: AES.encrypt(this.userInfo.password)}
          this.$store.dispatch('auth/login', useInfo)
              .then((res) => {
                this.loading = false
                this.$store.dispatch('auth/getInfo')
                this.$store.dispatch('main/setSocketConnFlag', true)
                this.$store.dispatch('main/setSysMessageList')
                this.userInfo.username = ''
                this.userInfo.password = ''
                this.userInfo.code = ''
                this.userInfo.codeKey = ''
                this.checkNewUser(res.user)
                this.reloadCode()
              })
              .catch((err) => {
                this.loading = false
                this.userInfo.code = ''
                this.reloadCode()
              })
        } else {
          console.log('error submit!!')
          return false
        }
        setTimeout(() => {
          this.loading = false
        }, 2000)
      })
    },
    toLogin() {
      this.login = true
      this.$store.dispatch('auth/setLoginFlag', true)
    },
    logoutHandle() {
      this.$store.dispatch('auth/logout')
    },
    init() {
      getLinkList().then(res => {
        this.navLinkList = res.data
        setCache(HEADER_NAV_LINK_LIST_KEY, res.data)
      }).catch(err => {
        this.navLinkList = getCache(HEADER_NAV_LINK_LIST_KEY)
      })
    },
    changeCode() {
      this.refresh = false
      this.$nextTick(() => {
        this.refresh = true
      })
    },
    querySearchAsync(queryString, cb) {
      if (this.hotSearchArticleList && this.hotSearchArticleList.length > 0) {
        const searchList = this.hotSearchArticleList.map(a => {
          const obj = {
            articleId: a.articleId,
            value: a.articleTitle
          }
          return obj;
        })
        cb(searchList)
      } else {
        cb([])
      }
    },
    registerHandle() {
      this.$refs.registerForm.validate(valid => {
        if (valid) {
          this.loading = true
          const registerInfo = {...this.registerInfo, password: AES.encrypt(this.registerInfo.password), password2: AES.encrypt(this.registerInfo.password2)}
          register(registerInfo).then(res => {
            this.$message.success('用户注册成功')
            this.loading = false
            this.registerInfo = defaultRegisterInfo
            this.$refs.registerForm.resetFields()
            this.nativeToLogin()
          })
        } else {
          console.log('error submit!!')
          return false
        }
        setTimeout(() => {
          this.loading = false
        }, 2000)
      })
    },
    nativeToLogin() {
      this.login = true
      this.$refs.loginForm.resetFields();
    },
    nativeToRegister() {
      this.login = false
      this.$refs.registerForm.resetFields();
    },
    sendEmailCode() {
      this.$refs.registerForm.validateField('email', valid => {
        if (!valid) {
          if (this.pause) {
            this.$message.error('操作太频繁,请稍后再试')
          } else {
            sendEmailCode({email: this.registerInfo.email}).then(res => {
              this.$message.success('验证码发送成功,请注意查收!')
              this.pause = true
              const timer = setInterval(() => {
                if (this.time > 0) {
                  this.time = this.time - 1;
                }
                if (this.time <= 0) {
                  this.pause = false;
                  clearInterval(timer)
                  this.time = 30
                }
              }, 1000)
            })
          }
        }
      });
    },
    checkNewUser(user) {
      this.$nextTick(() => {
        if (user.userType && user.userType === 1) {
          this.dialogVisible = true
        }
      })
    },
    toUpdateSelfInfo() {
      this.dialogVisible = false
      this.$router.push('/profile/personal')
    },

    tradeInit(){
      getPointRechargeTypeList().then(res => {
        this.pointTypeList = res.data
      })
    },
    pay() {
      this.paySucc = false;
      this.tradeDialogVisible = true;
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
        this.tradeDialogVisible = true;
        const _this = this;
        //使用webSocket发送请求，下面会简单介绍websocket使用
        if ("WebSocket" in window) {
          // 打开一个 web socket
          var ws = new WebSocket(`${process.env.VUE_APP_Websocket}://${location.host}${URL_PREFIX}/bindingRecord/${token}`);
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
                _this.tradeDialogVisible = false;
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

<style scoped lang="less">
ol, ul, li {
  list-style: none;
}

.header-all {
  min-width: 1080px;
  min-height: 120px;
  .login-form, .register-form {
    width: 80%;
    margin: 0 auto;

    .img-code {
      cursor: pointer;
    }
  }

  .header-top {
    border-bottom: 1px solid #dedede;

    .top-nav {
      padding-top: 10px;
      height: 40px;
      width: 1080px;
      line-height: 30px;
      margin: 0 auto 0;

      .menu-topmenu-container {
        border: 0;
        font-family: inherit;
        font-size: 100%;
        font-style: inherit;
        margin: 0;
        outline: 0;
        padding: 0;
        vertical-align: baseline;

        // 最顶部
        .top-menu {
          float: right;
          margin: 0 8px 0 0;
          li {
            float: left;
            margin-right: 20px;

            a {
              color: #444;
              text-decoration: none;
            }

            a:hover {
              color: #409eff;
            }
          }
        }
      }
    }
  }

  .header-main {
    background: #fff;
    z-index: 999;
    right: 0;
    border-bottom: 1px solid #ddd;
    box-shadow: 0 1px 1px rgb(0 0 0 / 4%);

    .top-menu {
      width: 1080px;
      margin: 0 auto;
      display: table;
      transition-duration: .5s;

      .logo-search{
        min-height: 90px;
        padding: 10px;
        box-sizing: border-box;
        display: flex;
        align-items: center;

        .nav-search {
          color: #fff;
          line-height: 26px;
          margin: 28px 10px 0 10px;
          padding: 0 8px 3px;
          cursor: pointer;
          border-radius: 3px;
          border: 1px solid #409eff;
          box-shadow: 0 1px 1px rgb(0 0 0 / 4%);

          .fa {
            display: inline-block;
            font: normal normal normal 14px/1 FontAwesome;
            font-size: inherit;
            text-rendering: auto;
            -webkit-font-smoothing: antialiased;
          }
        }

        .nav-search:hover {
          background: #409eff;
          border: 1px solid #595959;
        }

        .logo-site {
          float: left;
          width: 40%;
          min-width: 10%;
          max-height: 100px;
          transition-duration: .5s;

          .site-title {
            font-size: 24px;
            font-size: 2.4rem;
            font-weight: 700;
            padding: 0 0 2px 0;

            a {
              color: #555;
            }
          }
          .site-description{

          }
        }

        .logo-site::before {
          content: "";
          position: absolute;
          left: -665px;
          top: -460px;
          width: 220px;
          height: 15px;
          background-color: rgba(255, 255, 255, .5);
          -webkit-transform: rotate(-45deg);
          -moz-transform: rotate(-45deg);
          -ms-transform: rotate(-45deg);
          -o-transform: rotate(-45deg);
          transform: rotate(-45deg);
          -webkit-animation: searchLights 1s ease-in 1s infinite;
          -o-animation: searchLights 1s ease-in 1s infinite;
          animation: searchLights 1s ease-in 1s infinite;
        }

        .search-main {
          float: left;
          width: 60%;
          text-align: center;

          .search-input {
            box-sizing: border-box;
            padding: 0;
            width: 50%;
            font: 14px "Microsoft YaHei", Helvetica;
            margin-right: 10px;
          }
        }
      }
    }

    // 进入(显示)开始时和离开(隐藏)结束时效果
    .search-main-enter,
    .search-main-leave-to {
      // 设置透明度，0表示隐藏，1表示显示，我这里解释的并不是很对，这是css基础知识，大家可以在w3C中查找资料了解一下
      opacity: 0;
      height: 0;
    }

    // 进入(显示)过程中和离开(隐藏)过程中的效果
    .search-main-enter-active,
    .search-main-leave-active {
      // 设置过渡持续时间
      transition: all 0.5s;
      height: 80px;
    }
  }
}

</style>
