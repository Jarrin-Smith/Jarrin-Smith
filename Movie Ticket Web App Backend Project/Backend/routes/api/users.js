const express = require("express");
const router = express.Router();
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const keys = require("../../config/keys");
const google_client_ID= keys.frontend_google_clientID
//google-auth
const { OAuth2Client } = require("google-auth-library");
const client = new OAuth2Client(google_client_ID);
// Load input validation
const validateRegisterInput = require("../../validation/register");
const validateLoginInput = require("../../validation/login");
// Load User model
const User = require("../../models/User");


// @route POST api/users/register
// @desc Register user
// @access Public
router.post("/register", (req, res) => {
    // Form validation
  const { errors, isValid } = validateRegisterInput(req.body);
  // Check validation
    if (!isValid) {
      return res.status(400).json(errors);
    }
  User.findOne({ $or: [{ mobile: req.body.mobile }, { email: req.body.email }]}).then(user => {
      if (user) {
        //console.log(user);
        return res.status(400).json({ emailormobile: "Email or Mobile already exists" });
      } else {
        const newUser = new User({
          first_name: req.body.first_name,
          last_name:req.body.last_name,
          email: req.body.email,
          password: req.body.password,
          mobile:req.body.mobile,
          auth_type:"Normal",
        });
  // Hash password before saving in database
        bcrypt.genSalt(10, (err, salt) => {
          bcrypt.hash(newUser.password, salt, (err, hash) => {
            if (err) throw err;
            newUser.password = hash;
            newUser
              .save()
              .then(user => res.json(user))
              .catch(err => console.log(err));
          });
        });
      }
    });
});

// @route POST api/users/login
// @desc Login user and return JWT token
// @access Public
router.post("/login", (req, res) => {
  // Form validation
const { errors, isValid } = validateLoginInput(req.body);
// Check validation
  if (!isValid) {
    return res.status(400).json(errors);
  }
const emailormobile = req.body.emailormobile;
  const password = req.body.password;
// Find user by email

const query = {
    $or: [
      { email: emailormobile },
      { mobile: emailormobile }
    ]
  };
  
  User.findOne( query).then(user => {
    // Check if user exists
    if (!user) {
      return res.status(404).json({ emailormobilenotfound: "Not found" });
    }
// Check password
    bcrypt.compare(password, user.password).then(isMatch => {
      if (isMatch) {
        // User matched
        // Create JWT Payload
        const payload = {
          id: user.id,
          name: user.first_name
        };
// Sign token
        jwt.sign(
          payload,
          keys.secretOrKey,
          {
            expiresIn: 31556926 // 1 year in seconds
          },
          (err, token) => {
            res.json({
              success: true,
              token: "Bearer " + token,
              user:user
            });
          }
        );
      } else {
        return res
          .status(400)
          .json({ passwordincorrect: "Password incorrect" });
      }
    });
  });
});

// @route POST api/users/google_login
// @desc Login user and return JWT token
// @access Public

router.post("/google_login", async (req, res) => {
  const { idToken } = req.body;

  try {
    const ticket = await client.verifyIdToken({
      idToken: idToken,
      audience: google_client_ID,
    });

    const { name, email, picture } = ticket.getPayload();

    let user = await User.findOne({ email: email });

    if (user) {
      if (user.auth_type !== "GoogleLogin") {
        return res
          .status(401)
          .json({ message: "Please login with your previously given password" });
      } else {
        const payload = {
          id: user.id,
          name: user.first_name,
        };

        jwt.sign(
          payload,
          keys.secretOrKey,
          {
            expiresIn: 31556926, // 1 year in seconds
          },
          (err, token) => {
            res.json({
              success: true,
              token: "Bearer " + token,
            });
          }
        );
      }
    } else {
      const newUser = new User({
        email: email,
        first_name: name,
        last_name: "",
        mobile: "",
        auth_type: "GoogleLogin",
      });

      newUser.save((err, user) => {
        if (err) throw err;

        const payload = {
          id: user.id,
          name: user.first_name,
        };

        jwt.sign(
          payload,
          keys.secretOrKey,
          {
            expiresIn: 31556926, // 1 year in seconds
          },
          (err, token) => {
            res.json({
              success: true,
              token: "Bearer " + token,
            });
          }
        );
      });
    }
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: "Server Error" });
  }
});

  router.post("/updatepassword", (req, res) => {
  // Form validation
  const emailOrMobile = req.body.emailormobile;  // login name to search through database
  const oldPassword = req.body.password;   // old password to search through database
  const newPassword = req.body.newPassword; // new password the user wants 
  const confirmPassword = req.body.confirmPassword; // to confirm they entered new password correctly

  // Find user by email
  const query = {
      $or: [
        { email: emailOrMobile },
        { mobile: emailOrMobile }
      ]
    };
    
    User.findOne(query).then(user => {
  // Check to find old password
      bcrypt.compare(oldPassword, user.password).then(isMatch => {
        if (isMatch) {

          // change to new password
          if(confirmPassword == newPassword){
            user.password = newPassword
          }
          else{
            return res.status(400).json({ confirmPassword: "Confirm password doesn't match with actual password." });
          }
          

          // crypt and hash it to database
          bcrypt.genSalt(10, (err, salt) => {
            bcrypt.hash(user.password, salt, (err, hash) => {
              if (err) throw err;
              user.password = hash;
                user.save()
                .then(user => res.json(user))
                .catch(err => console.log(err));
            });
          });   
        }
      });
    });
  });
    

module.exports = router;