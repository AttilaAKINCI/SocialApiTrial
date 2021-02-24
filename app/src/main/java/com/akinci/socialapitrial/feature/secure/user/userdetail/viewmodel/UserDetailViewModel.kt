package com.akinci.socialapitrial.feature.secure.user.userdetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.socialapitrial.common.helper.Event
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.secure.user.data.output.userdetail.UserTimeLineResponse
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse
import com.akinci.socialapitrial.feature.secure.user.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
        private val userRepository: UserRepository
) : ViewModel() {

    // eventHandler sends event feedback to UI layer(Fragment)
    private val _eventHandler = MutableLiveData<Event<Resource<List<UserTimeLineResponse>>>>()
    val eventHandler : LiveData<Event<Resource<List<UserTimeLineResponse>>>> = _eventHandler

    private val tweetFetchCount = 20
    private var userTimeLine = listOf<UserTimeLineResponse>()

    // user info
    var userInfo = MutableLiveData<UserResponse>()

    init {
        Timber.d("UserDetailViewModel created..")
    }

    fun fetchTimeLineData(userId: Long, screenName: String){
        if(userInfo.value == null){ getUserInfo(userId, screenName) }
        if(userTimeLine.isEmpty()){
            _eventHandler.postValue(Event(Resource.Loading()))
            getUserTimeLine(userId)
        }
    }

    private fun getUserInfo(userId: Long, userName: String){
        viewModelScope.launch {
            when(val userResponse = userRepository.getUserInfo(userId, userName)) {
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

    private fun getUserTimeLine(userId : Long){
        viewModelScope.launch {
            when(val userTimeLineResponse = userRepository.getUserTimeLine(userId, tweetFetchCount)) {
                is Resource.Success -> {
                    // user time line response fetched
                    Timber.d("User TimeLine is fetched...")
                    userTimeLine = userTimeLineResponse.data!!
                    _eventHandler.postValue(Event(Resource.Success(userTimeLineResponse.data)))
                }
                is Resource.Error -> {
                    // error occurred while fetching user time line
                    _eventHandler.postValue(Event(Resource.Error(userTimeLineResponse.message)))
                }
            }
        }
    }

}