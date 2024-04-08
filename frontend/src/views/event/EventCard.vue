<template>
  <div class="event-card" @click="$emit('click')">
    <div class="card-image">
      <img :src="imageUrl" alt="Event Image">
    </div>
    <div class="card-content">
      <h2 class="event-title">{{ event.title }}</h2>
      <p class="event-date-time">{{ formattedDateTime }}</p>
      <p class="event-location">{{ event.location.street }} {{ event.location.number }}, {{ event.location.city }} {{ event.location.postalCode }}</p>
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
        : require('@/assets/defaultEventImg.jpeg');
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

<style lang="scss" scoped>
.event-card {
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  overflow: hidden;
  margin-bottom: 25px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;

  &:hover {
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
    cursor: pointer;
  }
}

.card-image img {
  width: 100%;
  display: block;
  height: 180px;
  object-fit: cover;
}

.card-content {
  padding: 15px;
  background: #ffffff;
  text-align: left;

  .event-title {
    font-size: 1.3em;
    font-weight: 1000;
    color: #333;
    margin-bottom: 8px;
  }

  .event-date, .event-time, .event-location {
    font-weight: 900;
    font-size: 1.2em;
    color: #555;
    margin: 1px 0;
  }

  .event-location {
    font-weight: 700;
    font-size: 1.2em;
    margin-top: 7px;
  }
}
</style>

