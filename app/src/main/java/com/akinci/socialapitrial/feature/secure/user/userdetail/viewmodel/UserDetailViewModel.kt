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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserDetailViewModel @Inject constructor(
        private val userRepository: UserRepository
) : ViewModel() {

    // eventHandler sends event feedback to UI layer(Fragment)
    /** eventHandler is attached an observer in fragment. On layout change or onConfiguration change
     * fragment automatically observes again and again. I need to process event data once in order
     * to achieve that I used Event.kt helper
    **/
    private val _eventHandler = MutableLiveData<Event<Resource<List<UserTimeLineResponse>>>>()
    val eventHandler : LiveData<Event<Resource<List<UserTimeLineResponse>>>> = _eventHandler

    private val tweetFetchCount = 20
    private var userTimeLine = listOf<UserTimeLineResponse>()

    // user info
    private val _userInfo = MutableLiveData<UserResponse>()
    val userInfo : LiveData<UserResponse> = _userInfo

    init {
        Timber.d("UserDetailViewModel created..")
    }

    fun fetchTimeLineData(userId: Long, screenName: String){
        /** fetchTimeLineData is triggered in UserDetailFragments OnStart method.
         * Fragment can call OnStart method multiple times. So if I achieved data before
         * I don't need to fetch it again and again **/
        if(userInfo.value == null){ getUserInfo(userId, screenName) }
        if(userTimeLine.isEmpty()){
            _eventHandler.postValue(Event(Resource.Loading()))
            getUserTimeLine(userId)
        }
    }

    private fun getUserInfo(userId: Long, userName: String){
        viewModelScope.launch(Dispatchers.IO) {
            Timber.tag("getUserInfo-VMScope").d("Top-level: current thread is ${Thread.currentThread().name}")
            when(val userResponse = userRepository.getUserInfo(userId, userName)) {
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

    private fun getUserTimeLine(userId : Long){
        viewModelScope.launch(Dispatchers.IO){
            Timber.tag("getUserTimeLine-VMScope").d("Top-level: current thread is ${Thread.currentThread().name}")
            when(val userTimeLineResponse = userRepository.getUserTimeLine(userId, tweetFetchCount)) {
                is Resource.Success -> {
                    // user time line response fetched
                    Timber.d("User TimeLine is fetched...")
                    userTimeLineResponse.data?.let {
                        userTimeLine = it
                        _eventHandler.postValue(Event(Resource.Success(it)))
                    }
                }
                is Resource.Error -> {
                    // error occurred while fetching user time line
                    _eventHandler.postValue(Event(Resource.Error(userTimeLineResponse.message)))
                }
            }
        }
    }

}