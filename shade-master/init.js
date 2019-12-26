// Require passport module for authentication
const passport = require('./passport/passport');

// Require routes modules for route handling
const loginRoutes = require('./routes/login-routes');
const usernameRoutes = require('./routes/username-routes');
const postsRoutes = require('./routes/posts-routes');
const connectionRoutes = require('./routes/connection-routes');

// Export init module
module.exports = (app) => {
  // Instantiate all modules
  passport(app);
  loginRoutes(app);
  usernameRoutes(app);
  postsRoutes(app);
  connectionRoutes(app);
};
