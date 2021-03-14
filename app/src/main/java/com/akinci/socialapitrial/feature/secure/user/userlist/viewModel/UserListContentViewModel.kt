package com.akinci.socialapitrial.feature.secure.user.userlist.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.akinci.socialapitrial.common.coroutines.CoroutineContextProvider
import com.akinci.socialapitrial.common.helper.Event
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse
import com.akinci.socialapitrial.feature.secure.user.repository.UserRepository
import com.akinci.socialapitrial.feature.secure.user.userlist.adapter.viewpager.ViewPagerMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserListContentViewModel @Inject constructor(
    private val coroutineContext : CoroutineContextProvider,
    private val userRepository: UserRepository
) : ViewModel() {

    // followers event handler
    /** followersEventHandler is attached an observer in fragment. On layout change or onConfiguration change
     * fragment automatically observes again and again. I need to process event data once in order
     * to achieve that I used Event.kt helper
     **/
    private val _followersEventHandler = MutableLiveData<Event<Resource<List<UserResponse>>>>()
    val followersEventHandler : LiveData<Event<Resource<List<UserResponse>>>> = _followersEventHandler

    // friends event handler
    /** friendsEventHandler is attached an observer in fragment. On layout change or onConfiguration change
     * fragment automatically observes again and again. I need to process event data once in order
     * to achieve that I used Event.kt helper
     **/
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
        /** fetchInitialData is triggered in UserListContentFragment OnStart method.
         * Fragment can call OnStart method multiple times. So if I achieved data before
         * I don't need to fetch it again and again **/
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
        viewModelScope.launch(coroutineContext.IO) {
            Timber.tag("getFollowers-VMScope").d("Top-level: current thread is ${Thread.currentThread().name}")
            when(val followersResponse = userRepository.fetchFollowers(followersCursor)) {
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
        viewModelScope.launch(coroutineContext.IO) {
            Timber.tag("getFollowings-VMScope").d("Top-level: current thread is ${Thread.currentThread().name}")
            when(val followingsResponse = userRepository.fetchFollowings(followingsCursor)) {
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