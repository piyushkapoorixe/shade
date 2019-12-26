// Require and instantiate express
const express = require('express');
const app = express();

// Require helmet for information security
const helmet = require('helmet');

// Require init module to instantiate all modules
const init = require('./init');

// Require path for path joining
const path = require('path');

// Retrieve port number from environment, or use 3000
const port = process.env.PORT || 3000;

// To prevent attacks and caching
app.use(helmet());
app.use(helmet.noCache());

// Serve static files from /public directory
app.use('/public', express.static(path.join(__dirname, 'public')));

// Parse body of all incoming requests
app.use(express.urlencoded({ extended: false }));

// Set ejs as view engine
app.set('view engine', 'ejs');

// Instantiate modules
init(app);

// 404 handler
app.use((req, res) => {
  // Render error view
  res.render('error', { errorMessage: 'Not Found' });
});

// Start server and listen for requests
app.listen(port, () => {
  console.log('Listening on port', port);
});
