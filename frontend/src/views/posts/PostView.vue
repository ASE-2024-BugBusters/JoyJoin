<template>
  <div class="container">
    <div class="row">
      <!--Left section: Image (Slide images)-->
      <div class="col-md-7 d-flex justify-content-center align-items-center">
        <ImageSlider />
      </div>

      <!--Right section: Caption, Tags, Events, Likes, Comment-->
      <div class="col-md-5" style="text-align: left;">
        <!--Username-->
        <div class="left-right-content-container">
          <div class="left-content">
            <img class="user-image" src="../../assets/camera-icon.png" alt="User Profile Picture">
            <div class="user-info">
              <div class="username"> {{ post.username }}</div>
              <div class="post-info">
                <span v-if="post.event">{{ post.event }}</span>
                <span v-else-if="!post.event && isEditMode">No event is binded</span>
                <font-awesome-icon v-if="isEditMode" class="post-icon post-info-icon" :icon="['fas', 'edit']" title="Edit Event"/>
              </div>
              <div class="post-info">
                            <span v-if="post.taggedpeople.length">With {{ taggedusername }}
                                <span class="taggedPeopleMore" v-if="taggedPeopleSeeMore" @click="openTaggedPeopleModal">...more</span>
                            </span>
                <span v-if="!post.taggedpeople.length && isEditMode">No one is tagged</span>
                <font-awesome-icon v-if="isEditMode" class="post-icon post-info-icon" :icon="['fas', 'edit']" title="Edit Tagged People" @click="openPostTagModal"/>
              </div>
            </div>
          </div>
          <div class="right-content">
            <font-awesome-icon v-if="!isEditMode" class="post-icon" :icon="['fas', 'edit']" @click="editPost" title="Edit"/>
            <font-awesome-icon v-else class="post-icon" :icon="['fas', 'save']" @click="editPost" title="Save Changes"/>
            <font-awesome-icon class="post-icon post-icon-right" :icon="['fas', 'trash']" @click="deletePost" title="Delete Post"/>
            <PopupContent ref="confirmDialogue"></PopupContent>
          </div>
        </div>

        <!--Caption-->
        <textarea class="post-caption" :class="isEditMode? 'post-caption-edit' : ''" v-model="post.caption" ref="post_caption" :readonly="!isEditMode"></textarea>

        <!--Likes & Share-->
        <div class="post-likes-share">
          <PostTotalLikes :currentUser="currentUser" :postlikes="post.likes"></PostTotalLikes>
          <Share></Share>
        </div>

        <!--Comments-->
        <div class="post-comments-container">
          <PostComments :currentUser="currentUser" :post="post"></PostComments>
        </div>

      </div>
    </div>
  </div>
  <PostTag ref="postTagModal" :taggedpeople="post.taggedpeople" @saveTags="savedTags"></PostTag>
  <AllUsersModal ref="postTaggedPeopleModal" :users="post.taggedpeople" :header="tagpeopleHeader"></AllUsersModal>
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

export default {
  components: {
    ImageSlider, PopupContent, PostComments, PostTotalLikes, PostTag, AllUsersModal, Share
  },
  data() {
    return {
      currentUser: "09f80c29-bf3f-4b4b-a23e-d179eba82821",
      isEditMode: false,
      leave_comment: '',
      taggedPeopleSeeMore: false,
      tagpeopleHeader: "Tagged People",
      post: {
        username: "yuinkwann",
        userimage: "../../assets/camera-icon.png",
        // event: "",
        // tagpeople: [],
        event: "Bridal Shower",
        taggedpeople: [{username: 'seancheee'}, {username: 'kayannn'}],
        caption: "I love party!",
        likes: [
          {
            "id": 5,
            "username": "cercil",
            "bios": "I love snacks",
            "image": "require('@/assets/user_images/user1.jpg')"
          },
          {
            "id": 6,
            "username": "yuinywai",
            "bios": "I wish to see boyboy",
            "image": "require('@/assets/user_images/user1.jpg')"
          },
          {
            "id": 7,
            "username": "yishujia",
            "bios": "I like puma",
            "image": "require('@/assets/user_images/user1.jpg')"
          },
          {
            "id": 8,
            "username": "kaokahee",
            "bios": "I am poor",
            "image": "require('@/assets/user_images/user1.jpg')"
          },
          {
            "id": 9,
            "username": "leeziying",
            "bios": "why so many people kao me. Tsun jin black magic queen. why so many people kao me. Tsun jin black magic queen.",
            "image": "require('@/assets/user_images/user1.jpg')"
          },
          {
            "id": 10,
            "username": "cheee",
            "bios": "okok",
            "image": "require('@/assets/user_images/user1.jpg')"
          }],
        // likes: [],
        comments: [
          {
            cid: 1,
            username: "kayannn",
            comment: "Soo beautiful! Soo beautiful! Soo beautiful! Soo beautiful! Soo beautiful! Soo beautiful! Soo beautiful! Soo beautiful! Soo beautiful! Soo beautiful! Soo beautiful! "
          },
          {
            cid: 2,
            username: "huiling",
            comment: "nice to meet you! I would like to meet you again in Kepong! You're so evil and I am really like your style! "
          },
          {
            cid: 3,
            username: "kayannn",
            comment: "nice to meet you! I would like to meet you again in Kepong! You're so evil and I am really like your style! "
          },
          {
            cid: 4,
            username: "kayannn",
            comment: "nice to meet you! I would like to meet you again in Kepong! You're so evil and I am really like your style! "
          }
        ]
      }
    }
  },
  methods: {
    editPost() {
      this.isEditMode = !this.isEditMode
    },
    async deletePost() {
      const ok = await this.$refs.confirmDialogue.show({
        title: 'Delete Post',
        message: 'Are you sure you want to delete this post?',
        yesButton: 'Delete Forever',
        noButton: 'No',
      })
      // If you throw an error, the method will terminate here unless you surround it wil try/catch
      if (ok) {
        setTimeout(() => {
          // Show success message modal
          this.$refs.confirmDialogue.show({
            title: 'Delete Post',
            message: 'You have successfully deleted the post!',
            messageIcon: ['fas', 'check-circle'],
            messageIconColor: 'green',
          });
          // Automatically close success message modal after 3 seconds
          setTimeout(() => {
            this.$refs.confirmDialogue._cancel();
            this.$router.push({name: 'home'})
          }, 2000);
        }, 0)

      } else {
      }
    },
    async openPostTagModal() {
      await this.$refs.postTagModal.show({taggedpeople: this.post.taggedpeople});
    },
    savedTags(temp_taggedpeople) {
      this.post.taggedpeople = structuredClone(toRaw(temp_taggedpeople))
      this.$refs.postTagModal._cancel()
    },
    openTaggedPeopleModal() {
      return this.$refs.postTaggedPeopleModal.show();
    }
  },
  computed: {
    taggedusername() {
      let username_list = this.post.taggedpeople.map(taggedperson => taggedperson.username).join(", ")
      if (username_list.length >= 25) {
        username_list = username_list.substring(0, 25);
        this.taggedPeopleSeeMore = true;
      }
      return username_list
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

.post-info {
  color: darkgray;
  font-size: 13px;
}

.post-comments-container {
  padding: 10px;
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
  min-height: 150px;
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

.taggedPeopleMore {
  font-style: italic;
  cursor: pointer;
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
</style>