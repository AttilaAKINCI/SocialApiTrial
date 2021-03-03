package com.akinci.socialapitrial.feature.secure.user.userlist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.socialapitrial.common.helper.Event
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.storage.LocalPreferenceConfig
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse
import com.akinci.socialapitrial.feature.secure.login.repository.LoginRepository
import com.akinci.socialapitrial.feature.secure.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val loginRepository: LoginRepository,
    private val sharedPreferences: Preferences
) : ViewModel() {

    // eventHandler sends event feedback to UI layer(Fragment)
    private val _eventHandler = MutableLiveData<Event<Resource<String>>>()
    val eventHandler: LiveData<Event<Resource<String>>> = _eventHandler

    // user info
    private val _userInfo = MutableLiveData<UserResponse>()
    val userInfo: LiveData<UserResponse> = _userInfo

    init {
        Timber.d("UserListViewModel created..")
    }

    /** Called when UserListFragment created. **/
    fun fetchUserInfo() {
        // fetch each data if it's not fetched before
        if (userInfo.value == null) {
            getUserInfo()
        }
    }

    private fun getUserInfo() {
        viewModelScope.launch {
            val userId = sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_ID) ?: ""
            val userName = sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_NAME) ?: ""

            if (userId.isNotEmpty() && userName.isNotEmpty()) {
                when (val userResponse = userRepository.getUserInfo(userId.toLong(), userName)) {
                    is Resource.Success -> {
                        // user info is fetched
                        Timber.d("User info is fetched...")
                        _userInfo.value = userResponse.data!!
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
    fun signOut() {
        viewModelScope.launch {
            when (val signOutResponse = loginRepository.signOut()) {
                is Resource.Success -> {
                    // signOut request is completed..
                    signOutResponse.data?.let {
                        Timber.d("Signing out...")
                        /** delete all session related keys **/
                        // TODO sharedpreference i kullanan baska bir class eklenebilir bence su logicleri tutan
                        // Class TokenHelper(sharedpreference : SharedPref){
                        //    fun clearTokens(){
                        //        Tum tokenlar burada silinebilir
                        //    }
                        // }
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