<template>
  <div class="container">
    <div class="columns is-centered">
      <div class="column is-half">
        <h1 class="title">Login</h1>
        <form @submit.prevent="login">
          <div class="field">
            <label class="label">Email</label>
            <div class="control">
              <input class="input" type="text" v-model="email" placeholder="Enter your email">
            </div>
          </div>
          <div class="field">
            <label class="label">Password</label>
            <div class="control">
              <input class="input" type="password" v-model="password" placeholder="Enter your password">
            </div>
            <p class="help is-danger" v-if="loginFailed">{{ errorMessage }}</p>
          </div>
          <div class="field">
            <div class="control">
              <button class="button is-primary">Login</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>

import {BASE_URL_USER_SERVICE} from "../../../config/dev.env";
import axios from "axios";

export default {
  data() {
    return {
      email: "",
      password: "",
      errorMessage: "Email or password is wrong",
      loginFailed: false
    };
  },
  methods: {
    async login() {
      const data = {
        email: this.email,
        password: this.password,
      }
      await axios.post( BASE_URL_USER_SERVICE + "/auth/login", data).then(response => {
        sessionStorage.setItem("jwtToken", response.data.token);
        sessionStorage.setItem("userId", response.data.userId);
        this.$router.push({path: "/"});
      })
          .catch(error => {
            this.loginFailed = true;
          })
    }
  }
}
</script>

<style>
.container {
  margin-top: 50px;
}
</style>
