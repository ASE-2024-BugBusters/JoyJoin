<template>
  <transition :name="transitionEffect">
    <div class="carousel-item" v-show="currentSlide === index">
      <img :src="slide" class="image-item" alt="Image Index"/>
    </div>
  </transition>
</template>

<script>
export default {
  props: ["slide", "currentSlide", "index", "direction"],
  computed:{
    transitionEffect(){
      return this.direction === "right" ? "slide-out" : "slide-in";
    }
  }
}
</script>

<style scoped>
.carousel-item {
  position: absolute;
  display: block;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}
.slide-in-enter-active,
.slide-in-leave-active,
.slide-out-enter-active,
.slide-out-leave-active{
  transition: all 1s ease-in-out;
}
.slide-in-enter-from {
  transform: translateX(-100%);
}
.slide-in-leave-to{
  transform: translateX(100%);
}
.slide-out-enter-from {
  transform: translateX(100%);
}
.slide-out-leave-to{
  transform: translateX(-100%);
}
.image-item{
  width: 450px;
  height: 450px;
  object-fit: cover;
  border: 1px solid lightgrey;
}
</style>