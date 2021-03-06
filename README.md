# SocialApiTrial
SocialApiTrial is a proof of concept application of Twitter API services without using any 3rd party twitter library. 
Contains pure Twitter API refenrence integrations

## UI
SocialApiTrial application consist of 5 different fragments and 2 root activity. Each root activity symbolize different flows of features 
and also hold a container layout in order to manage fragments which will be controlled by navigation component.

[APK Link (https://drive.google.com/file/d/1eIR9T2MoW8vPkkPISeJbvE5ssqIcUl2D/view?usp=sharing)](https://drive.google.com/file/d/1eIR9T2MoW8vPkkPISeJbvE5ssqIcUl2D/view?usp=sharing)

Fragments :
* SplashFragment
* LoginFragment
* UserListFragment
* ViewPagerContentFragment
* UserDetailFragment

<img src="https://user-images.githubusercontent.com/21987335/111656768-e278e380-881b-11eb-8f38-05c0959c733f.gif" width="400"/>

## 3rd party lib. usages & Tech Specs
* Patterns
    - MVVM design pattern
    - Repository pattern for data management
* JetPack Libs
    - Navigation Component
* Twitter API integration
* Retrofit
* Kotlin Coroutines
* LiveData
* Glide image loading 
* Facebook Shimmer Lib.
* Lottie animation Lib.
* Moshi Json handler
* Scalar Converters
* Timber Client logging
* Dependency Injection (HILT) 
* DataBinding
* RecyclerView with List Adapter and DiffUtil
* ViewPager 2 and Tablayout
* Single Activity multiple Fragments approach
* Unit testing samples & HILT integrations for testing
* MockK library for unit testing
* Junit5
* Thruth (assertions)

## Twitter API Service Integrations
* POST oauth/request_token		-> request token
* GET oauth/authorize			-> authorize
* POST oauth/access_token		-> get access token
* POST oauth/invalidate_token	-> invalidate access token
* GET followers/list  			-> Follower list
* GET friends/list				-> Following list
* GET users/show				-> User info
* GET statuses/user_timeline	-> User timeLine

## Future Improvements
* Paging3 library integration for recycler views
* More testing code

## MVVM Design
Each UI part or application is assumed as independent feature so each feature has view, viewmodel and model(shown as data group) part of architecture.
Necessary abstractions are applied so as to ensure injections and polymorphic usages of classes.

UI, ViewModel, Repository layers are separated and they communicate each other with the help of event base informer(Resource.kt). 

## Data sharing / storing for fragments
In this task example, I have used fragment/activity scoped viewmodels in order to share / store data for fragments. For Activity scoped viewmodels hold data until activity is destroyed.
There is no need for usage of local storage. (In this case ROOM database can be used.)

## Error Handling 
SocialApiTrial application makes network requests in order to acquire some user dependent data and authorization. Network related or device related errors can be occurred during runtime. 

For network requests HTTP responses between 200 and 299 indicates that successful communication, between 400 and 599
indicates error which has to be handled.

Repository layer handles that errors and informs ViewModel Layer with Resource.kt type. ViewModel Layer is informs UI layer if it necessary.

Notes: `Optional json field should be marked as nullable ( ? ) otherwise service response can not be mapped to data class`

## Testing
For unit tests, I created some utility testing classes. MockK and JUnit5 is used for unit testing. Junit4 is used for integration testing

Context dependent tests are placed under androidTest folder, Independent class and utility tests are placed 
under test folder.

## Request Header Management
This integration contains OAuth 1.0 (3-legged authentication) of Twitter API. 
For each request, some header parameters has to be provided by application. 

##### Request Manipulation for header management
Retrofit and httpClient is initiated in hilt module so During the initialization additional interceptors are applied
in order to manipulate each request.

## Application UI Flow

#### SplashFragment
App is initiates with lottie animation of twitter logo. After animation app navigates to login fragment. 

#### LoginFragment
Login is handled over Twitter API OAuth 1.0 (3-legged) authentication. Login flow is started with twitter button. 
After successful authentication some secret keys/token is stored for further api calls and app navigates to secure dashboard.

#### UserListFragment
In secure dashboard first page is UserListFragment. In this fragment followers and friends are listed in recyclerviews which is placed in view pager. 
Each click action navigates user to corresponding peoples timeline(UserDetailFragment).

#### ViewPagerContentFragment
In UserListFragment viewpager is used. For each tab differend fragment part is initiated. ViewPagerContentFragment is provides a solution for the management of these tab instances

#### UserDetailFragment
UserDetailFragment is consist of timeline data of followers or friends.

#### ScreenShots 
<img src="https://github.com/AttilaAKINCI/SocialApiTrial/blob/twitter/images/splash.png" width="200">   <img
src="https://github.com/AttilaAKINCI/SocialApiTrial/blob/twitter/images/not-logged-in.png" width="200">   <img
src="https://github.com/AttilaAKINCI/SocialApiTrial/blob/twitter/images/logged-in.png" width="200">   <img
src="https://github.com/AttilaAKINCI/SocialApiTrial/blob/twitter/images/twitter-login-1.png" width="200">   <img
src="https://github.com/AttilaAKINCI/SocialApiTrial/blob/twitter/images/twitter-login-2.png" width="200">   <img
src="https://github.com/AttilaAKINCI/SocialApiTrial/blob/twitter/images/twitter-login-3.png" width="200">   <img
src="https://github.com/AttilaAKINCI/SocialApiTrial/blob/twitter/images/dashboard-followers.png" width="200">   <img
src="https://github.com/AttilaAKINCI/SocialApiTrial/blob/twitter/images/dashboard-friends.png" width="200">   <img
src="https://github.com/AttilaAKINCI/SocialApiTrial/blob/twitter/images/user-timeline.png" width="200">   

# License

The code is licensed as:

```
Copyright 2021 Attila Akıncı

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

