<template>
  <main>
    <login />
    <profile />
    <adduser/>
    <!-- <pre v-if="isAuthenticated">
    <v-card>
      <v-tabs v-model="tab" bg-color="primary">
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
  </pre> -->
  </main>
</template>

<script>
import DailyRapport from './components/DailyRapport.vue';
import EntriesPage from './components/EntriesPage.vue';
import Login from './components/Login.vue';
import adduser from './components/addUser.vue';
// import Logout from './components/Logout.vue';
import Profile from './components/Profile.vue';
import { useAuth0 } from '@auth0/auth0-vue';

export default {
  name: 'App',
  components: { DailyRapport, EntriesPage, Login, Profile, adduser },
  data: () => ({
    tab: null,
  }),
  setup() {
    const { isAuthenticated, user } = useAuth0();

    return {
      user,
      isAuthenticated,
    };
  }
}
</script> 

