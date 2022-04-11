Original App Design Project
===

# School Hub

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
School Hub is an app for students to know what organizations and clubs that exists on campus. 
It allows for users to connect with different clubs and be notified of any events they organize.
The app also provides a guide to nearby resources and amenities that are made available to students by the college.

### App Evaluation
[Evaluation of your app across the following attributes]
- **Category:** Social Networking
- **Mobile:** This app is going to be developed for mobile devices only.
- **Story:** Connects users to different on campus clubs. The user can subscribe to them to get notified of any events and activities.
- **Market:** This app is for college students.
- **Habit:** This app could be used as often as an individual wants depending on what kind of clubs they're looking for.
- **Scope:** First we would start by making a list of all campus clubs available to view and subscribe to. Once a students subscribe to a club they will have access to all the activities and benefits the club provides.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Users logs in with username & password to see app home screen
* On Login user is presented with a list of existing clubs and a map of on campus amneties
* Student Profiles contain personal information & list of clubs they are subcribed to
* Club Profiles contain information about the clubs and allows users to subcribe to them
* Campus Map displays the map of the school
* List of on campus amenities for students

**Optional Nice-to-have Stories**

* Settings Screen to allow users to edit their profile screen
* Student Profile's club list can contain a notification icon

### 2. Screen Archetypes

* Login Screen
   * User can log in to their accounts
* Stream
   * User can view a list of recent announcements
   * User can click on announcements to go to the associated club screen
* Profile Screens
   * Students will have their own profile profiles
   * Clubs will have their own club profiles
* Maps
   * Users will be able to see nearby amenities through the use of maps

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Home
* Profile

**Flow Navigation** (Screen to Screen)

* Forced Log-in -> Account creation if no login is available
* Home -> List of Clubs
* Home -> List of Amenities
* List Of Clubs -> Club Profile
* User Profile -> Club Profile

## Wireframes
<img src="School Hub (Student) Porfile App Wireframe.png" width=600>
<img src="Student Sign Up Process Wireframe.jpg" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
#### Post

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | clubAdmin        | Pointer to User| post author |
   | clubMember        | Pointer to User| post author |
   | image         | File     | image that user posts |
   | caption       | String   | image caption by author |
   | commentsCount | Number   | number of comments that has been posted to an image |
   | upvoteCount    | Number   | number of upvotes for the post |
   | downvoteCount    | Number   | number of downvotes for the post |

#### Events

   | Property      | Type     | Description |
   | ------------- | -------- | ------------|
   | objectId      | String   | unique id for the user post (default field) |
   | clubAdmin        | Pointer to User| post author |
   | image         | File     | image that user posts |
   | eventDecription       | String   | image caption by author |
   | eventLocation       | String   | event location |
   | eventDate       | Datetime   | event date |
   | commentsCount | Number   | number of comments that has been posted to an image |
   | upvoteCount    | Number   | number of upvotes for the post |
   | downvoteCount    | Number   | number of downvotes for the post |
 
### Networking
- Home Feed Screen
  - Query all posts from different clubs
  - (Create/POST) Create a new like on a post
  - (Delete) Delete existing like
  - (Create/POST) Create a new comment on a post
  - (Delete) Delete existing comment

- Create Post Screen (Only Admins)
  - (Create/POST) Create a new post object

- Student Profile Screen
  - (Read/GET) Query logged in user object
- User Management Screen
   - (Read/GET) Query members list object
   - (Delete) Delete existing member
