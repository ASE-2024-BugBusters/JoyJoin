<template>
  <div class="filter">
    <form>
      <div class="form-group">
        <label for="formGroupExampleInput">Title</label>
        <input v-model="filters.title" type="text" class="form-control" id="title" placeholder="Input title to filter events">
      </div>
      <div class="form-group">
        <label for="formGroupExampleInput">City</label>
        <input v-model="filters.city" type="text" class="form-control" id="city" placeholder="Input city to filter events">
      </div>
      <div class="form-group">
        <label for="formGroupExampleInput">Time</label>
        <input v-model="filters.time" type="datetime-local" class="form-control" id="time" placeholder="Select title to time events">
      </div>
      <div class="form-group">
        <label for="formGroupExampleInput">Category</label>
        <Multiselect
            v-model="filters.tags"
            tag-placeholder="Add new tag"
            placeholder="Select or add tags"
            label="label"
            track-by="value"
            :options="tags"
            :multiple="true"
            :taggable="true"
            @tag="addTag"
        ></Multiselect>
      </div>
      <div class="form-group">
        <label for="formGroupExampleInput">Exclude Full Events?</label>
        <input type="checkbox" v-model="filters.excludeFullEvents">
      </div>
      <div class="form-group">
        <button class="btn btn-outline-dark "@click="clearFilters" type="reset">Clear All Filters</button>
      </div>
<!--      <button class="btn btn-outline-primary" @click="fetchEvents">Filter Events</button>-->

    </form>

  </div>
  <div class="events container">
    <div class="columns is-multiline" v-if="events.length > 0">
      <div v-for="event in events" :key="event.eventId" class="column is-one-quarter">
        <EventCard :event="event" @click.native="goToAction(event)" />
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
import Multiselect from '@vueform/multiselect';
import {BASE_URL_EVENT_SERVICE, INTEREST_TAGS} from "../../../config/dev.env";

export default {
  props: ['isPostAddOrEdit'],
  name: 'EventsList',
  components: {
    EventCard,
    Multiselect
  },
  data() {
    return {
      events: [],
      filters: {
        title: '',
        city: '',
        time: '',
        tags: [],
        excludeFullEvents: false
      },
      tags: INTEREST_TAGS.map(tag => ({
        label: tag.label,
        value: tag.value
      }))
    }
  },
  created() {
    this.fetchEvents();
  },
  watch: {
    // 监听 filters 对象的变化
    filters: {
      handler: function() {
        this.fetchEvents();
      },
      deep: true
    }
  },
  methods: {
    fetchEvents() {
      const params = new URLSearchParams();
      for (const key in this.filters) {
        if (this.filters[key]) {
          if (Array.isArray(this.filters[key]) && this.filters[key].length) {
            // Join array elements into a comma-separated string
            params.append(key, this.filters[key].join(','));
          } else if (!Array.isArray(this.filters[key])) {
            params.append(key, this.filters[key]);
          }
        }
      }
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
    goToAction(event) {
      if (this.isPostAddOrEdit) {
        this.$emit('addTaggedEvent', event);
      } else {
        const eventId = event.eventId;
        this.$router.push({ name: 'EventView', params: { eventId } });
      }
    },
    clearFilters() {
      this.filters = {
        title: '',
        city: '',
        time: '',
        tags: '',
        excludeFullEvents: false
      };
    },
    addTag(newTag) {
      const tag = {
        label: newTag,
        value: newTag.toLowerCase().replace(/\W/g, '')
      };
      this.tags.push(tag);
      this.filters.tags.push(tag);
    }
  }
};
</script>

<style lang="scss" scoped>
@import "@vueform/multiselect/themes/default.css";
.events {
  margin-top: 20px;
  text-align: center;
  overflow-y: hidden;
}
.filter {
  margin: 2em;
  display: block;
}
.field {
  flex-grow: 1;
  min-width: 300px;
  margin-bottom: 1em;
}
.no-events {
  font-size: 1.5em;
  color: black;
}
.form-group {
  align-items: center;
  width: 30%;
  margin: 1em;
}
label {
  margin-right: 1em;
  margin-left: 2em;
  font-weight: bold;
}
form {
  display: flex;
  justify-content: center;
  align-items: center;
}
button {
  font-weight: bold;
}
</style>
