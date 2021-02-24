package com.akinci.socialapitrial.feature.secure.userlist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.socialapitrial.common.helper.Event
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.secure.userlist.data.output.community.UserResponse
import com.akinci.socialapitrial.feature.secure.userlist.repository.UserListRepository
import com.akinci.socialapitrial.feature.secure.userlist.adapter.viewpager.ViewPagerMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ViewPagerViewModel @Inject constructor(
    private val userListRepository: UserListRepository
) : ViewModel() {

    // followers event handler
    private val _followersEventHandler = MutableLiveData<Event<Resource<List<UserResponse>>>>()
    val followersEventHandler : LiveData<Event<Resource<List<UserResponse>>>> = _followersEventHandler

    // friends event handler
    private val _friendsEventHandler = MutableLiveData<Event<Resource<List<UserResponse>>>>()
    val friendsEventHandler : LiveData<Event<Resource<List<UserResponse>>>> = _friendsEventHandler

    // follower list
    private var followersCursor = -1L
    private var followers = listOf<UserResponse>()

    // friend list
    private var followingsCursor = -1L
    private var friends = listOf<UserResponse>()

    init {
        Timber.d("ViewPagerViewModel created..")
    }

    fun fetchInitialData(mode: ViewPagerMode){
        //send Loading state for shimmer loading
        when(mode){
            ViewPagerMode.FOLLOWERS -> {
                if(followers.isEmpty()){
                    _followersEventHandler.postValue(Event(Resource.Loading()))
                    getFollowers()
                }
            }
            ViewPagerMode.FRIENDS -> {
                if(friends.isEmpty()){
                    _friendsEventHandler.postValue(Event(Resource.Loading()))
                    getFollowings()
                }
            }
        }
    }

    private fun getFollowers(){
        viewModelScope.launch {
            when(val followersResponse = userListRepository.fetchFollowers(followersCursor)) {
                is Resource.Success -> {
                    // followers response fetched
                    Timber.d("Followers list is fetched...")
                    followersResponse.data?.let {
                        followersCursor = it.next_cursor
                        it.users.let { data ->
                            followers = data
                            _followersEventHandler.postValue(Event(Resource.Success(data)))
                        }
                    }
                }
                is Resource.Error -> {
                    // error occurred while fetching followers
                    _followersEventHandler.postValue(Event(Resource.Error(followersResponse.message)))
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
                    followingsResponse.data?.let {
                        followingsCursor =it.next_cursor
                        it.users?.let { data ->
                            friends = data
                            _friendsEventHandler.postValue(Event(Resource.Success(data)))
                        }
                    }
                }
                is Resource.Error -> {
                    // error occurred while fetching followings
                    _friendsEventHandler.postValue(Event(Resource.Error(followingsResponse.message)))
                }
            }
        }
    }

}