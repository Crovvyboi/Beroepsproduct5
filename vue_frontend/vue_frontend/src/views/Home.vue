<template>
  <main>
    <div class="center-container">

      <div style="margin-top: 3500px;">
        <div >
          <login />
          <profile />
          <adduser/>
        </div>
      </div>
      <div style="margin-top: 200px;">
        <div>
          <pre v-if="isAuthenticated">
            <v-card >
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
          </pre>
        </div>
      </div>
    </div>
  </main>
  </template>
  
  <style>
  .center-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    height: 90vh; /* Volledige hoogte van het scherm */
  }
  
  .center-image-container {
    text-align: center;
  }
  
  .center-image {
    max-width: 100%; /* Zorg ervoor dat de afbeelding niet breder is dan de container */
    max-height: auto; /* Zorg ervoor dat de afbeelding niet hoger is dan de container */
  }
  
  .clickable-wrapper {
    cursor: pointer;
    text-decoration: none; /* Verwijder onderstreepte stijl voor router-link */
    transition: width 0.3s; /* Voeg een overgang toe voor een vloeiende animatie */
  }
  
  .clickable-wrapper:hover {
    width: 10%; /* Verander de breedte wanneer de gebruiker er overheen zweeft */
  }
  </style>

<script>
import DailyRapport from '../components/DailyRapport.vue';
import EntriesPage from '../components/EntriesPage.vue';
import Login from '../components/Login.vue';
import adduser from '../components/addUser.vue';
// import Logout from './components/Logout.vue';
import Profile from '../components/Profile.vue';
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
  