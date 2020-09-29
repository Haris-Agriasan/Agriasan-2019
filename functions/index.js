// The Cloud Functions for Firebase SDK to create Cloud Functions and setup triggers.
const functions = require('firebase-functions');

// The Firebase Admin SDK to access the Firebase Realtime Database.
const admin = require('firebase-admin');
admin.initializeApp();
exports.checkflag = functions.database.ref('/Questions/{q_id}/') //give your database path instead here
.onUpdate((snapshot, context) => {
const temptoken = 'elHzh0MLwvI:GeP3qC9cb3GZMm7BdDNxhNm9pvvU4WWSDX4QUt29-miElptYB5PtCX7bdua6NQ7e09JSRMd-qFxf14j6tWLMDaRp5C4GB9TxAj5qxa8Pou9FkzvbEGxJBLvd-qwOcSKCcUvl22Tsu7jP';  //replace it with your app token
// const flag = snapshot.before.val();   TO GET THE OLD VALUE BEFORE UPDATE
const flag = snapshot.after.val();
let statusMessage = `Message from the clouds as ${flag}`
var message = {
notification: {
title: 'cfunction',
body: statusMessage
},
token: temptoken
};
admin.messaging().send(message).then((response) => {
console.log("Message sent successfully:", response);
return response;
})
.catch((error) => {
console.log("Error sending message: ", error);
});
});