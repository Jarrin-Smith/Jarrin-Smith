const express = require("express");
const router = express.Router();
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const keys = require("../../config/keys");
const crypto = require('crypto');
const google_client_ID = keys.frontend_google_clientID;

// Load input validation
const validateRegisterInput = require("../../validation/register");
const validateLoginInput = require("../../validation/login");
// Load User model
const User = require("../../models/User");

const nodemailer = require('nodemailer');
console.log("forgot_password")

router.post('/forgotPassword', async (req, res) => {
  const { email } = req.body;
  console.log(email)
  try {
    // Check if email exists in the database
    const user = await User.findOne({ email: email });
    if (!user) {
      return res.status(404).json({ user: "User not found" });;
    }

    // Generate a unique token and store it in the database
    const token = crypto.randomBytes(20).toString('hex');
    user.resetPasswordToken = token;
    user.resetPasswordExpires = Date.now() + 3600000; // Token expires in 1 hour
    await user.save();

    // Send password reset email to user
    const transporter = nodemailer.createTransport({
      service: 'gmail',
      auth: {
        user: keys.EMAIL_User,
        pass: keys.pswd,
      },
    });

    const mailOptions = {
      from: keys.EMAIL_User,
      to: email,
      subject: 'Password Reset Request',
      text: `You are receiving this email because you (or someone else) have requested the reset of the password for your account.\n\n
        Please click on the following link, or paste this into your browser to complete the process:\n\n
        ${keys.localhost}reset-password?token=${token}\n\n
        If you did not request this, please ignore this email and your password will remain unchanged.\n`,
    };

    transporter.sendMail(mailOptions, (error, info) => {
      if (error) {
        console.log(error);
        res.status(500).json({ status : "Internal Server Error" });
      } else {
        console.log('Email sent: ' + info.response);
        
      }
    });

    res.status(200).json({ message: "Password reset link sent successfully" });;
  } catch (err) {
    console.log(err);
    res.status(500).json({ status : "Internal Server Error" });
  }
});

module.exports = router;
