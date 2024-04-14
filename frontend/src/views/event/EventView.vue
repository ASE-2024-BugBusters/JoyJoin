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
<!--                  <div class="mb-3">-->
<!--                    <span class="badge" v-for="label in tagLabels" :key="label">{{ label }}</span>-->
<!--                  </div>-->
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
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import axios from 'axios';
import {TAGS} from "../../../config/dev.env";
export default {
  data() {
    return {
      event: null,
      source: TAGS,
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
  },
  created() {
    this.fetchEventData();
  },
  methods: {
    fetchEventData() {
      const eventId = this.$route.params.id;
      axios.get(`http://localhost:8084/api/events/${eventId}`)
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
  },
};
</script>
<style lang="scss" scoped>
.container {
  width: 80vw;
  max-width: 1200px;

  margin: auto;
  font-family: Arial,'Roboto', sans-serif;
}
@media (max-width: 768px) {
  .container {
    width: 95vw; // 在小屏幕设备上，容器宽度为100%
  }
}
.card-header {
  background: #131516;
}

.card-title {
  color: #9dff0b;
  text-align: center;
  font-size: 2.5em;
}
.card {
  margin-top: 10px;
  background-color: #f8f9fa;
}
i {
  margin-right: 0.5em;
}
.card-body {
  text-align: left;
  //font-size: 1.3em;
}
.card-text {
  font-size: 1.3em;
}
.badge {
  color: #9dff0b;
  margin-right: 0.5em; // Add horizontal space between badges
  margin-bottom: 0.5em; // Optional: add vertical space if the badges wrap to a new line
  background: #131516;
  white-space: nowrap; /* Prevents badge text from breaking into multiple lines */
  line-height: normal; /* Resets line height to avoid extra vertical spacing */
}
.tags-container {
  display: flex; /* Makes the container a flexbox */
  align-items: center; /* Aligns children vertically in the center */
  flex-wrap: wrap; /* Allows tags to wrap to next line on small screens */
}
.tag-badges {
  display: flex; /* Makes the badges container also a flexbox */
  flex-wrap: wrap; /* Allows badges to wrap */
  align-items: center;
  margin-left: 10px; /* Adds space between "Tags:" and the badges */
}
.card-img-top {
  width: 100%; /* Makes the image take the full width of the card */
  height: 200px; /* Sets a fixed height for all images */
  object-fit: cover; /* Ensures the image covers the area without distorting aspect ratio */
}
.img-fluid {
  width: 100%;
  max-width: 60%; /* adjusts to roughly half the container width with some margin */
  height: auto;
}
</style>

