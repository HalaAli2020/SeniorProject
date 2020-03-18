const functions = require('firebase-functions');
const admin = require('firebase-admin');
const nodemailer = require('nodemailer');
admin.initializeApp()

//const gmailEmail = functions.config().gmail.email;
//const gmailPassword = functions.config().gmail.password;

exports.sendEmailNotification=functions.database.ref('Reports/{classkey}').onCreate((snap, ctx)=>{
	const data=snap.val();
	const accuser=data.accuserID;
	const mailTransport=nodemailer.createTransport({
	service: 'gmail',
	auth:{
	user: 'unit.school.team.help@gmail.com',
	pass: 'adeelhalapalaktyler',
	},
	});
	try{
	mailTransport.sendMail({
	from: '"USER" <accuser>',
	to: 'unit.school.team.help@gmail.com',
	subject: 'Report issue',
	text: `Hello, Admin! The accuser, ${data.accuserID}, is reporting the user with the uid of ${data.accusedID} of inappropriate content. Here is the reported message:${data.complaintext}`,
	});
	console.log('successfully sent email', accuser);}
	catch(error){console.log('There was an error', error);}
return 0;
});
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
