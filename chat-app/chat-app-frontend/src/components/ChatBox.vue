<template>
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
      <textarea></textarea>
      <div class="buttons">
        <button class="close-btn">关闭</button>
        <button class="send-btn">发送</button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ChatBox',
};
</script>

<style scoped>
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
  justify-content: space-between;
  margin-bottom: 10px;
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
  flex-direction: column;
}

textarea {
  resize: none;
  height: 100px;
  border: 1px solid #ddd;
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
</style>
