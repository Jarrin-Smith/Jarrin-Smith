
const express = require("express");
const mongoose = require("mongoose");
const bodyParser = require("body-parser");
const passport = require("passport");
const jwt = require('jsonwebtoken');
const FacebookStrategy = require('passport-facebook').Strategy;
const users = require("./routes/api/users");
const employeeUsers = require("./routes/api/employeeUsers");
const keys = require("./config/keys");


const shows = require("./routes/api/shows");
const movies = require("./routes/api/movies");
const forgot_password_email = require("./routes/api/email");
const forgot_password_mobile = require("./routes/api/mobile");
const reset_password = require("./routes/api/resetPassword");
const show_bookings=require("./routes/api/showbookings");
const promotional_offers=require("./routes/api/promotionOffers");

const ShowBookings = require("./models/ShowBookings");


const paymentRoute = require("./routes/api/payment")

const app = express();
var cors=require('cors');
app.use(cors());
// Bodyparser middleware


app.use(
  bodyParser.urlencoded({
    extended: false
  })
);
app.use(bodyParser.json());

const configureRoutes = require("./routes/api")

configureRoutes(app);
// DB Config
const db = require("./config/keys").mongoURI;

//console.log("Hello, " + db );
// Connect to MongoDB
mongoose
  .connect(
    db,
    { useNewUrlParser: true }
  )
  .then(() => console.log("MongoDB successfully connected"))
  .catch(err => console.log(err));

 // Passport middleware
//app.use(passport.initialize());
// Passport config
//require("./config/passport")(passport);
// Routes
app.use("/api/users", users);
app.use("/api/employeeUsers", employeeUsers);
app.use("/api/shows", shows);
app.use("/api/email", forgot_password_email);
app.use("/api/mobile", forgot_password_mobile);
app.use("/api/resetpassword",reset_password);
app.use("/api/showbookings",show_bookings);
app.use("/api/movies",movies);
app.use("/api/promotionOffers",promotional_offers);

app.use(passport.initialize());

passport.use(new FacebookStrategy({
    clientID: keys.FB_App_ID,
    clientSecret: keys.FB_App_secret,
    callbackURL: '/facebook_login/callback'
}, async (accessToken, refreshToken, profile, done) => {
    try {

        const user_data = { id: profile.id, name: profile.displayName, first_name: profile.first_name, email: profile.email, picture: profile.picture };
        let user = await User.findOne({ id: user_data.id });

        if (!user) {
            const newUser = new User({
                id: user_data.id,
                password: "NoPassword",
                email: user_data.email,
                first_name: user_data.first_name,
                last_name: "",
                mobile: "",
                auth_type: "FacebookLogin",
            });
            try {
                await newUser.save();
            } catch (err) {
                console.error(err);
            }

        }
        const token = jwt.sign({ name: user.name }, 'secret');
        console.log(user)
        return done(null, { user, token });
    } catch (err) {
        console.error(err);
        throw err;
        // res.status(500).json({ message: "Server Error" });
    }
}));

app.get('/facebook_login', passport.authenticate('facebook'));

app.get('/facebook_login/callback', passport.authenticate('facebook', { session: false }), (req, res) => {
  //res.json({ token: req.user.token });
  //res.json({ user_profile: req.user });
  console.log(req)
  console.log({
      success: true,
      token: "Bearer " + req.user.token,
      user: req.user.user.first_name
  })
  res.json({
      success: true,
      token: "Bearer " + req.user.token,
      user: req.user.user.first_name
  });
});

// app.get('/movies', async (req, res) => {
//   try {
//     const movies = await Movie.find();
//     res.json(movies);
//   } catch (err) {
//     console.error(err);
//     res.status(500).send('Server error');
//   }
// });



const port = process.env.PORT || 3000; // 5000 process.env.port is Heroku's port if you choose to deploy the app there
app.listen(port, () => console.log(`Server up and running port ${port} !`));
