<template>
  <div class="events container">
    <h1 class="subtitle is-3" v-if="events.length > 0">Check Out Upcoming Events</h1>
    <div>
      <input v-model="filters.title" placeholder="Title">
      <input v-model="filters.city" placeholder="City">
      <input v-model="filters.time" type="datetime-local">
      <select v-model="filters.tags">
        <option disabled value="">Select tags</option>
        <option v-for="tag in tags" :key="tag.value" :value="tag.value">
          {{ tag.label }}
        </option>
      </select>
      <label>
        <input type="checkbox" v-model="filters.excludeFullEvents">
        Exclude full events
      </label>
      <button @click="fetchEvents">Filter Events</button>

    </div>
    <div v-if="events.length > 0" class="columns is-multiline">
      <div v-for="event in events" :key="event.eventId" class="column is-one-quarter">
        <EventCard :event="event" @image-clicked="goToEvent" />
      </div>
    </div>
    <div v-else class="no-events">
      No Events
    </div>
  </div>
</template>


<script>
import axios from 'axios';
import EventCard from '@/views/event/EventCard.vue';
import {BASE_URL_EVENT_SERVICE, INTEREST_TAGS} from "../../../config/dev.env";

export default {
  name: 'EventsList',
  components: {
    EventCard,
  },
  data() {
    return {
      events: [],
      filters: {
        title: '',
        city: '',
        time: '',
        tags: '',
        excludeFullEvents: false  // 默认不排除满员的活动
      },
      tags: INTEREST_TAGS  // 将标签列表作为数据属性存储
    }
  },
  created() {
    this.fetchEvents();
  },
  methods: {
    fetchEvents() {
      const params = new URLSearchParams();
      if (this.filters.title) params.append('title', this.filters.title);
      if (this.filters.city) params.append('city', this.filters.city);
      if (this.filters.time) params.append('time', this.filters.time);
      if (this.filters.tags) params.append('tags', this.filters.tags);
      if (this.filters.excludeFullEvents) params.append('excludeFullEvents', 'true');

      const getAllEventsUrl = `${BASE_URL_EVENT_SERVICE}/events/filter?${params.toString()}`;

      axios.get(getAllEventsUrl, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
        }
      })
          .then(response => {
            this.events = response.data;
          })
          .catch(error => {
            console.error("There was an error fetching the events:", error);
          });
    },

    goToEvent(eventId) {
      this.$router.push({ name: 'EventView', params: { eventId } });
    }
  }
};
</script>


<style lang="scss" scoped>
.events {
  margin-top: 100px;
  text-align: center;
  overflow-y: hidden;
}
h1 {
  font-weight: bold;
  color: black;
}
.no-events {
  font-size: 1.5em;
  color: black;
}
</style>
