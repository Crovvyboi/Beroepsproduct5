// main.js
import { createApp } from 'vue';
import App from './App.vue';
import router from './router';  // Importeer de router

import 'vuetify/styles';
import { createVuetify } from 'vuetify';
import * as components from 'vuetify/components';
import * as directives from 'vuetify/directives';
import '@mdi/font/css/materialdesignicons.css';

const vuetify = createVuetify({
  components,
  directives,
  icons: {
    defaultSet: 'mdi',
  }
});

const app = createApp(App);
app.use(vuetify);
app.use(router);  // Voeg de router toe aan de app
app.mount('#app');
