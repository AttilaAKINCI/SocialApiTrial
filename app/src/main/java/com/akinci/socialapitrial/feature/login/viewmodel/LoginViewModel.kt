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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginRepository: LoginRepository,
    private val sharedPreferences: Preferences
) : ViewModel() {

    val isLoggedIn = MutableLiveData(false)

    // after access token service, this event is fired in order to proceed dashboard.
    private val _loginEventHandler = MutableLiveData<Event<Resource<Boolean>>>()
    val loginEventHandler : LiveData<Event<Resource<Boolean>>> = _loginEventHandler

    // authorizeEventHandler sends event feedback to UI layer(Fragment)
    private val _authorizeEventHandler = MutableLiveData<Event<Resource<String>>>()
    val authorizeEventHandler : LiveData<Event<Resource<String>>> = _authorizeEventHandler

    init {
        Timber.d("LoginViewModel created..")

        val loginState = sharedPreferences.getStoredTag(LocalPreferenceConfig.IS_LOGGED_IN)
        isLoggedIn.value = loginState != ""
    }

    // connected to fragment_login sign-in button
    fun actionSignInWithTwitter() {
        if(isLoggedIn.value!!){
            /** Already logged in proceed to dashboard. **/
            _loginEventHandler.postValue(Event(Resource.Success(true)))
        }else{
            /** 3 Legged login steps... **/
            viewModelScope.launch {
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

        // simulate access token acquired. TODO fix it later.
        _loginEventHandler.postValue(Event(Resource.Success(true)))

    }
}