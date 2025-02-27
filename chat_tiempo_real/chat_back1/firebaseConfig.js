// firebaseConfig.js
const admin = require('firebase-admin');
const { initializeApp } = require('firebase/app');
const { getAuth } = require('firebase/auth');
const serviceAccount = require('./chattiemporeal-e454e-firebase-adminsdk-fbsvc-efc0548f32.json');

// Inicializar Firebase Admin SDK
admin.initializeApp({
    credential: admin.credential.cert(serviceAccount),
});


const firebaseConfig = {
    apiKey: "AIzaSyCcZaNvJShR6G0UNkrmN3s2PZ9duK8nvC0",
    authDomain: "chattiemporeal-e454e.firebaseapp.com",
    projectId: "chattiemporeal-e454e",
    storageBucket: "chattiemporeal-e454e.firebasestorage.app",
    messagingSenderId: "434118252750",
    appId: "1:434118252750:web:b76d93fdbf183eada2989f",
    measurementId: "G-H77MR2NKE8"
  };
  
  const firebaseApp = initializeApp(firebaseConfig);
  const auth1 = getAuth(firebaseApp);
  const auth=admin.auth();
const db = admin.firestore(); // Inicializa Firestore


// Exporta las instancias de Firestore y Auth
module.exports = { db, auth };