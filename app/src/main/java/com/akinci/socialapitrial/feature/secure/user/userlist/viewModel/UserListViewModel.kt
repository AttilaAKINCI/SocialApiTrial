package com.akinci.socialapitrial.feature.secure.user.userlist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.socialapitrial.common.coroutine.CoroutineContextProvider
import com.akinci.socialapitrial.common.helper.Event
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.storage.LocalPreferenceConfig
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.feature.secure.login.repository.LoginRepository
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse
import com.akinci.socialapitrial.feature.secure.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val coroutineContext : CoroutineContextProvider,
    private val userRepository: UserRepository,
    private val loginRepository: LoginRepository,
    private val sharedPreferences: Preferences
) : ViewModel() {

    // eventHandler sends event feedback to UI layer(Fragment)
    /** eventHandler is attached an observer in fragment. On layout change or onConfiguration change
     * fragment automatically observes again and again. I need to process event data once in order
     * to achieve that I used Event.kt helper
     **/
    private val _eventHandler = MutableLiveData<Event<Resource<String>>>()
    val eventHandler : LiveData<Event<Resource<String>>> = _eventHandler

    // user info
    private val _userInfo = MutableLiveData<UserResponse>()
    val userInfo : LiveData<UserResponse> = _userInfo

    init {
        Timber.d("UserListViewModel created..")
    }

    /** Called when UserListFragment created. **/
    fun fetchUserInfo(){
        // fetch each data if it's not fetched before
        if(userInfo.value == null){ getUserInfo() }
    }

    private fun getUserInfo(){
        viewModelScope.launch(coroutineContext.IO) {
            Timber.tag("getUserInfo-VMScope").d("Top-level: current thread is ${Thread.currentThread().name}")
            val userId = sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_ID) ?: ""
            val userName = sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_NAME) ?: ""

            if(userId.isNotEmpty() && userName.isNotEmpty()){
                when(val userResponse = userRepository.getUserInfo(userId.toLong(), userName)) {
                    is Resource.Success -> {
                        // user info is fetched
                        Timber.d("User info is fetched...")
                        userResponse.data?.let {
                            _userInfo.postValue(it)
                        }
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
        viewModelScope.launch(coroutineContext.IO) {
            Timber.tag("signOut-VMScope").d("Top-level: current thread is ${Thread.currentThread().name}")
            when(val signOutResponse = loginRepository.signOut()) {
                is Resource.Success -> {
                    // signOut request is completed..
                    signOutResponse.data?.let{
                        Timber.d("Signing out...")
                        /** delete all session related keys **/
                        sharedPreferences.clear()
                        //Pass sign out action to fragment
                        _eventHandler.postValue(Event(Resource.Info("Signing out. Navigating to Login")))
                        delay(1000)
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