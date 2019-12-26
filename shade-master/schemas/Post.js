// Require mongoose
const mongoose = require('mongoose');

// Connect mongoose to database
mongoose.connect(process.env.DATABASE_URI, {
  useNewUrlParser: true,
  useCreateIndex: true
});

// Create postSchema from mongoose.Schema
const postSchema = new mongoose.Schema({
  username: {
    type: String,
    required: true
  },
  content: {
    type: String,
    required: true
  },
  date: {
    type: Date,
    default: Date.now
  },
  audience: {
    type: [String],
    required: true
  }
});

// Convert postSchema into Post model, and export
module.exports = mongoose.model('Post', postSchema);
