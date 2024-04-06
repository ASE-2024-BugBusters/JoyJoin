<template>
  <PopupModal ref="popup" :custom_modal_width="custom_modal_width">
    <!--Below part will be rendered into PopUpModal <slot></slot> ...START-->
    <h5 class="modal-title">{{ title }}</h5>
    <p><font-awesome-icon  v-if="messageIcon.length" :icon="messageIcon" :style="{ color : messageIconColor}"/> {{ message }}</p>
    <div class="btns" v-if="yesButton && noButton">
      <button class="modal-btn no-btn" @click="_cancel">{{ noButton }}</button>
      <button class="modal-btn yes-btn" @click="_confirm">{{ yesButton }}</button>
    </div>
    <!--Will be rendered into PopUpModal <slot></slot> ...END-->
  </PopupModal>
</template>

<script>
import PopupModal from './PopupModal.vue'

export default {
  name: 'PopupContent',
  components: { PopupModal },
  data: () => ({
    // Parameters that change depending on the type of dialogue
    title: undefined,
    message: undefined, // Main text content
    messageIcon: [],
    messageIconColor: undefined,
    yesButton: undefined, // Text for confirm button; leave it empty because we don't know what we're using it for
    noButton: undefined, // text for cancel button

    // Private variables
    resolvePromise: undefined,
    rejectPromise: undefined,
    custom_modal_width: "width: 50%;",
  }),
  methods: {
    show(opts = {}) {
      this.title = undefined
      this.message = undefined
      this.messageIcon = []
      this.messageIconColor = undefined
      this.yesButton = undefined
      this.noButton = undefined
      this.resolvePromise = undefined
      this.rejectPromise = undefined

      this.title = opts.title
      this.message = opts.message
      if(opts.yesButton){
        this.yesButton = opts.yesButton
      }
      if (opts.noButton) {
        this.noButton = opts.noButton
      }
      if (opts.messageIcon) {
        this.messageIcon = opts.messageIcon
      }
      if(opts.messageIconColor){
        this.messageIconColor = opts.messageIconColor
      }
      // Once we set our config, we tell the popup modal to open
      this.$refs.popup.open()
      // Return promise so the caller can get results
      return new Promise((resolve, reject) => {
        this.resolvePromise = resolve
        this.rejectPromise = reject
      })
    },

    _confirm() {
      this.$refs.popup.close()
      this.resolvePromise(true)
    },

    _cancel() {
      this.$refs.popup.close()
      this.resolvePromise(false)
    },
  },
}
</script>

<style scoped>
.modal-title{
  margin-top: 0;
  margin-bottom: 5px;
  font-weight: bold;
}
.btns {
  display: flex;
  justify-content: center;
  align-content: center;
  margin-top: 20px;
}

.modal-btn{
  padding: 0.5em 1em;
  color: green;
  border: 1px solid green;
  border-radius: 5px;
  font-weight: bold;
  font-size: 14px;
  text-transform: uppercase;
  cursor: pointer;
  background-color: white;
  min-width: 100px;
}

.yes-btn {
  color: red;
  border: 1px solid red;
  margin-left: 15px;
}
.yes-btn:hover{
  background-color: red;
  color: white;
}

.no-btn {
  color: green;
  border: 1px solid green;
  margin-right: 15px;
}
.no-btn:hover{
  background-color:green;
  color:white;
}
</style>