<template>
  <div class="login-register">
    <div class="login-container">
      <h2 class="title">工大聊天室</h2>
      <el-form :model="loginForm" @submit.native.prevent="handleLogin" class="login-form">
        <el-form-item label="用户名">
          <el-input v-model="loginForm.user_name"></el-input>
        </el-form-item>
        <el-form-item label="密码">
          <el-input type="password" v-model="loginForm.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-button">登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import axios from '../axios';

export default {
  name: 'LoginPage',
  data() {
    return {
      loginForm: {
        user_name: '',
        password: ''
      }
    };
  },
  methods: {
    async handleLogin() {
      try {
        console.log(this.loginForm);
        const response = await axios.post('/user/login', this.loginForm);
        if (response.data && response.data.user_id) {
          localStorage.setItem('userId', response.data.user_id); // 确保这里存储了用户 ID
          this.$router.push('/main');
        } else {
          this.$message.error('用户名或密码错误。');
        }
      } catch (error) {
        this.$message.error(error.response ? error.response.data : '登录失败。');
      }
    }
  }
};
</script>

<style scoped>
.login-register {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(135deg, #a0c4ff 0%, #c4f0ff 100%); /* 淡蓝色背景 */
}

.login-container {
  background-color: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  width: 100%;
  max-width: 400px;
}

.title {
  text-align: center;
  margin-bottom: 20px;
  font-size: 24px;
  color: #333;
}

.login-form {
  display: flex;
  flex-direction: column;
}

.el-form-item {
  margin-bottom: 15px;
}

.login-button {
  width: 100%;
}
</style>
