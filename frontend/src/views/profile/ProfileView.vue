<script setup>

</script>

<template>
  <div class="user-profile container" v-if="userProfile">
    <UserModal id="followers" :visible="showFollowerList" :users="followers" :action_text='"Remove"'
      @action="handleRemoveFollower" @close="showFollowerList = false"></UserModal>
    <UserModal id="followings" :visible="showFollowingList" :users="followings" :action_text='"Unfollow"'
      @action="handleUnfollow" @close="showFollowingList = false"></UserModal>

    <div class="row">
      <div class="col-12 col-md-4">
        <div class="avatar-container">
          <img class="avatar" :src="userProfile.avatar_url" alt="User Avatar">
        </div>
      </div>
      <div class="col-12 col-md-8">
        <h1>{{ userProfile.name }}</h1>
        <h2>{{ userProfile.nickname }}</h2>
        <p><strong>Bio:</strong> {{ userProfile.biography }}</p>
        <p><strong>Tags:</strong> {{ userProfile.interestTags }}</p>
        <div>
          <router-link :to="{ name: 'EditProfile' }" class="btn btn-primary" v-if="isSelf">Edit</router-link>
          <button type="button" class="btn btn-primary" v-if="!isSelf && !isFollowing" id="follow_btn" @click="follow">Follow Me</button>
          <button type="button" class="btn btn-primary" v-if="!isSelf && isFollowing" id="unfollow_btn" @click="unfollow">Unfollow</button>
          <button type="button" class="btn btn-primary" @click="showFollowerList = true">Followers</button>
          <button type="button" class="btn btn-primary" @click="showFollowingList = true">Followings</button>
        </div>
      </div>
    </div>
  </div>

  <!--User's All Posts-->
  <UserAllPosts></UserAllPosts>
</template>

<script>
import axios from 'axios'
import { BASE_URL_USER_SERVICE, INTEREST_TAGS } from "../../../config/dev.env";
import { useRoute } from 'vue-router';
import UserAllPosts from '@/components/Posts/UserAllPosts.vue';
import UserModal from '@/components/User/UserModal.vue';
import { computed, onMounted, ref } from 'vue';


export default {
  name: 'UserProfile',
  components: {
    UserAllPosts,
    UserModal,
  },
  setup() {
    const isLoading = ref(false);
    const userId = ref("");
    const isSelf = ref(false);
    const userProfile = ref(null);
    const followers = ref([]);
    const followings = ref([]);
    const showFollowerList = ref(false);
    const showFollowingList = ref(false);
    const isFollowing = computed({
      get() {
        for (let follower in followers.value) {
          if (follower.userId == userId.value) {
            return true;
          }
        }
        return false;
      }
    });

    const route = useRoute();
    userId.value = route.params.user_id;
    isSelf.value = (userId.value == sessionStorage.userId);
    console.log(userId.value)
    console.log(sessionStorage.userId);
    console.log(userId.value == sessionStorage.userId)
    console.log(userId.value === sessionStorage.userId)

    const axios_options = {
      headers: {
        'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
      }
    };

    const fetchUserProfile = async function () {
      const getProfileUrl = BASE_URL_USER_SERVICE + "/user/users/" + userId.value;
      const default_avatar_url = "https://www.gravatar.com/avatar/2c7d99fe281ecd3bcd65ab915bac6dd5?s=250";
      try {
        const response = await axios.get(getProfileUrl, axios_options);
        const data = response.data[0];
        console.log(data);
        userProfile.value = {
          nickname: data.nickname,
          biography: data.biography
        };
        if (data.avatar) {
          userProfile.value.avatar_url = data.avatar.urls[0].url;
        } else {
          userProfile.value.avatar_url = default_avatar_url;
        }
        if (data.firstName || data.lastName) {
          userProfile.value.name = data.firstName + ' ' + data.lastName;
        }
        if (data.interestTags) {
          let interestTags = [];
          for (const tag of data.interestTags) {
            for (const pair of INTEREST_TAGS) {
              if (tag === pair.value) {
                interestTags.push(pair.label);
              }
            }
          }
          userProfile.value.interestTags = interestTags.join(', ');
        }
      } catch (e) {
        console.error(e);
        alert('Failed to fetch user profile');
      }
    };

    const fetchFollowers = async function () {
      const url = BASE_URL_USER_SERVICE + '/user/users/' + userId.value + '/follower';
      console.log(url);
      try {
        const response = await axios.get(url, axios_options);
        followers.value = response.data;
      } catch (error) {
        console.error('Error fetching followers:', error);
        followers.value = [];
      }
    };

    const fetchFollowings = async function () {
      const url = BASE_URL_USER_SERVICE + '/user/users/' + userId.value + '/followee';
      console.log(url);
      try {
        const response = await axios.get(url, axios_options);
        followers.value = response.data;
      } catch (error) {
        console.error('Error fetching followers:', error);
        followers.value = [];
      }
    };

    const follow = async function(e) {
      const follower_id = sessionStorage.userId;
      const followee_id = userId.value;
      const url = BASE_URL_USER_SERVICE + '/user/users/' + follower_id + '/followee/' + followee_id;
      try {
        const resp = await axios.put(url, axios_options);
      } catch(e) {
        console.error(e)
        alert("failed to follow user");
      }
      await fetchFollowers();
    };
    const unfollow = async function(e) {
      console.log(e)
    };
    const handleRemoveFollower = async function(user) {
      console.log(user);
    };
    const handleUnfollow = async function(user) {
      console.log(user);
    };

    onMounted(async () => {
      Promise.all([
        fetchUserProfile(),
        fetchFollowers(),
        fetchFollowings(),
      ])
    });

    return {
      isLoading,
      isFollowing,
      userId,
      isSelf,
      userProfile,
      followers,
      followings,
      showFollowerList,
      showFollowingList,

      follow,
      unfollow,
      handleRemoveFollower,
      handleUnfollow,
    }
  },
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
  border-radius: 50%;
  width: 150px;
  height: 150px;
  object-fit: cover;
}

@media (min-width: 768px) {
  .avatar-container {
    justify-content: flex-start;
  }
}
</style>