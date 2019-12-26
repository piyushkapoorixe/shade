$(document).ready(() => {
  const username = $('#username').text();

  const activityTab = $('#activity-tab');
  const connectionsTab = $('#connections-tab');
  const aboutTab = $('#about-tab');

  const activityContainer = $('#activity-container');
  const connectionsContainer = $('#connections-container');
  const aboutContainer = $('#about-container');

  const friendsTab = $('#friends-tab');
  const familyTab = $('#family-tab');
  const acquaintancesTab = $('#acquaintances-tab');
  const followingTab = $('#following-tab');

  const friendsContainer = $('#friends-container');
  const familyContainer = $('#family-container');
  const acquaintancesContainer = $('#acquaintances-container');
  const followingContainer = $('#following-container');

  const activityList = $('#activity-list');
  const activityEndText = $('#activity-end-text');

  const friendsList = $('#friends-list');
  const familyList = $('#family-list');
  const acquaintancesList = $('#acquaintances-list');
  const followingList = $('#following-list');

  const aboutList = $('#about-list');

  const newUsernameSmallText = $('#new-username-small-text');
  const passwordSmallText = $('#password-small-text');

  const oldPasswordSmallText = $('#old-password-small-text');
  const newPasswordSmallText = $('#new-password-small-text');

  const consentPasswordSmallText = $('#consent-password-small-text');

  const redColor = '#dc3545';

  let skip = 0;
  const limit = 10;

  // Trigger when activityTab is clicked
  activityTab.click(() => {
    // Add 'active' class to activityTab, and remove from other
    connectionsTab.removeClass('active');
    aboutTab.removeClass('active');
    activityTab.addClass('active');

    // Show activityContainer, and hide other
    connectionsContainer.css('display', 'none');
    aboutContainer.css('display', 'none');
    activityContainer.css('display', 'block');

    return false;
  });

  // Trigger on every input change in content field
  $('#content').on('input', () => {
    $('#content').css('border-color', '');
  });

  // Trigger on every input change in audience field
  $('#audience').on('input', () => {
    $('#audience').css('border-color', '');
  });

  // Trigger on post-form submission
  $('#post-form').submit(() => {
    const postContent = $('#content');
    const postAudience = $('#audience');

    // If postContent is empty
    if (!postContent.val()) {
      postContent.css('border-color', redColor);
      return false;
    }
    // If postAudience is not selected
    else if (postAudience.val() === '0') {
      postAudience.css('border-color', redColor);
      return false;
    }
    // Submit post
    else return true;
  });

  // Function to get own posts
  function getOwnPosts(skip, limit) {
    // Get user's posts
    $.getJSON(username + '/posts/own/' + skip + '/' + limit, (json) => {
      const jsonLength = json.length;

      // No posts found
      if (!jsonLength) {
        // If no initial posts found
        if (!skip) {
          activityEndText
            .removeAttr('href')
            .addClass('text-muted')
            .text('Nothing to see here!')
            .off('click');
        }
        // If no further posts found
        else {
          activityEndText
            .removeAttr('href')
            .addClass('text-muted')
            .text("That's all, folks!")
            .off('click');
        }
      } else {
        // For each post
        for (let i = 0; i < jsonLength; i++) {
          // Append a list group item
          activityList.append(
            '<li class="list-group-item">' +
              '<h6 class="mb-0">' +
              json[i].username +
              '<small class="text-muted float-right">' +
              new Date(json[i].date)
                .toString()
                .split(' ')
                .slice(0, 4)
                .join(' ') +
              '</small>' +
              '</h6>' +
              '<p class="mb-0">' +
              json[i].content +
              '</p>' +
              '</li>'
          );
        }

        // If less than 10 posts were found
        if (jsonLength < 10) {
          activityEndText
            .removeAttr('href')
            .addClass('text-muted')
            .text("That's all, folks!")
            .off('click');
        } else {
          // Show 'More' link
          activityEndText
            .attr('href', '#')
            .removeClass('text-muted')
            .text('More');
        }
      }
    });
  }

  // Trigger when activity-end-text is clicked
  activityEndText.click(() => {
    // Show Loading... text
    activityEndText
      .removeAttr('href')
      .addClass('text-muted')
      .text('Loading...');

    // Get own posts
    getOwnPosts(skip, limit);

    // Increment 'skip' by 10
    skip += 10;

    return false;
  });

  // Trigger when connectionsTab is clicked
  connectionsTab.click(() => {
    // Add 'active' class to connectionsTab, and remove from other
    activityTab.removeClass('active');
    aboutTab.removeClass('active');
    connectionsTab.addClass('active');

    // Show connectionsContainer, and hide other
    activityContainer.css('display', 'none');
    aboutContainer.css('display', 'none');
    connectionsContainer.css('display', 'block');

    return false;
  });

  // Trigger when friends tab is clicked
  friendsTab.click(() => {
    // Add 'active' class to friend tab, and remove from others
    familyTab.removeClass('active');
    acquaintancesTab.removeClass('active');
    followingTab.removeClass('active');
    friendsTab.addClass('active');

    // Show friends container, and hide others
    familyContainer.css('display', 'none');
    acquaintancesContainer.css('display', 'none');
    followingContainer.css('display', 'none');
    friendsContainer.css('display', 'block');

    return false;
  });

  // Trigger when family tab is clicked
  familyTab.click(() => {
    // Add 'active' class to family tab, and remove from others
    acquaintancesTab.removeClass('active');
    followingTab.removeClass('active');
    friendsTab.removeClass('active');
    familyTab.addClass('active');

    // Show family container, and hide others
    acquaintancesContainer.css('display', 'none');
    followingContainer.css('display', 'none');
    friendsContainer.css('display', 'none');
    familyContainer.css('display', 'block');

    return false;
  });

  // Trigger when acquaintances is clicked
  acquaintancesTab.click(() => {
    // Add 'active' class to acquaintances tab, and remove from others
    familyTab.removeClass('active');
    followingTab.removeClass('active');
    friendsTab.removeClass('active');
    acquaintancesTab.addClass('active');

    // Show acquaintances container, and hide others
    familyContainer.css('display', 'none');
    followingContainer.css('display', 'none');
    friendsContainer.css('display', 'none');
    acquaintancesContainer.css('display', 'block');

    return false;
  });

  // Trigger when following tab is clicked
  followingTab.click(() => {
    // Add 'active' class to following tab, and remove from others
    familyTab.removeClass('active');
    acquaintancesTab.removeClass('active');
    friendsTab.removeClass('active');
    followingTab.addClass('active');

    // Show following container, and hide others
    familyContainer.css('display', 'none');
    acquaintancesContainer.css('display', 'none');
    friendsContainer.css('display', 'none');
    followingContainer.css('display', 'block');

    return false;
  });

  // Show 'Loading...' text in all connection tabs
  friendsList.html(
    '<a href="#" class="list-group-item disabled" tabindex="-1">' +
      '<h6 class="mb-0">' +
      'Loading...' +
      '</h6>' +
      '</a>'
  );

  familyList.html(
    '<a href="#" class="list-group-item disabled" tabindex="-1">' +
      '<h6 class="mb-0">' +
      'Loading...' +
      '</h6>' +
      '</a>'
  );

  acquaintancesList.html(
    '<a href="#" class="list-group-item disabled" tabindex="-1">' +
      '<h6 class="mb-0">' +
      'Loading...' +
      '</h6>' +
      '</a>'
  );

  followingList.html(
    '<a href="#" class="list-group-item disabled" tabindex="-1">' +
      '<h6 class="mb-0">' +
      'Loading...' +
      '</h6>' +
      '</a>'
  );

  // Get user's connections
  $.getJSON('/' + username + '/connections', (json) => {
    const friends = json.friends;
    const family = json.family;
    const acquaintances = json.acquaintances;
    const following = json.following;

    const friendsLength = friends.length;
    const familyLength = family.length;
    const acquaintancesLength = acquaintances.length;
    const followingLength = following.length;

    // If there are no connections :(
    if (!followingLength) {
      // Show text accordingly
      friendsList.html(
        '<p class="text-center text-muted">Nothing to see here!</p>'
      );
      familyList.html(
        '<p class="text-center text-muted">Nothing to see here!</p>'
      );
      acquaintancesList.html(
        '<p class="text-center text-muted">Nothing to see here!</p>'
      );
      followingList.html(
        '<p class="text-center text-muted">Nothing to see here!</p>'
      );
    } else {
      // Remove any html
      friendsList.html('');
      familyList.html('');
      acquaintancesList.html('');
      followingList.html('');

      // If there are no friends :(
      if (!friendsLength) {
        // Show text accordingly
        friendsList.html(
          '<p class="text-center text-muted">Nothing to see here!</p>'
        );
      } else {
        // Append friends one by one in list
        for (let i = 0; i < friendsLength; i++) {
          friendsList.append(
            '<a href="/' +
              friends[i] +
              '" class="list-group-item list-group-item-action">' +
              '<div class="d-flex w-100 justify-content-between">' +
              '<h6 class="mb-0">' +
              friends[i] +
              '</h6>' +
              '<small class="text-primary">' +
              'Visit Profile' +
              '</small>' +
              '</div>' +
              '</a>'
          );
        }
      }

      // If there is no family :(
      if (!familyLength) {
        // Show text accordingly
        familyList.html(
          '<p class="text-center text-muted">Nothing to see here!</p>'
        );
      } else {
        // Append family one by one in list
        for (let i = 0; i < familyLength; i++) {
          familyList.append(
            '<a href="/' +
              family[i] +
              '" class="list-group-item list-group-item-action">' +
              '<div class="d-flex w-100 justify-content-between">' +
              '<h6 class="mb-0">' +
              family[i] +
              '</h6>' +
              '<small class="text-primary">' +
              'Visit Profile' +
              '</small>' +
              '</div>' +
              '</a>'
          );
        }
      }

      // If there are no acquaintances :(
      if (!acquaintancesLength) {
        // Show text accordingly
        acquaintancesList.html(
          '<p class="text-center text-muted">Nothing to see here!</p>'
        );
      } else {
        // Append acquaintances one by one in list
        for (let i = 0; i < acquaintancesLength; i++) {
          acquaintancesList.append(
            '<a href="/' +
              acquaintances[i] +
              '" class="list-group-item list-group-item-action">' +
              '<div class="d-flex w-100 justify-content-between">' +
              '<h6 class="mb-0">' +
              acquaintances[i] +
              '</h6>' +
              '<small class="text-primary">' +
              'Visit Profile' +
              '</small>' +
              '</div>' +
              '</a>'
          );
        }
      }

      // Append connections one by one in list
      for (let i = 0; i < followingLength; i++) {
        followingList.append(
          '<a href="/' +
            following[i] +
            '" class="list-group-item list-group-item-action">' +
            '<div class="d-flex w-100 justify-content-between">' +
            '<h6 class="mb-0">' +
            following[i] +
            '</h6>' +
            '<small class="text-primary">' +
            'Visit Profile' +
            '</small>' +
            '</div>' +
            '</a>'
        );
      }
    }
  });

  // Trigger when aboutTab is clicked
  aboutTab.click(() => {
    // Add 'active' class to aboutTab, and remove from other
    activityTab.removeClass('active');
    connectionsTab.removeClass('active');
    aboutTab.addClass('active');

    // Show aboutContainer, and hide other
    activityContainer.css('display', 'none');
    connectionsContainer.css('display', 'none');
    aboutContainer.css('display', 'block');

    return false;
  });

  // Show Loading... text while loading 'about' info
  aboutList.html(
    '<a href="#" class="list-group-item disabled" tabindex="-1">' +
      '<h6 class="mb-0">' +
      'Loading...' +
      '</h6>' +
      '</a>'
  );

  // Get 'about' info
  $.getJSON('/' + username + '/about', (json) => {
    // Populate aboutList with 'about' info
    aboutList.html(
      '<a href="#" class="list-group-item list-group-item-action" ' +
        'data-toggle="modal" data-target="#username-change-modal">' +
        '<h6 class="mb-0">' +
        'Username' +
        '<small class="text-muted float-right">' +
        username +
        '</small>' +
        '</h6>' +
        '</a>' +
        '<a href="#" class="list-group-item list-group-item-action" ' +
        'data-toggle="modal" data-target="#password-change-modal">' +
        '<h6 class="mb-0">' +
        'Password' +
        '<small class="text-muted float-right">' +
        '<em>Hidden</em>' +
        '</small>' +
        '</h6>' +
        '</a>' +
        '<a href="#" class="list-group-item list-group-item-action" ' +
        'data-toggle="modal" data-target="#consent-change-modal">' +
        '<h6 class="mb-0">' +
        'Consent' +
        '<small class="text-muted float-right">' +
        (json.consent ? 'Yes' : 'No') +
        '</small>' +
        '</h6>' +
        '</a>' +
        '<a href="#" class="list-group-item disabled" tabindex="-1">' +
        '<h6 class="mb-0">' +
        'Joined' +
        '<small class="text-muted float-right">' +
        new Date(json.joined)
          .toString()
          .split(' ')
          .slice(1, 4)
          .join(' ') +
        '</small>' +
        '</h6>' +
        '</a>'
    );
  });

  // Function to add username check on link
  function addCheck() {
    // Trigger when check is clicked
    $('#check').click(() => {
      const newUsernameValue = $('#new-username').val();

      // Show Please Wait text
      newUsernameSmallText
        .removeClass('text-danger text-success')
        .text('Please Wait...');

      // Check if new username is available
      $.getJSON('/check/' + newUsernameValue, (json) => {
        // Available
        if (json.available)
          newUsernameSmallText
            .removeClass('text-danger')
            .addClass('text-success')
            .text('Available!');
        // Not available
        else
          newUsernameSmallText
            .removeClass('text-success')
            .addClass('text-danger')
            .text('Not Available');
      });

      return false;
    });
  }

  // Trigger on every input change in new-username field
  $('#new-username').on('input', () => {
    const newUsernameValue = $('#new-username').val();

    // If newUsername field is empty
    if (!newUsernameValue) newUsernameSmallText.text('');
    // If non-alphabet character is present
    else if (newUsernameValue.match(/[^a-z]/i))
      newUsernameSmallText
        .removeClass('text-success')
        .addClass('text-danger')
        .text('Alphabet Characters Only');
    // If newUsername is short
    else if (newUsernameValue.length < 4)
      newUsernameSmallText
        .removeClass('text-success')
        .addClass('text-danger')
        .text('Minimum 4 Characters');
    // Show check link
    else {
      newUsernameSmallText
        .removeClass('text-danger text-success')
        .html('<a href="#" id="check">Check</a>');

      // Add username check on link
      addCheck();
    }
  });

  // Trigger on every input in password field
  $('#password').on('input', () => {
    const passwordValue = $('#password').val();

    // If password field is empty
    if (!passwordValue) passwordSmallText.text('');
    // If password is short
    else if (passwordValue.length < 6)
      passwordSmallText
        .removeClass('text-success')
        .addClass('text-danger')
        .text('Minimum 6 Characters');
    // Remove small text
    else passwordSmallText.text('');
  });

  // Trigger on username-change-form submission
  $('#username-change-form').submit(() => {
    const newUsernameValue = $('#new-username').val();
    const passwordValue = $('#password').val();

    // If non-alphabet character is present
    if (newUsernameValue.match(/[^a-z]/i)) {
      newUsernameSmallText
        .removeClass('text-success')
        .addClass('text-danger')
        .text('Alphabet Characters Only');

      return false;
    }
    // If newUsernameValue is short
    else if (newUsernameValue.length < 4) {
      newUsernameSmallText
        .removeClass('text-success')
        .addClass('text-danger')
        .text('Minimum 4 Characters');

      return false;
    }
    // If password is short
    else if (passwordValue.length < 6) {
      passwordSmallText
        .removeClass('text-success')
        .addClass('text-danger')
        .text('Minimum 6 Characters');

      return false;
    }

    // Valid inputs
    return true;
  });

  // Trigger on every input change in old password field
  $('#old-password').on('input', () => {
    const oldPassword = $('#old-password').val();

    // If old password field is empty
    if (!oldPassword) oldPasswordSmallText.text('');
    // If old password is short
    else if (oldPassword.length < 6)
      oldPasswordSmallText
        .removeClass('text-success')
        .addClass('text-danger')
        .text('Minimum 6 Characters');
    // Remove small text
    else oldPasswordSmallText.text('');
  });

  // Trigger on every input change in new password field
  $('#new-password').on('input', () => {
    const newPassword = $('#new-password').val();

    // If new password field is empty
    if (!newPassword) newPasswordSmallText.text('');
    // If new password is short
    else if (newPassword.length < 6)
      newPasswordSmallText
        .removeClass('text-success')
        .addClass('text-danger')
        .text('Minimum 6 Characters');
    // Remove small text
    else newPasswordSmallText.text('');
  });

  // Trigger on password-change-form submission
  $('#password-change-form').submit(() => {
    const oldPassword = $('#old-password').val();
    const newPassword = $('#new-password').val();

    // If invalid old password
    if (oldPassword.length < 6) {
      oldPasswordSmallText
        .removeClass('text-success')
        .addClass('text-danger')
        .text('Minimum 6 Characters');

      return false;
    }
    // If invalid new password
    else if (newPassword.length < 6) {
      newPasswordSmallText
        .removeClass('text-success')
        .addClass('text-danger')
        .text('Minimum 6 Characters');

      return false;
    }

    // Valid inputs
    return true;
  });

  // Trigger on every input in consent-password field
  $('#consent-password').on('input', () => {
    const consentPasswordValue = $('#consent-password').val();

    // If consent password field is empty
    if (!consentPasswordValue) consentPasswordSmallText.text('');
    // If consent password is short
    else if (consentPasswordValue.length < 6)
      consentPasswordSmallText
        .removeClass('text-success')
        .addClass('text-danger')
        .text('Minimum 6 Characters');
    // Remove small text
    else consentPasswordSmallText.text('');
  });

  // Trigger on every consent-change-form submission
  $('#consent-change-form').submit(() => {
    const consentPasswordValue = $('#consent-password').val();

    // If consent password is short
    if (consentPasswordValue.length < 6) {
      consentPasswordSmallText
        .removeClass('text-success')
        .addClass('text-danger')
        .text('Minimum 6 Characters');

      return false;
    }

    // Valid input
    return true;
  });
});
