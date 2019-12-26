$(document).ready(() => {
  const username = $('#username').text();

  const activityTab = $('#activity-tab');
  const aboutTab = $('#about-tab');

  const activityContainer = $('#activity-container');
  const aboutContainer = $('#about-container');

  const activityList = $('#activity-list');
  const activityEndText = $('#activity-end-text');

  const aboutList = $('#about-list');

  let skip = 0;
  const limit = 10;

  // Trigger when activity tab is clicked
  activityTab.click(() => {
    aboutTab.removeClass('active');
    activityTab.addClass('active');

    aboutContainer.css('display', 'none');
    activityContainer.css('display', 'block');

    return false;
  });

  // Function to get connection-only and public posts
  function getConnectionPosts(skip, limit) {
    // Get user's posts
    $.getJSON(username + '/posts/connection/' + skip + '/' + limit, (json) => {
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

    // Get connection-only and public posts
    getConnectionPosts(skip, limit);

    // Increment 'skip' by 10
    skip += 10;

    return false;
  });

  // Trigger when about tab is clicked
  aboutTab.click(() => {
    activityTab.removeClass('active');
    aboutTab.addClass('active');

    activityContainer.css('display', 'none');
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
      '<a href="#" class="list-group-item disabled" tabindex="-1">' +
        '<h6 class="mb-0">' +
        'Username' +
        '<small class="text-muted float-right">' +
        username +
        '</small>' +
        '</h6>' +
        '</a>' +
        '<a href="#" class="list-group-item disabled" tabindex="-1">' +
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
});
