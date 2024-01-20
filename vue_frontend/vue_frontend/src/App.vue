<!-- App.vue -->
<template>
  <v-app id="inspire">
    <v-navigation-drawer v-model="drawer" class="bg-blue">
      <v-list-item title="Menu" subtitle="Informatica AD"></v-list-item>
      <v-divider></v-divider>
      <router-link to="/home" class="router-link">
        <v-list-item link title="Home"></v-list-item>
      </router-link> 
      <router-link to="/actuele-data" class="router-link">
        <v-list-item link title="Actuele Data"></v-list-item>
      </router-link>
      <router-link to="/historie" class="router-link">
        <v-list-item link title="Historie"></v-list-item>
      </router-link>
               
    </v-navigation-drawer>

    <v-app-bar class="bg-blue-lighten-1">
      <v-app-bar-nav-icon @click="drawer = !drawer">
        <v-icon icon="mdi-menu" size="large"></v-icon>
      </v-app-bar-nav-icon>

      <v-app-bar-title>Aquathermie</v-app-bar-title>
    </v-app-bar>

    <v-main class="bg-red-lighten-5">
      <router-view></router-view>
    </v-main>
  </v-app>
</template>

<script>
import { ref } from 'vue';

const drawer = ref(null);

export default {
  name: 'App',
  data() {
    return {
      drawer: null
    };
  }
};
</script>

<style scoped>
/* Voeg aangepaste stijlen toe voor router-links */
.router-link  {
  text-decoration: none !important;
  color: white !important;
  font-weight: bold !important;
}
</style>
