<template>
  <div class="container">
    <div class="columns is-centered">
      <div class="column is-half">
        <h1 class="title">Login</h1>
        <form @submit.prevent="login">
          <div class="field">
            <label class="label">Username</label>
            <div class="control">
              <input class="input" type="text" v-model="username" placeholder="Enter your username">
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

export default {
  data() {
    return {
      username: '',
      password: ''
    };
  },
  methods: {
    login() {
      // Implement login logic here
      console.log('Username:', this.username);
      console.log('Password:', this.password);
    }
  }
}
</script>

<style>
.container {
  margin-top: 50px;
}
</style>
