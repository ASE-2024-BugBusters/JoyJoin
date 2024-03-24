<template>
  <h1 class="page-header">New post</h1>
  <div class="create-post">
    <!--Images-->
    <div class="image-upload">
      <DisplayImages />
    </div>

    <!--Caption-->
    <div class="create-post-div">
      <textarea class="post-caption" v-model="caption" placeholder="Write a caption..."></textarea>
    </div>

    <!--Tag people-->
    <router-link to="/posttag">
      <div class="create-post-div">
        <div class="left-content">
          <img class="post-icon" alt="Post Tag" src="../../assets/post-tag.png">
          Tag people
        </div>
        <div class="right-content">
          <img class="post-icon post-icon-right" src="../../assets/right-arrow.png">
        </div>
      </div>
    </router-link>

    <!--Add event-->
    <router-link to="/postevent">
      <div class="create-post-div">
        <div class="left-content">
          <img class="post-icon" alt="Event Tag" src="../../assets/post-event.png" >
          Add event
        </div>
        <div class="right-content">
          <img class="post-icon post-icon-right" src="../../assets/right-arrow.png">
        </div>
      </div>
    </router-link>

    <br>
    <!--Submit Button-->
    <div class="submit" @click="post">Share</div>
  </div>
</template>

<script>
import DisplayImages from './DisplayImages.vue'

export default {
  data() {
    return {
      images: [],
      caption: ''
    };
  },
  components: {DisplayImages},
  methods: {
    handleFileChange(event) {
      const files = event.target.files;
      if (!files.length) return;

      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();

        reader.onload = (e) => {
          this.images.push(e.target.result);
        };

        reader.readAsDataURL(file);
      }
    },
    post() {
      // Logic to post the data
      console.log('Posting...', this.caption, this.images);
    }
  }
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

/* .image-upload {
margin-bottom: 20px;
}

.preview {
display: flex;
flex-wrap: wrap;
}

.preview img {
max-width: 120px;
max-height: 120px;
margin-right: 10px;
margin-bottom: 10px;
} */

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


textarea {
  resize: none;
  width: 500px;
  height: 123px;
  border: none;
  outline: none;

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
  margin-right:0px;
}


</style>