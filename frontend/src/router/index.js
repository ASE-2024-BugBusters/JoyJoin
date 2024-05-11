import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import NotificationIcon from "@/components/Notification/NotificationIcon.vue";
import ProfileView from "@/views/profile/ProfileView.vue";
import NotFound from "../views/NotFound.vue";

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
    path: "/event/:eventId/edit-image",
    name: "EditImage",
    component: () => import('../views/event/EditImage.vue'),
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
    path: "/post/create",
    name: "createPost",
    component: () => import('../views/posts/CreatePost.vue'),
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
    component: () => import('../views/event/CreateEvent.vue'),
    beforeEnter: (to, from, next) => {
      if (sessionStorage.getItem("jwtToken") && sessionStorage.getItem("userId")) {
        next();
      } else {
        next({name: 'home'})
      }
    }
  },
  {
    path: "/notification",
    name: "notification",
    component: NotificationIcon
  },
  {
    path: "/post/:id",
    name: "post",
    component: () => import('../views/posts/PostView.vue'),
    beforeEnter: (to, from, next) => {
      if (sessionStorage.getItem("jwtToken") && sessionStorage.getItem("userId")) {
        next();
      } else {
        next({name: 'home'})
      }
    }
  },
  {
    path: "/profile/:user_id",
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
    name: "posts",
    component: () => import('../components/Posts/UserAllPosts.vue'),
    beforeEnter: (to, from, next) => {
      if (sessionStorage.getItem("jwtToken") && sessionStorage.getItem("userId")) {
        next();
      } else {
        next({name: 'home'})
      }
    }
  },
    //Catch all Error Page
  {
    path: "/:catchAll(.*)",
    name: "NotFound",
    component: NotFound
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
