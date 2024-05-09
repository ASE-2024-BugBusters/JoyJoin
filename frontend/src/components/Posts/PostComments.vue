<template>
  <!--Display previous comments-->
  <div v-if="comments.length" class="post-previous-comments" >
    <div ref="commentsContainer">
      <div v-for="comment in comments" :key="comment.id">
        <div class="post-comment">
          <div class="left-content">
            <img class="user-image comment-user-image" src="../../assets/camera-icon.png" alt="User Profile Picture"/>
            <div class="user-info comment-font">
              <div class="username">{{ comment.user.id }}</div>
              <div class="post-info">{{ comment.comment }}</div>
            </div>
          </div>
          <div class="button-wrapper">
            <button class="post-icon btn-close" v-if="comment.user.id === currentUser" @click="removeCommentAPI(comment.id)"></button>
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
    <textarea v-model="leave_comment" placeholder="Leave a comment..." required></textarea>
    <button class="button-wrapper" :disabled="!leave_comment">
      <font-awesome-icon :icon="['fas', 'paper-plane']" @click="submitCommentAPI"  />
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
        const createCommentUrl = BASE_URL_POST_SERVICE + "/posts/comments/create";
        await axios.post(createCommentUrl, data, {
            headers: {
              'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            }
          })
              .then(response => {
                this.leave_comment = ''
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
      const deleteCommentUrl = this.prefix_url + "/posts/comments/" + comment_id;
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
  },
};
</script>

<style scoped>
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
}
.post-add-comment {
  margin-top: 10px;
  border-bottom: 0;

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
}

.post-add-comment {
  margin-top: 10px;
  border-bottom: 0;
}

.comment-user-image {
  width: 35px;
  height: 35px;
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
}
</style>
