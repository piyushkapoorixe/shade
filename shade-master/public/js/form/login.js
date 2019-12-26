$(document).ready(() => {
  let usernameField, usernameSmallText, passwordField, passwordSmallText;

  usernameField = $('#username');
  usernameSmallText = $('#username-small-text');
  passwordField = $('#password');
  passwordSmallText = $('#password-small-text');

  const redColor = '#dc3545';

  // Trigger on every input change in username field
  usernameField.on('input', () => {
    usernameSmallText.text('');
    usernameField.css('border-color', '');
  });

  // Trigger on every input change in password field
  passwordField.on('input', () => {
    passwordSmallText.text('');
    passwordField.css('border-color', '');
  });

  // Trigger on form submission
  $('#form').submit(() => {
    const usernameValue = $('#username').val();
    const passwordValue = $('#password').val();

    // If usernameValue field is empty
    if (!usernameValue) {
      usernameField.css('border-color', redColor);
      return false;
    }
    // If usernameValue is short
    else if (usernameValue.length < 4) {
      usernameSmallText.addClass('text-danger').text('Invalid');
      return false;
    }

    // If passwordValue field is empty
    if (!passwordValue) {
      passwordField.css('border-color', redColor);
      return false;
    }
    // If passwordValue is short
    else if (passwordValue.length < 6) {
      passwordSmallText.addClass('text-danger').text('Invalid');
      return false;
    }

    // Inputs are valid
    return true;
  });
});
