package com.akinci.socialapitrial.feature.login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.socialapitrial.common.helper.Event
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.network.RestConfig
import com.akinci.socialapitrial.common.storage.LocalPreferenceConfig
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.feature.login.repository.LoginRepository
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val sharedPreferences: Preferences
) : ViewModel() {

    private val _isLoggedIn = MutableLiveData(false)
    val isLoggedIn : LiveData<Boolean> = _isLoggedIn

    // after access token service, this event is fired in order to proceed dashboard.
    /** loginEventHandler is attached an observer in fragment. On layout change or onConfiguration change
     * fragment automatically observes again and again. I need to process event data once in order
     * to achieve that I used Event.kt helper
     **/
    private val _loginEventHandler = MutableLiveData<Event<Resource<String>>>()
    val loginEventHandler : LiveData<Event<Resource<String>>> = _loginEventHandler

    // authorizeEventHandler sends event feedback to UI layer(Fragment)
    /** authorizeEventHandler is attached an observer in fragment. On layout change or onConfiguration change
     * fragment automatically observes again and again. I need to process event data once in order
     * to achieve that I used Event.kt helper
     **/
    private val _authorizeEventHandler = MutableLiveData<Event<Resource<String>>>()
    val authorizeEventHandler : LiveData<Event<Resource<String>>> = _authorizeEventHandler

    init {
        Timber.d("LoginViewModel created..")

        val loginState = sharedPreferences.getStoredTag(LocalPreferenceConfig.IS_LOGGED_IN)
        _isLoggedIn.value = loginState != ""
    }

    // connected to fragment_login sign-in button
    fun actionSignInWithTwitter() {
        if(isLoggedIn.value!!){
            /** Already logged in proceed to dashboard. **/
            viewModelScope.launch(Dispatchers.IO) {
                _loginEventHandler.postValue(Event(Resource.Info("User already logged-in")))
                delay(1000)
                _loginEventHandler.postValue(Event(Resource.Success(null)))
            }
        }else{
            /** 3 Legged login steps... **/
            viewModelScope.launch(Dispatchers.IO) {
                Timber.tag("signIn-VMScope").d("Top-level: current thread is ${Thread.currentThread().name}")
                // 3 legged authentication started.
                /** STEP 1. REQUEST TOKEN **/
                when(val requestTokenResponse = loginRepository.requestToken()) {
                    is Resource.Success -> {
                        // request token service is completed..
                        requestTokenResponse.data?.let{
                            Timber.d("Request token has been acquired.")
                            sharedPreferences.setStoredTag(LocalPreferenceConfig.OAUTH_TOKEN, it.oauth_token)
                            sharedPreferences.setStoredTag(LocalPreferenceConfig.OAUTH_TOKEN_SECRET, it.oauth_token_secret)
                        }
                    }
                    is Resource.Error -> {
                        // request token service encountered an error.
                        _authorizeEventHandler.postValue(Event(Resource.Error(requestTokenResponse.message)))
                    }
                }

                /** STEP 2. NAVIGATE AUTHORIZATION PAGE **/
                val authorizationString =
                    RestConfig.API_BASE_URL +
                            RestConfig.AUTHORIZE_URL + "?" +
                            "oauth_token=${sharedPreferences.getStoredTag(LocalPreferenceConfig.OAUTH_TOKEN)}"

                Timber.d("Authorization has been initiated.")
                _authorizeEventHandler.postValue(Event(Resource.Success(authorizationString)))
            }
        }
    }

    fun getAccessToken(){
        /** STEP 3. GET ACCESS TOKEN **/
        Timber.d("Access Token will be fetched")

        viewModelScope.launch(Dispatchers.IO) {
            Timber.tag("getAccessToken-VMScope").d("Top-level: current thread is ${Thread.currentThread().name}")
            val oAuthToken = sharedPreferences.getStoredTag(LocalPreferenceConfig.OAUTH_TOKEN) ?: ""
            val oAuthTokenVerifier = sharedPreferences.getStoredTag(LocalPreferenceConfig.OAUTH_TOKEN_VERIFIER) ?: ""

            when(val accessTokenResponse = loginRepository.getAccessToken(oAuthToken, oAuthTokenVerifier)){
                is Resource.Success -> {
                    // access token service is completed..
                    accessTokenResponse.data?.let{
                        Timber.d("Access token has been acquired.")
                        sharedPreferences.setStoredTag(LocalPreferenceConfig.ACCESS_TOKEN, it.oauth_token)
                        sharedPreferences.setStoredTag(LocalPreferenceConfig.ACCESS_TOKEN_SECRET, it.oauth_token_secret)
                        sharedPreferences.setStoredTag(LocalPreferenceConfig.USER_ID, it.user_id)
                        sharedPreferences.setStoredTag(LocalPreferenceConfig.USER_NAME, it.screen_name)
                        sharedPreferences.setStoredTag(LocalPreferenceConfig.IS_LOGGED_IN, "1")

                        Timber.d("Access token service has been completed.")
                        _loginEventHandler.postValue(Event(Resource.Info("Access token is acquired. Proceed to Dashboard")))
                        delay(1000)
                        _loginEventHandler.postValue(Event(Resource.Success(null)))
                    } ?: _loginEventHandler.postValue(Event(Resource.Error("Access token response data is null")))
                }
                is Resource.Error -> {
                    // request token service encountered an error.
                    _loginEventHandler.postValue(Event(Resource.Error(accessTokenResponse.message)))
                }
            }
        }
    }

}