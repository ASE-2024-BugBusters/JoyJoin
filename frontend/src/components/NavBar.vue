<template>
  <nav class="navbar container" role="navigation" aria-label="main navigation">
    <div class="navbar-brand">
      <a class="navbar-item" href="/">
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
        <router-link to="/" class="navbar-item">Home</router-link>
        <router-link to="/about" class="navbar-item">About</router-link>
        <router-link to="/profile" class="navbar-item">Profile</router-link>
      </div>
      <div class="navbar-end">
        <div class="navbar-item">
          <div class="buttons">
            <a class="button is-dark">
              <strong>Sign In</strong>
            </a>
            <a class="button is-dark" @click="getData">
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

export default {
  name: 'Nav',
  data() {
    return {
      responseData: null,
    }
  },
  methods: {
    async getData() {
      try {
        const response = await axios.get('http://localhost:9191/user-service/user');
        this.responseData = response.data;
        console.log(this.responseData)
      } catch (error) {
        console.error('Error fetching data:', error);
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