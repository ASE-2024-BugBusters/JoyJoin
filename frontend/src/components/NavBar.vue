<template>
  <nav class="navbar container" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
      <a class="navbar-item" href="/" style="text-decoration: none">
        <strong class="is-size-4">JoyJoin</strong>
      </a>
      <a
          role="button"
          class="navbar-burger burger"
          aria-label="menu"
          aria-expanded="false"
          data-target="navbarBasicExample"
      >
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
        <span aria-hidden="true"></span>
      </a>
    </div>
    <div id="navbar" class="navbar-menu">
      <div class="navbar-start">
        <router-link to="/" class="navbar-item" style="text-decoration: none">Home</router-link>
        <router-link to="/about" class="navbar-item noTextDecoration" style="text-decoration: none">About</router-link>
        <router-link to="/profile" class="navbar-item noTextDecoration" style="text-decoration: none">Profile</router-link>
        <router-link to="/post" class="navbar-item noTextDecoration" style="text-decoration: none">Create Post</router-link>
        <router-link to="/postview" class="navbar-item noTextDecoration" style="text-decoration: none">Specific Post</router-link>
      </div>
      <div class="navbar-end">
        <div class="navbar-item">
          <div class="buttons noTextDecoration">
            <NotificationIcon></NotificationIcon>
            <a class="button is-dark" style="text-decoration: none" @click="toRegisterPage">
              <strong>Sign In</strong>
            </a>
            <a class="button is-dark noTextDecoration" style="text-decoration: none" @click="toLoginPage">
              <strong>Login</strong>
            </a>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>
<script>
import axios from "axios";
import NotificationIcon from "@/components/Notification/NotificationIcon.vue";

export default {
  name: 'Nav',
  components: {NotificationIcon},
  data() {
    return {
      responseData: null,
    }
  },
  methods: {
    async toLoginPage() {
      try {
        await this.$router.push({path: "/login"});
      } catch (error) {
        console.error('Error navigating to login page:', error);
      }
    },

    async toRegisterPage() {
      try {
        await this.$router.push({path: "/register"});
      } catch (error) {
        console.error('Error navigating to login page:', error);
      }
    }
  }
};
</script>
<style lang="scss" scoped>
nav {
  margin-top: 25px;
  margin-bottom: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #d88d00;
    }
  }
}

</style>