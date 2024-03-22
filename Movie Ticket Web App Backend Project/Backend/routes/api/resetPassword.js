const express = require("express");
const router = express.Router();
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const keys = require("../../config/keys");
const crypto = require('crypto');

const User = require("../../models/User");
console.log("hello reset")

router.post('/', async (req, res) => {
  const { token, password } = req.body;
  console.log(token,password);
  // Check if token exists in the database and is not expired

  const query={
    $and: [{resetPasswordToken: {$exists: true, $eq: token}},
  {resetPasswordExpires: { $exists: true, $gt: Date.now() }}
]}

  const user = await User.findOne(query);
console.log(user);
  if (!user) {
    return res.status(400).json({ status : "Password reset link has been expired" });
  }

  // Hash the new password and save it to the database
  const salt = await bcrypt.genSalt(10);
  user.password = await bcrypt.hash(password, salt);
  user.resetPasswordToken = undefined;
  user.resetPasswordExpires = undefined;
  await user.save();

  res.status(200).json({ message : "Password reset successful" });
});

module.exports = router;
