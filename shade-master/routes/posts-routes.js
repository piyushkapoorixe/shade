// Require models
const User = require('../schemas/User');
const Post = require('../schemas/Post');

// Export posts-routes module
module.exports = (app) => {
  // Function to validate post
  function validatePost(req, res, next) {
    const content = req.body.content;
    const audience = req.body.audience;

    // If content is empty or audience is not selected
    if (!content.length || audience === '0') {
      // Render error view
      res.render('error', { errorMessage: 'Invalid Post' });
    }
    // Proceed with posting
    else return next();
  }

  // POST requests handler, for /:username/post
  app.route('/:username/post').post(validatePost, (req, res) => {
    // If user is logged in
    if (req.isAuthenticated()) {
      const paramUsername = req.params.username;
      const currentUsername = req.user.username;

      // If requested username and current username are same
      if (paramUsername === currentUsername) {
        const content = req.body.content;
        const audience = req.body.audience;

        // Find current user's connections
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
              const friends = user.connections.friends;
              const family = user.connections.family;
              const acquaintances = user.connections.acquaintances;

              // If audience is set to 'friends'
              if (audience === 'friends') {
                // Prepare post object
                const post = {
                  username: currentUsername,
                  content: content,
                  audience: friends
                };

                // Save post in database
                Post.create(post, (err, post) => {
                  // If error
                  if (err) {
                    // Log error
                    console.error(err);

                    // Render error view
                    res.render('error', { errorMessage: 'An Error Occured' });
                  }
                  // Post saved successfully, redirect to /currentUsername
                  else res.redirect('/' + currentUsername);
                });
              }
              // If audience is set to 'family'
              else if (audience === 'family') {
                // Prepare post object
                const post = {
                  username: currentUsername,
                  content: content,
                  audience: family
                };

                // Save post in database
                Post.create(post, (err, post) => {
                  // If error
                  if (err) {
                    // Log error
                    console.error(err);

                    // Render error view
                    res.render('error', { errorMessage: 'An Error Occured' });
                  }
                  // Post saved successfully, redirect to /currentUsername
                  else res.redirect('/' + currentUsername);
                });
              }
              // If audience is set to 'acquaintances'
              else if (audience === 'acquaintances') {
                // Prepare post object
                const post = {
                  username: currentUsername,
                  content: content,
                  audience: acquaintances
                };

                // Save post in database
                Post.create(post, (err, post) => {
                  // If error
                  if (err) {
                    // Log error
                    console.error(err);

                    // Render error view
                    res.render('error', { errorMessage: 'An Error Occured' });
                  }
                  // Post saved successfully, redirect to /currentUsername
                  else res.redirect('/' + currentUsername);
                });
              }
              // If audience is set to 'public'
              else if (audience === 'public') {
                // Prepare post object
                const post = {
                  username: currentUsername,
                  content: content,
                  audience: ['public']
                };

                // Save post in database
                Post.create(post, (err, post) => {
                  // If error
                  if (err) {
                    // Log error
                    console.error(err);

                    // Render error view
                    res.render('error', { errorMessage: 'An Error Occured' });
                  }
                  // Post saved successfully, redirect to /currentUsername
                  else res.redirect('/' + currentUsername);
                });
              }
              // Invalid post
              else {
                // Render error view
                res.render('error', { errorMessage: 'Invalid Post' });
              }
            }
          }
        );
      }
      // Requested username and current username are not same
      else res.render('error', { errorMessage: 'Not Allowed' });
    }
    // User is not logged in
    else res.redirect('/login');
  });

  // GET requests handler, for /:username/posts/home/:skip/:limit
  app.route('/:username/posts/home/:skip/:limit').get((req, res) => {
    // If user is logged in
    if (req.isAuthenticated()) {
      const paramUsername = req.params.username;
      const currentUsername = req.user.username;

      // If user wants to access its own home posts
      if (paramUsername === currentUsername) {
        // Find all posts with current user as audience
        Post.find({ audience: currentUsername })
          .sort('-date')
          .skip(Number(req.params.skip))
          .limit(Number(req.params.limit))
          .select('-audience')
          .exec((err, posts) => {
            // If error
            if (err) {
              // Log error
              console.error(err);

              // Rende error view
              res.render('error', { errorMessage: 'An Error Occured' });
            }
            // Posts retrieved successfully
            else {
              // Send posts as json
              res.json(posts);
            }
          });
      }
      // User wants to access someone else's home posts
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

  // GET requests handler, for /:username/posts/own/:skip/:limit
  app.route('/:username/posts/own/:skip/:limit').get((req, res) => {
    // If user is logged in
    if (req.isAuthenticated()) {
      const paramUsername = req.params.username;
      const currentUsername = req.user.username;

      // If user wants its own posts
      if (paramUsername === currentUsername) {
        // Find all user's posts
        Post.find({ username: currentUsername })
          .sort('-date')
          .skip(Number(req.params.skip))
          .limit(Number(req.params.limit))
          .select('-audience')
          .exec((err, posts) => {
            // If error
            if (err) {
              // Log error
              console.error(err);

              // Render error view
              res.render('error', { errorMessage: 'An Error Occured' });
            }
            // Posts found successfully
            else {
              // Send posts as json
              res.json(posts);
            }
          });
      }
      // User wants someone else's posts
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

  // GET requests handler, for /:username/posts/connection/:skip/:limit
  app.route('/:username/posts/connection/:skip/:limit').get((req, res) => {
    // If user is logged in
    if (req.isAuthenticated()) {
      const paramUsername = req.params.username;
      const currentUsername = req.user.username;

      // If user wants its connection posts
      if (paramUsername === currentUsername) {
        // Render error view
        res.render('error', { errorMessage: 'Not Allowed' });
      }
      // User wants someone else's connection posts
      else {
        // Find user-only and public posts
        Post.find({
          username: paramUsername,
          audience: { $in: [currentUsername, 'public'] }
        })
          .sort('-date')
          .skip(Number(req.params.skip))
          .limit(Number(req.params.limit))
          .select('-audience')
          .exec((err, posts) => {
            // If error
            if (err) {
              // Log error
              console.error(err);

              // Render error view
              res.render('error', { errorMessage: 'An Error Occured' });
            }
            // Posts retrieved successfully
            else {
              // Send posts as json
              res.json(posts);
            }
          });
      }
    }
    // User is not logged in
    else {
      // Redirect to /login
      res.redirect('/login');
    }
  });

  // GET requests handler, for /:username/posts/public/:skip/:limit
  app.route('/:username/posts/public/:skip/:limit').get((req, res) => {
    // Find public posts of requested user
    Post.find({ username: req.params.username, audience: 'public' })
      .sort('-date')
      .skip(Number(req.params.skip))
      .limit(Number(req.params.limit))
      .select('-audience')
      .exec((err, posts) => {
        // If error
        if (err) {
          // Log error
          console.error(err);

          // Render error view
          res.render('error', { errorMessage: 'An Error Occured' });
        } else {
          // Send posts as json
          res.json(posts);
        }
      });
  });
};
