<template>
  <div>
    <pre v-if="!isAuthenticated">
        <button @click="login">Log in</button>
      </pre>
    <!-- <button @click="login">Log in</button> -->
  </div>
</template>
<script>
import { useAuth0 } from '@auth0/auth0-vue';

export default {
  setup() {
    const { loginWithRedirect, isAuthenticated } = useAuth0();

    return {
      isAuthenticated,
      login: () => {
        loginWithRedirect();
      }
    };
  }
};
</script>