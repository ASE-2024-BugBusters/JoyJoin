<template>
  <div class="image-div">
    <!-- <input id="file_input" type="file" multiple @change="handleFileChange" style="display: block" > -->
    <input id="file_input" type="file" multiple @change="handleFileChange" style="display: none;" >
    <div class="container">
      <div v-if="images.length" class="slider-wrapper">
        <!--Left Button-->
        <div id="prev-slide" class="slide-button" @click="clickImageSliderBtn($event)">
          <font-awesome-icon :icon="['fas', 'chevron-left']" class="left-right-btn"></font-awesome-icon>
        </div>
        <!--Image List-->
        <div class="image-list" @scroll="scrollImageList">
          <div class="image-container" v-for="(image, index) in images" :key="index">
            <img :src="image" class="image-item" alt="Image">
            <button type="button" class="remove-button btn-close" @click="removeImage(index)"></button>
          </div>
          <div class="image-container" key="add-image-btn" v-if="images.length < maximumImages" @click="open_file" style="cursor: pointer;">
            <div class="image-item">
              <font-awesome-icon :icon="['fas', 'circle-plus']" style="pointer-events: none; width: 100px; height: 100px;"/>
            </div>
          </div>
        </div>
        <!--Right Button-->
        <div id="next-slide" class="slide-button" @click="clickImageSliderBtn($event)">
          <font-awesome-icon :icon="['fas', 'chevron-right']" class="left-right-btn"></font-awesome-icon>
        </div>
      </div>
      <div v-else>
        <div class="add-image-button" @click="open_file">
          <font-awesome-icon :icon="['fas', 'images']" class="add-image-icon"/>
          Add images
        </div>
      </div>
      <div class="error-msg">{{ error }}</div>
      <div v-if="!img_valid" class="error-msg">Please upload image.</div>
    </div>
  </div>
</template>


<script>
import { onUpdated, ref } from 'vue'
export default{
  props: ["images"],
  name: "DisplayImages",
  setup(props) {
    const images = ref(props.images)
    const error = ref(null)
    const maximumImages = ref(5);
    const img_valid = ref(true);

    const clickImageSliderBtn = (event) => {
      const imageList = document.querySelector(".slider-wrapper .image-list");
      const btnDirection = event.target.id
      const direction = btnDirection === 'prev-slide' ? -1 : 1;
      const scrollAmount = imageList.clientWidth * direction;
      imageList.scrollBy({ left: scrollAmount, behavior: "smooth"});
    }

    const handleSlideButton = () => {
      const imageList = document.querySelector(".slider-wrapper .image-list");
      const slideButtons = document.querySelectorAll(".slider-wrapper .slide-button");
      if (slideButtons.length){
        if(images.value.length > 0){
          const maxScrollLeft = imageList.scrollWidth - imageList.clientWidth;
          slideButtons[0].style.display = imageList.scrollLeft <= 0 ? "none" : "flex";
          slideButtons[1].style.display = imageList.scrollLeft >= maxScrollLeft ? "none" : "flex";
        }else{
          slideButtons[0].style.display = "none";
          slideButtons[1].style.display = "none";
        }
      }
    }

    const scrollImageList = () => {
      handleSlideButton()
    }

    //Function: Remove file from file_input
    const removeFileFromFileList = (index) => {
      const dt = new DataTransfer()
      const input = document.getElementById('file_input')
      const { files } = input

      for (let i = 0; i < files.length; i++) {
        const file = files[i]
        if (index !== i)
          dt.items.add(file) // here you exclude the file. thus removing it.
      }
      input.files = dt.files // Assign the updates list
    }

    const removeImage = (image_index) => {
      images.value.splice(image_index, 1);
      removeFileFromFileList(image_index)
      validateImages()
    }

    const handleFileChange = (event) => {
      error.value = null;
      const files = event.target.files;

      if (!(files.length && ((images.value.length + files.length) <= maximumImages.value))) {
        img_valid.value = true
        error.value = "You can insert maximum of 5 images."
        return;
      }

      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();

        reader.onload = (e) => {
          images.value.push(e.target.result);
          img_valid.value = true;
        };

        reader.readAsDataURL(file);
      }
    }

    const open_file = () => {
      document.getElementById('file_input').click();
    }

    const validateImages = () => {
      let valid = true;
      if (!images.value.length){
        img_valid.value = false
        valid = false;
      }
      return valid;
    }

    onUpdated(() => {
      handleSlideButton();
    });

    return {images, error, img_valid, maximumImages, clickImageSliderBtn, scrollImageList, removeImage, handleFileChange, open_file, validateImages}
  },

}
</script>

<style scoped>
.image-div{
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 400px;
  background: #F1F4FD;
}
.container {
  max-width: 1200px;
  width: 95%;
  margin-top: 0px;
  margin-left: 15px;
  margin-right: 15px;
}
.slider-wrapper{
  position: relative;
}
.slider-wrapper .slide-button{
  position: absolute;
  top: 50%;
  height: 50px;
  width:50px;
  color: #fff;
  border: none;
  outline: none;
  background: grey;
  font-size:2.2rem;
  cursor: pointer;
  border-radius: 50%;
  transform: translateY(-50%);
  display: flex;
  justify-content: center;
  align-items: center;
}
.slider-wrapper .slide-button:hover{
  background: #444;
  height: 52px;
  width: 52px;
}
.slider-wrapper .slide-button#prev-slide{
  left: -20px;
  display: none;
  z-index:2;
}
.slider-wrapper .slide-button#next-slide{
  right: -20px;
  z-index:2;
}
.left-right-btn{
  width: 20px;
  height: 20px;
  pointer-events: none;
}

.slider-wrapper .image-list {
  display: grid;
  gap: 18px;
  grid-template-columns: repeat(5, 1fr);
  overflow-x: auto;
  scrollbar-width: none;
  margin-top: 20px;
  margin-bottom: 20px;
}

.slider-wrapper .image-list::-webkit-scrollbar{
  display: none;
}

.image-container {
  position: relative;
}

.slider-wrapper .image-list .image-item{
  width: 345px;
  height: 345px;
  object-fit: cover;
  border: 1px solid lightgrey;
  display: flex;
  align-items: center;
  justify-content: center;
  max-width: none;

}

.image-container .remove-button{
  position: absolute;
  width: 30px;
  height: 30px;
  top: 5px;
  right: 5px;
  background-color: lightgrey;
  opacity: 0.8;
  border: none;
  padding: 5px;
  border-radius: 50%;
  cursor: pointer;
}

.add-image-icon {
  width: 25px;
  height: 25px;
  margin-right: 10px;
}
.add-image-button{
  display: inline-flex;
  padding: 10px 15px 10px 15px;
  background-color: white;
  border-radius: 5px;
  transition-duration: 0.4s;
  cursor: pointer;
  border: 1px solid #e7e7e7;
  justify-content: center;
  align-content: center;
  width: 155px;

}
.add-image-button:hover{
  background-color: #e7e7e7;
}
.error-msg{
  color:red;
}
</style>