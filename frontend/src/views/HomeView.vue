<template>
  <div class="home">
    <section class="hero is-dark">
      <div class="hero-body">
        <div class="container">
          <h1 class="title">Welcome to JoyJoin</h1>
          <h2 class="subtitle">
            Discover Exciting Events Nearby!
          </h2>
          <div class="button-block" v-if="!isLoggedIn">
            <button class="button is-xl is-dark" @click="toLoginPage">
              Sign Up to browse and register for Events
            </button>
          </div>
        </div>
      </div>
    </section>

    <h2 class="event-title" v-if="jwt">Check out our upcoming events</h2>
    <EventList/>
  </div>
</template>

<script>
import EventList from "@/views/event/EventList.vue";

export default {
  name: 'home',
  components: {
    EventList
  },
  data() {
    return {
      jwt: (sessionStorage.getItem("jwtToken") !== null || sessionStorage.getItem("userId") !== null),
    }
  },
  computed: {
    isLoggedIn() {
      // Properly check if both JWT token and user ID exist in sessionStorage
      return sessionStorage.getItem("jwtToken") !== null && sessionStorage.getItem("userId") !== null;
    }
  },
  methods: {
    toLoginPage() {
      try {
        this.$router.push({path: "/login"});
      } catch (error) {
        console.error('Error navigating to login page:', error);
      }
    }
  },
};
</script>

<style lang="scss" scoped>
.hero {
  text-align: center;
  background-image: url('https://cdn.auth0.com/blog/vue-meetup/event-banner.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  height: 400px;
}

.hero-body .title {
  text-shadow: 4px 4px 4px rgba(0, 0, 0, 0.6);
  padding: 40px 0 20px 0;
  font-size: 60px;
}

.subtitle {
  text-shadow: 4px 4px 4px rgba(0, 0, 0, 0.7);
  font-size: 30px;
}

.button-block {
  text-align: center;
  position: absolute;
  bottom: -150px;
  width: 100%;

  .button {
    margin-right: 50px;
    padding-left: 50px;
    padding-right: 50px;
  }
}

.is-xl {
  font-size: 1.7rem;
}

.event-title {
  margin-top: 60px;
  text-align: center;
}
</style>

