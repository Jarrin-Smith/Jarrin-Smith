
const express = require("express");
const router = express.Router();
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const keys = require("../../config/keys");

const Shows = require('../../models/Shows');

router.get("/search", (req, res) => {
  const { minprice, maxprice, startdate, enddate, location } = req.query;

  const aggregate = [
    {
      $lookup: {
        from: "movietheaters",
        localField: "show_movietheater",
        foreignField: "movietheater_id",
        as: "show_movietheater",
      },
    },
    {
      $lookup: {
        from: "theaterscreens",
        localField: "show_theaterscreen",
        foreignField: "theaterscreen_id",
        as: "show_theaterscreen",
      },
    },
    {
      $unset: "show_movietheater.movietheater_screens",
    },
    {
      $lookup: {
        from: "movies_view",
        localField: "show_movie",
        foreignField: "movie_id",
        as: "show_movie",
      },
    },
  ];

  if (location) {
    aggregate.push({
      $match: {
        "show_movietheater.movietheater_location": location,
      },
    });
  }

  if (minprice || maxprice) {
    const matchStage = {};
    if (minprice) {
      matchStage.$gte = parseInt(minprice);
    }
    if (maxprice) {
      matchStage.$lte = parseInt(maxprice);
    }
    aggregate.push({
      $match: {
        show_rate: matchStage,
      },
    });
  }

  if (startdate || enddate) {
    const matchStage = {};
    if (startdate) {
      matchStage.$gte = new Date(startdate);
    }
    if (enddate) {
      const endDateObj = new Date(enddate);
      endDateObj.setHours(23);
      endDateObj.setMinutes(59);
      endDateObj.setSeconds(59);
      matchStage.$lte = endDateObj;
    }
    console.log(matchStage)
    aggregate.push({
      $match: {
        show_time: matchStage,
      },
    });
  }
//console.log(aggregate)
  Shows.Shows.aggregate(aggregate, function (err, docs) {
    if (!err) {
      res.json({ shows: docs });
    } else {
      return res.status(404).json({ shows: "Shows not found" });
    }
  });
});

module.exports = router;