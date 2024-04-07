<template>
  <div class="container">
    <div class="row">
      <div class="col-4 d-flex justify-content-center align-items-center" v-for="(image, index) in images" :key="index">
        <div class="image-container">
          <div style="cursor:pointer">
            <img :src="image.url" class="img-fluid mb-3" alt="Image">
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
    data() {
        return {
            images: [
                { url: require('@/assets/camera-icon.png') },
                { url: require('@/assets/cross-icon.png') },
                { url: require('@/assets/logo.png') },
                { url: require('@/assets/tag-icon.png') }
                // Add more image URLs as needed
            ]
        };
    }
}
</script>

<style scoped>
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
  object-fit: contain; /* Ensure the image fits within the container */
}

.row>*{
    padding-left:2.5px;
    padding-right:2.5px;
}
</style>