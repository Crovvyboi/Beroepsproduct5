<template>
    <div>
        <pre v-if="isAuthenticated">
          <h2>User Profile</h2>
          <code>{{ user.name}}</code>
          <code>{{ user.email }}</code>

          <logout />
        </pre>
    </div>
</template>
<script>
import { useAuth0 } from '@auth0/auth0-vue';
import Logout from './Logout.vue';

export default {
    name: 'App',
    components: {Logout},
    setup() {
        const { loginWithRedirect, user, isAuthenticated } = useAuth0();

        return {
            login: () => {
                loginWithRedirect();
            },
            user,
            isAuthenticated
        };
    }
};
</script>