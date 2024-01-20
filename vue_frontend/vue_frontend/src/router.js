// router.js
import { createRouter, createWebHistory } from 'vue-router';
import ActueleData from '@/views/ActueleData.vue';
import Historie from '@/views/Historie.vue';
import Home from '@/views/Home.vue';


const routes = [
  {
    path: '/actuele-data',
    name: 'ActueleData',
    component: ActueleData
  },
  {
    path: '/historie',
    name: 'Historie',
    component: Historie
  },
  {
    path: '/home',
    name: 'Home',
    component: Home
  }
  // Voeg andere routes toe zoals nodig
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
