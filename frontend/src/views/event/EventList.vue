<template>
  <div class="events container">
    <h2 class="subtitle is-3">Check out our upcoming events</h2>
    <div class="columns is-multiline">
      <div v-for="event in events" :key="event.id" class="column is-one-quarter">
        <EventCard :event="event" @click.native="goToEvent(event.id)" />
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import EventCard from '@/views/event/EventCard.vue';
import {BASE_URL_EVENT_SERVICE} from "../../../config/dev.env";
export default {
  name: 'EventsList',
  components: {
    EventCard,
  },
  data() {
    return {
      events: [],
    }
  },
  created() {
    this.fetchEvents();
  },
  methods: {
    fetchEvents() {
      const getAllEventsUrl = BASE_URL_EVENT_SERVICE + "/events"
      axios.get(getAllEventsUrl, {
        headers: {
          // 'Content-Type': 'application/json',
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
    goToEvent(id) {
      this.$router.push({ name: 'EventView', params: { id } });
    }
  }
};
</script>

<style lang="scss" scoped>
.events {
  margin-top: 100px;
  text-align: center;
}
</style>
