<template>
  <PopupModal ref="popup">
    <h2 class="page-header">Tag Event</h2>
    <div class="container">
      <!--All Events-->
      <div class="row">
        <div class="all-events-container">
          <EventList :isPostAddOrEdit="true" @addTaggedEvent="saveTagEvent"></EventList>
        </div>
      </div>

      <!--Clear Button-->
      <div class="row">
        <div class="d-flex justify-content-center align-items-center">
          <div class="save-btn" @click="saveTagEvent(null)">Clear Tagged Event</div>
        </div>
      </div>
    </div>
  </PopupModal>
</template>

<script>
import {toRaw} from 'vue'
import PopupModal from '@/components/Popup/PopupModal.vue'
import {BASE_URL_USER_SERVICE} from "../../../config/dev.env";
import axios from "axios";
import EventList from "@/views/event/EventList.vue";

export default {
  components: {EventList, PopupModal },
  data(){
    return {
      search: '',
    }
  },
  methods: {
    show(opts = {}) {
      this.search = '';

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
    saveTagEvent(_event) {
      this.$emit('saveTagEvent', _event)
    }
  },
}
</script>

<style scoped>
.all-events-container{
  overflow-y: auto;
  overflow-x: hidden;
  height: 68vh;
}
.page-header {
  border-bottom:2px solid #D3D3D3;
  padding-top: 10px;
  padding-bottom: 10px;
  color: black;
  font-weight:bold;
}
.subtitle{
  margin-bottom: 15px;
  margin-top: 5px;
}
/* Search Bar */
.search-container {
  position: relative;
  width: 100%;
  margin-bottom: 15px;
}
.search-icon {
  position: absolute;
  left: 30px;
  top: 50%;
  transform: translateY(-50%);
  color: gray;
}
.search-input {
  padding: 17px;
  padding-left: 55px;
  width: 95%;
  border: 1px solid #f5f5f5;
  font-size: 13px;
  color: gray;
  outline: 0;
}
.middle-border-line{
  border-left: 1px solid grey;
}
.save-btn{
  background: red;
  padding: 15px;
  border-radius: 10px;
  width: 60%;
  cursor: pointer;
  color: white;
  margin-top: 15px;
  text-align: center;
  z-index: 1;
  margin-bottom: 15px;
}

@media (max-width:767px){
  .middle-border-line {
    border-left: 0;
  }
  .save-btn{
    margin-top: 10px;
    width: 100%;
  }
}

</style>