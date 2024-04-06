<template>
  <div>
    <h1>Tag people</h1>
    <input class="search-input" type="text" v-model="search" placeholder="Search for a person">

    <!--Tagged people-->
    <h4>Tags</h4>
    <div>
      <div v-if="taggedpeople.length">
        <div class="user-tag-container">
          <div v-for="user in taggedpeople" :key="user.username" class="user-tag-profile">
            <img class="remove-button" src="../../assets/cross-icon.png" @click="removeTaggedPeople(user)">
            <img class="tagged-userimage" src="../../assets/camera-icon.png" alt="User Image">
            <div class="tagged-username">{{ user.username }}</div>
          </div>
        </div>
      </div>
      <div v-else>
        There is no tags yet...
      </div>
    </div>

    <!--All users-->
    <h3>Users</h3>
    <div v-if="searchingUsers.length">
      <div v-for="user in searchingUsers" :key="user.username">
        <div class="create-post-div">
          <div class="left-content">
            <!-- <img class="user-image" :src="user.image" >  -->
            <img class="user-image" src="../../assets/camera-icon.png">
            <div class="user-info">
              <div class="username">{{ user.username }}</div>
              <div class="bios">{{ user.bios }}</div>
            </div>
          </div>
          <div class="right-content">
            <img class="post-icon post-icon-right" src="../../assets/tag-icon.png" @click="addTaggedPeople(user)">
          </div>
        </div>
      </div>
    </div>
    <div v-else>
      Couldn't find anymore users...
    </div>

    <br>
    <!--Save Button-->
    <div class="submit">Save Tags</div>
  </div>
</template>

<script>

import { computed, ref } from 'vue'

export default {
  name: 'PostTag',
  setup() {
    //Initialize ref
    const search = ref('')
    const taggedpeople = ref([])

    //Load all users information
    const users = ref([
      {
        "id": 1,
        "username": "adachannn",
        "bios": "Software Developer",
        "image": "require('@/assets/user_images/user1.jpg')"
      },
      {
        "id": 2,
        "username": "Zisen",
        "bios": "Hello everybody!",
        "image": "require('@/assets/user_images/user2.jpg')"
      }
    ])
    // const { users, error, load } = getUsers()
    // load()

    // Displaying the All-Users list: Accoding to the search + not in the taggedpeople_list
    const searchingUsers = computed(() => {
      return users.value.filter((user) => {
        return user.username.includes(search.value) &&
            !taggedpeople.value.some((taggedUser) => taggedUser.username === user.username)
      })
    })

    const addTaggedPeople = (user) => {
      taggedpeople.value.push(user)
    }

    const removeTaggedPeople = (user) => {
      taggedpeople.value.splice(taggedpeople.value.indexOf(user), 1)
    }

    return { users, searchingUsers, search, taggedpeople, addTaggedPeople, removeTaggedPeople }
  }
}
</script>

<style scoped>
.search-input {
  padding: 17px;
  padding-left: 55px;
  width: 450px;
  border: 1px solid #f5f5f5;
  font-size: 13px;
  color: gray;
  outline: 0;
}

.user-image {
  display: inline-block;
  width: 40px;
  height: 40px;
  border-radius: 50%;
  margin-right:15px;
  background-repeat: no-repeat;
  background-position: center center;
  background-size: cover;
  border: 1px solid grey;
}

.create-post-div {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 5px;
  margin: 10px auto;
  max-width: 500px;
  color: black;
  border-bottom: solid 1px #D3D3D3;
  cursor: default;
}
.left-content {
  display: flex;
  align-items: flex-start;
}
.user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.username {
  font-weight: bold;
}
.bios {
  color: gray;
  font-size: 13px;
}
.post-icon{
  cursor: pointer;
}


.user-tag-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
}

.user-tag-profile {
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100px;
}

.remove-button {
  position: absolute;
  top: 0;
  right: 0;
  width: 17px;
  height: 17px;
  border: none;
  border-radius: 50%;
  color: white;
  font-weight: bold;
  cursor: pointer;
}

.tagged-userimage {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid grey;
}

.tagged-username {
  margin-top: 5px; /* Adjust as needed for spacing between image and username */
  text-align: center;
}

.submit{
  background: #24a0ed;
  padding: 15px;
  border-radius: 10px;
  margin: 10px auto;
  max-width: 500px;
  cursor: pointer;
  color: white;
}

.post-icon {
  width: 25px;
  height: 25px;
  margin-right: 10px;
}
.post-icon-right{
  margin-right:0px;
}

</style>