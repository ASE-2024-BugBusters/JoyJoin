<template>
  <div class="card">
    <img class="card-img-top" :src="imageUrl" alt="Event Image" @click="emitClickEvent">
    <div class="card-body">
      <h4 class="card-title">{{ event.title }}</h4>
    </div>
    <ul class="list-group list-group-flush">
      <li class="list-group-item">{{ formattedDateTime }}</li>
      <li class="list-group-item">{{ event.location.street }} {{ event.location.number }}, {{ event.location.city }} {{ event.location.postalCode }}</li>
    </ul>
    <div class="card-body">
      <span @click="" class="card-link"><i class="bi bi-bookmark-plus-fill"></i></span>
      <span @click="copyLink" class="card-link"><i class="bi bi-share-fill"></i></span>
    </div>
  </div>
</template>

<script>
export default {
  props: ['event'],
  computed: {
    imageUrl() {
      return this.event.images && this.event.images.length > 0
        ? this.event.images[0].urls[0].url
        : require('@/assets/event_default.jpeg');
    },
    formattedDateTime() {
      const date = new Date(this.event.time);
      const dateString = date.toLocaleDateString('en-US', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'short' });
      const timeString = date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', hour12: false });
      return `${dateString} AT ${timeString}`;
    },
    shareUrl() {
      const baseUrl = window.location.origin;
      return `${baseUrl}/event/${this.event.eventId}`;
    }
  },
  methods: {
    emitClickEvent() {
      this.$emit('image-clicked', this.event.eventId);
    },
    copyLink() {
      if (navigator.clipboard) {
        navigator.clipboard.writeText(this.shareUrl)
            .then(() => {
              alert('Share link is copied to the clipboard!');
            })
            .catch(err => {
              console.error('Fail to copy link to clipboard: ', err);
            });
      } else {
        const textArea = document.createElement('textarea');
        textArea.value = this.shareUrl;
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();
        try {
          document.execCommand('copy');
          alert('Share link is copied to the clipboard!');
        } catch (err) {
          console.error('Fail to copy link to clipboard: ', err);
        }
        document.body.removeChild(textArea);
      }
    }
  }
};
</script>

<style scoped>
  .card {
    border: 1.2px solid rgba(0, 0, 0, 0.37);
    border-radius: 10px;
    overflow: hidden;
    margin-bottom: 25px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    transition: box-shadow 0.3s ease;
    font-weight: bold;
    width: 900px;
    height: auto;
  }
  .card:hover {
    box-shadow: 0 5px 15px rgba(0, 0, 0, 0.8);
  }
  img:hover {

   cursor: pointer;
  }
  .card-title {
    font-size: 1.5em;
    font-weight: 1000;
    margin: 0;
  }
  .card-img-top {
  width: 100%;
  display: block;
  height: 200px;
  object-fit: cover;
  border-bottom: solid black 0.5px;
  }
  i {
    margin: 2em;
    cursor: pointer;
  }
  .card-link > i {
    font-size: 1.4em;
  }
</style>

