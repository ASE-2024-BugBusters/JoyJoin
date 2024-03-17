import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import 'bulma/css/bulma.css';
import axios from 'axios';
createApp(App).use(router).mount('#app')
