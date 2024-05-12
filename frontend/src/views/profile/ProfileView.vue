<script setup>

</script>

<template>
  <div v-if="userProfile" class="user-profile container">
    <UserModal id="followers" :action_text='"Remove"' :users="followers" :visible="showFollowerList"
               @action="handleRemoveFollower" @close="showFollowerList = false" @navigate="handleNavigate"></UserModal>
    <UserModal id="followings" :action_text='"Unfollow"' :users="followings" :visible="showFollowingList"
               @action="handleUnfollow" @close="showFollowingList = false" @navigate="handleNavigate"></UserModal>

    <div class="row">
      <div class="col-12 col-md-4">
        <div class="avatar-container">
          <img :src="userProfile.avatar_url" alt="User Avatar" class="avatar">
        </div>
      </div>
      <div class="col-12 col-md-8">
        <h1>{{ userProfile.name }}</h1>
        <h2>{{ userProfile.nickname }}</h2>
        <p><strong>Bio:</strong> {{ userProfile.biography }}</p>
        <p><strong>Tags:</strong> {{ userProfile.interestTags }}</p>
        <div>
          <router-link v-if="isSelf()" :to="{ name: 'EditProfile' }" class="btn btn-primary">Edit</router-link>
          <button v-if="!isSelf() && !isFollowing()" id="follow_btn" class="btn btn-primary" type="button"
                  @click="follow">
            Follow
          </button>
          <button v-if="!isSelf() && isFollowing()" id="unfollow_btn" class="btn btn-danger" type="button"
                  @click="unfollow">Unfollow
          </button>
          <button class="btn btn-primary" type="button" @click="showFollowerList = true">Followers</button>
          <button class="btn btn-primary" type="button" @click="showFollowingList = true">Followings</button>
        </div>
      </div>
    </div>
  </div>

  <!--User's All Posts-->
  <UserAllPosts></UserAllPosts>
</template>

<script>
import axios from 'axios'
import {BASE_URL_USER_SERVICE, INTEREST_TAGS} from "../../../config/dev.env";
import {useRoute, useRouter} from 'vue-router';
import UserAllPosts from '@/components/Posts/UserAllPosts.vue';
import UserModal from '@/components/User/UserModal.vue';
import {onMounted, ref, watch} from 'vue';


export default {
  name: 'UserProfile',
  components: {
    UserAllPosts,
    UserModal,
  },
  setup() {
    const isLoading = ref(false);
    const userId = ref("");
    const userProfile = ref(null);
    const followers = ref([]);
    const followings = ref([]);
    const showFollowerList = ref(false);
    const showFollowingList = ref(false);

    const route = useRoute();
    userId.value = route.params.user_id;

    const isFollowing = () => {
      for (const index in followers.value) {
        const follower = followers.value[index];
        if (follower.id === sessionStorage.userId) {
          return true;
        }
      }
      return false;
    };

    const isSelf = () => userId.value === sessionStorage.userId

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
        followings.value = response.data;
      } catch (error) {
        console.error('Error fetching followers:', error);
        followings.value = [];
      }
    };

    const followee_resource_url = (follower_id, followee_id) => {
      return BASE_URL_USER_SERVICE + '/user/users/' + follower_id + '/followee/' + followee_id;
    }

    const follow = async function (e) {
      const url = followee_resource_url(sessionStorage.userId, userId.value);
      try {
        const resp = await axios.put(url, null, axios_options);
        const new_follower = resp.data;
        followers.value.push(new_follower);
      } catch (e) {
        console.error(e);
        alert("failed to follow user");
      }
    };
    const unfollow = async function (e) {
      const url = followee_resource_url(sessionStorage.userId, userId.value);
      try {
        const resp = await axios.delete(url, axios_options);
        const removed_follower = resp.data;
        followers.value = followers.value.filter(item => item.id !== removed_follower.id);
      } catch (e) {
        console.error(e);
        alert("failed to unfollow user");
      }
    };
    const handleRemoveFollower = async function (user) {
      const url = followee_resource_url(user.id, userId.value);
      try {
        const resp = await axios.delete(url, axios_options);
        const removed_follower = resp.data;
        followers.value = followers.value.filter(item => item.id !== removed_follower.id);
      } catch (e) {
        console.error(e);
        alert("failed to remove follower");
      }
    };
    const handleUnfollow = async function (user) {
      const url = followee_resource_url(userId.value, user.id);
      try {
        const resp = await axios.delete(url, axios_options);
        const removed_followee = resp.data;
        followings.value = followings.value.filter(item => item.id !== removed_followee.id);
      } catch (e) {
        console.error(e);
        alert("failed to unfollow user");
      }
    };

    const router = useRouter();
    const handleNavigate = async function (user) {
      await router.push(user.id);
    }

    const init = async () => {
      await Promise.all([
        fetchUserProfile(),
        fetchFollowers(),
        fetchFollowings(),
      ])
    }

    watch(() => route.params.user_id, (new_id) => {
      console.log(new_id);
      userId.value = new_id;
      showFollowerList.value = false;
      showFollowingList.value = false;
      init();
    });

    onMounted(init);

    return {
      isLoading,
      userId,
      userProfile,
      followers,
      followings,
      showFollowerList,
      showFollowingList,
      isFollowing,
      isSelf,

      follow,
      unfollow,
      handleRemoveFollower,
      handleUnfollow,
      handleNavigate,
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