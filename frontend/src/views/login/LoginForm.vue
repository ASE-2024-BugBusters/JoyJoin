<template>
  <div class="container">
    <div class="columns is-centered">
      <div class="column is-half">
        <h1 class="title">Login</h1>
        <form @submit.prevent="login">
          <div class="field">
            <label class="label">Username</label>
            <div class="control">
              <input class="input" type="text" v-model="email" placeholder="Enter your username">
            </div>
          </div>
          <div class="field">
            <label class="label">Password</label>
            <div class="control">
              <input class="input" type="password" v-model="password" placeholder="Enter your password">
            </div>
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

import {BASE_URL} from "../../../config/dev.env";
import axios from "axios";

export default {
  data() {
    return {
      email: '',
      password: ''
    };
  },
  methods: {
    async login() {
      const data = {
        email: this.email,
        password: this.password,
      }
      console.log(data)
      await axios.post(BASE_URL + "user-service/api/auth/login", data).then(response => {
        sessionStorage.setItem("jwtToken", response.data.token);
        this.$router.push({path: "/"});
      })
          .catch(error => {
            console.log("Error: ", error)
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
