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
              <p class="card-text"><i class="bi bi-calendar-check"></i><strong>Time:</strong> {{ formattedDateTime }}</p>
              <p class="card-text"><i class="bi bi-geo-alt-fill"></i><strong>Location:</strong> {{ event.location.street }} {{ event.location.number }}, {{ event.location.city }}</p>
              <p class="card-text"><i class="bi bi-people-fill"></i><strong>Participants:</strong> {{ event.participants.length }}/{{ event.participationLimit }}</p>
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
                <div class="card" style="width: 10rem;height: 8rem;" v-for="image in event.images" :key="image.key">
                  <img :src="getImageUrl(image)" :alt="`Event Image ${image.key}`" class="card-img">
                </div>
                <div class="card add-remove-card" style="width: 10rem; height: 8rem;" @click="toEditImage">
                  <div class="card-content">+</div>
                </div>
              </div>
              <div class="d-grid gap-2" v-if="!isCreator">
                <button class="btn btn-outline-primary" type="button" @click="toRegisterEvent" v-if="!isJoined & !isFullyOccupied">Join</button>
                <button class="btn btn-outline-warning" type="button" @click="toUnRegisterEvent" v-if="isJoined">Unregister</button>
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
      eventId: this.$route.params.eventId
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
      console.log("userId from sessionStorage:", userId); // Debugging
      console.log("Event creator ID:", this.event ? this.event.creatorId : "Event not loaded");
      return this.event && userId === this.event.creatorId;
    },
    isJoined() {
      const userId = sessionStorage.getItem('userId');
      console.log("userId from sessionStorage:", userId); // Debugging
      console.log("Event participants:", this.event ? this.event.participants : "Event not loaded");
      return this.event && this.event.participants.includes(userId);
    },
    isFullyOccupied() {
      return this.event.participationLimit = this.event.participants.length;
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
    async toDeleteEvent() {
      const eventId = this.$route.params.eventId;
      if (confirm('Are you sure you want to delete the event?')) {
        try {
          await axios.delete(`${BASE_URL_EVENT_SERVICE}/events/${eventId}`, {
            headers: {
              // 'Content-Type': 'application/json',
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
    async toEditImage() {
      const eventId = this.$route.params.eventId;
      await this.$router.push({name: "EditImage", params:{eventId}});
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
          })
          .catch(error => {
            console.error("Error unregistering event:", error);
            alert('Failed to unregister the event.');
          });
    }
  },
};
</script>
<style lang="scss" scoped>
.container {
  width: 80vw;
  max-width: 1200px;
  margin: auto;
  //padding: 20px;
  font-family: Arial, 'Roboto', sans-serif;
}

@media (max-width: 768px) {
  .container {
    width: 95vw;
    padding: 10px;
  }
  .add-remove-card {
    font-size: 2em; // Smaller plus sign on mobile devices
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
  font-size: 2.5em;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.card {
  margin-top: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(44, 62, 80, 0.15);
  position: relative;
  overflow: hidden;  /* hide the part over the card size */
  display: flex;
  align-items: center; /* vertically centered */
  justify-content: center; /* horizontally centered */
}

.card-body {
  //padding: 20px;
  text-align: left;
}
.card-text {
  font-size: 1.3em;
  margin-bottom: 10px;
  text-align: left;
}

.badge {
  background: #2c3e50;
  color: white;
  margin-right: 0.5em;
  margin-bottom: 0.5em;
  border-radius: 5px;
  white-space: nowrap;
  align-items: center; /* 垂直居中 */
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
;
}

.card-img {
  border-radius: 5px;
  width: 100%;    /* 默认宽度为容器宽度 */
  height: auto;   /* 高度自动，保持原始长宽比 */
  min-height: 100%; /* 确保图片至少和容器一样高 */
  object-fit: cover; /* 覆盖整个容器，多余的部分将被裁剪 */
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
  border: 1px solid #2c3e50;
}
.add-remove-card {
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 3em;  // 大号加号
  color: #2c3e50;  // 颜色匹配
  cursor: pointer;
  position: relative; // 为伪元素定位提供基准
  overflow: hidden;   // 隐藏溢出的文本

  .card-content {
    transition: opacity 0.3s ease, transform 0.3s ease; // 平滑变换
    opacity: 1; // 默认显示
  }

  &:hover .card-content {
    opacity: 0;  // 隐藏加号
    transform: scale(0.1); // 缩小加号
  }

  &:hover::after {
    content: 'Add/Remove';
    position: absolute;  // 绝对定位使文本居中
    top: 50%;  // 垂直居中
    left: 50%;  // 水平居中
    transform: translate(-50%, -50%); // 精确居中
    font-size: 0.4em;  // 文本大小适中
    font-weight: bold;
    color: #2c3e50;  // 文本颜色
    transition: opacity 0.3s ease; // 平滑显示
    opacity: 1; // 确保在hover时显示
    white-space: nowrap;  // 防止文本折行
  }
}

</style>

