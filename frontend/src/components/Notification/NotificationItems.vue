<template>
  <div v-if="notifications.length">
    <div v-for="news in notifications" :key="news.by">
      <div class="news-container">
        <div class="left-content">
          <router-link :to="{ name: 'home'}">
            <img class="user-image notification-user-image" src="../../assets/camera-icon.png" alt="User Profile Picture"/>
          </router-link>
          <div class="notification-info">
            <div>
              <router-link :to="{ name: 'home'}"><span class="username">{{ news.by }}</span></router-link>
              <span v-if="news.type === 'like'">&nbsp;liked your post.</span>
              <span v-else-if="news.type === 'comment'">&nbsp;commented your post.</span>
              <span v-else-if="news.type === 'new_post'">&nbsp;has just posted. Check it out!</span>
            </div>
          </div>
        </div>
        <router-link :to="{ name: 'post'}" class="post-wrapper">
          <img class="post_small_img" src="../../assets/camera-icon.png" alt="Post Picture">
        </router-link>
      </div>
    </div>
  </div>
  <div v-else>
    There is no nofication yet...
  </div>
</template>

<script>
export default {
  data() {
    return {
      currentUser: "kayannn",
      isRead: false,
      notifications: [
        {type: "like", by: "adachan", post: "12", timestamp: "202404041912"},
        {type: "comment", by: "seanchee", post: "12", timestamp: "202404041912"},
        {type: "new_post", by: "cercil", post: "14", timestamp: "202404041912"},
        {type: "like", by: "adachan", post: "12", timestamp: "202404041912"},
        {type: "comment", by: "seanchee", post: "12", timestamp: "202404041912"},
        {type: "new_post", by: "cercil", post: "14", timestamp: "202404041912"},
        {type: "like", by: "adachan", post: "12", timestamp: "202404041912"},
        {type: "comment", by: "seanchee", post: "12", timestamp: "202404041912"},
        {type: "new_post", by: "cercil", post: "14", timestamp: "202404041912"},
      ]
    }
  },
  methods: {

  }

}
</script>

<style scoped>
.news-container{
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  color: black;
  border-bottom: 1px solid #ECECEC;
  margin-bottom: 3px;
  padding: 10px;
}
.notification-user-image {
  width: 35px;
  height: 35px;
}
.left-content {
  display: flex;
  align-items: center;
}
.post_small_img{
  width: 35px;
  height: 35px;
  border-radius: 3px;
  border:1px solid #ECECEC;
  object-fit: cover;
}
.notification-info {
  display: flex;
  flex-direction: column;
  text-align: left;
  font-size: 13px;
  align-items: flex-start;
}
.username{
  color: black;
  text-decoration:none;
}
.username:hover{
  text-decoration: underline;
}
</style>