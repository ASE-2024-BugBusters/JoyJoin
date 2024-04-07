<template>
  <div>
    <div class="notifications-icon" @click="openNotificationList()">
      <font-awesome-icon :icon="['far', 'bell']" class="noti-icon" title="Notification"/>
    </div>
    <div class="notifications-container" v-if="notification_open">
      <NotificationItems></NotificationItems>
    </div>
  </div>
</template>

<script>
import NotificationItems from './NotificationItems.vue';
export default {
  components: {NotificationItems},
  data() {
    return {
      notification_open: false,
    }
  },
  methods: {
    openNotificationList(){
      this.notification_open = !this.notification_open
    }
  }

}
</script>

<style scoped>
.notifications-icon{
  padding: 7px;
  border-radius: 50%;
  border: 1px solid #2c3e50;
  display: inline-block;
  width: 40px;
  height: 40px;
  text-align: center;
  cursor: pointer;
  margin: 0 5px;
}
.noti-icon{
  width: 20px;
  height: 20px;
}
.notifications-container{
  position: absolute;
  right: 0;
  top: 60px;
  background-color: white;
  color: #2c3e50;
  border: 1px solid #2c3e50;
  width: 400px;
  height: 70vh;
  padding: 10px;
  overflow-y: auto;
  overflow-x: hidden;
  border-radius: 3px;
  box-shadow: 3px 3px darkgray;
}
</style>