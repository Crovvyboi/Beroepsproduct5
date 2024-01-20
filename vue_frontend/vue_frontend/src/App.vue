<!-- App.vue -->
<template>
<<<<<<< Updated upstream
  <main>
    <v-card>
      <v-tabs
        v-model="tab"
        bg-color="primary"
      >
        <v-tab value="one">Daily Raport</v-tab>
        <v-tab value="two">Entries</v-tab>
      </v-tabs>

      <v-card-text>
        <v-window v-model="tab">
          <v-window-item value="one">
            <DailyRapport />
          </v-window-item>

          <v-window-item value="two">
            <EntriesPage />
          </v-window-item>
        </v-window>
      </v-card-text>
    </v-card>   
  </main>  
</template>

<script>
  import DailyRapport from './components/DailyRapport.vue';
  import EntriesPage from './components/EntriesPage.vue'

  export default{
    name: 'App',
    components: {DailyRapport, EntriesPage},
    data: () => ({
      tab: null,
    }),
  }
</script>

<!-- <style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style> -->
=======
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
>>>>>>> Stashed changes
