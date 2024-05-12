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
        <router-link v-if="jwt" :to="userProfilePath()" class="navbar-item noTextDecoration" style="text-decoration: none">Profile</router-link>
        <router-link v-if="jwt" to="/post/create" class="navbar-item noTextDecoration" style="text-decoration: none">Create New Post</router-link>
        <router-link v-if="jwt" to="/events/create" class="navbar-item noTextDecoration" style="text-decoration: none">Publish Event</router-link>
      </div>
      <div class="navbar-end">
        <div v-if="!jwt" class="navbar-item">
          <div class="buttons noTextDecoration">
            <a class="button is-dark" style="text-decoration: none" @click="toRegisterPage">
              <strong>Register</strong>
            </a>
            <a class="button is-dark noTextDecoration" style="text-decoration: none" @click="toLoginPage">
              <strong>Login</strong>
            </a>
          </div>
        </div>
        <div v-else class="navbar-item">
          <div class="buttons noTextDecoration">
            <a class="button is-dark" style="text-decoration: none" @click="logout">
              <strong>Logout</strong>
            </a>
          </div>
        </div>
      </div>
    </div>
  </nav>
</template>
<script>
import axios from "axios";
import {BASE_URL} from "../../config/dev.env";

export default {
  name: 'Nav',
  data() {
    return {
      responseData: null,
      jwt: false
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
    },

    async logout() {
      await axios.post(BASE_URL + "user-service/api/auth/logout", {}, {
        headers: {
          "Authorization": `Bearer ${sessionStorage.getItem("jwtToken")}`
        }
      }).then(response => {
        sessionStorage.removeItem("jwtToken");
        sessionStorage.removeItem("userId");
        this.checkJwt();
        this.$router.push({path: "/"});
      }).catch(error => {
            console.log("Error: ", error);
      })
    },

    checkJwt() {
      this.jwt = (sessionStorage.getItem("jwtToken") !== null || sessionStorage.getItem("userId") !== null);
    },

    userProfilePath() {
      return `/profile/${sessionStorage.userId}`;
    }
  },
  watch: {
    $route() {
      this.checkJwt();
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