$(document).ready(() => {
  const username = $('#username').text();

  const activityTab = $('#activity-tab');
  const searchTab = $('#search-tab');
  const notifsTab = $('#notifs-tab');

  const activityContainer = $('#activity-container');
  const searchContainer = $('#search-container');
  const notifsContainer = $('#notifs-container');

  const activityList = $('#activity-list');
  const activityEndText = $('#activity-end-text');

  const searchInputField = $('#search-input');
  const searchButton = $('#search-button');
  const searchResult = $('#search-result');

  const redColor = '#dc3545';

  let skip = 0;
  const limit = 10;

  // Trigger when activity-tab is clicked
  activityTab.click(() => {
    // Add 'active' class to activityTab, and remove from others
    searchTab.removeClass('active');
    notifsTab.removeClass('active');
    activityTab.addClass('active');

    // Show activityContainer, and hide others
    searchContainer.css('display', 'none');
    notifsContainer.css('display', 'none');
    activityContainer.css('display', 'block');

    return false;
  });

  // Function to get home posts
  function getHomePosts(skip, limit) {
    // Get posts
    $.getJSON(username + '/posts/home/' + skip + '/' + limit, (json) => {
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

    // Get home posts
    getHomePosts(skip, limit);

    // Increment 'skip' by 10
    skip += 10;

    return false;
  });

  // Trigger when search-tab is clicked
  searchTab.click(() => {
    // Add 'active' class to searchTab, and remove from others
    activityTab.removeClass('active');
    notifsTab.removeClass('active');
    searchTab.addClass('active');

    // Show searchContainer, and hide others
    activityContainer.css('display', 'none');
    notifsContainer.css('display', 'none');
    searchContainer.css('display', 'block');

    return false;
  });

  // Trigger when notifs-tab is clicked
  notifsTab.click(() => {
    // Add 'active' class to notifsTab, and remove from others
    activityTab.removeClass('active');
    searchTab.removeClass('active');
    notifsTab.addClass('active');

    // Show notifsContainer, and hide others
    activityContainer.css('display', 'none');
    searchContainer.css('display', 'none');
    notifsContainer.css('display', 'block');

    return false;
  });

  // Function to trigger search
  function triggerSearch() {
    // Get input value
    const searchInputValue = searchInputField.val();

    // If empty input
    if (!searchInputValue) {
      searchResult.css('display', 'none');
      searchInputField.css('border-color', redColor);
    } else {
      // Show Loading... card
      searchResult
        .css('display', 'block')
        .html(
          '<div class="card-body">' +
            '<h5 class="card-title">' +
            'Please wait...' +
            '</h5>' +
            '</div>'
        );

      // Remove border-color
      searchInputField.css('border-color', '');

      // Check username existence
      $.getJSON('/check/' + searchInputValue, (json) => {
        // User is registered
        if (!json.available) {
          searchResult.html(
            '<div class="card-body">' +
              '<h5 class="card-title">' +
              searchInputValue +
              '</h5>' +
              '<h6 class="card-subtitle mb-2 text-muted">' +
              'Registered User' +
              '</h6>' +
              '<a href="/' +
              searchInputValue +
              '" class="card-link">' +
              'Visit Profile' +
              '</a>' +
              '</div>'
          );
        } else {
          // User is not registered
          searchResult.html(
            '<div class="card-body">' +
              '<h5 class="card-title">' +
              searchInputValue +
              '</h5>' +
              '<h6 class="card-subtitle mb-2 text-muted">' +
              'Not Registered' +
              '</h6>' +
              '</div>'
          );
        }
      });
    }
  }

  // Trigger when 'enter' is pressed
  searchInputField.keyup((event) => {
    // Keycode of 'enter' is 13
    if (event.which === 13) triggerSearch();
  });

  // Trigger when search button is clicked
  searchButton.click(() => {
    triggerSearch();
  });
});
