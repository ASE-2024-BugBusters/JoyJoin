<template>
  <h2 class="page-header">New post</h2>
  <div class="create-post">
    <!--Images-->
    <div class="image-upload">
      <ImagePreview ref="imagePreview" :images="images"/>
      <!-- <div v-if="!images_valid" class="error-msg image-error-msg">Please upload image.</div> -->
    </div>

    <!--Caption-->
    <div class="create-post-div">
      <div class="caption-wrapper">
        <textarea v-on:keyup="caption.length>0 ? caption_valid=true : caption_valid=false" class="post-caption" v-model="caption" placeholder="Write a caption..." required></textarea>
        <div v-if="!caption_valid" class="error-msg">Please write a caption.</div>
      </div>
    </div>

    <!--Tag people-->
    <!-- <router-link to="/posttag"> -->
    <div class="create-post-div" @click="openPostTagModal">
      <div class="left-content">
        <img class="post-icon" alt="Post Tag" src="../../assets/post-tag.png">
        <div class="user-info">
          <div>Tag people</div>
          <div class="post-info">
            <span v-if="taggedpeople.length">{{ taggedusername }}</span>
          </div>
        </div>
      </div>
      <div class="right-content">
        <font-awesome-icon :icon="['fas', 'chevron-right']" class="post-icon post-icon-right"></font-awesome-icon>
      </div>
    </div>
    <!-- </router-link> -->

    <!--Add event-->
    <router-link to="/postevent">
      <div class="create-post-div">
        <div class="left-content">
          <img class="post-icon" alt="Event Tag" src="../../assets/post-event.png" >
          Add event
        </div>
        <div class="right-content">
          <font-awesome-icon :icon="['fas', 'chevron-right']" class="post-icon post-icon-right"></font-awesome-icon>
        </div>
      </div>
    </router-link>

    <br>
    <!--Submit Button-->
    <div class="submit" @click="createNewPost" :disabled="!caption">Share</div>
  </div>
  <PostTag ref="postTagModal" @saveTags="savedTags"></PostTag>
  <PopupContent ref="successDialogue"></PopupContent>
</template>

<script>
import ImagePreview from '../../components/Images/ImagePreview.vue'
import PopupContent from '@/components/Popup/PopupContent.vue';
import PostTag from './PostTag.vue';
import {toRaw} from "vue";
export default {
  components: {ImagePreview, PostTag, PopupContent},
  data() {
    return {
      images: [],
      caption: '',
      // taggedpeople: [{username: 'seancheee'}, {username: 'kayannn'}],
      taggedpeople: [],
      images_valid: true,
      caption_valid: true,
      taggedPeopleDisplayMaxiLength: 60,
    };
  },
  methods: {
    validateFields(){
      let err = 0;
      let valid = true;
      // (1) Validate Caption
      if (!this.caption){
        this.caption_valid = false;
        err++;
      }
      // (2) Validate Images
      if (!this.$refs.imagePreview.validateImages()){
        err++;
      }
      //Summarize validity
      if (err > 0){
        valid = false;
      }
      return valid;
    },
    createNewPost() {
      // Logic to post the data
      const valid = this.validateFields();
      if (valid){
        //Post API send to database
        //...
        //Return back to homepage
        setTimeout(() => {
          // Show success message modal
          this.$refs.successDialogue.show({
            title: 'Succesfully Create Post',
            message: 'You have successfully create the post! You will be navigate back to your post-profile.',
            messageIcon: ['fas', 'check-circle'],
            messageIconColor: 'green',
          });
          // Automatically close success message modal after 3 seconds
          setTimeout(() => {
            this.$refs.successDialogue._cancel();
            this.$router.push({name: 'home'})
          }, 2000);
        }, 0)
      }

    },
    async openPostTagModal(){
      await this.$refs.postTagModal.show({taggedpeople: this.taggedpeople});
    },
    savedTags(temp_taggedpeople){
      if(temp_taggedpeople){
        this.taggedpeople = structuredClone(toRaw(temp_taggedpeople))
      }
      this.$refs.postTagModal._cancel()
    }

  },
  computed: {
    taggedusername() {
      let username_list = this.taggedpeople.map(taggedperson => taggedperson.username).join(", ")
      if (username_list.length >= this.taggedPeopleDisplayMaxiLength){
        username_list = username_list.substring(0, this.taggedPeopleDisplayMaxiLength) + "...";
      }
      return username_list
    }
  },
}
</script>


<style scoped>
.page-header {
  border-bottom:2px solid #D3D3D3;
  padding-top: 10px;
  padding-bottom: 10px;
  color: black;
  font-weight:bold;
}

.create-post {
  max-width: 1000px;
  margin: auto;
}

.image-upload {
  margin: 10px auto;
  max-width: 95%;
  min-height: 300px;
  border-radius: 3px;
  background-color: #F5F5F5;
}

.file-upload-label {
  cursor: pointer;
  display: block;
  border: 1px solid #D3D3D3;
}

.else-label {
  color: #808080;
}

.preview-container {
  position: relative;
  overflow: hidden;
}

.preview {
  display: flex;
  justify-content: center;
}

.preview img {
  max-width: 100%;
  max-height: 220px;
  margin-right: 10px;
  transition: opacity 0.3s ease-in-out;
}

.controls {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  width: 100%;
  display: flex;
  justify-content: space-between;
  z-index: 1;
}

.controls button {
  background-color: transparent;
  border: none;
  color: white;
  font-size: 24px;
  cursor: pointer;
  outline: none;
}

.controls button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

a{
  text-decoration: none;
}
.caption-wrapper{
  position: relative;
  width: 100%;
}

textarea {
  resize: none;
  width: 100%;
  height: 123px;
  border: none;
  outline: none;
  display: block;
}
.submit{
  background: #24a0ed;
  padding: 15px;
  border-radius: 10px;
  margin: 10px auto;
  max-width: 500px;
  cursor: pointer;
  color: white;
}
.create-post-div {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px;
  margin: 10px auto;
  max-width: 500px;
  cursor: pointer;
  color: black;
  border-bottom: solid 1px #D3D3D3;
}
.left-content {
  display: flex;
  align-items: center;
}
.post-icon {
  width: 25px;
  height: 25px;
  margin-right: 10px;
}
.post-icon-right{
  font-size: 10px;
  margin-right:0px;
  width: 20px;
  height: 20px;
  color: darkgrey;
}
.user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.post-info {
  color: darkgray;
  font-size: 13px;
}
.error-msg{
  color:red;
}
.image-error-msg{
  position: absolute;
  left: 50%;
  transform: translateX(-50%);
  margin-top: -175px;
}
</style>