import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import CreatePost from "../views/posts/CreatePost.vue";
import PostTag from "../views/posts/PostTag.vue";

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
    name: "eventSingle",
    component: () => import('../views/EventView.vue')
  },
  {
    path: "/post",
    name: "post",
    component: CreatePost
  },
  {
    path: "/posttag",
    name: "posttag",
    component: PostTag
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
