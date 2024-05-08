<template>
  <div class="card">
    <img class="card-img-top" :src="imageUrl" alt="Event Image" @click="$emit('click')">
    <div class="card-body">
      <h4 class="card-title">{{ event.title }}</h4>
    </div>
    <ul class="list-group list-group-flush">
      <li class="list-group-item">{{ formattedDateTime }}</li>
      <li class="list-group-item">{{ event.location.street }} {{ event.location.number }}, {{ event.location.city }} {{ event.location.postalCode }}</li>
    </ul>
    <div class="card-body">
      <span @click="" class="card-link"><i class="bi bi-bookmark-plus-fill"></i></span>
      <span @click="" class="card-link"><i class="bi bi-share-fill"></i></span>
    </div>
  </div>
</template>

<script>
export default {
  props: ['event'],
  computed: {
    imageUrl() {
      return this.event.images && this.event.images.length > 0
        ? this.event.images[0].urls[0].url
        : require('@/assets/event_default.jpeg');
    },
    formattedDateTime() {
      const date = new Date(this.event.time);
      const dateString = date.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'short' });
      const timeString = date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', hour12: false });
      return `${dateString} AT ${timeString}`;
    },
  }
};
</script>

<style scoped>
  .card {
    border: 1.2px solid rgba(0, 0, 0, 0.37);
    border-radius: 10px;
    overflow: hidden;
    margin-bottom: 25px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    transition: box-shadow 0.3s ease;
    font-weight: bold;
  }
  .card:hover {
   box-shadow: 0 5px 15px rgba(0, 0, 0, 0.8);
   cursor: pointer;
  }
  .card-title {
    font-size: 1.5em;
    font-weight: 1000;
    margin: 0;
  }
  .card-img-top {
  width: 100%;
  display: block;
  height: 180px;
  object-fit: cover;
  border-bottom: solid black 0.5px;
  }
  i {
    margin: 3em;
  }
  .card-link > i {
    font-size: 1.1em; /* 调整为更大的尺寸，您可以根据需要修改这个值 */
  }
</style>

