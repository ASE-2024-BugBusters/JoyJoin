<template>
  <!--Display previous comments-->
  <div v-for="comment in post.comments" :key="comment.cid">
      <div class="post-comment">
        <div class="left-content">
          <img class="user-image comment-user-image" src="../../assets/camera-icon.png" alt="User Profile Picture"/>
          <div class="user-info comment-font">
            <div class="username">{{ comment.username }}</div>
            <div class="post-info">{{ comment.comment }}</div>
          </div>
        </div>
        <div class="button-wrapper">
          <button class="post-icon btn-close" v-if="comment.username === currentUser" @click="removeComment(comment.cid)"></button>
        </div>
      </div>
  </div>
</template>

<script>
export default {
  props: ["currentUser", "post"],
  methods: {
    removeComment(remove_cid) {
      const index = this.post.comments.findIndex((comment) => comment.cid === remove_cid);
      if (index !== -1) {
        this.post.comments.splice(index, 1);
      }
    },
  },
};
</script>

<style scoped>
.post-comment{
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    color: black;
    border-bottom: 1px solid #ECECEC;
    margin-bottom: 3px;
}
.comment-user-image {
width: 35px;
height: 35px;
}
.comment-font{
font-size:13px;
}
</style>
