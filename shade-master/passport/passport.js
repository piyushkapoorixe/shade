// Require express-session and passport for authentication
const session = require('express-session');
const passport = require('passport');
const LocalStrategy = require('passport-local').Strategy;

// Require bcryptjs for password hashing
const bcrypt = require('bcryptjs');

// Require User model
const User = require('../schemas/User');

// Export passport module
module.exports = (app) => {
  // Initialise express- and passport session
  app.use(
    session({
      secret: process.env.SESSION_SECRET,
      resave: true,
      saveUninitialized: true
    })
  );
  app.use(passport.initialize());
  app.use(passport.session());

  // Function to serialise user
  passport.serializeUser((user, done) => {
    done(null, user._id);
  });

  // Function to deserialise user
  passport.deserializeUser((id, done) => {
    User.findById(id, (err, user) => {
      done(err, user);
    });
  });

  // Implement a passport LocalStrategy
  passport.use(
    new LocalStrategy((username, password, done) => {
      // Try to find user in database
      User.findOne({ username: username }, 'username password', (err, user) => {
        // Error in retrieving user
        if (err) return done(err);
        // Incorrect username
        else if (!user) return done(null, false, 'No Such User');
        // Incorrect password
        else if (!bcrypt.compareSync(password, user.password))
          return done(null, false, 'Wrong Password Entered');
        // Correct username and password
        else return done(null, user);
      });
    })
  );
};
