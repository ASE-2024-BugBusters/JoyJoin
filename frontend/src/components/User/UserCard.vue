<template>
    <div class="user-card">
        <img :src="avatar_url()" :alt="user.nickname" class="avatar">
        <div class="user-info">
            <h3>{{ user.nickname }}</h3>
        </div>
        <button @click="action" class="action-button"> {{ action_text }}</button>
    </div>
</template>

<script>
export default {
    props: {
        user: {
            type: Object,
            required: true,
        },
        action_text: {
            type: String,
            required: true,
        }
    },
    methods: {
        avatar_url() {
            const default_avatar_url = "https://www.gravatar.com/avatar/2c7d99fe281ecd3bcd65ab915bac6dd5?s=250";
            if (!this.user || !this.user.avatar || !this.user.avatar.urls || !this.user.avatar.urls[0] || !this.user.avatar.urls[0].url) {
                return default_avatar_url;
            }
            return this.user.avatar.urls[0].url;
        },
        action() {
            this.$emit('action', this.user);
        }
    }
}
</script>

<style scoped>
.user-card {
    display: flex;
    align-items: center;
    margin: 10px;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

.avatar {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    margin-right: 15px;
}

.user-info h3 {
    margin: 0;
    font-size: 16px;
}

.user-info p {
    margin: 0;
    color: #666;
}

.unfollow-button {
    margin-left: auto;
    padding: 5px 10px;
    background-color: #f44336;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}
</style>