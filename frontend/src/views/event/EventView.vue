<template>
  <div class="event-single" v-if="event">
    <div class="event-details">
      <h1 class="event-title">{{ event.title }}</h1>
      <p class="event-date-time">{{ formattedDateTime }}</p>
      <p class="event-location">{{ event.location.street }}, {{ event.location.city }}</p>
      <p class="event-participation-limit">Participants limit: {{ event.participationLimit }}</p>
      <p class="event-description">{{ event.description }}</p>

      <div class="event-tags">
        <span class="tag" v-for="tag in event.tags" :key="tag">{{ tag }}</span>
      </div>
    </div>

    <div class="event-images">
      <img v-for="image in event.images" :src="getImageUrl(image)" :alt="`Event Image ${image.key}`" class="event-image" :key="image.key">
    </div>

  </div>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      event: null,
    };
  },
  computed: {
    formattedDateTime() {
      if (!this.event || !this.event.time) return '';
      const date = new Date(this.event.time);
      return date.toLocaleString('en-US', {
        year: 'numeric', month: 'long', day: 'numeric',
        hour: '2-digit', minute: '2-digit', hour12: true
      });
    },
  },
  created() {
    this.fetchEventData();
  },
  methods: {
    fetchEventData() {
      const eventId = this.$route.params.id;
      axios.get(`http://localhost:8084/api/event/${eventId}`)
        .then(response => {
          this.event = response.data;
        })
        .catch(error => {
          console.error("There was an error fetching the event details:", error);
        });
    },
    getImageUrl(image) {
      return image.urls[0].url;
    },
  },
};
</script>

<style lang="scss" scoped>
.event-single {
  .event-images {
    display: flex;
    flex-wrap: wrap;
    justify-content: center;
    margin-bottom: 20px;
    width: auto;

    .event-image {
      max-width: 100%;
      max-height: 180px;
      margin: 5px;
      object-fit: cover;
    }
  }

  .event-details {
    margin: 20px;
  }

}
</style>
