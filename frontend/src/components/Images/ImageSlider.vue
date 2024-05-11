<template>
  <div class="carousel">
    <div class="carousel-inner">
      <carousel-indicators v-if="slides.length > 1" :total="slides.length" :current-index="currentSlide" @switch="switchSlide($event)"></carousel-indicators>
      <carousel-item v-for="(slide, index) in slides" :slide="slide" :key="`item-${index}`" :current-slide="currentSlide" :index="index" :direction="direction">
      </carousel-item>
      <carousel-controls v-if="slides.length > 1" @prev="prev" @next="next"></carousel-controls>
    </div>
  </div>
</template>

<script>
import CarouselControls from './CarouselControls.vue'
import CarouselItem from './CarouselItem.vue'
import CarouselIndicators from './CarouselIndicators.vue'

export default {
  props: ['images'],
  name: "ImageSlider",
  components: {CarouselItem, CarouselControls, CarouselIndicators},
  data() {
    return {
      currentSlide: 0,
      slides: [],
      // slides: this.images != null ? this.images.map(image => image.urls[0].url).stream() : [require('@/assets/camera-icon.png')],
      direction: "right",
    }
  },
  watch: {
    images: {
      handler(newVal) {
        // Check if images prop is provided and contains data
        if (newVal && newVal.length > 0) {
          this.slides = newVal.map(image => image.urls[0].url);
        } else {
          // Fallback image if no images are provided
          this.slides = [require('@/assets/camera-icon.png')];
        }
      },
      immediate: true // Trigger handler immediately on component mount
    }
  },
  methods: {
    async loadImages(){
      if(this.images != null){
        for( const image of this.images){
          this.slides.push(image.urls[0].url);
        }
      }else{
        this.slides.push(require('@/assets/camera-icon.png'))
      }
    },
    setCurrentSlide(index) {
      this.currentSlide = index;
    },
    prev(step = -1){
      const index = this.currentSlide > 0 ? this.currentSlide + step : this.slides.length - 1;
      this.setCurrentSlide(index);
      this.direction = "left";
    },
    next(step = 1){
      const index = this.currentSlide < this.slides.length - 1 ? this.currentSlide + step : 0;
      this.setCurrentSlide(index);
      this.direction = "right";
    },
    switchSlide(index){
      const step = index - this.currentSlide;
      if (step > 0){
        this.next(step)
      }else{
        this.prev(step)
      }
    }
  },
}
</script>

<style scoped>
.carousel{
  display:flex;
  justify-content: center;
}
.carousel-inner{
  position: relative;
  width: 500px;
  height: 450px;
  overflow: hidden;
}
</style>