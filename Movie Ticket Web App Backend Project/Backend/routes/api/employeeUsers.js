const express = require("express");
const router = express.Router();
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const keys = require("../../config/keys");


//const employeeCollection = mongoose.connection.collection("employeeCollection");

//const validateLoginInput = require("../../validation/login");
// Load User model

const EmployeeUser = require('../../models/EmployeeUser');

router.post("/login", (req, res) => {
    // Form validation
  //const { errors, isValid } = validateLoginInput(req.body);
  // Check validation
  
  const EmployeeId = req.body.EmployeeId;
  const password = req.body.password;
  // Find user by email
  
  const query = {
      $or: [
        { EmployeeId: EmployeeId }
       
      ]
    };
    
    EmployeeUser.findOne( query).then(user => {
        console.log('monogo',user);
      // Check if user exists
      if (!user) {
        return res.status(404).json({ user: "User Not found" });
      }
  // Check password
      bcrypt.compare(password, user.password).then(isMatch => {
        if (isMatch) {
          // User matched
          // Create JWT Payload
          const payload = {
            id: user.id,
            name: user.name
          };
  // Sign token
          jwt.sign(
            payload,
            keys.secretOrKey,
            {
              expiresIn: 31556926 // 1 year in seconds
            },
            (err, token) => {
                if (err) {
                  console.error(err);
                  res.status(500).json({ error: "Internal server error" });
                } else {
                  res.json({ success: true, token: "Bearer " + token,EmployeeType:user.EmployeeType });
                }
              }
          );
        } else {
          return res
            .status(401)
            .json({ passwordincorrect: "Password incorrect" });
        }
      });
    });
  });
  
module.exports = router;