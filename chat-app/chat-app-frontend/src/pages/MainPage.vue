<template>
  <div class="main-page">
    <el-container>
      <el-header class="header">
        <div class="header-content">
          <div class="user-info" @click="triggerFileInput">
            <el-avatar :src="currentUser.avatar" size="large" class="avatar"></el-avatar>
            <span class="user_name">{{ currentUser.user_name }}</span>
            <input type="file" ref="fileInput" style="display: none" @change="handleFileChange" />
          </div>
          <span class="header-title">哈工大聊天室</span>
          <el-button class="logout-button" type="danger" @click="EXIT">EXIT</el-button>
        </div>
      </el-header>
      <el-container>
        <el-aside width="300px" class="sidebar">
          <el-autocomplete
              v-model="searchQuery"
              placeholder="Search friends"
              :fetch-suggestions="searchFriends"
              @select="handleSelect"
              :trigger-on-focus="false"
              popper-append-to-body
              :popper-class="'autocomplete-popper'"
              clearable
          >
            <template slot-scope="{ item }">
              <div class="name">{{ item.user_name }}</div>
            </template>
          </el-autocomplete>
          <div class="list-container">
            <div class="title">好友列表</div>
            <el-scrollbar class="friend-list">
              <el-menu class="el-menu-vertical-demo">
                <el-menu-item
                    v-for="(friend, index) in friends"
                    :key="friend.id"
                    :index="index.toString()"
                    @click="selectFriend(friend)"
                    class="friend-item"
                >
                  {{ friend.username }}
                </el-menu-item>
              </el-menu>
            </el-scrollbar>
          </div>
          <div class="list-container">
            <div class="title">好友请求</div>
            <el-scrollbar class="request-list">
              <el-menu class="el-menu-vertical-demo">
                <el-menu-item
                    v-for="(request, index) in friendRequests"
                    :key="request.id"
                    :index="index.toString()"
                    class="friend-item"
                >
                  {{ request.senderuser_name }}
                  <el-button
                      type="success"
                      size="mini"
                      @click="acceptFriendRequest(request.id)"
                      style="margin-left: 30px"
                  >接受</el-button>
                  <el-button
                      type="danger"
                      size="mini"
                      class="reject-button"
                      style="margin-left: 5px"
                  >拒绝</el-button>
                </el-menu-item>
              </el-menu>
            </el-scrollbar>
          </div>
        </el-aside>
        <el-container class="content">
          <chat-window
              v-if="selectedFriend && isFriend"
              :friend="selectedFriend"
              :messages="messages"
              :newMessage="newMessage"
              :currentUser="currentUser"
              @send-message="sendMessage"
              @close-chat="closeChat"
          ></chat-window>
        </el-container>
      </el-container>
    </el-container>
    <el-dialog :visible.sync="dialogVisible" title="Add Friend">
      <span>Would you like to send a friend request to {{ selectedFriend?.user_name }}?</span>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="sendFriendRequest">Send</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from '../axios';
import ChatWindow from '../components/ChatWindow.vue';


