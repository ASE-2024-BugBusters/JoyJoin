<template>
  <div class="container" v-if="!loading">
    <div class="columns is-centered">
      <div class="column is-half">
        <h1 class="title">Edit Event</h1>
        <form @submit.prevent="editEvent">
          <div class="field">
            <label class="label">Title</label>
            <div class="control">
              <input class="input" type="text" v-model="event.title" :placeholder="event.title || 'Enter title'" required>
            </div>
          </div>
          <div class="field">
            <label class="label">Time</label>
            <div class="control">
              <input class="input" type="datetime-local" v-model="event.time" :placeholder="event.time || 'Select time'" :min="minDateTime" required>
            </div>
          </div>
          <div class="flex-row">
            <div class="field">
              <label class="label">Street</label>
              <div class="control">
                <input class="input" type="text" v-model="event.location.street" :placeholder="event.location.street || 'Enter street'" required>
              </div>
            </div>
            <div class="field">
              <label class="label">Number</label>
              <div class="control">
                <input class="input" type="number" v-model="event.location.number" :placeholder="event.location.number || 'Enter number'" required>
              </div>
            </div>
            <div class="field">
              <label class="label">City</label>
              <div class="control">
                <input class="input" type="text" v-model="event.location.city" :placeholder="event.location.city || 'Enter city'" required>
              </div>
            </div>
            <div class="field">
              <label class="label">Country</label>
              <div class="control">
                <input class="input" type="text" v-model="event.location.country" :placeholder="event.location.country || 'Enter country'" required>
              </div>
            </div>
          </div>

          <div class="field">
            <label class="label">Participation Limit</label>
            <div class="control">
              <input class="input" type="number" v-model="event.participationLimit" :placeholder="event.participationLimit || 'Enter limit'" required>
            </div>
          </div>

          <div class="field">
            <label class="label">Tags</label>
            <div class="control">
              <Multiselect
                  v-model="multiValue"
                  mode="tags"
                  :close-on-select="false"
                  :searchable="true"
                  :create-option="false"
                  :options="source"
                  option-label="label"
                  option-value="value"
                  placeholder=""
                  required
              />
            </div>
          </div>

          <div class="field">
            <label class="label">Description</label>
            <div class="control">
              <textarea class="textarea" v-model="event.description" :placeholder="event.description || 'Enter description'" rows="7"></textarea>
            </div>
          </div>
          <div class="field">
            <div class="control">
              <button class="button is-primary" @click="editEvent">Save</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import Multiselect from '@vueform/multiselect';
import {ref, reactive, onMounted, computed, nextTick} from 'vue';
import { useRouter } from 'vue-router';
import { INTEREST_TAGS, BASE_URL_EVENT_SERVICE } from "../../../config/dev.env";

export default {
  components: {
    Multiselect
  },
  setup() {
    const router = useRouter();
    const eventId = router.currentRoute.value.params.eventId;
    const minDateTime = computed(() => {
      let now = new Date();
      return now.toISOString().substring(0, 16); // YYYY-MM-DDTHH:MM format
    });
    const event = reactive({
      title: "",
      time: "",
      location: { street: "", number: "", city: "", country: "" },
      participationLimit: "",
      description: "",
    });

    const multiValue = ref([]);
    const source = ref(INTEREST_TAGS); // Static list of all possible tags
    const loading = ref(true);

    const fetchEventData = async () => {
      try {
        const response = await axios.get(`${BASE_URL_EVENT_SERVICE}/events/${eventId}`, {
          headers: { 'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}` }
        });
        Object.assign(event, response.data);
        event.tags = ref([]);
        // Convert tag strings to objects based on INTEREST_TAGS
        // multiValue.value = response.data.tags.map(tagString => {
        //   const tagObject = source.value.find(tag => tag.value === tagString);
        //   return tagObject ? { label: tagObject.label, value: tagObject.value } : { label: tagString, value: tagString };
        // });

      } catch (error) {
        console.error("Error fetching event data:", error);
      } finally {
        loading.value = false;
      }
    };


    const editEvent = async () => {
      if (!event.title.trim() || !event.time || !multiValue.value.length ||
          !event.location.street.trim() || !event.location.number || !event.location.city.trim() ||
          !event.location.country.trim() || !event.participationLimit) {
        alert('Please fill all the required fields.');
        return;
      }
      await nextTick();
      try {
        const updatedEvent = {
          title: event.title,
          time: new Date(event.time).toISOString(),
          location: event.location,
          participationLimit: parseInt(event.participationLimit),
          description: event.description,
          // tags: multiValue.value.map(tag => tag.value), // Send only the tag values
          tags: multiValue.value,
        };
        console.log("hiii");
        console.log(multiValue.value);
        const response = await axios.patch(`${BASE_URL_EVENT_SERVICE}/events/${eventId}`, updatedEvent, {
          headers: { 'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}` }
        });
        console.log("Successfully updated event:", response.data);
        await router.push({name: 'EventView', params: {eventId}});
      } catch (error) {
        console.error("Error updating the event:", error);
      }
    };
    onMounted(fetchEventData);

    return {
      event, editEvent, multiValue, source, loading, minDateTime
    };
  }
}
</script>


<style scoped>
.container {
  margin-top: 50px;
}
.subtitle {
  font-weight: bold;
}
.textarea {
  min-height: 150px;
  width: 100%;
}
.flex-row {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.field {
  flex-grow: 1;
  min-width: 300px;
}
</style>
