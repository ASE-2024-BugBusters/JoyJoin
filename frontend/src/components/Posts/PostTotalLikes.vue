<template>
<!--Total Number of Likes-->
<div class="post-likes">
    <font-awesome-icon ref="likeBtn" :icon="faicon" class="like-btn" :class="{ 'loved-btn': isLiked }" @click="updateLikes" />
    <div class="post-likes-by">
        <span v-if="postlikes.length" class="likes-label" @click="openLikesModal">{{ postlikes.length }} Like(s)</span>
        <span v-else>Send some likes!</span>
    </div>
</div>
<!--Liked By Who's Modal-->
<AllUsersModal ref="postLikedByModal" :users="postlikes" :header="header"></AllUsersModal>
</template>

<script>
import AllUsersModal from '@/components/Posts/AllUsersModal.vue';
import {BASE_URL_POST_SERVICE} from "../../../config/dev.env";
import axios from "axios";
export default {
    props: ["postId"],
    components: {AllUsersModal},
    data() {
        return {
            currentUser: sessionStorage.getItem('userId'),
            postlikes: [],
            isLiked: false,
            header: "Likes",
            faicon: ['far', 'heart'],
        }
    },
    created(){
      this.fetchAllLikesByPostIdAPI();
    },
    methods: {
      // Method: Get all Likes by PostId's API
      async fetchAllLikesByPostIdAPI() {
        const getAllLikesUrl = BASE_URL_POST_SERVICE + "/posts/" + this.postId + "/likes";
        await axios.get(getAllLikesUrl, {
          headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
          }
        })
            .then(response => {
              this.postlikes = response.data;
              this.isLiked = this.postlikes.some(likedUser => likedUser.id === this.currentUser) ? true : false;
              this.updateLikeButtonIcon();
            })
            .catch(error => {
              console.error("[fetchAllLikesByPostIdAPI] There was an error fetching the post's likes:", error);
            });
      },
      // Method: Add/Remove like
      updateLikes() {
        // Check if the currentUser liked this post before
        const likedBefore = this.postlikes.some(likedUser => likedUser.id === this.currentUser) ? true : false;
        // If yes, meaning user is "Disliking"; If no, meaning user is "Liking"
        const action = !likedBefore
        this.updateLikedUserAPI(action);
      },
      // Method: Update likedUsersList's API
      async updateLikedUserAPI(likeDislike) {  // likeDislike = 1 (Like); likeDislike = 0 (dislike)
        const updateLikeUrl = BASE_URL_POST_SERVICE + "/posts/likes/create";
        const data = {
            postId: this.postId,
            likeUsersId: sessionStorage.getItem('userId'),
            liked: likeDislike
          };
          await axios.patch(updateLikeUrl, data, {
            headers: {
              'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            }
          })
              .then(response => {
                this.fetchAllLikesByPostIdAPI();
              })
              .catch(error => {
                console.error("[updateLikedUserAPI] There was an error like/dislike the post:", error);
              });
      },
      // Method: Displaying the user(liked) information
      openLikesModal() {
        this.$refs.postLikedByModal.show();
      },
      // Method: Update the shape of the "Love"(button)
      updateLikeButtonIcon() {
        this.faicon = this.isLiked ? ['fas', 'heart'] : ['far', 'heart'];
      },
    },
}
</script>

<style>
.post-likes{
    /* padding: 5px 10px;
    border-top: 1px solid lightgrey;
    border-bottom: 1px solid lightgrey;
    color: #2c3e50; */
    display: inline-block;
}
.like-btn{
    cursor: pointer;
    color: #2c3e50;
}
.loved-btn{
    color:red;
}
.post-likes-by{
    margin-left:5px;
    display: inline-block;
}
.likes-label{
    cursor: pointer;
    text-decoration: underline;
}
.likes-label:hover{
    font-weight: bold;
}

</style>