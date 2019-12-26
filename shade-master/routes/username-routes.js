// Require bcrypt for password hashing
const bcrypt = require('bcryptjs');

// Require User model
const User = require('../schemas/User');

// Export profile-routes module
module.exports = (app) => {
  // Function to check username existence
  function checkUsernameExistence(req, res, next) {
    // Check if username exists
    User.countDocuments({ username: req.params.username }, (err, count) => {
      // If error
      if (err) {
        // Log error
        console.error(err);

        // Render error view
        res.render('error', { errorMessage: 'An Error Occured' });
      }
      // Username doesn't exist, render error view
      else if (!count) res.render('error', { errorMessage: 'No Such User' });
      // Username exists
      else return next();
    });
  }

  // Function to validate new fields
  function validateNewFields(req, res, next) {
    const field = req.params.field;

    // If username change is required
    if (field === 'username') {
      // If username is invalid
      if (
        req.body.new_username.match(/[^a-z]/i) ||
        req.body.new_username.length < 4
      )
        // Render error view
        res.render('error', { errorMessage: 'Invalid Username' });
      // If password is invalid
      else if (req.body.password.length < 6)
        // Render error view
        res.render('error', { errorMessage: 'Wrong Password Entered' });
      // Continue with username change
      else return next();
    }
    // If password change is required
    else if (field === 'password') {
      // If old password is invalid
      if (req.body.old_password.length < 6)
        // Render error view
        res.render('error', { errorMessage: 'Wrong Old Password' });
      // If new password is invalid
      else if (req.body.new_password.length < 6)
        // Render error view
        res.render('error', { errorMessage: 'Invalid New Password' });
      // Continue with password change
      else return next();
    }
    // If consent change is required
    else if (field === 'consent') {
      // If consent password is invalid
      if (req.body.consent_password.length < 6)
        // Render error view
        res.render('error', { errorMessage: 'Wrong Old Password' });
      // Continue with consent change
      else return next();
    }
    // Invalid field
    else res.render('error', { errorMessage: 'An Error Occured' });
  }

  // GET requests handler, for /:username
  app.route('/:username').get(checkUsernameExistence, (req, res) => {
    const paramUsername = req.params.username;

    // If user is logged in
    if (req.isAuthenticated()) {
      const currentUsername = req.user.username;

      // If user is accesssing own profile
      if (paramUsername === currentUsername) {
        // Render user's own profile
        res.render('own', { username: currentUsername });
      }
      // User is accessing other's profile
      else {
        // Check if user is in connections
        User.findOne(
          { username: currentUsername },
          'connections',
          (err, user) => {
            // If error
            if (err) {
              // Log error
              console.error(err);

              // Render error view
              res.render('error', { errorMessage: 'An Error Occured' });
            } else {
              const friends = user.connections.friends;
              const family = user.connections.family;
              const acquaintances = user.connections.acquaintances;

              // User is in in connections
              if (
                friends.includes(paramUsername) ||
                family.includes(paramUsername) ||
                acquaintances.includes(paramUsername)
              )
                // Render user specific profile
                res.render('connection', { username: paramUsername });
              // User is not in connections, render public profile
              else res.render('other', { username: paramUsername });
            }
          }
        );
      }
    }
    // User is not logged in, render public profile
    else {
      res.render('public', { username: paramUsername });
    }
  });

  // GET requests handler, for /:username/about
  app.route('/:username/about').get((req, res) => {
    // If user is logged in
    if (req.isAuthenticated()) {
      // Retrieve username, consent, joined fields of requested user
      User.findOne(
        { username: req.params.username },
        '-_id consent joined',
        (err, user) => {
          // If error
          if (err) {
            // Log error
            console.error(err);

            // Render error view
            res.render('error', { errorMessage: 'An Error Occured' });
          } else {
            // Send user object as json
            res.json(user);
          }
        }
      );
    }
    // User is not logged in, redirect to /login
    else res.redirect('/login');
  });

  // POST requests handler, for /:username/change/:field
  app.route('/:username/change/:field').post(validateNewFields, (req, res) => {
    // If user is logged in
    if (req.isAuthenticated()) {
      const paramUsername = req.params.username;
      const currentUsername = req.user.username;

      // If requested user is same as current user
      if (paramUsername === currentUsername) {
        const field = req.params.field;

        // If username change is required
        if (field === 'username') {
          const newUsername = req.body.new_username;
          const password = req.body.password;

          // Check new username existence
          User.countDocuments({ username: newUsername }, (err, count) => {
            // If error
            if (err) {
              // Log error
              console.error(err);

              // Render error view
              res.render('error', { errorMessage: 'An Error Occured' });
            }
            // If username is not available
            else if (count) {
              // Render error view
              res.render('error', { errorMessage: 'Username Not Available' });
            }
            // Check if entered password is correct
            else {
              User.findOne(
                { username: currentUsername },
                'password',
                (err, user) => {
                  // If error
                  if (err) {
                    // Log error
                    console.error(err);

                    // Render error view
                    res.render('error', { errorMessage: 'An Error Occured' });
                  }
                  // If entered password is not correct
                  else if (!bcrypt.compareSync(password, user.password))
                    // Render error view
                    res.render('error', {
                      errorMessage: 'Wrong Password Entered'
                    });
                  // Password is correct, change username
                  else {
                    User.updateOne(
                      { username: currentUsername },
                      { username: newUsername },
                      (err, raw) => {
                        // If error
                        if (err) {
                          // Log error
                          console.error(err);

                          // Render error view
                          res.render('error', {
                            errorMessage: 'An Error Occured'
                          });
                        }
                        // Username change successful
                        else {
                          // Log user out
                          req.logout();

                          // Render login form with alert
                          res.render('form', {
                            type: 'login',
                            alertMessage: 'Username Changed Successfully!',
                            alertType: 'success'
                          });
                        }
                      }
                    );
                  }
                }
              );
            }
          });
        }
        // If password change is required
        else if (field === 'password') {
          const oldPassword = req.body.old_password;
          const newPassword = req.body.new_password;

          // Retrieve saved password
          User.findOne(
            { username: currentUsername },
            'password',
            (err, user) => {
              // If error
              if (err) {
                // Log error
                console.error(err);

                // Render error view
                res.render('error', { errorMessage: 'An Error Occured' });
              }
              // If wrong password is entered
              else if (!bcrypt.compareSync(oldPassword, user.password))
                // Render error view
                res.render('error', { errorMessage: 'Wrong Old Password' });
              else {
                // Convert new password into its hash
                const hashedPassword = bcrypt.hashSync(newPassword, 8);
                // Change password
                User.updateOne(
                  { username: currentUsername },
                  { password: hashedPassword },
                  (err, raw) => {
                    // If error
                    if (err) {
                      // Log error
                      console.error(err);

                      // Render error view
                      res.render('error', { errorMessage: 'An Error Occured' });
                    }
                    // Password changed successfully
                    else {
                      // Log user out
                      req.logout();

                      // Render login form with alert
                      res.render('form', {
                        type: 'login',
                        alertMessage: 'Password Changed Successfully!',
                        alertType: 'success'
                      });
                    }
                  }
                );
              }
            }
          );
        }
        // If consent change is required
        else if (field === 'consent') {
          const consentPassword = req.body.consent_password;

          // Check if entered password is correct
          User.findOne(
            { username: currentUsername },
            'password',
            (err, user) => {
              // If error
              if (err) {
                // Log error
                console.error(err);

                // Render error view
                res.render('error', { errorMessage: 'An Error Occured ' });
              }
              // If entered password is not correct
              else if (!bcrypt.compareSync(consentPassword, user.password))
                // Render error view
                res.render('error', { errorMessage: 'Wrong Password Entered' });
              else {
                // Password is correct, change consent
                User.updateOne(
                  { username: currentUsername },
                  { consent: req.body.consent ? true : false },
                  (err, raw) => {
                    // If error
                    if (err) {
                      // Log error
                      console.error(err);

                      // Render error view
                      res.render('error', { errorMessage: 'An Error Occured' });
                    }
                    // Consent changed successfully
                    else {
                      // Log user out
                      req.logout();

                      // Render login form with alert
                      res.render('form', {
                        type: 'login',
                        alertMessage: 'Consent Changed Successfullly!',
                        alertType: 'success'
                      });
                    }
                  }
                );
              }
            }
          );
        }
        // Wrong field
        else res.render('error', { errorMessage: 'Not Found' });
      }
      // Requested user is not same as current user
      else res.render('error', { errorMessage: 'Not Allowed' });
    }
    // User is not logged in, redirect to /login
    else res.redirect('/login');
  });

  // GET requests handler, for /check/:username
  app.route('/check/:username').get((req, res) => {
    // Check if user is already registered
    User.countDocuments({ username: req.params.username }, (err, count) => {
      // If error
      if (err) {
        console.error(err);
        res.render('error', { errorMessage: 'An Error Occured' });
      }
      // If user is already registered
      else if (count) res.json({ available: false });
      // User isn't registered
      else res.json({ available: true });
    });
  });
};
