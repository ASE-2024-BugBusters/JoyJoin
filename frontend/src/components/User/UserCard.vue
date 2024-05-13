<template>
  <div class="user-card">
    <img :src="avatar_url()" :alt="user.accountName" class="avatar" @click="navigateToUser">
    <div class="user-info">
      <h3 @click="navigateToUser">{{ user.accountName }}</h3>
    </div>
    <button @click="action" class="action-button"> {{ action_text }}</button>
  </div>
</template>

<script setup>
import {useRouter} from "vue-router";

const props = defineProps({
  user: Object,
  action_text: String,
})
const emit = defineEmits(['action', 'navigate'])

function avatar_url() {
  const default_avatar_url = require('@/assets/Default_User_Icon.png');
  if (!props.user || !props.user.avatar || !props.user.avatar.urls || !props.user.avatar.urls[0] || !props.user.avatar.urls[0].url) {
    return default_avatar_url;
  }
  return props.user.avatar.urls[0].url;
}

function action() {
  emit('action', props.user)
}

function navigateToUser() {
  emit('navigate', props.user)
}
</script>

<style scoped>
.user-card {
  display: flex;
  align-items: center;
  margin: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right: 15px;
}

.user-info h3 {
  margin: 0;
  font-size: 16px;
}

.user-info p {
  margin: 0;
  color: #666;
}

.action-button {
  margin-left: auto;
  padding: 5px 10px;
  background-color: #f44336;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>