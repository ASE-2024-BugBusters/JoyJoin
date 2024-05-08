<template>
  <div class="all-users-container">
    <div v-if="taggedpeople.length" class="row row-cols-5">
        <div class="col justify-content-center align-items-center user-tag-container" v-for="user in taggedpeople" :key="user.username">
            <div class="tagged-user-container">
                <img class="tagged-userimage" v-if="!user.avatar" src="../../assets/Default_User_Icon.png" alt="User Profile Picture">
                <img class="tagged-userimage" v-else :src="user.avatar.urls[0].url" alt="User Profile Picture">
                <button v-if="isAddOrEdit" type="button" class="remove-button btn-close" @click="removeTaggedPeople(user)"></button>
            </div>
            <div class="tagged-username">{{ user.id }}</div>
        </div>
    </div>
    <div v-else>
        No user is tagged.
    </div>
  </div>
</template>

<script>
export default {
    props: ['taggedpeople', 'isAddOrEdit'],
    methods: {
        removeTaggedPeople(user){
            const index = this.taggedpeople.findIndex(taggeduser => taggeduser.id === user.id)
            if (index >= 0) {
                this.taggedpeople.splice(index, 1)
            }
        }
    }
}
</script>

<style scoped>
.all-users-container{
  overflow-y: auto;
  overflow-x: hidden;
  padding-top: 10px;
}
.user-tag-container {
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  align-items: flex-start;
  margin-bottom: 10px;
}
.tagged-user-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}
.tagged-userimage {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  border: 1px solid grey;
}
.tagged-username {
  margin-top: 5px; /* Adjust as needed for spacing between image and username */
  text-align: center;
  min-width: 50px;
}
.remove-button{
  position: absolute;
  width: 7px;
  height: 7px;
  top: -7px;
  right: -7px;
  background-color: lightgrey;
  opacity: 0.8;
  border: none;
  padding: 5px;
  border-radius: 50%;
  font-size: 9px;
  cursor: pointer;
}

</style>