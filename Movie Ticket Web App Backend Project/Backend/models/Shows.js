const mongoose = require("mongoose");
//const MovieTheaterCollection = require("./MovieTheater");
//const MoviesCollection = require("./Movies");

const Schema = mongoose.Schema;
// const TheaterScreen = MovieTheaterCollection.TheaterScreenSchema
// //const Movie = MoviesCollection.MoviesSchema
// const Language = MoviesCollection.LanguageSchema
// Create Schema
const ShowsSchema = new Schema({
  show_id: String,
  show_time: String,
  show_movietheater: String,
  show_movie: Number,
  show_theaterscreen :String,
  show_rate: Number
});

const Shows = mongoose.model("Shows", ShowsSchema);


module.exports = { Shows }

//localhost:3000/api/shows/search?request_param=New York