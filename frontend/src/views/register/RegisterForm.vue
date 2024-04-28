<template>
  <!--  <div>-->
  <!--    &lt;!&ndash; Button to open the modal &ndash;&gt;-->
  <!--    <button class="button" @click="showModal = true">Register</button>-->

  <!--    &lt;!&ndash; Modal &ndash;&gt;-->
  <!--    <div class="modal" :class="{ 'is-active': showModal }">-->
  <!--      <div class="modal-background"></div>-->
  <!--      <div class="modal-content">-->
  <!--        <div class="box">-->
  <!--          <p>Congratulations! Registration Successful.</p>-->
  <!--          &lt;!&ndash; Additional content or styling can be added here &ndash;&gt;-->
  <!--        </div>-->
  <!--      </div>-->
  <!--      <button class="modal-close is-large" aria-label="close" @click="showModal = false"></button>-->
  <!--    </div>-->
  <!--  </div>-->
  <div class="container">
    <div class="columns is-centered">
      <div class="column is-half">
        <h1 class="title">Registration</h1>
        <form @submit.prevent="register">
          <div class="field">
            <label class="label">First Name</label>
            <div class="control">
              <input class="input" type="text" v-model="firstName" placeholder="Enter your first name...">
            </div>
          </div>
          <div class="field">
            <label class="label">Last Name</label>
            <div class="control">
              <input class="input" type="text" v-model="lastName" placeholder="Enter your last name...">
            </div>
          </div>
          <div class="field">
            <label class="label">Birthdate</label>
            <div class="control">
              <input class="input" type="date" v-model="birthDate">
            </div>
          </div>
          <div class="field">
            <label class="label">Username</label>
            <div class="control">
              <input class="input" type="text" v-model="username" placeholder="Enter your account name...">
            </div>
          </div>
          <div class="field">
            <label class="label">Email</label>
            <div class="control">
              <input class="input" type="text" v-model="email" placeholder="Enter your email...">
            </div>
          </div>
          <div class="field">
            <label class="label">Password</label>
            <div class="control">
              <input class="input" type="password" v-model="password" placeholder="Enter your password...">
            </div>
          </div>
          <div class="field">
            <label class="label">Verify Password</label>
            <div class="control">
              <input class="input" type="password" v-model="verifyPassword" placeholder="Verify your password...">
            </div>
            <p class="help is-danger" v-if="verifyPasswordError">{{ verifyPasswordErrorMessage }}</p>
          </div>
          <div class="field">
            <div class="control">
              <button class="button is-primary">Register</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import {BASE_URL, BASE_URL_USER_SERVICE} from "../../../config/dev.env";

export default {
  data() {
    return {
      showModal: false,
      firstName: "",
      lastName: "",
      birthDate: "",
      username: "",
      usernameError: "",
      email: "",
      password: "",
      verifyPassword: "",
      verifyPasswordError: false,
      verifyPasswordErrorMessage: "The password and it's verification need to be identical"
    };
  },
  methods: {
    async register() {
      if (this.password === this.verifyPassword) {
        this.verifyPasswordError = false;

        const data = {
          firstName: this.firstName,
          lastName: this.lastName,
          email: this.email,
          userName: this.username,
          password: this.password,
          birthDate: this.birthDate
        };
        
        await axios.post(BASE_URL_USER_SERVICE + "/auth/register", data).then(response => {
              this.$router.push({path: "/"});
            })
            .catch(error => {
              console.log("Error: ", error)
            })
      } else {
        this.verifyPasswordError = true;
      }
    }

  }
}
</script>

<style>
.container {
  margin-top: 50px;
}
</style>
