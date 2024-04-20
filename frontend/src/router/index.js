import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CreatePost from "../views/posts/CreatePost.vue";
import NotificationIcon from "@/components/Notification/NotificationIcon.vue";
import PostView from "@/views/posts/PostView.vue";
import ProfileView from "@/views/profile/ProfileView.vue";
import EditProfileView from "@/views/profile/EditProfileView.vue";

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView
  },
  {
    path: '/about',
    name: 'about',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: function () {
      return import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
    }
  },
  {
    path: "/event/:id",
    name: "EventView",
    component: function () {
      return import('../views/event/EventView.vue')
    }
  },
  {
    path: "/login",
    name: "login",
    component: () => import('../views/login/LoginView.vue')
  },
  {
    path: "/register",
    name: "register",
    component: () => import('../views/register/RegisterView.vue')
  },
  {
    path: "/post",
    name: "post",
    component: CreatePost
  },
  {
    path: "/events/create",
    name: "createEvent",
    component: () => import('../views/event/CreateEvent.vue')
  },
  {
    path: "/notification",
    name: "notification",
    component: NotificationIcon
  },
  {
    path: "/postview",
    name: "postview",
    component: PostView
  },
  {
    path: "/profile",
    name: "profile",
    component: ProfileView
  },
  {
    path: "/profile/edit",
    name: "EditProfile",
    component: EditProfileView
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
