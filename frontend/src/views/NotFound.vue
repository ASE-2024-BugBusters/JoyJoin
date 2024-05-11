<template>
  <div>
    <h2>
      <font-awesome-icon :icon="['fas', 'exclamation-triangle']" class="error-icon"/>
      Error 404
    </h2>
    <br>
    <h5>The page is not found.</h5>
    <br/>
    <br/>
    <router-link :to="{name: 'home'}">
      <div class="button is-primary">Return Home</div>
    </router-link>

  </div>
</template>

<style scoped>
.error-icon{
  color: #d88d00;
}
</style>
<script setup>
</script>