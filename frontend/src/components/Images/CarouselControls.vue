<template>
  <button class="carousel-control left" @click="$emit('prev')" title="Previous">
    <font-awesome-icon :icon="['fas', 'chevron-left']" ></font-awesome-icon>
  </button>
  <button class="carousel-control right" @click="$emit('next')" title="Next">
    <font-awesome-icon :icon="['fas', 'chevron-right']" ></font-awesome-icon>
  </button>
</template>

<script>
export default {
  emits: ['prev', 'next']
}
</script>

<style scoped>
.carousel-control{
  background: rgba(0, 0, 0, .3);
  border: none;
  display: inline-block;
  position: absolute;
  height: 52px;
  width: 52px;
  top: calc(50%  - 25px);
  color: #f3f3f3;
  cursor: pointer;
  border-radius: 50%;
}
.carousel-control:hover{
  background: rgba(0, 0, 0, .7);
  border: none;
  display: inline-block;
  position: absolute;
  height: 52px;
  width: 52px;
  top: calc(50%  - 25px);
  color: white;
  cursor: pointer;
  border-radius: 50%;
}
.left {
  left : 0;
}
.right {
  right: 0;
}
</style>