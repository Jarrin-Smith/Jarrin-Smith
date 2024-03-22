const express = require("express");
const router = express.Router();
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const keys = require("../../config/keys");
const crypto = require('crypto');


// Load input validation
const validateRegisterInput = require("../../validation/register");
const validateLoginInput = require("../../validation/login");
// Load User model
const User = require("../../models/User");

const nodemailer = require('nodemailer');
console.log("sending_promotions")

router.post('/sendPromotions', async (req, res) => {
  //const { promoHeadline, promoMessage } = req.body;
  const { headline, message } = req.body;
  console.log(headline, message)
  try {
    // Get all users from the database
    const users = await User.find({});
    const recipients = users.filter(user => user.email);
    const recipientEmails = recipients.map(recipient => recipient.email);
    
    // Send promotional email to all recipients
    const transporter = nodemailer.createTransport({
      service: 'gmail',
      auth: {
        user: keys.EMAIL_User,
        pass: keys.pswd,
      },
    });

    const mailOptions = {
      from: keys.EMAIL_User,
      to: recipientEmails.join(','),
      subject: headline,
      text: message,
    };

    transporter.sendMail(mailOptions, (error, info) => {
      if (error) {
        console.log(error);
        res.status(500).json({ status : "Internal Server Error" });
      } else {
        console.log('Email sent: ' + info.response);
        res.status(200).json({ message: "Promotional offers sent successfully" });;
      }
    });
  } catch (err) {
    console.log(err);
    res.status(500).json({ status : "Internal Server Error" });
  }
});

module.exports = router;
