import { initializeApp } from 'firebase/app';
import 'firebase/auth';
import 'firebase/firestore';

const firebaseConfig = {
    apiKey: "AIzaSyA0ofl9gBsGQdTdDIRo9GPxpGYwuFO2_zI",
    authDomain: "aquathermie-f518b.firebaseapp.com",
    projectId: "aquathermie-f518b",
    storageBucket: "aquathermie-f518b.appspot.com",
    messagingSenderId: "75189171983",
    appId: "1:75189171983:web:9c4c0c39c5dc165aca707a"
};

const firebaseApp = initializeApp(firebaseConfig);

export { firebaseApp };