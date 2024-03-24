<template>
  <div class="modal" v-show="isVisible">
    <div class="modal-content">
      <span class="close" @click="close">&times;</span>
      <h2>Publish Event</h2>
      <form @submit.prevent="submitEvent">
        <div>
          <label for="title">title:</label>
          <input type="text" id="title" v-model="event.title" required>
        </div>
        <div>
          <label for="time">time:</label>
          <input type="datetime-local" id="time" v-model="event.time" required>
        </div>
        <div>
          <label for="location">location:</label>
          <input type="text" id="location" v-model="event.location" required>
        </div>
        <div>
          <label for="participationLimit">participationLimit:</label>
          <input type="number" id="participationLimit" v-model="event.participationLimit" required>
        </div>
        <div>
          <label for="description">description:</label>
          <textarea id="description" v-model="event.description" required></textarea>
        </div>
        <div>
          <label for="tags">tags:</label>
          <input type="text" id="tags" v-model="tagString" placeholder="tag1,tag2">
        </div>
        <button type="submit">submit</button>
      </form>
    </div>
  </div>
</template>


<script>
import axios from 'axios';

export default {
  props: ['isVisible'],
  data() {
    return {
      event: {
        title: '',
        time: '', // This should be a string in the format 'YYYY-MM-DDTHH:MM:SS'
        location: '',
        participationLimit: null,
        description: '',
        tags: []
      },
      tagString: ''
    };
  },
  watch: {
    tagString(newVal) {
      this.event.tags = newVal.split(',').map(tag => tag.trim()).filter(tag => tag);
      console.log("isVisible changed to: ", newVal);
    }
  },
  methods: {
    close() {
      this.$emit('close');
    },
    submitEvent() {
      // 格式化时间为后端所需的格式 'YYYY-MM-DDTHH:MM:SS'
      this.event.time = new Date(this.event.time).toISOString();

      const apiUrl = 'http://localhost:8084/events';
      axios.post(apiUrl, this.event, {
        headers: {
          'Content-Type': 'application/json', // 明确设置Content-Type头
        }
      })
          .then(response => {
            console.log(response.data);
            this.close();
            this.resetForm();
          })
          .catch(error => {
            // 处理错误
            console.error("There was an error submitting the event:", error);
            if (error.response) {
              // 请求已发出，但服务器响应的状态码不在 2xx 范围内
              console.log(error.response.data);
              console.log(error.response.status);
              console.log(error.response.headers);
            } else if (error.request) {
              // 请求已发出，但没有收到响应
              console.log(error.request);
            } else {
              // 发送请求时出了点问题
              console.log('Error', error.message);
            }
          });

    },

    resetForm() {
      this.event = {
        title: '',
        time: '',
        location: '',
        participationLimit: null,
        description: '',
        tags: []
      };
      this.tagString = '';
    }
  }
}
</script>
<style>
.modal {
  position: fixed;
  z-index: 1;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgb(0,0,0);
  background-color: rgba(0,0,0,0.4);
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 80%;
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}
</style>