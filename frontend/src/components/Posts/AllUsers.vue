<template>
  <div class="all-users-container">
    <div v-if="searchingUsers.length"  >
        <div v-for="user in searchingUsers" :key="user.id">
            <div class="create-post-div">
                <div class="left-content navigate-info" @click="navigateToUserProfile(user.id)" title="Navigate to User Profile">
                    <!-- <img class="user-image" :src="user.image" >  -->
                    <img class="user-image" v-if="!user.avatar" src="../../assets/Default_User_Icon.png" alt="User Profile Picture">
                    <img class="user-image" v-else :src="user.avatar.urls[0].url" alt="User Profile Picture">
                    <div class="user-info">
                        <div class="username">{{ user.id }}</div>
                        <div class="bios">{{user.biography }}</div>
                    </div>
                </div>
                <div v-if="isAddOrEdit" class="right-content">
                    <img class="post-icon post-icon-right" src="../../assets/tag-icon.png" @click="addTaggedPeople(user)" alt="Tag Icon">
                </div>
            </div>
        </div>
    </div>
    <div v-else>
        Couldn't find anymore users.
    </div>
  </div>
</template>

<script>
export default {
    props: ['searchingUsers','isAddOrEdit'],
    methods: {
        addTaggedPeople(user){
            if(this.isAddOrEdit){
                this.$emit('addTaggedPeople', user)
            }
        },
      // Method: Navigate to user Profile
      navigateToUserProfile(userId){
        this.$router.push({name: 'profile', params:{"user_id": userId} });
      },
    },
    computed(){

    }
}
</script>

<style>
.all-users-container{
  overflow-y: auto;
  overflow-x: hidden;
  height: 60vh;
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
.right-content{
  min-width: 25px;
  margin-top: 5px;
}
.user-image {
  display: inline-block;
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin-right:15px;
  background-repeat: no-repeat;
  background-position: center center;
  background-size: cover;
  border: 1px solid grey;
  object-fit: cover;
}
.user-info {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
  text-align: left;
}
.username {
  font-weight: bold;
}
.bios {
  color: gray;
  font-size: 13px;
}
.post-icon {
  width: 25px;
  height: 25px;
  margin-right: 10px;
  cursor: pointer;
}
.post-icon-right{
  margin-right:0;
}
.navigate-info{
  cursor: pointer;
}
.navigate-info:hover{
  font-weight: bold;
}

</style>