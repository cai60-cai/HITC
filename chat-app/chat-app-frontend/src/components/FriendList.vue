<template>
  <div class="friend-list">
    <el-input v-model="searchQuery" placeholder="Search friends" @input="searchFriends"></el-input>
    <el-button @click="getFriendRequests">Friend Requests</el-button>
    <el-list>
      <el-list-item v-for="friend in friends" :key="friend.id" @click="selectFriend(friend)">
        {{ friend.user_name }}
      </el-list-item>
    </el-list>
    <el-dialog title="Friend Requests" :visible.sync="dialogVisible">
      <el-list>
        <el-list-item v-for="request in friendRequests" :key="request.id">
          {{ request.senderId }}
          <el-button @click="acceptRequest(request.id)">Accept</el-button>
        </el-list-item>
      </el-list>
    </el-dialog>
  </div>
</template>

<script>
import axios from '../axios';

export default {
  name: 'FriendList',
  data() {
    return {
      friends: [],
      friendRequests: [],
      searchQuery: '',
      dialogVisible: false
    };
  },
  methods: {
    searchFriends() {
      // Implement search logic here
    },
    selectFriend(friend) {
      this.$emit('friendSelected', friend);
    },
    async getFriendRequests() {
      this.dialogVisible = true;
      try {
        const response = await axios.get(`/user/friendRequests?userId=${this.$root.userId}`);
        this.friendRequests = response.data;
      } catch (error) {
        console.error(error);
      }
    },
    async acceptRequest(requestId) {
      try {
        await axios.post('/user/acceptFriendRequest', { requestId });
        this.getFriendRequests(); // Refresh the requests list
      } catch (error) {
        console.error(error);
      }
    }
  },
  created() {
    // Fetch friend list on component creation
    this.friends = [
      // Example friend list; replace with actual API call
      { id: 1, user_name: 'Friend 1' },
      { id: 2, user_name: 'Friend 2' }
    ];
  }
};
</script>

<style scoped>
.friend-list {
  width: 300px;
  padding: 10px;
  border-right: 1px solid #ddd;
}
</style>
