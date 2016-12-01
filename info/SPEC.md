# Web Interface API Spec

## Main Page

This page will provide an interface for finding GitHub users
relevant to a particular set of terms or a full issue (depending on what the end user
puts in the text box). It should contain a single large editable text field.
Whenever this text field is updated (or alternatively whenever the user enters a new word),
the page should make a POST call to the backend address `/users/search` with the following contents:

```javascript
{
  "query": "<text from textbox>"
}
```

The response to this request will contain a list of users with the following structure:

```javascript
{
  "users":
  [
    {
      "id": "jalexanderqed",
      "avatar": "https://avatars1.githubusercontent.com/u/9711028?v=3&s=460",
      "keywords": [
        "memory",
        "segfault",
        "rocksdb",
        "allocate"
      ]
    },
    ...
  ]
}
```

`avatar` is a link to the user's GitHub user profile picture, and `keywords`
is an array of words from topics this user specializes in. These users should be displayed on the page in
a list, either below or next to the main input box. Clicking on a user in this list should link to the page `/users/<id>` described below.

## User Page

These pages (which will be built dynamically by the backend) will include the user's GitHub photo and a list of the users top topics. In this case, a "topic" is the internal number of the topic used by the model followed by a list of words that describe that topic. The page should also include a list of links to the issues this user has commented on so we can demonstrate the original source of the data.
