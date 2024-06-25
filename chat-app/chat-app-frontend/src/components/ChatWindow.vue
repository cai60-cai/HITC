<template>
  <div class="chat-window">
    <div class="header">
      <span class="title">{{friend.username}}</span>
      <el-button
          type="danger"
          size="mini"
          @click="closeChat"
          style="background-color: #20C997; border-color: #20C997; color: #FFFFFF;">
        关闭
      </el-button>
    </div>
    <div class="message-container" ref="messageContainer">
      <div
          class="message"
          v-for="message in localMessages"
          :key="message.id"
          :class="{
           'message-sent': message.senderId === currentUser.user_id,
           'message-received': message.senderId !== currentUser.user_id}"
      >

        <div class="message-content">
          <div class="text">{{ message.content }}</div>
        </div>
        <div v-if="message.senderId !== currentUser.user_id" class="message-avatar-left">
<!--          <el-avatar :src="message.senderAvatarUrl" size="large" class="avatar"></el-avatar>-->
        </div>
        <div v-else class="message-avatar-right">
          <el-avatar :src="currentUser.avatar" size="large" class="avatar"></el-avatar>
        </div>
      </div>
    </div>
    <div class="chat-box">
      <div class="toolbar">
        <button class="icon-btn"><i class="icon smiley"></i></button>
        <button class="icon-btn"><i class="icon scissors"></i></button>
        <button class="icon-btn"><i class="icon folder"></i></button>
        <button class="icon-btn"><i class="icon image"></i></button>
        <button class="icon-btn"><i class="icon phone"></i></button>
        <button class="icon-btn"><i class="icon envelope"></i></button>
        <button class="icon-btn"><i class="icon microphone"></i></button>
        <button class="icon-btn"><i class="icon clock"></i></button>
      </div>
      <div class="message-input">
      <textarea
          v-model="localMessage"
          placeholder="Type a message"
          @keyup.enter="sendMessage"
          class="message-textarea"
      ></textarea>
        <div class="buttons">
          <button class="close-btn" @click="closeChat">关闭</button>
          <button class="send-btn" @click="sendMessage">发送</button>
        </div>
      </div>
    </div>
<!--    <div class="input-container">-->
<!--      <el-input-->
<!--          v-model="localMessage"-->
<!--          placeholder="Type a message"-->
<!--          @keyup.enter="sendMessage"-->
<!--      />-->
<!--      <el-button type="primary" @click="sendMessage">发送</el-button>-->
<!--    </div>-->
  </div>
</template>

<script>
import axios from 'axios';

export default {
  props: {
    friend: Object,
    messages: Array,
    currentUser: Object,
  },
  data() {
    return {
      localMessage: '',
      localMessages: [],
      localcurrentUser: {
        user_id: null,
        user_name: '',
        avatar: ''
      }
    };
  },
  watch: {
    messages: {
      handler(newMessages) {
        this.localMessages = [...newMessages];
        this.scrollToBottom();

      },
      immediate: true,
    },
  },
  methods: {
    async fetchCurrentUser() {
      const user_id = localStorage.getItem('userId');
      if (!user_id) {
        this.$router.push('/');
        return;
      }
      try {
        const response = await axios.get(`/user/${user_id}`);
        this.localCurrentUser = response.data;
        const baseUrl = 'http://127.0.0.1:3000/server';
        this.localCurrentUser.avatar = `${baseUrl}${this.localCurrentUser.avatar}`;
      } catch (error) {
        console.error(error);
        this.$message.error('Failed to fetch user information.');
      }
    },

    async sendMessage() {
      if (!this.localMessage.trim()) return;

      const message = {
        senderId: this.currentUser.user_id,
        receiverId: this.friend.friendId,
        content: this.localMessage,
        senderUsername: this.currentUser.user_name,
        messageContent: this.localMessage,
        senderAvatarUrl: this.currentUser.avatar
      };

      try {
        const response = await axios.post('http://localhost:3001/user/sendMessage', message);
        this.localMessages.push({
          ...message,
          id: response.data.id, // 使用响应的ID更新本地消息
          timestamp: new Date().toISOString(),
        });
        this.localMessage = '';
        this.scrollToBottom();
      } catch (error) {
        console.error('Error sending message:', error);
      }
    },
    closeChat() {
      this.$emit('close-chat');
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messageContainer;
        container.scrollTop = container.scrollHeight;
      });
    },
  },
};
</script>
<style scoped>
.chat-window {
  display: flex;
  flex-direction: column;
  height: 95%;
  width: 100%;
  border-radius: 15px;
  border: 1px solid #ccc;
  background: white;
  box-sizing: border-box;
  overflow: hidden;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #ccc;
  padding: 10px;
  background: #007BFF;
  color: white;
}

.title {
  font-size: 1.5em;
  font-weight: bold;
  text-align: center;
  flex: 1;
  color: white;
}

.message-container {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  border-bottom: 1px solid #ccc;
  background: #F8F9FA;
}

.message {
  display: flex;
  margin-bottom: 10px;
}

.message-sent {
  justify-content: flex-end;
}

.message-received {
  justify-content: flex-start;
}

.message-content {
  display: flex;
  align-items: center;
  max-width: 80%;
}

.text {
  background-color: #20C997;
  padding: 10px;
  border-radius: 10px;
  word-wrap: break-word;
  word-break: break-all;
  font-size: 18px; /* 设置基础字体大小 */
}

.message-sent .text {
  background-color: #007BFF;
  color: white;
  font-size: 20px; /* 设置发送消息的字体大小 */
}

.message-received .text {
  background-color: white;
  color: black;
  font-size: 20px; /* 设置接收消息的字体大小 */
}


.input-container {
  display: flex;
  align-items: center;
  padding: 10px;
  border-top: 1px solid #ccc;
  background-color: white;
}

.el-input {
  flex: 1;
  margin-right: 10px;
}

.chat-box {
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 10px;
}

.toolbar {
  display: flex;
  justify-content: flex-start;
  padding: 5px;
  background: #f5f5f5;
  border-bottom: 1px solid #ddd;
}

.icon-btn {
  background: none;
  border: none;
  padding: 5px;
  cursor: pointer;
}

.icon {
  font-size: 20px;
}

.smiley::before { content: '😊'; }
.scissors::before { content: '✂️'; }
.folder::before { content: '📁'; }
.image::before { content: '🖼️'; }
.phone::before { content: '📞'; }
.envelope::before { content: '✉️'; }
.microphone::before { content: '🎤'; }
.clock::before { content: '🕒'; }

.message-input {
  display: flex;
  background: #f5f5f5;
  flex-direction: column;
  margin-top: 10px;
}

textarea {
  resize: none;
  height: 150px; /* 调整文本框的高度 */
  background-color: #f5f5f5; /* 设置背景色为浅灰色 */
  border: none; /* 去掉边框 */
  border-radius: 5px;
  padding: 10px;
  margin-bottom: 10px;
}

.buttons {
  display: flex;
  justify-content: flex-end;
}

.close-btn, .send-btn {
  background-color: #fff;
  border: 1px solid #ddd;
  border-radius: 5px;
  padding: 10px 20px;
  margin-left: 10px;
  cursor: pointer;
}

.send-btn {
  background-color: #007bff;
  color: white;
  border: none;
}
.message-avatar-left {
  margin-right: 10px;
  order: 1; /* 将头像排在左边 */
}

.message-avatar-right {
  margin-left: 10px;
  order: 3; /* 将头像排在右边 */
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

</style>