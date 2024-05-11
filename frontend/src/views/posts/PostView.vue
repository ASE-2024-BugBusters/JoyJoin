<template>
  <div class="container" v-if="finishLoaded && !pageError">
    <div class="row">
      <!--Left section: Image (Slide images)-->
      <div class="col-md-7 d-flex justify-content-center align-items-center">
        <ImageSlider :images="post.images"/>
      </div>

      <!--Right section: Caption, Tags, Events, Likes, Comment-->
      <div class="col-md-5" style="text-align: left;">
        <!--Username-->
        <div class="left-right-content-container">
          <div class="left-content">
            <img class="user-image navigate-info" v-if="!post.user.avatar" src="../../assets/Default_User_Icon.png" alt="User Profile Picture" @click="navigateToUserProfile(post.user.id)" title="Navigate to User Profile" />
            <img class="user-image navigate-info" v-else :src="post.user.avatar.urls[0].url"  alt="User Profile Picture" @click="navigateToUserProfile(post.user.id)" title="Navigate to User Profile" />
            <div class="user-info">
              <div class="username navigate-info" v-if="post.user" @click="navigateToUserProfile(post.user.id)" title="Navigate to User Profile"> {{ post.user.accountName }}</div>
              <div class="post-info" :class="isEditMode? 'post-info-edit' : ''">
                <span class="navigate-info" v-if="post.taggedEvent" @click="navigateToEvent(post.taggedEvent.eventId)" title="Navigate to Event Information">{{ post.taggedEvent.title }}</span>
                <span v-else-if="!post.taggedEvent && isEditMode">No event is binded</span>
                <font-awesome-icon v-if="isEditMode" class="post-icon post-info-icon" :icon="['fas', 'edit']" title="Edit Event" @click="openPostEventModal"/>
              </div>
              <div class="post-info" v-if="post.taggedUsers" :class="isEditMode? 'post-info-edit' : ''">
                <span v-if="post.taggedUsers.length">With
                  <span @click="openTaggedPeopleModal" class="taggedPeopleSpan" >
                    {{ taggedusername }}
                    <span class="taggedPeopleMore" v-if="taggedUsersSeeMore">...more</span>
                  </span>
                </span>
                <span v-if="!post.taggedUsers.length && isEditMode">No one is tagged</span>
                <font-awesome-icon v-if="isEditMode" class="post-icon post-info-icon" :icon="['fas', 'edit']" title="Edit Tagged People" @click="openPostTagModal"/>
              </div>
            </div>
          </div>
          <div class="right-content" v-if="post.user">
            <div v-if="post.user.id === currentUser">
              <font-awesome-icon v-if="!isEditMode" class="post-icon" :icon="['fas', 'edit']" @click="editPost" title="Edit"/>
              <font-awesome-icon v-else class="post-icon" :icon="['fas', 'save']" @click="editPost" title="Save Changes"/>
              <font-awesome-icon class="post-icon post-icon-right" :icon="['fas', 'trash']" @click="deletePost" title="Delete Post"/>
              <PopupContent ref="confirmDialogue"></PopupContent>
            </div>
          </div>
        </div>

        <!--Caption-->
        <textarea class="post-caption" :class="isEditMode? 'post-caption-edit' : ''" v-model="post.caption" ref="post_caption" :readonly="!isEditMode"></textarea>

        <!--Created On-->
        <div class="post-create-time">Created on: {{formattedDateTime}}</div>

        <!--Likes & Share-->
        <div class="post-likes-share">
          <PostTotalLikes :postId="postId"></PostTotalLikes>
          <Share></Share>
        </div>

        <!--Comments-->
        <div class="post-comments-container">
          <PostComments :postId="postId"></PostComments>
        </div>

      </div>
    </div>
  </div>
  <div v-else-if="pageError">
    <NotFound></NotFound>
  </div>
  <div v-else>
    <LoadView></LoadView>
  </div>
  <PostTag ref="postTagModal" :taggedpeople="post.taggedUsers" @saveTags="savedTags"></PostTag>
  <PostEvent ref="postEventModal" @saveTagEvent="savedTagEvent"></PostEvent>
  <AllUsersModal ref="postTaggedPeopleModal" :users="post.taggedUsers" :header="tagpeopleHeader"></AllUsersModal>

