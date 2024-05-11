<template>
    <div v-if="visible" class="modal-overlay">
        <div class="modal-window">
            <button class="close-button" @click="closeModal">Close</button>
            <div class="user-list" v-if="users != null">
                <UserCard v-for="user in users" :key="user.id" :user="user" :action_text="action_text" @action="handleAction" />
                <div v-if="users == null || users.length == 0">No Users</div>
            </div>
        </div>
    </div>
</template>

<script>
import UserCard from './UserCard.vue';

export default {
    components: {
        UserCard
    },
    props: {
        visible: Boolean,
        users: Array,
        action_text: String,
    },
    methods: {
        closeModal() {
            this.$emit('close');
        },
        handleAction(user) {
            this.$emit('action', user);
        }
    }
}
</script>

<style scoped>
.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: rgba(0, 0, 0, 0.6);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.modal-window {
    background: white;
    padding: 20px;
    border-radius: 10px;
    width: 80%;
    max-width: 600px;
    max-height: 80%;
    overflow-y: auto;
}

.close-button {
    position: absolute;
    top: 10px;
    right: 10px;
    padding: 5px 10px;
    background-color: #ccc;
    border: none;
    cursor: pointer;
}

.user-list {
    display: flex;
    flex-direction: column;
}
</style>