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
                <input class="input" type="number" v-model="event.location.number" :placeholder="event.location.number || 'Enter number'" min="1" required>
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
              <input class="input" type="number" v-model="event.participationLimit" :placeholder="event.participationLimit || 'Enter limit'" min="2" required>
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
              <button class="btn btn-outline-secondary" @click="returnToEventView"><strong>Return</strong></button>
              <button class="btn btn-outline-success" @click="editEvent"><strong>Save</strong></button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { ref, reactive, onMounted, computed, nextTick, watch } from 'vue';
import { useRouter } from 'vue-router';
import { BASE_URL_EVENT_SERVICE } from "../../../config/dev.env";

export default {
  setup() {
    const router = useRouter();
    const eventId = router.currentRoute.value.params.eventId;
    const minDateTime = computed(() => new Date().toISOString().substring(0, 16));
    const event = reactive({
      title: "",
      time: "",
      location: { street: "", number: "", city: "", country: "" },
      participationLimit: "",
      description: "",
    });
    const originalEvent = ref({});
    const dirty = ref(false);
    const loading = ref(true);

    const fetchEventData = async () => {
      loading.value = true;
      try {
        const response = await axios.get(`${BASE_URL_EVENT_SERVICE}/events/${eventId}`, {
          headers: { 'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}` }
        });
        Object.assign(event, response.data);
        originalEvent.value = JSON.parse(JSON.stringify(response.data)); // 深拷贝初始化
      } finally {
        loading.value = false;
      }
    };

    watch(event, (newVal, oldVal) => {
      dirty.value = JSON.stringify(newVal) !== JSON.stringify(originalEvent.value);
    }, { deep: true });

    const returnToEventView = () => {
      if (dirty.value) {
        if (confirm('You have unsaved changes. Do you want to save them before leaving?')) {
          editEvent(); // Save changes before returning
        } else {
          router.push({name: 'EventView', params: {eventId}}); // Leave without saving
        }
      } else {
        router.push({name: 'EventView', params: {eventId}});
      }
    };

    onMounted(fetchEventData);

    return {
      event, loading, minDateTime, returnToEventView
    };
  }
}
</script>




<style scoped>
.container {
  margin-top: 50px;
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
.control {
  display: grid;
  grid-auto-flow: column;
  gap: 10px;  /* This sets the gap between any grid items */
}
.btn {
  font-weight: bold;
}
</style>
