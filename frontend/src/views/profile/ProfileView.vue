<script setup>

</script>

<template>
  <div class="user-profile container" v-if="userProfile">
    <div>
      <!-- 模态框 -->
      <div v-if="showModal" class="modal" style="display: block" tabindex="-1">
        <div class="modal-dialog">
          <div class="modal-content">
            <div class="modal-header">
              <h5 class="modal-title">Follower</h5>
              <button type="button" class="close" @click="closeModal">
                <span>&times;</span>
              </button>
            </div>
            <div class="modal-body">
              <ul>
                <li v-for="user in followers" :key="user.id">{{ user.name }}</li>
              </ul>
              <div v-if="followers.length === 0">没有关注者。</div>
            </div>
            <div class="modal-footer">
              <button type="button" class="btn btn-secondary" @click="closeModal">关闭</button>
            </div>
          </div>
        </div>
      </div>
      <div v-if="showModal" class="modal-backdrop"></div>
    </div>

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
        <div>
          <router-link :to="{ name: 'EditProfile' }" class="btn btn-primary" v-if="isSelf">Edit</router-link>
          <button type="button" class="btn btn-primary" v-if="!isSelf" id="follow_btn">Follow Me</button>
          <button type="button" class="btn btn-primary" v-if="!isSelf" id="unfollow_btn">Unfollow</button>
          <button type="button" class="btn btn-primary" @click="showFollowerList">Followers</button>
          <button type="button" class="btn btn-primary" @click="showFollowingList">Followings</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import {BASE_URL_USER_SERVICE, INTEREST_TAGS} from "../../../config/dev.env";
import {useRoute} from "vue-router";
import {computed, ref} from "vue";

export default {
  name: 'UserProfile',
  setup() {
    const route = useRoute();
    const userId = computed(() => route.params.user_id);
    const isSelf = sessionStorage.userId === userId.value;

    const showFollowerList = ref(false);
    const showFollowingList = ref(false);
    const followers = ref([]);
    const followings = ref([]);

    const openFollowerList = async () => {
      showFollowerList.value = true;
      try {
        const response = await axios.get('https://api.example.com/followers');
        followers.value = response.data; // 假设API直接返回用户列表
      } catch (error) {
        console.error('Error fetching followers:', error);
        followers.value = [];
      }
    };

    const openFollowingList = async () => {
      showFollowerList.value = true;
      try {
        const response = await axios.get('https://api.example.com/followers');
        followers.value = response.data; // 假设API直接返回用户列表
      } catch (error) {
        console.error('Error fetching followers:', error);
        followers.value = [];
      }
    };

    const closeFollowerList = () => {
      showFollowerList.value = false;
    };

    const closeFollowingList = () => {
      showFollowingList.value = false;
    };

    return {
      userId, isSelf,
      followers,
      followings,
      showFollowerList,
      showFollowingList,
      closeFollowerList,
      closeFollowingList
    };
  },
  data() {
    return {
      userProfile: null
    }
  },
  created() {
    this.checkFollowStatus();
    this.fetchUserProfile();
  },
  methods: {
    async checkFollowStatus() {
      try {
        const 
      }
    },
    async fetchUserProfile() {
      try {
        const getProfileUrl = BASE_URL_USER_SERVICE + "/user/users/" + this.userId
        const response = await axios.get(getProfileUrl, {
          headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
          }
        })
        const data = response.data[0]
        console.log(data)
        this.userProfile = {
          nickname: data.nickname,
          biography: data.biography
        }
        if (data.avatar) {
          this.userProfile.avatar_url = data.avatar.urls[0].url
        }
        if (data.firstName || data.lastName) {
          this.userProfile.name = data.firstName + ' ' + data.lastName
        }
        if (data.interestTags) {
          let interestTags = []
          for (const tag of data.interestTags) {
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

.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1040;
}

.modal {
  position: fixed;
  top: 10%;
  left: 50%;
  transform: translateX(-50%);
  z-index: 1050;
}
</style>