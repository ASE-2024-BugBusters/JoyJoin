<template>
  <div class="events container">
    <h1 class="subtitle is-3" v-if="events.length > 0">Check Out Upcoming Events</h1>
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
