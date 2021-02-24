package com.akinci.socialapitrial.feature.secure.user.userdetail.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.socialapitrial.common.helper.Event
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.secure.user.data.output.userdetail.UserTimeLineResponse
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
    private val _eventHandler = MutableLiveData<Event<Resource<String>>>()
    val eventHandler : LiveData<Event<Resource<String>>> = _eventHandler

    private val tweetFetchCount = 20
    var userTimeLine = MutableLiveData<List<UserTimeLineResponse>>()

    init {
        Timber.d("UserDetailViewModel created..")
    }

    fun getUserTimeLine(userId : Long){
        viewModelScope.launch {
            when(val userTimeLineResponse = userRepository.getUserTimeLine(userId, tweetFetchCount)) {
                is Resource.Success -> {
                    // user time line response fetched
                    Timber.d("Followers list is fetched...")
                    userTimeLine.value = userTimeLineResponse.data!!
                }
                is Resource.Error -> {
                    // error occurred while fetching user time line
                    _eventHandler.postValue(Event(Resource.Error(userTimeLineResponse.message)))
                }
            }
        }
    }

}