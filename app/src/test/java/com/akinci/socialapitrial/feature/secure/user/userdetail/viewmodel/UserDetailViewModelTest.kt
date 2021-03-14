package com.akinci.socialapitrial.feature.secure.user.userdetail.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.akinci.socialapitrial.common.coroutines.TestContextProvider
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.secure.user.data.output.userdetail.UserTimeLineResponse
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse
import com.akinci.socialapitrial.feature.secure.user.repository.UserRepository
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

class UserDetailViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule() // for live data usage

    @MockK
    lateinit var userRepository: UserRepository

    lateinit var userDetailViewModel : UserDetailViewModel
    @ExperimentalCoroutinesApi
    lateinit var coroutineContext : TestContextProvider

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        coroutineContext = TestContextProvider()
        userDetailViewModel = UserDetailViewModel(coroutineContext, userRepository)
    }

    @After
    fun tearDown() { unmockkAll() }

    @Test
    fun `getUserInfo get success response then sets userInfo`(){
        coEvery { userRepository.getUserInfo(any(), any()) } returns
                Resource.Success(UserResponse(
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

        userDetailViewModel.userInfo.observeForever{
            assertThat(it.name).isEqualTo("TestName")
            assertThat(it.id).isEqualTo("1".toLong())
            assertThat(it.screen_name).isEqualTo("ScreenTestName")
        }

        userDetailViewModel.getUserInfo(1L, "SampleName")
    }

    @Test
    fun `getUserInfo gets error response sends Resource Error`(){
        coEvery { userRepository.getUserInfo(any(), any()) } returns Resource.Error("Get User Info Service Encountered an Error")

        userDetailViewModel.eventHandler.observeForever{
            assertThat(it).isNotNull()
            when(val value = it.getContentIfNotHandled()){
                is Resource.Error -> {
                    assertThat(value.message).isEqualTo("Get User Info Service Encountered an Error")
                }
            }
        }

        userDetailViewModel.getUserInfo(1L, "SampleName")
    }

    @Test
    fun `getUserTimeLine get success response then sends Resource Success`(){
        coEvery { userRepository.getUserTimeLine(any(), any()) } returns
                Resource.Success( listOf(
                        UserTimeLineResponse(
                        1L,
                        "UserTimeLine",
                        null,
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
                        "SomeTime"
                        ),
                        UserTimeLineResponse(
                            2L,
                            "UserTimeLine2",
                            null,
                            UserResponse(
                                1L,
                                "TestName2",
                                "ScreenTestName2",
                                "Ist2",
                                "Unknown2",
                                101,
                                111,
                                true,
                                "https://backgroundImage2.com",
                                "https://profileImage2.com",
                                "https://profileBanner2.com"
                            ),
                            "SomeTime2"
                        )
                    )
                )

        userDetailViewModel.eventHandler.observeForever{
            assertThat(it).isNotNull()
            when(val value = it.getContentIfNotHandled()){
                is Resource.Success -> {
                    assertThat(value.data).isNotEmpty()
                    assertThat(value.data?.size).isGreaterThan(0)

                    val userTimeLineResponse = value.data?.get(0)
                    val userTimeLineResponse2 = value.data?.get(1)

                    assertThat(userTimeLineResponse?.user?.name).isEqualTo("TestName")
                    assertThat(userTimeLineResponse2?.user?.name).isEqualTo("TestName2")
                }
            }
        }

        userDetailViewModel.getUserTimeLine(1L)
    }

    @Test
    fun `getUserTimeLine get error response then sends Resource Error`(){
        coEvery { userRepository.getUserTimeLine(any(), any()) } returns Resource.Error("Get Time Line Service Encountered an Error")

        userDetailViewModel.eventHandler.observeForever{
            assertThat(it).isNotNull()
            when(val value = it.getContentIfNotHandled()){
                is Resource.Error -> {
                    assertThat(value.message).isEqualTo("Get Time Line Service Encountered an Error")
                }
            }
        }

        userDetailViewModel.getUserTimeLine(1L)
    }

}