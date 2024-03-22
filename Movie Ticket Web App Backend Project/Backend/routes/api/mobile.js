const express = require("express");
const router = express.Router();
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const keys = require("../../config/keys");
const google_client_ID= keys.frontend_google_clientID
const crypto = require("crypto");

const User = require("../../models/User");

router.post('/forgotPassword', async (req, res) => {
    const { mobile } = req.body;
    console.log(mobile)
    try {
        // Check if email exists in the database
        const user = await User.findOne({ mobile:mobile });
        if (!user) {
            return res.status(404).json({ user: "User with mobile number not found" });
        }

        // Generate a unique token and store it in the database
        const token = crypto.randomBytes(20).toString('hex');
        user.resetPasswordToken = token;
        user.resetPasswordExpires = Date.now() + 3600000; // Token expires in 1 hour
        await user.save();

        const accountSid = keys.accountSid;
        const authToken = keys.auth_token_twilio;
        const twilioclient = require('twilio')(accountSid, authToken);

        twilioclient.messages
            .create({
                body:`You are receiving this message because you (or someone else) have requested the reset of the password for your account.\n\nPlease click on the following link, or paste this into your browser to complete the process:\n\n${keys.localhost}reset-password?token=${token}\n\nIf you did not request this, please ignore this message and your password will remain unchanged.\n`,
                messagingServiceSid: keys.phone_number,
                to: mobile
            })
            .then(message => console.log(message.sid))
            .catch(error => {
                console.log(error);
                res.status(500).json({ status: "Failed to send message" });
            });

        res.status(200).json({ message: "Password reset link sent successfully" });
    } catch (err) {
        console.log(err);
        res.status(500).json({ status : "Internal Server Error" });
    }
});

module.exports = router;
