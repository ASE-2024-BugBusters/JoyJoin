<template>
  <transition name="fade">
    <div class="popup-modal" v-if="isVisible" @click.self="close">
      <div class="window" :style="custom_modal_width" >
        <div class="right-button-wrapper">
          <button class="btn-close" @click="close"></button>
        </div>
        <slot></slot>
      </div>
    </div>
  </transition>
</template>

<script>
export default {
  props: ["custom_modal_width"],
  name: 'PopupModal',
  data: () => ({
    isVisible: false,
  }),
  methods: {
    open() {
      this.isVisible = true
    },
    close() {
      this.isVisible = false
    },
  },
}
</script>

<style scoped>
/* css class for the transition */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s;
}
.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.popup-modal {
  background-color: rgba(0, 0, 0, 0.5);
  position: fixed;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 0.5rem;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 31;
  text-align: center;
  width: 100vw;
  height: 100vh;
}

.window {
  background: #fff;
  border-radius: 5px;
  box-shadow: 2px 4px 8px rgba(0, 0, 0, 0.2);
  min-width: 450px;
  margin-left: auto;
  margin-right: auto;
  padding: 1rem;
  width: 95vw;
}
.right-button-wrapper {
  min-width: 25px;
  display: flex;
  justify-content: flex-end;
}

</style>