import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CreatePost from "../views/posts/CreatePost.vue";
import NotificationIcon from "@/components/Notification/NotificationIcon.vue";
import PostView from "@/views/posts/PostView.vue";
import ProfileView from "@/views/profile/ProfileView.vue";
import EditProfileView from "@/views/profile/EditProfileView.vue";
import UserAllPosts from "@/components/Posts/UserAllPosts.vue";

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
    path: "/event/:eventId",
    name: "EventView",
    component: () => import('../views/event/EventView.vue'),
    beforeEnter: (to, from, next) => {
      if (sessionStorage.getItem("jwtToken") && sessionStorage.getItem("userId")) {
        next();
      } else {
        next({name: 'home'})
      }
    }

  },
  {
    path: "/event/:eventId/edit",
    name: "EditEvent",
    component: () => import('../views/event/EditEvent.vue'),
    beforeEnter: (to, from, next) => {
      if (sessionStorage.getItem("jwtToken") && sessionStorage.getItem("userId")) {
        next();
      } else {
        next({name: 'home'})
      }
    }
  },
  {
    path: "/event/:eventId/image-edit",
    name: "EditImage",
    component: () => import('../views/event/ImageEdit.vue'),
    beforeEnter: (to, from, next) => {
      if (sessionStorage.getItem("jwtToken") && sessionStorage.getItem("userId")) {
        next();
      } else {
        next({name: 'home'})
      }
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
    component: CreatePost,
    beforeEnter: (to, from, next) => {
      if (sessionStorage.getItem("jwtToken") && sessionStorage.getItem("userId")) {
        next();
      } else {
        next({name: 'home'})
      }
    }
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
    path: "/post/:id",
    name: "post",
      component: PostView,
      beforeEnter: (to, from, next) => {
          if (sessionStorage.getItem("jwtToken") && sessionStorage.getItem("userId")) {
              next();
          } else {
              next({name: 'home'})
          }
      }
  },
  {
    path: "/profile",
    name: "profile",
    component: ProfileView,
    beforeEnter: (to, from, next) => {
      if (sessionStorage.getItem("jwtToken") && sessionStorage.getItem("userId")) {
        next();
      } else {
        next({name: 'home'})
      }
    }
  },
  {
    path: "/profile/edit",
    name: "EditProfile",
      component: () => import('../views/profile/EditProfileView.vue'),
      beforeEnter: (to, from, next) => {
          if (sessionStorage.getItem("jwtToken") && sessionStorage.getItem("userId")) {
              next();
          } else {
              next({name: 'home'})
          }
      }
  },
  {
    path: "/posts",
    name: "AllPosts",
    component: UserAllPosts
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
