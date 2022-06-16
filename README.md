Original App Design Project - README Template
===

# Arcade Cabinet Locator

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
A locator for arcade games worldwide. Users can search for a game, and a map will show all the arcades where it is located. Users can upload requests to have an arcade cabinet location uploaded onto the app, which will be verified by developers. Users can also flag existing arcade locations if they are no longer valid.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Gaming/Maps
- **Mobile:** Mobile users can see what arcade games are near them so long as they have their phone and an internet connection.
- **Story:** A user who wants to find a certain arcade game near them will search it on the app, and results will show where that certain arcade game is located.
- **Market:** Targetted towards those who enjoy arcade games and want to find a certain game near them.
- **Habit:** The user would use this app whenever they would like to find an arcade game near them, or if they want to contribute to the community and upload locations themselves.
- **Scope:** The app will primarily be a database (since there is no way to locate arcade games with an API) that users can request to upload locations to.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Users should see a map of the Earth with markers showing the locations of arcade cabinets.
* Users should be able to submit a request to have an arcade cabinet location placed on the map.
* Users can search an arcade game location.
* Users will be able to flag an arcade game location if it is incorrect.

**Optional Nice-to-have Stories**

* Users can discuss/comment on arcade cabinet locations
    * Users can also reply to and report comments, using disqus
* Users can sign up for push notification alerts when a certain arcade game gets verified near them
* Application can display information for each arcade game by pulling it from the internet

### 2. Screen Archetypes

* Login / Register
   * Users can log in, continue as guest, or make a new account.
* Home
   * Users can search for arcade games and see them on the map.
* More Info
    * Users can get more information on an arcade game, and do other actions.
* Report
    * Users can report a location they believe to be incorrect to developers for review.
* Upload
    * Users can upload a location for a game to developers for review.
* Profile
    * User can see information on who they are and settings for their profile.

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home
* Upload
* Profile

**Flow Navigation** (Screen to Screen)

* Home Screen
    * Users can see and search for game locations
* Upload Screen
    * Users can open the camera to take a picture and open the photo gallery to upload one.
* Profile
    * Contains several settings for the user.

## Wireframes
<img src="wireframe.png" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
### Models
#### Game Location
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the game location (default field) |
   | author        | Pointer to User | author of game location |
   | address     | String | address of game |
   | image         | File     | image that user posts of a game |
   | description       | String   | description / notes by author |
   | gameDescription | String | description of game, pulled from wikipedia |
   | commentsCount | Number   | number of comments that has been posted to an image |
   | createdAt     | DateTime | date when post is created (default field) |
   | updatedAt     | DateTime | date when post is last updated (default field) |
   | verified     | boolean | true if location is verified to be displayed on map, false otherwise |

#### User
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user (default field) |
   | username        | String | image author |
   | password         | String     | user password |
   | profilePic       | File   | user profile picture |
   | joined | DateTime   | date user created profile |
   | requests | Array of pointers to requests  | list of requests for game locations user has made |
   | isDev      | boolean   | unique id for the user (default field) |

#### Report
   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the report (default field) |
   | author        | Pointer to User | author of report |
   | post         | Pointer to Game Location or Comment    | unique id of reported game location or comment |
   | description       | String   | description of report |
   | approved     | boolean | true if report is confirmed, false otherwise |
   | reportType    | String | either comment or game |

#### Comment
 Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | author        | Pointer to User | author of comment |
   | contents     | String | contents of comment |
   | attachments         | File     | attachments to comment |
   | likes       | integer   | number of likes in comment |
   | replies | Array of comment pointers   | list of comments that have been replied to this one |
   | createdAt     | DateTime | date when comments was created (default field) |
   | edited     | boolean | true if comment has been edited, false otherwise |

### Networking
#### List of network requests by screen

- Home Map Screen 
    - (Create/POST) Create a request for a new location or to update a location
    - (Read/GET) Get all the game locations to display on the map
        ```java
        ParseQuery<Location> query = ParseQuery.getQuery(Location.class);
        // include data referred by address
        query.include(Location.KEY_ADDRESS);
        // start an asynchronous call for addresses
        query.findInBackground(new FindCallback<Location>() {
            @Override
            public void done(List<Location> posts, ParseException e) {
                // check for errors
                if (e != null) {
                    Log.e(TAG, "Issue with getting Locations", e);
                    return;
                }
                // TODO: Process locations
            }
        });
        ```
- Game information Screen
    - (Create/POST) Create a comment under this game
    - (Create/POST) Like and report comments
    - (Create/POST) Create a game report
    - (Create/POST) If user is author of game location, they can create a request for an update or for deletion
    - (Read/GET) Query game description from Wikipedia API
    - (Read/GET) Display all existing comment and comment information
    - (Update/PUT) User can update their existing comments
    - (Delete) Delete a comment
- Profile Screen
    - (Read/GET) Read user information
    - (Update/PUT) User can update profile info, including username, password, and profile image
    - (Delete) User can delete their account
- [OPTIONAL: List endpoints if using existing API such as Yelp]
