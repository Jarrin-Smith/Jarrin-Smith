const mongoose = require("mongoose");
const Schema = mongoose.Schema;
// Create Schema
const EmployeeUserSchema = new Schema({
  first_name: {
    type: String,
    required: true
  },
  last_name: {
    type: String
    
  },
  email: {
    type: String,
    
  },
  password: {
    type: String,
    required: true
  },
 
  EmployeeId: {
    type: Number  
  },
  EmployeeType: {
    type: String  
  },
  TheatreID: {
    type: Number  
  },
  
 

});
module.exports = EmployeeUser = mongoose.model("employeeCollectionion", EmployeeUserSchema);