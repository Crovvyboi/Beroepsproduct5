import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'

import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { createAuth0 } from '@auth0/auth0-vue';
import { firebaseApp } from './firebase';

// const vuetify = createVuetify({
//   components,
//   directives,
// })

const app = createApp(App);
app.use(
  createAuth0({
    domain: "dev-dvcmi3f0j8fjslxr.us.auth0.com",
    clientId: "EOEZz7tJt1hEt1DQuEECGFj5zBhXhvIj",
    authorizationParams: {
      redirect_uri: window.location.origin
    }
  })
);

app.use(
  createVuetify({
    components,
    directives,
  })
);


app.mount('#app');
// createApp(App).use(vuetify).mount('#app')
