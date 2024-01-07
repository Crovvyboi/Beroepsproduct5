<template>
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
