<template>
  <PopupModal ref="popup">
    <h2 class="page-header">Tag people</h2>
    <div class="container">
      <div class="row">
        <!--Left section: Tagged people-->
        <div class="col-md-6">
          <h4 class="subtitle">Tags</h4>
          <TaggedUsers :isAddOrEdit="true" :taggedpeople="temp_taggedpeople"></TaggedUsers>
        </div>

        <!--Right section: Search all users-->
        <div class="col-md-6 middle-border-line">
          <h4 class="subtitle">All Users</h4>
          <div class="search-container">
            <font-awesome-icon :icon="['fas', 'search']" class="search-icon" />
            <input class="search-input" type="text" v-model="search" placeholder="Search for a person">
          </div>
          <AllUsers :isAddOrEdit="true" :searchingUsers="searchingUsers" @addTaggedPeople="addUserIntoTaggedList" ></AllUsers>
        </div>
      </div>

      <!--Save Button-->
      <div class="row">
        <div class="col-md-6 d-flex justify-content-center align-items-center">
          <div class="save-btn" @click="saveTags">Save Tags</div>
        </div>
      </div>
    </div>
  </PopupModal>
</template>

<script>
import {toRaw} from 'vue'
import TaggedUsers from '../../components/Posts/TaggedUsers.vue'
import AllUsers from '../../components/Posts/AllUsers.vue'
import PopupModal from '@/components/Popup/PopupModal.vue'
import {BASE_URL_USER_SERVICE} from "../../../config/dev.env";
import axios from "axios";

export default {
  components: { TaggedUsers, AllUsers, PopupModal },
  data(){
    return {
      search: '',
      users: [],
      temp_taggedpeople: []
    }
  },
  created(){
    this.getAllUsersAPI();
  },
  computed: {
    searchingUsers() {
      if(this.users) {
        return this.users.filter(user => {
          return user.id.includes(this.search) &&
              !this.temp_taggedpeople.some(taggedUser => taggedUser.id === user.id);
        });
      }
    }
  },
  methods: {
    // Method: Get All User Information
    async getAllUsersAPI() {
      const getAllUsersUrl = BASE_URL_USER_SERVICE + "/user" ;
      await axios.get(getAllUsersUrl, {
        headers: {
          'Authorization': `Bearer ${sessionStorage.getItem("jwtToken")}`
        }
      })
          .then(response => {
            this.users = response.data;
          })
          .catch(error => {
            console.error("[getAllUsersAPI] There was an error getting the all users:", error);
          });
    },
    // Method: Add user into temporarily TaggedUser_list
    addUserIntoTaggedList(user){
      this.temp_taggedpeople.push(user)
    },
    show(opts = {}) {
      this.search = '';
      if(opts.taggedpeople){
        this.temp_taggedpeople = structuredClone(toRaw(opts.taggedpeople))
      }

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
    saveTags() {
      this.$emit('saveTags', this.temp_taggedpeople)
    }
  },
}
</script>

<style scoped>
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
  background: #24a0ed;
  padding: 15px;
  border-radius: 10px;
  width: 90%;
  cursor: pointer;
  color: white;
  margin-top: -60px;
  text-align: center;
  z-index: 1;
  margin-bottom: 10px;
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