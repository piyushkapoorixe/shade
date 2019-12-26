// Require User model
const User = require('../schemas/User');

// Export connection-routes module
module.exports = (app) => {
  // GET requests handler, for /:username/connect/:choice
  app.route('/:username/connect/:choice').get((req, res) => {
    // If user is logged in
    if (req.isAuthenticated()) {
      const paramUsername = req.params.username;
      const currentUsername = req.user.username;

      // If user is trying to connect with itself
      if (paramUsername === currentUsername) {
        // Render error view
        res.render('error', { errorMessage: 'Not Allowed' });
      }
      // User is trying to connect with other
      else {
        // Find user's connections
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
              const following = user.connections.following;

              // If user is already connected
              if (
                friends.includes(paramUsername) ||
                family.includes(paramUsername) ||
                acquaintances.includes(paramUsername)
              ) {
                // Render error view
                res.render('error', {
                  errorMessage: 'You are Already Connected'
                });
              }
              // User is not connected
              else {
                const choice = req.params.choice;

                // Push choice in appropriate fields
                if (choice === 'friend') {
                  friends.push(paramUsername);
                  following.push(paramUsername);
                } else if (choice === 'family') {
                  family.push(paramUsername);
                  following.push(paramUsername);
                } else if (choice === 'acquaintance') {
                  acquaintances.push(paramUsername);
                  following.push(paramUsername);
                }

                // Save updated user
                user.save((err) => {
                  // If error
                  if (err) {
                    // Log error
                    console.error(err);

                    // Render error view
                    res.render('error', { errorMessage: 'An Error Occured' });
                  }
                  // User connected successfully
                  else {
                    // Reload profile
                    res.redirect('/' + paramUsername);
                  }
                });
              }
            }
          }
        );
      }
    }
    // User is not logged in
    else {
      // Redirect to /login
      res.redirect('/login');
    }
  });

  // GET requests handler, for /:username/disconnect
  app.route('/:username/disconnect').get((req, res) => {
    // If user is logged in
    if (req.isAuthenticated()) {
      const paramUsername = req.params.username;
      const currentUsername = req.user.username;

      // If user is trying to disconnect with itself
      if (paramUsername === currentUsername) {
        // Render error view
        res.render('error', { errorMessage: 'Not Allowed' });
      }
      // User is trying to disconnect with other
      else {
        // Find user's connections
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
              const following = user.connections.following;

              // If user is connected as friend
              if (friends.includes(paramUsername)) {
                friends.splice(friends.indexOf(paramUsername), 1);
                following.splice(following.indexOf(paramUsername), 1);
              }
              // If user is connected as family
              else if (family.includes(paramUsername)) {
                family.splice(family.indexOf(paramUsername), 1);
                following.splice(following.indexOf(paramUsername), 1);
              }
              // If user is connected as acquaintance
              else if (acquaintances.includes(paramUsername)) {
                acquaintances.splice(acquaintances.indexOf(paramUsername), 1);
                following.splice(following.indexOf(paramUsername), 1);
              }
              // User is not connected
              else {
                // Render error view
                res.render('error', { errorMessage: 'You are Not Connected' });
              }

              // Save updated user
              user.save((err) => {
                // If error
                if (err) {
                  // Log error
                  console.error(err);

                  // Render error view
                  res.render('error', { errorMessage: 'An Error Occured' });
                }
                // User updated successfully
                else {
                  // Reload profile
                  res.redirect('/' + paramUsername);
                }
              });
            }
          }
        );
      }
    }
    // User is not logged in
    else {
      // Redirect to /login
      res.redirect('/login');
    }
  });

  // GET requests handler, for /:username/connections
  app.route('/:username/connections').get((req, res) => {
    // If user is logged in
    if (req.isAuthenticated()) {
      const paramUsername = req.params.username;
      const currentUsername = req.user.username;

      // If user wants to see its connections
      if (paramUsername === currentUsername) {
        // Find user's connections
        User.findOne(
          { username: currentUsername },
          '-_id connections',
          (err, user) => {
            // If error
            if (err) {
              // Log error
              console.error(err);

              // Render error view
              res.render('error', { errorMessage: 'An Error Occured' });
            } else {
              // Send response as json
              res.json(user.connections);
            }
          }
        );
      }
      // User wants to see someone else's connections
      else {
        // Render error view
        res.render('error', { errorMessage: 'Not Allowed' });
      }
    }
    // User is not logged in
    else {
      // Redirect to /login
      res.redirect('/login');
    }
  });
};
