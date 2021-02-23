package com.akinci.socialapitrial.feature.secure.userlist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.socialapitrial.common.helper.Event
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.storage.LocalPreferenceConfig
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.feature.secure.userlist.data.output.follower.FollowerOrFriendResponse
import com.akinci.socialapitrial.feature.secure.userlist.data.output.follower.UserResponse
import com.akinci.socialapitrial.feature.secure.userlist.repository.UserListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
        private val userListRepository: UserListRepository,
        private val sharedPreferences: Preferences
) : ViewModel() {

    // eventHandler sends event feedback to UI layer(Fragment)
    // success is used for sign out event
    // error case is used for generic error passing
    private val _eventHandler = MutableLiveData<Event<Resource<String>>>()
    val eventHandler : LiveData<Event<Resource<String>>> = _eventHandler

    // user info
    var userInfo = MutableLiveData<UserResponse>()

    // follower list
    private var followersCursor = -1L
    var followers = MutableLiveData<List<UserResponse>>()

    // friend list
    private var followingsCursor = -1L
    var friends = MutableLiveData<List<UserResponse>>()

    init {
        Timber.d("UserListViewModel created..")
    }

    /** Called when UserListFragment created. **/
    fun fetchInitialDashboardData(){
        // fetch each data if it's not fetched before
       //   if(userInfo.value == null){ getUserInfo() }
      //  if(followers.value == null){ getFollowers() }
      //  if(friends.value == null){ getFollowings() }
    }

    private fun getFollowers(){
        viewModelScope.launch {
            when(val followersResponse = userListRepository.fetchFollowers(followersCursor)) {
                is Resource.Success -> {
                    // followers response fetched
                    Timber.d("Followers list is fetched...")
                    followersCursor = followersResponse.data?.next_cursor ?: -1L
                    followers.value = followersResponse.data?.users
                }
                is Resource.Error -> {
                    // error occurred while fetching followers
                    _eventHandler.postValue(Event(Resource.Error(followersResponse.message)))
                }
            }
        }
    }

    private fun getFollowings(){
        viewModelScope.launch {
            val cursor = -1
            when(val followingsResponse = userListRepository.fetchFollowings(followingsCursor)) {
                is Resource.Success -> {
                    // following response fetched
                    Timber.d("Followings list is fetched...")
                    followingsCursor = followingsResponse.data?.next_cursor ?: -1L
                    friends.value = followingsResponse.data?.users
                }
                is Resource.Error -> {
                    // error occurred while fetching followings
                    _eventHandler.postValue(Event(Resource.Error(followingsResponse.message)))
                }
            }
        }
    }

    private fun getUserInfo(){
        viewModelScope.launch {
            val userId = sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_ID) ?: ""
            val userName = sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_NAME) ?: ""

            if(userId.isNotEmpty() && userName.isNotEmpty()){
                when(val userResponse = userListRepository.getUserInfo(userId.toLong(), userName)) {
                    is Resource.Success -> {
                        // user info is fetched
                        Timber.d("User info is fetched...")
                        userInfo.value = userResponse.data!!
                    }
                    is Resource.Error -> {
                        // error occurred while fetching user info
                        _eventHandler.postValue(Event(Resource.Error(userResponse.message)))
                    }
                }
            }
        }
    }

    /** bind to sign out button. **/
    fun signOut(){
        viewModelScope.launch {
            when(val signOutResponse = userListRepository.signOut()) {
                is Resource.Success -> {
                    // signOut request is completed..
                    signOutResponse.data?.let{
                        Timber.d("Signing out...")
                        /** delete all session related keys **/
                        sharedPreferences.setStoredTag(LocalPreferenceConfig.OAUTH_TOKEN, "")
                        sharedPreferences.setStoredTag(LocalPreferenceConfig.OAUTH_TOKEN_SECRET, "")
                        sharedPreferences.setStoredTag(LocalPreferenceConfig.OAUTH_TOKEN_VERIFIER, "")
                        sharedPreferences.setStoredTag(LocalPreferenceConfig.ACCESS_TOKEN, "")
                        sharedPreferences.setStoredTag(LocalPreferenceConfig.ACCESS_TOKEN_SECRET, "")
                        sharedPreferences.setStoredTag(LocalPreferenceConfig.USER_ID, "")
                        sharedPreferences.setStoredTag(LocalPreferenceConfig.USER_NAME, "")
                        sharedPreferences.setStoredTag(LocalPreferenceConfig.IS_LOGGED_IN, "")

                        //Pass sign out action to fragment
                        _eventHandler.postValue(Event(Resource.Success("Signed out....")))
                    }
                }
                is Resource.Error -> {
                    // send signout error messages to fragment
                    _eventHandler.postValue(Event(Resource.Error(signOutResponse.message)))
                }
            }
        }
    }
}