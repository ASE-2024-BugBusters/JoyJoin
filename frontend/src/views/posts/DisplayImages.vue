<template>
  <div class="image-div">
    <input id="file_input" type="file" multiple @change="handleFileChange" style="display: none">
    <div class="container">
      <div v-if="images.length" class="slider-wrapper">
        <button id="prev-slide" class="slide-button material-symbols-rounded" @click="clickImageSliderBtn($event)">chevron_left</button>
        <div class="image-list" @scroll="scrollImageList">
          <div class="image-container" v-for="(image, index) in images" :key="index">
            <img :src="image" class="image-item">
            <button type="button" class=" remove-button btn-close" @click="removeImage(index)"></button>
          </div>
        </div>
        <button id="next-slide" class="slide-button material-symbols-rounded" @click="clickImageSliderBtn($event)">chevron_right</button>
      </div>
      <div v-else>
        <div class="add-image-button" @click="open_file">
          <img class="add-image-icon" alt="Post Tag" src="../../assets/image-upload-icon.png">
          Add images
        </div>
      </div>
    </div>
  </div>
</template>


<script>
import { computed, onMounted, onUpdated, ref } from 'vue'
export default {
  name: "DisplayImages",
  setup() {
    const images = ref([])

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
      if(images.value.length > 0){
        const maxScrollLeft = imageList.scrollWidth - imageList.clientWidth;
        slideButtons[0].style.display = imageList.scrollLeft <= 0 ? "none" : "block";
        slideButtons[1].style.display = imageList.scrollLeft >= maxScrollLeft ? "none" : "block";
      }else{
        slideButtons[0].style.display = "none";
        slideButtons[1].style.display = "none";
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
    }

    const handleFileChange = (event) => {
      const files = event.target.files;
      if (!files.length) return;

      for (let i = 0; i < files.length; i++) {
        const file = files[i];
        const reader = new FileReader();

        reader.onload = (e) => {
          images.value.push(e.target.result);
        };

        reader.readAsDataURL(file);
      }
    }

    const open_file = () => {
      document.getElementById('file_input').click();
    }

    onUpdated(() => {
      handleSlideButton();
    });

    return {images, clickImageSliderBtn, scrollImageList, removeImage, handleFileChange, open_file}
  },

}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Material+Symbols+Rounded:opsz,wght,FILL,GRAD@48,400,0,0');

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
  width: 30px;
  height: 30px;
}
.add-image-button{
  display: inline-block;
  padding: 10px 15px 10px 15px;
  background-color: white;
  border-radius: 5px;
  transition-duration: 0.4s;
  cursor: pointer;
  border: 1px solid #e7e7e7;
}
.add-image-button:hover{
  background-color: #e7e7e7;
}
</style>