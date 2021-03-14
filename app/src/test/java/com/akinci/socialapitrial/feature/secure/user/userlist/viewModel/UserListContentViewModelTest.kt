package com.akinci.socialapitrial.feature.secure.user.userlist.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.akinci.socialapitrial.common.coroutines.TestContextProvider
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.FollowerOrFriendResponse
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse
import com.akinci.socialapitrial.feature.secure.user.repository.UserRepository
import com.akinci.socialapitrial.feature.secure.user.userlist.adapter.viewpager.ViewPagerMode
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.unmockkAll
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class UserListContentViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule() // for live data usage

    @MockK
    lateinit var userRepository: UserRepository

    lateinit var userListContentViewModel : UserListContentViewModel
    @ExperimentalCoroutinesApi
    lateinit var coroutineContext : TestContextProvider

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        coroutineContext = TestContextProvider()
        userListContentViewModel = UserListContentViewModel(coroutineContext, userRepository)
    }

    @After
    fun tearDown() { unmockkAll() }

    @Test
    fun `getFollowers returns Success`(){
        coEvery { userRepository.fetchFollowers(any()) } returns Resource.Success( getFollowerOrFriendDummyData() )

        userListContentViewModel.followersEventHandler.observeForever{
            assertThat(it).isNotNull()
            when(val value = it.getContentIfNotHandled()){
                is Resource.Loading -> { assertThat(true) }
                is Resource.Success -> {
                    assertThat(value.data).isNotEmpty()
                    assertThat(value.data?.size).isGreaterThan(0)

                    val user = value.data?.get(0)

                    assertThat(user?.name).isEqualTo("TestName")
                }
            }
        }

        userListContentViewModel.fetchInitialData(ViewPagerMode.FOLLOWERS)

    }

    @Test
    fun `getFollowings returns Success`(){
        coEvery { userRepository.fetchFollowings(any()) } returns Resource.Success( getFollowerOrFriendDummyData() )

        userListContentViewModel.friendsEventHandler.observeForever{
            assertThat(it).isNotNull()
            when(val value = it.getContentIfNotHandled()){
                is Resource.Loading -> { assertThat(true) }
                is Resource.Success -> {
                    assertThat(value.data).isNotEmpty()
                    assertThat(value.data?.size).isGreaterThan(0)

                    val user = value.data?.get(0)

                    assertThat(user?.name).isEqualTo("TestName")
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

    /** Dummy Data **/
    private fun getFollowerOrFriendDummyData() : FollowerOrFriendResponse{
        return FollowerOrFriendResponse(
            10L,
            "10L",
            1L,
            "1L",
            100,
             listOf(
                UserResponse(
                    1L,
                    "TestName",
                    "ScreenTestName",
                    "Ist",
                    "Unknown",
                    10,
                    11,
                    true,
                    "https://backgroundImage.com",
                    "https://profileImage.com",
                    "https://profileBanner.com"
                ),
                UserResponse(
                    1L,
                    "TestName",
                    "ScreenTestName",
                    "Ist",
                    "Unknown",
                    10,
                    11,
                    true,
                    "https://backgroundImage.com",
                    "https://profileImage.com",
                    "https://profileBanner.com"
                )
            )
        )
    }

}