<template>
  <PopupModal ref="popup" :custom_modal_width="custom_modal_width">
    <h4 class="page-header">{{ header }}</h4>
    <AllUsers :searchingUsers="users" :isAddOrEdit="false"></AllUsers>
  </PopupModal>
</template>

<script>
import PopupModal from '../Popup/PopupModal.vue'
import AllUsers from './AllUsers.vue'

export default {
    props: ['users', 'header'],
    components: {PopupModal, AllUsers},
    data() {
        return {
            custom_modal_width: "width: 40%",
        }
    },
    methods: {
        show(opts = {}) {
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
    }

}
</script>

<style>
.page-header {
  border-bottom:2px solid #D3D3D3;
  padding-top: 10px;
  padding-bottom: 10px;
  color: black;
  font-weight:bold;
}
</style>