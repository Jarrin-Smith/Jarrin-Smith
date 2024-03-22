const express = require("express");
const router = express.Router();
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const keys = require("../../config/keys");
const crypto = require("crypto");
const isEmpty = require("is-empty");

const ShowBookings = require("../../models/ShowBookings");
const Shows = require('../../models/Shows');
console.log("helloshowbooking")
router.get("/", (req, res) => {

    let { mobile, email } = req.query;

    email = !isEmpty(email) ? email : "";
    mobile = !isEmpty(mobile) ? mobile : "";

    const query = {
        $and: [
          {
            $or: [
              { booking_user: email },
              { booking_user: mobile }
            ]
          },
          { status: 'booked' }
        ]
      };
      

    ShowBookings.find(query, function(err, bookings){
        if(err) {
            return res.status(404).send({ message: "No bookings for this user" });
        }

        let combined = [];

        if(bookings.length > 0) {
            let count = 0;
            for (let i = 0; i < bookings.length; i++) {
                let id = bookings[i].booking_show;
                let aggregate=[                    {                      '$lookup': {                        'from': 'movietheaters',                         'localField': 'show_movietheater',                         'foreignField': 'movietheater_id',                         'as': 'show_movietheater'                      }                    }, {                      '$lookup': {                        'from': 'theaterscreens',                         'localField': 'show_theaterscreen',                         'foreignField': 'theaterscreen_id',                         'as': 'show_theaterscreen'                      }                    }, {                      '$unset': 'show_movietheater.movietheater_screens'                    }, {                      '$lookup': {                        'from': 'movies_view',                         'localField': 'show_movie',                         'foreignField': 'movie_id',                         'as': 'show_movie'                      }                    },                    {                        '$match': {                          "show_id": id,                        }                    }                ];
                
                Shows.Shows.aggregate(aggregate, function(err, shows){
                    if(err) {
                        return res.status(404).json({ shows: "Shows Not found" });
                    }

                    count++;

                    if(shows.length > 0) {
                        let show = shows[0];
                        let booking = bookings.find(booking => booking.booking_show === show.show_id);

                        let combinedObj = {
                            booking_id: booking._id,
                            booking_show: show,
                            booking_user: booking.booking_user,
                            booking_status: booking.booking_status,
                            booking_seats: booking.booking_seats,
                            booking_amount: booking.booking_amount,
                            booking_created: booking.booking_created
                        };

                        combined.push(combinedObj);
                    } else {
                        let booking = bookings.find(booking => booking.booking_show === id);

                        let combinedObj = {
                            booking_id: booking._id,
                            booking_show: null,
                            booking_user: booking.booking_user,
                            booking_status: booking.booking_status,
                            booking_seats: booking.booking_seats,
                            booking_amount: booking.booking_amount,
                            booking_created: booking.booking_created
                        };

                        combined.push(combinedObj);
                    }

                    if(count === bookings.length) {
                        res.json({ bookings: combined });
                    }
                });
            }
        } else {
            res.json({ bookings: [] });
        }
    });
});

module.exports = router;
