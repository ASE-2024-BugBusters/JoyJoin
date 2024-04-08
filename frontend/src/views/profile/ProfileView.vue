<script setup>

</script>

<template>
  <div class="user-profile container" v-if="userProfile">
    <div class="row">
      <div class="col-12 col-md-4">
        <div class="avatar-container">
          <img class="avatar" :src="userProfile.avatar_url" alt="User Avatar">
        </div>
      </div>
      <div class="col-12 col-md-8">
        <!-- 用户信息 -->
        <h1>{{ userProfile.name }}</h1>
        <h2>{{ userProfile.nickname }}</h2>
        <p><strong>Bio:</strong> {{ userProfile.biography }}</p>
        <p><strong>Tags:</strong> {{ userProfile.interestTags }}</p>
      </div>
      <router-link :to="{ name: 'EditProfile' }" class="btn btn-primary">Edit</router-link>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import {INTEREST_TAGS} from "../../../config/dev.env";

export default {
  name: 'UserProfile',
  data() {
    return {
      userProfile: null
    }
  },
  created() {
    this.fetchUserProfile();
  },
  methods: {
    async fetchUserProfile() {
      try {
        const response = await axios.get('http://localhost:8086/api/user/a2227c86-fb43-439c-b866-b5b4871d509d', {
          headers: {
            'Authorization': `Bearer ${sessionStorage.jwtToken}`
          }
        });
        this.userProfile = {
          nickname: response.data.nickname,
          biography: response.data.biography
        }
        if (response.data.avatar) {
          this.userProfile.avatar_url = response.data.avatar.urls[0].url
        }
        if (response.data.firstName || response.data.lastName) {
          this.userProfile.name = response.data.firstName + ' ' + response.data.lastName
        }
        if (response.data.interestTags) {
          let interestTags = []
          for (const tag of response.data.interestTags) {
            for (const pair of INTEREST_TAGS) {
              if (tag === pair.value) {
                interestTags.push(pair.label)
              }
            }
          }
          this.userProfile.interestTags = interestTags.join(', ')
        }
      } catch (e) {
        console.error(e)
        alert('Failed to fetch user profile')
      }
    }
  }
}
</script>

<style scoped>
.user-profile {
  margin-top: 20px;
}

.avatar-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.avatar {
  border-radius: 50%; /* 使图片变为圆形 */
  width: 150px; /* 设置图片大小 */
  height: 150px; /* 设置图片大小 */
  object-fit: cover; /* 确保图片完全覆盖容器 */
}

@media (min-width: 768px) {
  .avatar-container {
    justify-content: flex-start;
  }
}
</style>