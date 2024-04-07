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
export default {
    props: ["currentUser", "postlikes"],
    components: {AllUsersModal},
    data() {
        return {
            isLiked: false,
            header: "Likes",
            faicon: ['far', 'heart'],
        }
    },
    methods: {
        updateLikes(){
            const _status = this.postlikes.some(item => item.username === this.currentUser) ? true : false;
            if(_status){
                const index = this.postlikes.findIndex(item => item.username === this.currentUser);
                if (index !== -1) {
                    this.postlikes.splice(index, 1);
                }
            }else{
                this.postlikes.push({username: this.currentUser})
            }
            this.isLiked = !_status;
            this.updateLikeButtonIcon()
        },
        openLikesModal(){
            this.$refs.postLikedByModal.show();
        },
        updateLikeButtonIcon(){
          this.faicon = this.isLiked ? ['fas', 'heart'] : ['far', 'heart'];
        }
    },
    mounted(){
        this.isLiked = this.postlikes.some(item => item.username === this.currentUser) ? true : false;
        this.updateLikeButtonIcon()
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