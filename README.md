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
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