export default {
  name: 'MainPage',
  components: {
    ChatWindow
  },
  data() {
    return {
      searchQuery: '',
      friends: [],
      friendRequests: [],
      selectedFriend: null,
      messages: [],
      newMessage: '',
      currentUser: {},
      isFriend: false,
      dialogVisible: false
    };
  },
  async created() {
    await this.fetchCurrentUser();
    await this.fetchFriends();
    await this.fetchFriendRequests();
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
        this.currentUser = response.data;
        // 拼接自定义的基网址
        const baseUrl = 'http://127.0.0.1:3000/server';
        this.currentUser.avatar = `${baseUrl}${this.currentUser.avatar}`;
      } catch (error) {
        console.error(error);
        this.$message.error('Failed to fetch user information.');
      }
    },
    async fetchFriends() {
      try {
        const user_id = localStorage.getItem('userId');
        const response = await axios.get(`/user/friends/${user_id}`);
        this.friends = response.data.filter(friend => friend.id !== this.currentUser.user_id).map(friend => {
          console.log("sssssssssssss");
          console.log(response.data);
          console.log("sssssssssssss");
          console.log(friend);
          return friend;
        });
      } catch (error) {
        console.error(error);
      }
    },
    async fetchFriendRequests() {
      try {
        const userId = localStorage.getItem('userId');
        const response = await axios.get(`/user/friendRequests?userId=${userId}`);
        this.friendRequests = response.data.map(request => {
          request.avatar = `http://localhost:3001${request.avatar}`;
          return request;
        });
      } catch (error) {
        console.error(error);
      }
    },
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    handleFileChange(event) {
      const file = event.target.files[0];
      if (file) {
        this.uploadAvatar(file);
      }
    },
    async uploadAvatar(file) {
      const userId = localStorage.getItem('userId');
      const formData = new FormData();
      formData.append('avatar', file);
      try {
        const response = await axios.post(`http://localhost:3001/user/uploadAvatar/${userId}`, formData, {
          headers: {
            'Content-Type': 'multipart/form-data'
          }
        });
        this.currentUser.avatar = `http://localhost:3001${response.data.avatar}`;
      } catch (error) {
        console.error(error);
        this.$message.error('Failed to upload avatar.');
      }
    },

    async searchFriends(queryString, callback) {
      try {
        const response = await axios.get('/user/search', {
          params: { user_name: queryString }
        });
        const results = response.data.filter(user => user.user_id !== this.currentUser.user_id).map(user => {
          user.avatar = `http://localhost:3001${user.avatar}`;
          return user;
        });
        callback(results.map(user => ({
          value: user.user_name,
          ...user
        })));
      } catch (error) {
        console.error(error);
        callback([]);
      }
    },
    handleSelect(item) {
      // 检查好友是否在好友列表中
      const isFriend = this.friends.some(friend => friend.username === item.user_name);

      if (isFriend) {
        // 如果在好友列表中，选择好友进行聊天
        const friend = this.friends.find(friend => friend.username === item.user_name);
        this.selectFriend(friend);
      } else {
        // 如果不在好友列表中，显示加好友对话框，并确保聊天窗口不显示
        this.selectedFriend = item;
        this.isFriend = false;
        this.dialogVisible = true;
      }
      // 清空输入框
      this.searchQuery = '';
    },

    async selectFriend(friend) {
      // 检查是否在好友列表中
      this.isFriend = this.friends.some(f => f.id === friend.id);
      if (this.isFriend) {
        this.selectedFriend = friend;
        await this.fetchMessages(friend.friendId);
      } else {
        this.selectedFriend = null;
        this.dialogVisible = true;
      }
    },
    async fetchMessages(friendId) {
      try {
        const response = await axios.get('/user/messages', {
          params: { userId1: this.currentUser.user_id, userId2: friendId }
        });
        this.messages = response.data;
      } catch (error) {
        console.error(error);
      }
    },
    async sendMessage() {
      if (!this.newMessage || !this.selectedFriend) return;

      try {
        const response = await axios.post('/user/sendMessage', {
          senderId: this.currentUser.user_id,
          receiverId: this.selectedFriend.friendId,
          content: this.newMessage
        });
        this.messages.push(response.data);
        this.newMessage = '';
      } catch (error) {
        console.error(error);
      }
    },
    closeChat() {
      this.selectedFriend = null;
    },

    async sendFriendRequest() {
      try {
        const response = await axios.post('/user/sendFriendRequest', {
          senderId: this.currentUser.user_id,
          receiverId: this.selectedFriend.user_id
        });
        this.dialogVisible = false;
        this.$message.success(response.data);
      } catch (error) {
        console.error(error);
        this.$message.error('Failed to send friend request.');
      }
    },
    async acceptFriendRequest(requestId) {
      try {
        const response = await axios.post('/user/acceptFriendRequest', { requestId });
        this.$message.success(response.data);
        await this.fetchFriendRequests();
        await this.fetchFriends();
      } catch (error) {
        console.error(error);
      }
    },

    EXIT() {
      localStorage.removeItem('userId');
      this.$router.push('/');
    }
  }
};
</script>


<style scoped>



.main-page {
  display: flex;
  height: 100vh;
  background: linear-gradient(120deg, #FFFFFF 0%, #8fd3f4 100%); /* 背景渐变颜色 */
}

.header {
  background: #0056b3; /* 半透明黑色背景 */
  color: white;
  padding: 10px;
  text-align: center;
  font-size: 24px;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}


.header-title {
   position: absolute;
   left: 50%;
   transform: translateX(-50%);
   font-size: 1.5em; /* 更大一些的字体 */
   font-weight: bold;
   font-family: 'Brush Script MT', cursive; /* 飘逸的字体 */
   color: #ffffff; /* 纯白色 */
   text-shadow: 2px 2px 4px #000000; /* 添加更多的文本阴影以提高可读性 */
}



.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  cursor: pointer;
  display: flex;
  justify-content: center;
  align-items: center;
}

.user_name {
  margin-left: 10px;
  font-size: 1.2em;
  font-weight: bold;
}

.logout-button {
  background-color: #20C997;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 5px;
}

.sidebar {
  width: 300px;
  background: rgba(255, 255, 255, 0.8); /* 半透明白色背景 */
  padding: 20px;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1); /* 轻微阴影 */
  border-right: 1px solid #ddd;
}

.title {
  background-color: #0056b3; /* 更加柔和的绿色背景 */
  color: white;
  padding: 3px;
  font-size: 1.5em;
  font-weight: bold;
  border-radius: 10px;
  margin-bottom: 10px;
  text-align: center;
  font-family: 'Lucida Handwriting', cursive; /* 使用更加飘逸的字体 */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.3); /* 添加文本阴影以提高可读性 */
}

.list-container {
  background-color: rgba(255, 255, 255, 0.8); /* 半透明白色背景 */
  padding: 20px;
  margin-bottom: 20px;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1); /* 添加阴影效果 */
}
.friend-list, .request-list {
  height: 200px;
  overflow-y: auto;
}

.friend-item {
  display: flex;
  align-items: center;
  background-color: rgba(255, 255, 255, 0.7); /* 半透明白色背景 */
  margin-bottom: 1px;
  padding: 1px;
  border-radius: 1px;
  line-height: 0; /* 添加这一行以减少项的上下高度 */
}


.content {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
  box-sizing: border-box;
}

.el-autocomplete {
  display: flex;
  justify-content: center;
  margin-bottom: 10px;
}

.friend-request-buttons {
  display: flex;
  justify-content: space-around;
  margin-top: 5px;
}
</style>