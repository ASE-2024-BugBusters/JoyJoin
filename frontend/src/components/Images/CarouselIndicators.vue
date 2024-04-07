<template>
  <div class="carousel-indicators">
    <button class="carousel-indicator-item" :class="{ active: currentIndex === index }" v-for="(item, index) in total" :key="index" @click="$emit('switch', index)"></button>
  </div>
</template>

<script>
export default {
  emits: ['switch'],
  props: ["total", "currentIndex"]
}
</script>

<style scoped>
.carousel-indicators{
  position: absolute;
  transform: translateX(-50%);
  left: 50%;
  background: transparent;
  z-index: 2;
  margin-left: 0;
  margin-right: 0;
}
.carousel-indicator-item{
  width: 15px;
  height: 15px;
  border: none;
  background: black;
  margin: 0.2em;
  opacity: 0.5;
  border-radius: 50%;
  cursor: pointer;
}
.carousel-indicators.active{
  opacity: 1;
}
</style>