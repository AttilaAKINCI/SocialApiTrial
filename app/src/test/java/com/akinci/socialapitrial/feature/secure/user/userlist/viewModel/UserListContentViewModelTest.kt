package com.akinci.socialapitrial.feature.secure.user.userlist.viewModel

import com.akinci.socialapitrial.ahelpers.InstantExecutorExtension
import com.akinci.socialapitrial.ahelpers.TestContextProvider
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.FollowerOrFriendResponse
import com.akinci.socialapitrial.feature.secure.user.repository.UserRepository
import com.akinci.socialapitrial.feature.secure.user.userlist.adapter.viewpager.ViewPagerMode
import com.akinci.socialapitrial.jsonresponses.GetFollowerOrFriendResponse
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class UserListContentViewModelTest {

    @MockK
    lateinit var userRepository: UserRepository

    lateinit var userListContentViewModel : UserListContentViewModel

    private val coroutineContext = TestContextProvider()
    private val moshi = Moshi.Builder().build()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        userListContentViewModel = UserListContentViewModel(coroutineContext, userRepository)
    }

    @AfterEach
    fun tearDown() { unmockkAll() }

    @Test
    fun `getFollowers returns Success`(){
        coEvery { userRepository.fetchFollowers(any()) } returns Resource.Success(
            moshi.adapter(FollowerOrFriendResponse::class.java).fromJson(GetFollowerOrFriendResponse.followerOrFriendResponse)
        )

        userListContentViewModel.followersEventHandler.observeForever{
            assertThat(it).isNotNull()
            when(val value = it.getContentIfNotHandled()){
                is Resource.Loading -> { assertThat(true) }
                is Resource.Success -> {
                    assertThat(value.data).isNotEmpty()
                    assertThat(value.data?.size).isGreaterThan(0)

                    val user = value.data?.get(0)

                    assertThat(user?.name).isEqualTo("Vildan")
                }
            }
        }

        userListContentViewModel.fetchInitialData(ViewPagerMode.FOLLOWERS)
    }

    @Test
    fun `getFollowings returns Success`(){
        coEvery { userRepository.fetchFollowings(any()) } returns Resource.Success(
            moshi.adapter(FollowerOrFriendResponse::class.java).fromJson(GetFollowerOrFriendResponse.followerOrFriendResponse)
        )

        userListContentViewModel.friendsEventHandler.observeForever{
            assertThat(it).isNotNull()
            when(val value = it.getContentIfNotHandled()){
                is Resource.Loading -> { assertThat(true) }
                is Resource.Success -> {
                    assertThat(value.data).isNotEmpty()
                    assertThat(value.data?.size).isGreaterThan(0)

                    val user = value.data?.get(0)

                    assertThat(user?.name).isEqualTo("Vildan")
                }
            }
        }

        userListContentViewModel.fetchInitialData(ViewPagerMode.FRIENDS)
    }

    @Test
    fun `getFollowers returns Error`(){
        coEvery { userRepository.fetchFollowers(any()) } returns Resource.Error("Get Followers Service Encountered an Error")

        userListContentViewModel.followersEventHandler.observeForever{
            assertThat(it).isNotNull()
            when(val value = it.getContentIfNotHandled()){
                is Resource.Error -> {
                    assertThat(value.message).isEqualTo("Get Followers Service Encountered an Error")
                }
            }
        }

        userListContentViewModel.fetchInitialData(ViewPagerMode.FOLLOWERS)
    }

    @Test
    fun `getFollowings returns Error`(){
        coEvery { userRepository.fetchFollowings(any()) } returns Resource.Error("Get Followings Service Encountered an Error")

        userListContentViewModel.friendsEventHandler.observeForever{
            assertThat(it).isNotNull()
            when(val value = it.getContentIfNotHandled()){
                is Resource.Error -> {
                    assertThat(value.message).isEqualTo("Get Followings Service Encountered an Error")
                }
            }
        }

        userListContentViewModel.fetchInitialData(ViewPagerMode.FRIENDS)
    }

}