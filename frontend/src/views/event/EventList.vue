<template>
  <div class="events container">
    <div class="columns is-multiline">
      <div v-for="event in events" :key="event.eventId" class="column is-one-quarter">
<!--        <EventCard :event="event" @click.native="goToEvent(event.eventId)" />-->
        <EventCard :event="event" @click.native="goToAction(event)" />
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import EventCard from '@/views/event/EventCard.vue';
import {BASE_URL_EVENT_SERVICE} from "../../../config/dev.env";
export default {
  props: ['isPostAddOrEdit'],
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
    // goToEvent(eventId) {
    //   this.$router.push({ name: 'EventView', params: { eventId } });
    // }
    goToAction(event) {
      if(this.isPostAddOrEdit){
        this.$emit('addTaggedEvent', event)
      }else{
        const eventId = event.eventId;
        this.$router.push({ name: 'EventView', params: { eventId } });
      }

    }
  }
};
</script>

<style lang="scss" scoped>
.events {
  margin-top: 20px;
  text-align: center;
}
</style>
