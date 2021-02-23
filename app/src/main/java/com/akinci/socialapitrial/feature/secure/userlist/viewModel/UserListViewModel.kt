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

    // signOutEventHandler sends event feedback to UI layer(Fragment)
    private val _signOutEventHandler = MutableLiveData<Event<Resource<String>>>()
    val signOutEventHandler : LiveData<Event<Resource<String>>> = _signOutEventHandler

    // user info
    var userInfo = MutableLiveData<UserResponse>()

    // follower list
    var followers = MutableLiveData<List<UserResponse>>()

    // friend list
    var friends = MutableLiveData<List<UserResponse>>()

    init {
        Timber.d("UserListViewModel created..")
    }

    /** Called when UserListFragment created. **/
    fun fetchDashboardData(){
        //TODO check data is fetched before......
        getUserInfo()
        getFollowers()
        getFollowings()
    }

    private fun getFollowers(){

    }

    private fun getFollowings(){

    }

    private fun getUserInfo(){
        viewModelScope.launch {
            val userId = sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_ID) ?: ""
            val userName = sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_NAME) ?: ""

            if(userId.isNotEmpty() && userName.isNotEmpty()){
                when(val userResponse = userListRepository.getUserInfo(userId.toInt(), userName)) {
                    is Resource.Success -> {
                        // user info is fetched
                    }
                    is Resource.Error -> {
                        // error occurred while fetching user info

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
                        _signOutEventHandler.postValue(Event(Resource.Success("Signed out....")))
                    }
                }
                is Resource.Error -> {
                    // send signout error messages to fragment
                    _signOutEventHandler.postValue(Event(Resource.Error(signOutResponse.message)))
                }
            }
        }
    }



}