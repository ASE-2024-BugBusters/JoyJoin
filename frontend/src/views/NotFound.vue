<template>
  <div>
    <h2>
      <font-awesome-icon :icon="['fas', 'exclamation-triangle']" class="error-icon"/>
      {{ title }}
    </h2>
    <br>
    <h5>{{ message }}</h5>
    <br/>
    <br/>
    <router-link :to="{name: 'home'}">
      <div class="button is-primary">Return Home</div>
    </router-link>

  </div>
</template>
<script>
export default{
  props: ['pageTitle','pageMessage'],
  data() {
    return {
      title: this.pageTitle? this.pageTitle: "Error",
      message: this.pageMessage? this.pageMessage: "The page is not found."
    }
  }
}
</script>

<style scoped>
.error-icon{
  color: #d88d00;
}
</style>