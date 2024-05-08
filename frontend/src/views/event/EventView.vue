<template>
  <div class="event-single" v-if="event">
    <div class="container">
      <div class="row justify-content-center">
        <div class="col-lg-7">
          <div class="card">
            <div class="card-header">
              <h1 class="card-title">{{ event.title }}</h1>
            </div>
            <div class="card-body">
              <p class="card-text">
                <i class="bi bi-calendar-check-fill"></i>
                <span class="label"><strong>Date</strong></span>
                <span class="content">{{ formattedDate }}</span>
              </p>
              <p class="card-text">
                <i class="bi bi-alarm-fill"></i>
                <span class="label"><strong>Time:</strong></span>
                <span class="content">{{ formattedTime }}</span>
              </p>
              <p class="card-text">
                <i class="bi bi-geo-alt-fill"></i>
                <span class="label"><strong>Location:</strong></span>
                <span class="content">
                  {{ event.location.street }} {{ event.location.number }}, {{ event.location.postalCode }} {{ event.location.city }}
                </span>
              </p>
              <div class="card-text">
                <div class="tags-container">
                  <i class="bi bi-tags-fill"></i>
                  <span class="label"><strong>Category:</strong></span>
                  <span class="content">
                                      <div class="tag-badges">
                    <span class="badge" v-for="label in tagLabels" :key="label">{{ label }}</span>
                  </div>
                  </span>
                </div>
              </div>
              <p class="card-text">
                <i class="bi bi-chat-left-text-fill"></i>
                <span class="label"><strong>Description:</strong></span>
                <span class="content">
                  {{ event.description }}
                </span>
              </p>
              <p class="card-text">
                <i class="bi bi-people-fill"></i>
                <span class="label"><strong>Participants:</strong></span>
                <span class="content">
                  {{ participants.length }} / {{ event.participationLimit }}
                </span>
              </p>
              <p class="card-text">
                <i class="bi bi-images"></i>
                <span class="label"><strong>Event Images:</strong></span>
              </p>
              <div class="row g-2">
                <div class="image-card" v-for="image in event.images" :key="image.key" @click="isCreator ? toRemoveImage(image.key) : null" :class="{ 'creator-mode': isCreator }">
                  <img :src="getImageUrl(image)" :alt="`Event Image ${image.key}`" class="card-img" :class="{ 'clickable': isCreator }">
                </div>
              </div>

              <div class="d-grid gap-2" v-if="!isCreator">
                <button class="btn btn-outline-success" type="button" @click="toRegisterEvent" v-if="!isJoined & !isFullyOccupied">Join</button>
                <button class="btn btn-outline-warning" type="button" @click="toUnRegisterEvent" v-if="isJoined">Unregister</button>
              </div>
              <div class="d-grid gap-2" v-if="isCreator">
                <button class="btn btn-outline-secondary" type="button" @click="toUploadImages" v-if="event.images.length > 0">Update Images</button>
                <button class="btn btn-outline-secondary" type="button" @click="toUploadImages" v-if="event.images.length == 0">Upload Images</button>
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
      eventId: this.$route.params.eventId,
      participants: [],
      imageDetails: []
    };
  },
  computed: {
    formattedTime() {
      if (!this.event || !this.event.time) return '';
      const date = new Date(this.event.time);
      return date.toLocaleString('en-US', {
        hour: '2-digit', minute: '2-digit', hour12: false
      });
    },
    formattedDate() {
      if (!this.event || !this.event.time) return '';
      const date = new Date(this.event.time);
      return date.toLocaleString('en-US', {
        year: 'numeric', month: 'long', day: 'numeric'
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
      console.log("userId from sessionStorage:", userId); // Debugging
      console.log("Event creator ID:", this.event ? this.event.creatorId : "Event not loaded");
      return this.event && userId === this.event.creatorId;
    },
    isJoined() {
      const userId = sessionStorage.getItem('userId');
      console.log("userId from sessionStorage:", userId); // Debugging
      console.log("Event participants:", this.event ? this.participants : "Event not loaded");
      return this.event && this.participants.includes(userId);
    },
    isFullyOccupied() {
      return (this.event.participationLimit == this.participants.length);
    }
  },
  created() {
    this.fetchEventData();
    this.fetchParticipants();
  },
  watch: {
    event(newVal) {
      if (newVal) {
        this.extractImageDetails();
      }
    }
  },
  methods: {
    fetchEventData() {
      const eventId = this.$route.params.eventId;
      axios.get(`${BASE_URL_EVENT_SERVICE}/events/${eventId}`, {
        headers: {
          'Content-Type': 'application/json',
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
    extractImageDetails() {
      if (this.event && this.event.images) {
        this.imageDetails = this.event.images.map(image => ({
          bucket: image.bucket,
          key: image.key
        }));
      }
    },
    fetchParticipants() {
      const eventId = this.$route.params.eventId;
      axios.get(`${BASE_URL_EVENT_SERVICE}/events/${eventId}/participants`, {
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
        }
      })
          .then(response => {
            this.participants = response.data;
          })
          .catch(error => {
            console.error("There was an error fetching the event participants:", error);
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
      try {
        await this.$router.push({name: "EditEvent", params: this.eventId});
      } catch (error) {
        console.error('Error navigating to edit event page:', error);
      }
    },
    async toUploadImages() {
      try {
        await this.$router.push({name: "EditImage", params: this.eventId});
      } catch (error) {
        console.error('Error navigating to upload image page:', error);
      }
    },
    async toDeleteEvent() {
      const eventId = this.$route.params.eventId;
      if (confirm('Are you sure you want to delete the event?')) {
        try {
          await axios.delete(`${BASE_URL_EVENT_SERVICE}/events/${eventId}`, {
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            }
          });
          console.log('Event deleted successfully');
          await this.$router.push({path: "/"});
        } catch (error) {
          console.error('Error deleting event:', error);
        }
      }
    },
    async toRemoveImage(key) {
      const eventId = this.$route.params.eventId;
      this.imageDetails = this.imageDetails.filter(detail => detail.key !== key);
      const updateImagesRequest = {
        images: this.imageDetails.map(image => ({
          bucket: image.bucket,
          key: image.key
        }))
      };
      if (confirm('Are you sure you want to delete the image?')) {
        try {
          await axios.patch(`${BASE_URL_EVENT_SERVICE}/events/${eventId}/images`, updateImagesRequest, {
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
            }
          });
          console.log('Delete images successfully');
          this.fetchEventData();
        } catch (error) {
          console.error('Error deleting images:', error);
        }
      }
    },
    toRegisterEvent() {
      const eventId = this.$route.params.eventId;
      const userId = sessionStorage.getItem('userId');
      axios.post(`${BASE_URL_EVENT_SERVICE}/events/${eventId}/register/${userId}`, {}, {  // Ensure the body is an empty object if required, instead of null.
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
        }
      })
          .then(() => {
            alert('You have successfully joined the event!');
            // Refresh event data by re-fetching the details
            this.fetchEventData();
            this.fetchParticipants();
          })
          .catch(error => {
            console.error("Error joining event:", error);
            alert('Failed to join the event.');
          });
    },
    toUnRegisterEvent() {
      const eventId = this.$route.params.eventId;
      const userId = sessionStorage.getItem('userId');
      axios.delete(`${BASE_URL_EVENT_SERVICE}/events/${eventId}/remove/${userId}`, {  // Ensure the body is an empty object if required, instead of null.
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
        }
      })
          .then(() => {
            alert('You have successfully unregistered the event!');
            // Refresh event data by re-fetching the details
            this.fetchEventData();
            this.fetchParticipants();
          })
          .catch(error => {
            console.error("Error unregistering event:", error);
            alert('Failed to unregister the event.');
          });
    }
  }
}
</script>
<style lang="scss" scoped>
.container {
  width: 80vw;
  max-width: 1200px;
  margin: auto;
  font-weight: bold;
}
.col-lg-7 {
  width: 75%;
}
@media (max-width: 768px) {
  .container {
    width: 95vw;
    padding: 10px;
  }
  .add-remove-card {
    font-size: 2em;
  }
}

i {
  margin-right: 1em;
}

.card-header {
  background: #2c3e50;
  padding: 20px 10px;
  width: 100%;
  justify-content: center;
}

.card-title {
  color: white;
  font-size: 3.5em;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  font-weight: bold;
}

.card {
  margin-top: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(44, 62, 80, 0.15);
  position: relative;
  overflow: hidden;
}

.card-body {
  text-align: left;
}

.card-text {
  font-size: 1.3em;
  text-align: left;
  align-items: center;
}
.label {
  font-weight: bold;
  font-size: 1em;
  color: #2c3e50;
  display: inline;
  margin-right: 0.8em;
  cursor: default;
}

.content {
  color: black;
  font-size: 1em;
  font-weight: bold;
  display: inline;
}
.badge {
  background: #2c3e50;
  color: white;
  margin-right: 0.5em;
  margin-bottom: 0.5em;
  border-radius: 5px;
  align-items: center;
}

.tags-container {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  margin-bottom: 10px;
  justify-content: flex-start;
}

.tag-badges {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  margin-left: 5px;
  margin-top: 5px;
}
.image-card {
  width: 9rem;
  height: 7rem;
  position: relative;
  margin: 0.5rem;
}

.card-img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 5px;
  transition: opacity 0.3s ease;
  border: 1px black solid;
}

.image-card:hover .card-img {
  opacity: 1;
}
.image-card.creator-mode:hover .card-img {
  opacity: 0.4; /* Apply opacity only if creator */
}
.image-card::after {
  content: '';
  display: none; /* Hide by default */
}
.image-card.creator-mode:hover::after {
  content: 'Delete';
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: 1.2em;
  color: #fff;
  text-shadow: 0 0 8px rgba(0, 0, 0, 0.6);
  opacity: 1;
  z-index: 100;
  cursor: pointer;
  display: block;
  color: rgba(255, 30, 0, 0.99);
  font-weight: bold;
}

.img-fluid {
  width: 100%;
  max-width: 100%;
  height: auto;
}
.clickable {
  cursor: pointer; /* Changes cursor to pointer to indicate it's clickable */
}
.btn {
  width: auto;
  font-size: 1em;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(44, 62, 80, 0.10);
  margin-top: 0.5em;
  margin-bottom: 0.2em;
  border: 1px solid #2c3e50;
  font-weight: bold;
}
</style>


