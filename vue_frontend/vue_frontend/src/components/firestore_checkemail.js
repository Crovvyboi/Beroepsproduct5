// FirestoreService.js
import { getFirestore, collection, query, where, getDocs } from 'firebase/firestore';
import { firebaseApp } from '../firebase';

export async function checkPermissionByEmail(email) {
  const firestore = getFirestore(firebaseApp);
  const usersCollection = collection(firestore, 'users');

  const q = query(usersCollection, where('email', '==', email));
  const querySnapshot = await getDocs(q);

  return !querySnapshot.empty;
}