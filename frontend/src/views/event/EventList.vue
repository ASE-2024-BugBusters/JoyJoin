<template>
  <div class="events container">
    <h1 class="subtitle is-3">Check Out Upcoming Events</h1>
    <div class="columns is-multiline">
      <div v-for="event in events" :key="event.eventId" class="column is-one-quarter">
        <EventCard :event="event" @click.native="goToEvent(event.eventId)" />
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
      debugger;
      if (sessionStorage.getItem("jwtToken") !== null || sessionStorage.getItem("userId") !== null) {
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
      }
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
}
h1 {
  font-weight: bold;
  color: black;
}
</style>
