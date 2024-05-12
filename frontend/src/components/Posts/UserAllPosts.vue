<template>
  <div v-if="finishLoaded && !pageError">
    <div class="container" >
      <h2 class="subtitle">
        Your Posts:
      </h2>
      <div class="row" v-if="posts.length">
        <div class="col-3 d-flex justify-content-center align-items-center" v-for="post in posts" :key="post.id" @click="navigateToPost(post.id)">
          <div class="image-container">
            <div style="cursor:pointer">
              <img :src="post.images[0].urls[0].url" class="img-fluid mb-3" alt="Image">
            </div>
          </div>
        </div>
      </div>
      <div v-else class="no-posts">
        There is no posts.
      </div>
    </div>
    <h2 class="subtitle" style="margin-top: 30px">
      Attended Events:
    </h2>
  </div>

  <div class="container" v-else-if="pageError">
    <NotFound/>
  </div>
  <div class="container" v-else>
    <LoadView/>
  </div>
</template>

<script>
import {BASE_URL_POST_SERVICE} from "../../../config/dev.env";
import axios from "axios";
import LoadView from "@/components/Loader/LoadView.vue";
import NotFound from "@/views/NotFound.vue";
import EventsList from "@/views/event/EventList.vue";

export default {
  components: {EventsList, LoadView, NotFound},
    data() {
        return {
            posts: [],
            finishLoaded: false,
            pageError: false
        };
    },
    created(){
      console.log("Token: " + sessionStorage.getItem("jwtToken"));
      console.log("userId: " + sessionStorage.getItem('userId'));
      this.fetchAllPostsByUserIdAPI();
    },
    methods: {
      // Method: Get all post by UserId API
      async fetchAllPostsByUserIdAPI() {
        this.pageError = false;
        const getAllPostsUrl = BASE_URL_POST_SERVICE + "/posts/user/" + sessionStorage.getItem('userId');
        await axios.get(getAllPostsUrl, {
          headers: {
            'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
          }
        })
            .then(response => {
              this.posts = response.data;
              this.finishLoaded = true;
            })
            .catch(error => {
              this.pageError = true;
              console.error("[fetchAllPostsByUserIdAPI] There was an error fetching the all posts:", error);
            });
      },
      // Method: Navigate to specific post
      navigateToPost(postId) {
        this.$router.push({ name: 'post', params: { id: postId } });
      },

    }

}
</script>

<style scoped>
.container{
  margin-top: 10px;
  margin-bottom: 10px;
}
.image-container {
    position: relative;
    width: 100%;
    padding-top: 100%;
    border: 1px solid lightgrey;
    text-align: center; /* Center the image */
    margin-bottom:5px;
}
.image-container img {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  width: 100%;
  height: 100%;
  object-fit: cover; /* Ensure the image fits within the container */
}
.row>*{
    padding-left:2.5px;
    padding-right:2.5px;
}
.no-posts {
  text-align: center;
  font-size: 20px;
  color: darkgray;
  padding-top: 15px;
}

.container {
  max-height: 350px; /* Set the max height for the container */
  overflow-y: auto; /* Enable vertical scrolling */
}
</style>