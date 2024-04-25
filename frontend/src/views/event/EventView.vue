<<template>
  <div class="event-single" v-if="event">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-lg-7">
          <div class="card">
            <div class="card-header">
              <h1 class="card-title">{{ event.title }}</h1>
            </div>
            <div class="card-body">
              <p class="card-text"><i class="bi bi-calendar-check"></i><strong>Time:</strong> {{ formattedDateTime }}</p>
              <p class="card-text"><i class="bi bi-geo-alt-fill"></i><strong>Location:</strong> {{ event.location.street }}, {{ event.location.city }}</p>
              <p class="card-text"><i class="bi bi-people-fill"></i><strong>Participants limit:</strong> {{ event.participationLimit }}</p>
              <p class="card-text"><i class="bi bi-braces-asterisk"></i><strong>Description:</strong> {{ event.description }}</p>
              <div class="card-text">
                <div class="tags-container">
                  <i class="bi bi-tags-fill"></i>
                  <strong>Tags:</strong>
                  <div class="tag-badges">
                    <span class="badge" v-for="label in tagLabels" :key="label">{{ label }}</span>
                  </div>
                </div>
              </div>
              <div class="row g-2">
                <div class="card" style="width: 12rem;height: 11rem;" v-for="image in event.images" :key="image.key">
                  <img :src="getImageUrl(image)" :alt="`Event Image ${image.key}`" class="card-img-top">
                </div>
              </div>
              <div class="d-grid gap-2" v-if="isCreator">
                <button class="btn btn-outline-info" type="button" @click="toEditEvent">Edit</button>
                <button class="btn btn-outline-danger" type="button" @click="toDeleteEvent">Delete</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>

</template>
<script>
import axios from 'axios';
import {INTEREST_TAGS, BASE_URL_EVENT_SERVICE} from "../../../config/dev.env";
import EditEvent from "@/views/event/EditEvent.vue";
export default {
  data() {
    return {
      event: null,
      source: INTEREST_TAGS,
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
    tagLabels() {
      // Assuming `this.event.tags` is an array of tag values
      if (!this.event || !this.event.tags) return [];
      return this.event.tags.map(tagValue =>
          this.getLabelForTag(tagValue)
      );
    },
    isCreator() {
      const userId = sessionStorage.getItem('userId');
      return this.event && userId === this.event.creatorId;
    }
  },
  created() {
    this.fetchEventData();
  },
  methods: {
    fetchEventData() {
      const eventId = this.$route.params.eventId;
      axios.get(`${BASE_URL_EVENT_SERVICE}/events/${eventId}`, {
        headers: {
          // 'Content-Type': 'application/json',
          'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
        }
      })
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
    getLabelForTag(tagValue) {
      // Find the corresponding label for a given tag value
      const item = this.source.find(item => item.value === tagValue);
      return item ? item.label : 'Unknown';
    },
    async toEditEvent() {
      const eventId = this.$route.params.eventId;
      try {
        await this.$router.push({name: "EditEvent", params:{eventId}});
      } catch (error) {
        console.error('Error navigating to edit event page:', error);
      }
    },
    async toDeleteEvent() {
      const eventId = this.$route.params.eventId;
      if (confirm('Are you sure you want to delete the event?')) {
        try {
          await axios.delete(`${BASE_URL_EVENT_SERVICE}/events/${eventId}`, {
              headers: { 'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}` }
          });
          console.log('Event deleted successfully');
          await this.$router.push({path: "/"});
        } catch (error) {
          console.error('Error deleting event:', error);
        }
      }
    },
  },
};
</script>
<style lang="scss" scoped>
.container {
  width: 80vw;
  max-width: 1200px;
  margin: auto;
  padding: 20px;
  font-family: Arial, 'Roboto', sans-serif;
}

@media (max-width: 768px) {
  .container {
    width: 95vw;
    padding: 10px;
  }
}

.card-header {
  background: #2c3e50;
  padding: 20px 10px;
}

.card-title {
  color: white;
  text-align: center;
  font-size: 2.5em;
}

.card {
  margin-top: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(44, 62, 80, 0.15);
}

.card-body {
  padding: 20px;
  text-align: left;
}

.card-text {
  font-size: 1.3em;
  margin-bottom: 10px;
}

.badge {
  background: #2c3e50;
  color: white;
  margin-right: 0.5em;
  margin-bottom: 0.5em;
  padding: 5px 10px;
  border-radius: 5px;
  white-space: nowrap;
}

.tags-container {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.tag-badges {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  margin-left: 10px;
}

.card-img-top {
  width: 100%;
  height: auto;
  object-fit: cover;
  border-radius: 5px;
}

.img-fluid {
  width: 100%;
  max-width: 100%; /* Full width within its container */
  height: auto;
}

.row g-2 {
  margin: 2px;
}

.btn {
  //margin: 10px 5px;
  width: auto; /* Adjust width to fit content */
  //padding: 10px 20px;
  font-size: 1em;
  border: none;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(44, 62, 80, 0.10);
  font-weight: bold;
  margin-top: 0.5em;
  margin-bottom: 0.2em;
  //background: white;
  border: 3px solid #2c3e50;
}
</style>

