<template>
    <font-awesome-icon :icon="['fas', 'share-from-square']" @click="showURLModal" class="share-icon" title="Share" />
    <PopupModal ref="popup" :custom_modal_width="custom_modal_width">
        <h3 class="page-header">Share</h3>
        <input class="share-url-input" type="text" v-model="url" readonly>
        <font-awesome-icon :icon="['fas', 'clone']" @click="copyURL" class="share-icon" :class="{'clicked-share-icon': sharebtn_clicked}" :title="sharebtn_clicked? 'Copied': 'Copy'"/>
        <p>Copy the link and forward to other people!</p>
    </PopupModal>
</template>

<script>
import PopupModal from '../Popup/PopupModal.vue'

export default {
    components: {PopupModal},
    data(){
        return {
            custom_modal_width: "width: 60%;",
            url: "",
            sharebtn_clicked: false,
        }
    },
    methods: {
        showURLModal(){
            this.sharebtn_clicked = false;
            this.$refs.popup.open()
            // Return promise so the caller can get results
            return new Promise((resolve, reject) => {
                this.resolvePromise = resolve
                this.rejectPromise = reject
            })
        },
        copyURL() {
            this.sharebtn_clicked = true;
            const el = document.createElement('textarea');
            el.value = this.url;
            document.body.appendChild(el);
            el.select();
            document.execCommand('copy');
            document.body.removeChild(el);
        }
    },
    mounted(){
        this.url = window.location.href;
    },
}
</script>

<style scoped>
.share-url-input{
    padding: 10px 25px;
    border: 1px dotted grey;
    border-radius: 3px;
    min-width: 300px;
    text-align: center;
}
.share-icon{
    margin-left:10px;
    cursor: pointer;
    width: 20px;
    height: 20px;
}
.clicked-share-icon{
    color: #42b983;
}

</style>