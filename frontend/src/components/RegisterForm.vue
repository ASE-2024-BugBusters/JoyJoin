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
              <input class="input" type="text" v-model="username" placeholder="Enter your username...">
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

export default {
  data() {
    return {
      showModal: false,
      firstName: '',
      lastName: '',
      birthDate: '',
      username: '',
      email: '',
      password: '',
      verifyPassword: ''
    };
  },
  methods: {
    async getUser() {
      const data = {
        tag:"Address"
      }
      await axios.post("http://localhost:9191/post-service/post", data)
          .then(response => {
            console.log(response.data)
          });
    },
    async register() {
      const data = {
        firstName: "Josip",
        lastName: "Harambasic",
        email: "harambasic.josip97@gto6o.com",
        userName: "Sini33zztttouuuo",
        password:"1234"
      };

      // TODO: check why CORS-Policy blocks post request with requestbody
      await axios.post("http://localhost:9191/user-service/api/user", data)
          .then(response => {
            console.log("Successfully registered:", response.data)
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