</template>

<script>
import {toRaw} from 'vue';
import ImageSlider from '../../components/Images/ImageSlider.vue';
import PopupContent from '../../components/Popup/PopupContent.vue';
import PostComments from '../../components/Posts/PostComments.vue';
import PostTotalLikes from '../../components/Posts/PostTotalLikes.vue';
import PostTag from './PostTag.vue';
import AllUsersModal from '@/components/Posts/AllUsersModal.vue';
import Share from '@/components/Posts/Share.vue';
import {BASE_URL_POST_SERVICE} from "../../../config/dev.env";
import axios from "axios";
import PostEvent from "@/views/posts/PostEvent.vue";
import LoadView from "../../components/Loader/LoadView.vue";
import NotFound from "@/views/NotFound.vue";

export default {
  components: {
    NotFound,
    PostEvent,
    ImageSlider, PopupContent, PostComments, PostTotalLikes, PostTag, AllUsersModal, Share, LoadView
  },
  data() {
    return {
      currentUser: sessionStorage.getItem('userId'),
      postId: null,
      isEditMode: false,
      leave_comment: '',
      taggedUsersSeeMore: false,
      tagpeopleHeader: "Tagged People",
      usernameListMaximumLength: 25,
      post: {},
      finishLoaded: false,
      pageError: false
    }
  },
  created() {
    console.log("Token: " + sessionStorage.getItem("jwtToken"));
    console.log("userId: " + sessionStorage.getItem('userId'));
    this.postId = this.$route.params.id;
    console.log("postId: " + this.postId);
    this.fetchPostInfoAPI();
  },
  methods: {
    // Method: Get post's information API
    async fetchPostInfoAPI() {
      this.pageError = false;
      const getPostInfoUrl = BASE_URL_POST_SERVICE + "/posts/" + this.postId;
      await axios.get(getPostInfoUrl, {
        headers: {
          'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
        }
      })
          .then(response => {
            this.post = response.data;
            this.finishLoaded = true;
          })
          .catch(error => {
            this.pageError = true;
            console.error("[fetchPostInfoAPI] There was an error fetching the post's information:", error);
          });
    },
    // Method: Click to switch between "Edit" or "Save" Mode

    editPost(){
      this.isEditMode = !this.isEditMode
      // If it is "Save" Mode
      if (!this.isEditMode){
        this.updatePostAPI();
      }
    },
    // Method: Update Post API
    updatePostAPI(){
      // Extract the userId
      var _tuser = [];
      if(this.post.taggedUsers.length){
        _tuser = this.post.taggedUsers.map(user => user.id);
      }
      // Extract the eventId
      var _tevent = "";
      if(this.post.taggedEvent){
        _tevent = this.post.taggedEvent.eventId;
      }
      const data = {
        caption: this.post.caption,
        postId: this.postId,
        taggedEventId: _tevent,
        taggedUsersId: _tuser
      };
      const updatePostUrl = BASE_URL_POST_SERVICE + "/posts/update";
      axios.patch(updatePostUrl, data, {
        headers: {
          'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
        }
      })
          .then(response => {
            this.fetchPostInfoAPI();
          })
          .catch(error => {
            console.error("[updatePostAPI] There was an error updating the post's info:", error);
          });
    },
    // Method: Delete post
    async deletePost() {
      const ok = await this.$refs.confirmDialogue.show({
        title: 'Delete Post',
        message: 'Are you sure you want to delete this post?',
        yesButton: 'Delete Forever',
        noButton: 'No',
      })
      // If user click to "Delete"
      if (ok) {
        setTimeout(() => {
          // Show success message modal
          this.$refs.confirmDialogue.show({
            title: 'Delete Post',
            message: 'You have successfully deleted the post!',
            messageIcon: ['fas', 'check-circle'],
            messageIconColor: 'green',
          });

          // Delete the post in database
          this.deletePostAPI();

          // Automatically close success message modal after 3 seconds
          setTimeout(() => {
            this.$refs.confirmDialogue._cancel();
            this.$router.push({name: 'profile', params:{"user_id": this.currentUser} });
          }, 2000);



        }, 0)
      }
    },
    // Method: Delete Post by PostId API
    deletePostAPI() {
      const deletePostUrl = BASE_URL_POST_SERVICE + "/posts/" + this.postId;
      axios.delete(deletePostUrl, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
        }
      })
          .then(response => {})
          .catch(error => {
            this.er
            console.error("[removeCommentAPI] There was an error deleting the post's comments:", error);
          });
    },
    async openPostTagModal() {
      await this.$refs.postTagModal.show({taggedpeople: this.post.taggedUsers});
    },
    async openPostEventModal() {
      await this.$refs.postEventModal.show();
    },
    savedTags(temp_taggedUsers) {
      this.post.taggedUsers = structuredClone(toRaw(temp_taggedUsers))
      this.$refs.postTagModal._cancel()
    },
    savedTagEvent(temp_taggedevent){
      this.post.taggedEvent = structuredClone(toRaw(temp_taggedevent))
      this.$refs.postEventModal._cancel()
    },

    openTaggedPeopleModal() {
      return this.$refs.postTaggedPeopleModal.show();
    },
    // Method: Navigate to user Profile
    navigateToUserProfile(userId){
      this.$router.push({name: 'profile', params:{"user_id": userId} });
    },
    // Method: Navigate to Event's information
    navigateToEvent(eventId){
      this.$router.push({name: "EventView", params: { eventId: eventId }});
    }
  },
  computed: {
    // Method: Extract username of taggedUsers
    taggedusername() {
      this.taggedUsersSeeMore = false;
      let username_list = this.post.taggedUsers.map(taggedperson => taggedperson.accountName).join(", ")
      if (username_list.length >= this.usernameListMaximumLength) {
        username_list = username_list.substring(0, this.usernameListMaximumLength);
        this.taggedUsersSeeMore = true;
      }
      return username_list
    },
    // Format post.CreatedOn to "May 7, 2024 at 08:12 PM"
    formattedDateTime() {
      const date = new Date(this.post.createdOn);
      return date.toLocaleString('en-US', {
        year: 'numeric', month: 'long', day: 'numeric',
        hour: '2-digit', minute: '2-digit', hour12: true
      });
    }
  }
}
</script>

