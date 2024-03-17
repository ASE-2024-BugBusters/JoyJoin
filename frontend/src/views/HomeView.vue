<template>
  <div class="home">
    <section class="hero is-dark">
      <div class="hero-body">
        <div class="container">
          <h1 class="title">Welcome to JoyJoin</h1>
          <h2 class="subtitle">
            Discover Exciting Events Nearby!
          </h2>
          <div class="button-block">
            <button class="button is-xl is-dark" @click="toggleModal">
              Sign Up to browse and register for Events
            </button>
            <!-- 添加发布按钮 -->
            <button class="button is-xl is-info" @click="toggleModal">Publish Event</button>
          </div>
        </div>
      </div>
    </section>
    <EventList/>
    <!-- 条件渲染 PublishEventModal 组件 -->
    <PublishEventModal :isVisible="showModal" @close="toggleModal"/>
  </div>
</template>


<script>
import EventList from "@/components/EventList.vue";
import PublishEventModal from "@/components/PublishEventModal.vue"; // 确保路径正确

export default {
  name: 'home',
  components: {
    EventList,
    PublishEventModal
  },
  data() {
    return {
      showModal: false // 用于控制模态显示的布尔值
    };
  },
  methods: {
    toggleModal() {
      this.showModal = !this.showModal;
      console.log("toggleModal called", this.showModal);
    }
  }
};

</script>
<style lang="scss" scoped>

.hero {
  text-align: center;
  background-image: url('https://cdn.auth0.com/blog/vue-meetup/event-banner.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  height: 400px;
}
.hero-body .title {
  text-shadow: 4px 4px 4px rgba(0, 0, 0, 0.6);
  padding: 40px 0 20px 0;
  font-size: 60px;
}
.subtitle {
  text-shadow: 4px 4px 4px rgba(0, 0, 0, 0.7);
  font-size: 30px;
}
.button-block {
  text-align: center;
  margin-left: auto;
  margin-right: auto;
  width: 100%;
  position: absolute;
  bottom: -150px;
  .button {
    margin-right: 50px;
    padding-left: 50px;
    padding-right: 50px;
  }
  .welcome {
    width: 400px;
    padding: 10px;
    margin-left: auto;
    margin-right: auto;
  }
}
.is-xl {
  font-size: 1.7rem;
}
</style>