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
          <!--Display previous comments-->
          <div class="post-previous-comments" ref="commentsContainer">
            <PostComments :currentUser="currentUser" :post="post"></PostComments>
          </div>
          <!--Add a comment-->
          <div class="post-comment post-add-comment">
            <textarea v-model="leave_comment" placeholder="Leave a comment..." required></textarea>
            <button class="button-wrapper" :disabled="!leave_comment">
              <font-awesome-icon :icon="['fas', 'paper-plane']" @click="submitComment"  />
            </button>
          </div>
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
      currentUser: "kayannn",
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
            "id": 8,
            "username": "kaokahee",
            "bios": "Loving my life",
            "image": "require('@/assets/user_images/user1.jpg')"
          }],
        // likes: [],
        comments: [
          {cid: 1, username: "kayannn", comment: "Soo beautiful! "},
          {cid: 2, username: "huiling", comment: "I am living in Split, Croatia too! Maybe you can visit me next time!"},
          {cid: 3, username: "kayannn", comment: "The sea is so clear and blue! What a paradise! "},
          {cid: 4, username: "kayannn", comment: "I should really pay a visit to Croatia. "},
          {cid: 5, username: "huiling", comment: "Enjoy your vacation. "}
        ]
      }
    }
  },
  methods: {
    scrollToLastComment() {
      const lastChildElement = this.$refs.commentsContainer.lastElementChild;
      lastChildElement?.scrollIntoView({
        behavior: 'smooth',
      });
    },
    submitComment(){
      if(this.leave_comment){
        const _cmd = {cid: 5, username: "kayannn", comment: this.leave_comment}
        this.post.comments.push(_cmd)
        this.leave_comment = ''
        this.$nextTick(() => {
          this.scrollToLastComment();
        });
      }
    },
    editPost(){
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
    async openPostTagModal(){
      await this.$refs.postTagModal.show({taggedpeople: this.post.taggedpeople});
    },
    savedTags(temp_taggedpeople){
      this.post.taggedpeople = structuredClone(toRaw(temp_taggedpeople))
      this.$refs.postTagModal._cancel()
    },
    openTaggedPeopleModal(){
      return this.$refs.postTaggedPeopleModal.show();
    }
  },
  computed: {
    taggedusername() {
      let username_list = this.post.taggedpeople.map(taggedperson => taggedperson.username).join(", ")
      if (username_list.length >= 25){
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
.left-right-content-container{
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}
.left-content {
  display: flex;
  align-items: flex-start;
}
.right-content{
  min-width:25px;
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

.post-comments-container{
  padding: 10px;
}
.post-previous-comments{
  overflow-y: auto;
  overflow-x: hidden;
  height: 220px;
}
.post-comment{
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  color: black;
  border-bottom: 1px solid #ECECEC;
  margin-bottom: 3px;
}
.post-add-comment {
  margin-top:10px;
  border-bottom: 0;
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
  min-height:150px;
  color: black;
  width: 100%;
  border: 0;
}
.post-caption-edit {
  display: flex;
  justify-content: space-between;
  padding: 10px;
  margin: 10px auto;
  min-height:150px;
  color: black;
  width: 100%;
  border: 1px solid lightgrey;
  cursor: text;
}
.button-wrapper {
  width: 25px;
  display: flex;
  justify-content: flex-end;
  border: none;
  background: none;
}
.post-icon {
  width: 25px;
  height: 25px;
  margin-right: 10px;
  cursor: pointer;
}
.post-info-icon{
  width: 15px;
  height:15px;
  margin-left: 5px;
}
.post-icon-right{
  margin-right:0;
}
.taggedPeopleMore{
  font-style: italic;
  cursor: pointer;
  font-weight: 600;
}
.post-likes-share{
  padding: 5px 10px;
  border-top: 1px solid lightgrey;
  border-bottom: 1px solid lightgrey;
  color: #2c3e50;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>