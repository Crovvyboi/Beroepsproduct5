<template>
  <div>
    <!-- Display Medewerkers List if authenticated -->
    <div v-if="isAuthenticated && hasPermission">
      <h1>Medewerkers</h1>
      <table>
        <thead>
          <tr>
            <th>Werknemer</th>
            <th>Email</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="medewerker in medewerkers" :key="medewerker.id">
            <td>{{ medewerker.werknemer }}</td>
            <td>{{ medewerker.email }}</td>
            <td>
              <button @click="disableUser(medewerker.id)">Disable</button>
              <button @click="updateUser(medewerker.id)">Update</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<style>
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 10px;
  /* Adjust the margin-top value as needed */
}

th,
td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: left;
}

th {
  background-color: #f2f2f2;
}

button {
  margin-left: 5px;
  /* Adjust the margin-left value as needed */
}
</style>

<script>
// UsersList.vue
import { ref, onMounted } from 'vue';
import { getFirestore, collection, getDocs } from 'firebase/firestore';
import { firebaseApp } from '../firebase'; // Update the path accordingly
import { useAuth0 } from '@auth0/auth0-vue';
import { checkPermissionByEmail } from './firestore_checkemail';


export default {
  setup() {
    const medewerkers = ref([]);
    const { user, isAuthenticated } = useAuth0();
    const userEmail = ref(user?.email || '');
    const hasPermission = ref(false);

    onMounted(async () => {
      try {
        // Fetch Medewerkers List
        const firestore = getFirestore(firebaseApp);
        const medewerkersCollection = collection(firestore, 'users');
        const medewerkersSnapshot = await getDocs(medewerkersCollection);

        medewerkers.value = medewerkersSnapshot.docs.map((doc) => ({
          id: doc.id,
          ...doc.data(),
        }));

        // Check Permission by Email
        if (isAuthenticated.value && user.value && user.value.email) {
          userEmail.value = user.value.email;

          // Call the checkPermissionByEmail function
          hasPermission.value = await checkPermissionByEmail(userEmail.value);
        } else {
          console.warn('User is not authenticated or user details are not available.');
        }
      } catch (error) {
        console.error('Error:', error.message);
      }
    });

    return { medewerkers, user, isAuthenticated, userEmail, hasPermission };
  },
};
</script>

