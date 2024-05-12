<template>
  <!--Display previous comments-->
  <div v-if="comments.length" class="post-previous-comments" >
    <div ref="commentsContainer">
      <div v-for="comment in comments" :key="comment.id">
        <div class="post-comment">
          <div class="left-content">
            <img class="user-image comment-user-image navigate-info" v-if="!comment.user.avatar" src="../../assets/Default_User_Icon.png" alt="User Profile Picture" @click="navigateToUserProfile(comment.user.id)" title="Navigate to User Profile"/>
            <img class="user-image comment-user-image navigate-info" v-else :src="comment.user.avatar.urls[0].url" alt="User Profile Picture" @click="navigateToUserProfile(comment.user.id)" title="Navigate to User Profile"/>
            <div class="user-info comment-font">
              <div class="username navigate-info" @click="navigateToUserProfile(comment.user.id)" title="Navigate to User Profile">{{ comment.user.accountName }}</div>
              <div class="comment">{{ comment.comment }}</div>
            </div>
          </div>
          <div class="button-wrapper">
            <button class="post-icon btn-close" v-if="comment.user.id === currentUser" @click="removeCommentAPI(comment.id)" title="Delete Comment"></button>
          </div>
        </div>
      </div>
    </div>
  </div>
  <div v-else class="post-previous-comments post-no-comments" >
    There is no comment.
  </div>

  <!--Add a comment-->
  <div class="post-comment post-add-comment">
    <textarea v-model="leave_comment" placeholder="Leave a comment..." @keydown.enter="submitCommentAPI" required></textarea>
    <button class="button-wrapper" style="margin-top:0px;" :disabled="!leave_comment">
      <font-awesome-icon :icon="['fas', 'paper-plane']" @click="submitCommentAPI" title="Submit Comment" />
    </button>
  </div>
</template>

<script>
import {BASE_URL_POST_SERVICE} from "../../../config/dev.env";
import axios from "axios";

export default {
  props: ["postId"],
  data() {
    return {
      currentUser: sessionStorage.getItem('userId'),
      leave_comment: '',
      comments: []
    }
  },
  created() {
    this.fetchAllCommentsByPostIdAPI();
  },
  methods: {
    // Method: Get all comments by PostId's API
    async fetchAllCommentsByPostIdAPI(behavior = "auto") {
      const getAllCommentsUrl = BASE_URL_POST_SERVICE + "/posts/" + this.postId + "/comments";
      await axios.get(getAllCommentsUrl, {
        headers: {
          'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
        }
      })
          .then(response => {
            this.comments = response.data;
            setTimeout(() => {
              this.scrollToLastComment(behavior)
            })
          })
          .catch(error => {
            console.error("[fetchAllCommentsByPostId] There was an error fetching the post's comments:", error);
          });
    },
    // Method: Scroll to the latest comments
    scrollToLastComment(behavior = "smooth") {
      if (this.$refs.commentsContainer) {
        const lastChildElement = this.$refs.commentsContainer.lastElementChild;
        lastChildElement?.scrollIntoView({
          behavior: behavior,
        });
      }
    },
    // Method: Post a comment's API
    async submitCommentAPI() {
      if (this.leave_comment) {
        const data = {
          userId: this.currentUser,
          postId: this.postId,
          comment: this.leave_comment
        };
        this.leave_comment = '' // Clear the comment
        const createCommentUrl = BASE_URL_POST_SERVICE + "/posts/comments/create";
        await axios.post(createCommentUrl, data, {
          headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
          }
        })
            .then(response => {
              this.$nextTick(() => {
                this.fetchAllCommentsByPostIdAPI("smooth");
              });
            })
            .catch(error => {
              console.error("[submitComment] There was an error submitting the post's comment:", error);
            });
      }
    },
    // Method: Remove comment's API
    async removeCommentAPI(comment_id) {
      const index = this.comments.findIndex((comment) => comment.id === comment_id);
      if (index !== -1) {
        const deleteCommentUrl = BASE_URL_POST_SERVICE + "/posts/comments/" + comment_id;
        await axios.delete(deleteCommentUrl, {
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
          }
        })
            .then(response => {
              this.comments.splice(index, 1);
            })
            .catch(error => {
              console.error("[removeCommentAPI] There was an error deleting the post's comments:", error);
            });
      }
      //Call Delete Comment API
      // const deleteCommentUrl = BASE_URL_EVENT_SERVICE + "/posts/comments/" + comment_id;
      const deleteCommentUrl = BASE_URL_POST_SERVICE + "/posts/comments/" + comment_id;
      axios.delete(deleteCommentUrl, {
        // headers: {
        //   // 'Content-Type': 'application/json',
        //   'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
        // }
      })
          .then(response => {
          })
          .catch(error => {
            console.error("There was an error deleting the post's comments:", error);
          });
    },
    // Method: Navigate to user Profile
    navigateToUserProfile(userId){
      this.$router.push({name: 'profile', params:{"user_id": userId} });
    },
  },
};
</script>

<style scoped>
.left-content {
  display: flex;
  align-items: flex-start;
}
.right-content{
  min-width: 25px;
  margin-top: 5px;
}
.user-image {
  display: inline-block;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right:10px;
  background-repeat: no-repeat;
  background-position: center center;
  background-size: cover;
  border: 1px solid grey;
  object-fit: cover;
}
.user-image:hover{
  box-shadow: 2px 2px 2px 1px darkgray
}


.user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  text-align: left;
}
.username {
  font-weight: bold;
}
.username:hover {
  font-weight: bolder;
}
.post-previous-comments {
  overflow-y: auto;
  overflow-x: hidden;
  height: 200px;
}
.post-no-comments {
  text-align: center;
  font-size: 13px;
  color: darkgray;
  padding-top: 15px;
}
.post-comment {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  color: black;
  border-bottom: 1px solid #ECECEC;
  margin-bottom: 3px;
  padding: 1px 0px;
}
.post-add-comment {
  margin-top: 10px;
  border-bottom: 0;

}
.comment-user-image {
  width: 37.5px;
  height: 37.5px;
}
.comment-font {
  font-size: 13px;
}
textarea {
  resize: none;
  min-height: 65px;
  outline: none;
  padding: 5px;
  flex: 1;
  font-size: 13px;
}

.button-wrapper {
  width: 25px;
  display: flex;
  justify-content: flex-end;
  border: none;
  background: none;
  margin-top: 4px;
}
.navigate-info{
  cursor: pointer;
}
.navigate-info:hover{
  font-weight: bold;
}
.comment{
  word-break: break-word;
}
</style>
