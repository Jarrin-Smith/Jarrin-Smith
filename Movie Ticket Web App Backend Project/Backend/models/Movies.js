const mongoose = require("mongoose");
//const MovieTheaterCollection = require("./MovieTheater");
//const MoviesCollection = require("./Movies");

const Schema = mongoose.Schema;
// const TheaterScreen = MovieTheaterCollection.TheaterScreenSchema
// //const Movie = MoviesCollection.MoviesSchema
// const Language = MoviesCollection.LanguageSchema
// Create Schema
const Movies_schema = new Schema({
  movie_id: Number,
  movie_name: String,
  movie_genres: [String],
  movie_rating: String,
  movie_languages :[String],
  movie_length: String,
  movie_subtitles :[String],
  movie_cast :[String],
  movie_image: String,
  cast_actor:[String],
  movie_release_date:Date

});

const Movies = mongoose.model("Movies", Movies_schema);


module.exports = { Movies }