<style scoped>
.user-image {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid grey;
  display: inline-block;
  margin-right: 10px;
}
.user-image:hover{
  box-shadow: 2px 2px 2px 1px darkgray;
}

.left-right-content-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.left-content {
  display: flex;
  align-items: flex-start;
}

.right-content {
  min-width: 25px;
}

.user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.username {
  font-weight: bold;
}
.username:hover {
  font-weight: bolder;
}

.post-info {
  font-size: 13px;
}
.post-info-edit {
  color: darkgray;
}

.post-comments-container {
  padding: 5px 10px;
}

textarea {
  resize: none;
  min-height: 70px;
  outline: none;
  padding: 5px;
  flex: 1;
  font-size: 13px;
}

.post-caption {
  display: flex;
  justify-content: space-between;
  padding: 10px;
  margin: 10px auto;
  min-height: 120px;
  color: black;
  width: 100%;
  border: 0;
}

.post-caption-edit {
  display: flex;
  justify-content: space-between;
  padding: 10px;
  margin: 10px auto;
  min-height: 150px;
  color: black;
  width: 100%;
  border: 1px solid lightgrey;
  cursor: text;
}

.post-icon {
  width: 25px;
  height: 25px;
  margin-right: 10px;
  cursor: pointer;
}

.post-info-icon {
  width: 15px;
  height: 15px;
  margin-left: 5px;
}

.post-icon-right {
  margin-right: 0;
}
.taggedPeopleSpan{
  cursor: pointer;
}
.taggedPeopleSpan:hover{
  font-weight: bold;
}

.taggedPeopleMore {
  font-style: italic;
  font-weight: 600;
}

.post-likes-share {
  padding: 5px 10px;
  border-top: 1px solid lightgrey;
  border-bottom: 1px solid lightgrey;
  color: #2c3e50;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.navigate-info{
  cursor: pointer;
}
.navigate-info:hover{
  font-weight: bold;
}
.post-create-time{
  font-size: 13px;
  color: darkgray;
  padding-left: 10px;
  font-style: italic;
}

</style>