<template>
  <div class="filter" v-if="!attendedEvent">
    <form>
      <div class="form-group">
        <label for="formGroupExampleInput">Title</label>
        <input v-model="filters.title" type="text" class="form-control" id="title" placeholder="">
      </div>
      <div class="form-group">
        <label for="formGroupExampleInput">City</label>
        <input v-model="filters.city" type="text" class="form-control" id="city" placeholder="">
      </div>
      <div class="form-group">
        <label for="formGroupExampleInput">Date</label>
        <input v-model="filters.date" type="date" class="form-control" id="date" placeholder="">
      </div>
      <div class="form-group">
        <label for="formGroupExampleInput">Category</label>
        <Multiselect
            v-model="filters.tags"
            mode="tags"
            tag-placeholder="Add new tag"
            placeholder=""
            label="label"
            track-by="value"
            :options="tags"
            :multiple="true"
            :taggable="true"
            :searchable="true"
            @tag="addTag"
        ></Multiselect>
      </div>
      <div class="form-group">
        <label for="formGroupExampleInput">Exclude Full Events?</label>
        <input type="checkbox" v-model="filters.excludeFullEvents">
      </div>
      <div class="form-group">
        <button v-if="hasFilters" class="btn btn-outline-dark "@click="clearFilters" type="reset">Clear All Filters</button>
      </div>
    </form>

  </div>
  <div class="events container">
    <div class="columns is-multiline" v-if="events.length > 0">
      <div v-for="event in events" :key="event.eventId" class="column is-one-quarter">
        <EventCard :event="event" :isPostAddOrEdit = "isPostAddOrEdit" @image-clicked="goToAction" />
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
  props: ['isPostAddOrEdit', 'attendedEvent'],
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
        date: '',
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
    filters: {
      handler: function() {
        this.fetchEvents();
      },
      deep: true
    }
  },
  computed: {
    hasFilters() {
      return this.filters.title !== '' ||
          this.filters.city !== '' ||
          this.filters.date !== '' ||
          this.filters.tags.length > 0 ||
          this.filters.excludeFullEvents; // Check if checkbox is true
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
        // this.$emit('image-clicked', event.eventId);
        const eventId = event.eventId;
        this.$router.push({ name: 'EventView', params: { eventId } });
      }
    },
    clearFilters() {
      this.filters = {
        title: '',
        city: '',
        date: '',
        tags: [],
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
  margin: 2em auto; // Maintain margin for spacing around
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start; // Align items to the start of the line
  align-items: flex-start; // Align items to the top
  max-width: 1200px; // Control maximum width to avoid overly wide forms
}
.form-group {
  display: flex;
  flex-direction: column;
  align-items: center; // Center items for better alignment of checkbox
  margin: 0.5em;
  flex: 1 1 220px; // Allows flexibility with starting at 220px
  min-width: 220px; // Minimum width for smaller screens
  max-width: 300px; // Maximum width for larger screens
}
label {
  margin-bottom: 0.5em;
  width: 100%; // Ensure labels align properly over their fields
  font-weight: bold;
}
form {
  width: 100%; // Use full width to manage inner content
  display: flex;
  flex-direction: row; // Set form direction to row for side-by-side layout
  flex-wrap: wrap; // Allow wrapping
  justify-content: center; // Center content within form
  align-items: flex-start; // Align form items at the start vertically
}
button {
  flex: 0 1 auto; // Don't allow button to grow but can shrink if necessary
  margin-top: 1em; // Provide top margin for alignment
  padding: 0.5em 2em; // Larger padding for better visibility
}
.multiselect {
  width: 100%; // Ensure multiselect fills its container
}
input[type="checkbox"] {
  transform: scale(1.5); // Enlarge checkbox
  margin: 10px; // Provide margin for better spacing
  vertical-align: middle; // Align checkbox vertically for aesthetics
}
.no-events{
  font-weight: bold;
  margin-bottom: 5em;
  font-size: 2em;
}

</style>
