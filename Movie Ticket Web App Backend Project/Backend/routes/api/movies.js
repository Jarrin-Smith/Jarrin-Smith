const express = require("express");
const router = express.Router();
const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");
const keys = require("../../config/keys");

const Movies = require('../../models/Movies');

router.get("/movie", async (req, res) => {

  const { startdate, enddate } = req.query;
  

//console.log(matchStage);

//console.log(aggregate,"vhvh")
  aggregate=[
    {
    '$lookup': {
      'from': 'genres',
      'localField': 'movie_genres',
      'foreignField': 'genre_id',
      'as': 'movie_genres'
    }
  }, {
    '$lookup': {
      'from': 'casts',
      'localField': 'movie_cast',
      'foreignField': 'cast_id',
      'as': 'movie_cast'
    }
  }, {
    '$lookup': {
      'from': 'actors',
      'localField': 'movie_cast.cast_actor',
      'foreignField': 'actor_id',
      'as': 'cast_actor'
    }
  }, {
    '$lookup': {
      'from': 'languages',
      'localField': 'movie_languages',
      'foreignField': 'language_id',
      'as': 'movie_languages'
    }
  }, {
    '$lookup': {
      'from': 'languages',
      'localField': 'movie_subtitles',
      'foreignField': 'language_id',
      'as': 'movie_subtitles'
    }
  },
  {
    '$lookup': {
        'from': 'ratings',
        'localField': 'movie_rating',
        'foreignField': 'rating_id',
        'as': 'movie_rating'
      }
    }]


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
    console.log(matchStage);
    aggregate.push({
      $match: {
        "movie_release_date": matchStage,
      },
    });

    //console.log(matchStage,"hello")
  }

//console.log(aggregate);
  Movies.Movies.aggregate(aggregate, function (err, docs) {
    if (!err) {
      res.json({ movies: docs });
    } else {
      return res.status(404).json({ movies: "Movies not found" });
    }
  });
});

module.exports = router;
