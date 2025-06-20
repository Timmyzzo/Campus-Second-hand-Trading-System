<template>
  <div class="message-center-container">
    <h2>消息中心</h2>
    <div v-if="conversations.length > 0">
      <el-card v-for="convo in conversations" :key="convo.id" class="convo-card" @click="openChat(convo)">
        <div class="convo-content">
          <div class="left">
            <p class="product-name">关于: {{ convo.productName }}</p>
            <p class="other-party">对话人: {{ convo.otherPartyUsername }}</p>
          </div>
          <div class="right">
            <p class="last-message">{{ convo.content }}</p>
            <p class="time">{{ new Date(convo.created_at).toLocaleString() }}</p>
          </div>
        </div>
      </el-card>
    </div>
    <el-empty v-else description="暂无消息"></el-empty>

    <!-- 复用详情页的聊天对话框 -->
    <el-dialog v-model="chatDialogVisible" :title="`与 ${chatTarget.name} 的对话`" width="50%">
      <div class="message-box">
        <div v-for="msg in messages" :key="msg.id" class="message-item" :class="{ 'is-me': msg.senderId === currentUser.id }">
          <div class="message-content">{{ msg.content }}</div>
        </div>
      </div>
      <el-input v-model="newMessage" type="textarea" :rows="3" placeholder="请输入回复..." style="margin-top: 20px;"></el-input>
      <template #footer>
        <el-button @click="chatDialogVisible = false">关闭</el-button>
        <el-button type="primary" @click="replyMessage">发送</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { getMyConversationsService, getMessagesService, sendMessageService } from '@/api/message.js';
import { ElMessage } from 'element-plus';

const conversations = ref([]);
const currentUser = ref(JSON.parse(localStorage.getItem('user')));

onMounted(async () => {
  if (currentUser.value) {
    const response = await getMyConversationsService(currentUser.value.id);
    conversations.value = response.data;
  }
});

// --- 聊天对话框逻辑 ---
const chatDialogVisible = ref(false);
const messages = ref([]);
const newMessage = ref('');
const chatTarget = ref({}); // 存储当前对话对象

const openChat = async (convo) => {
  const otherPartyId = convo.sender_id === currentUser.value.id ? convo.receiver_id : convo.sender_id;
  chatTarget.value = {
    id: otherPartyId,
    name: convo.otherPartyUsername,
    productId: convo.product_id
  };

  chatDialogVisible.value = true;

  const response = await getMessagesService(convo.product_id, currentUser.value.id, otherPartyId);
  messages.value = response.data;
};

const replyMessage = async () => {
  if (!newMessage.value.trim()) return;
  const messageData = {
    productId: chatTarget.value.productId,
    senderId: currentUser.value.id,
    receiverId: chatTarget.value.id,
    content: newMessage.value,
  };
  await sendMessageService(messageData);
  newMessage.value = '';
  // 刷新当前对话
  const response = await getMessagesService(chatTarget.value.productId, currentUser.value.id, chatTarget.value.id);
  messages.value = response.data;
};
</script>

<style scoped>
.message-center-container {
  background: #fff;
  padding: 20px;
  border-radius: 5px;
}
/* 新增：设置标题颜色 */
h2 {
  text-align: center;
  margin-bottom: 20px;
  color: #303133;
  font-weight: 600;
}
.convo-card { margin-bottom: 15px; cursor: pointer; }
.convo-card:hover { background-color: #f5f7fa; }
.convo-content { display: flex; justify-content: space-between; align-items: center; }
.product-name { font-weight: bold; }
.other-party, .time { font-size: 14px; color: #909399; }
.last-message { max-width: 300px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }
/* 聊天对话框的样式 */
.message-box { height: 300px; overflow-y: auto; border: 1px solid #e4e7ed; padding: 10px; border-radius: 4px; }
.message-item { margin-bottom: 15px; max-width: 80%; }
.message-item .message-content { display: inline-block; padding: 10px 15px; border-radius: 10px; background-color: #f0f2f5; color: #303133; word-break: break-all; }
.message-item.is-me { margin-left: auto; text-align: right; }
.message-item.is-me .message-content { background-color: #409eff; color: #fff; }
</style>
