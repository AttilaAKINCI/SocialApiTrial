package com.akinci.socialapitrial.feature.secure.user.userlist.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.akinci.socialapitrial.common.coroutines.TestContextProvider
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.storage.LocalPreferenceConfig
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.feature.secure.login.data.output.SignOutResponse
import com.akinci.socialapitrial.feature.secure.login.repository.LoginRepository
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse
import com.akinci.socialapitrial.feature.secure.user.repository.UserRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class UserListViewModelTest {
    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule() // for live data usage

    @MockK
    lateinit var loginRepository: LoginRepository

    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var sharedPreferences: Preferences

    lateinit var userListViewModel : UserListViewModel
    //TODO experimantal coroutine apiyi her yerde koymak yerine, classin basina koyabilirsin
    @ExperimentalCoroutinesApi
    lateinit var coroutineContext : TestContextProvider

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        coroutineContext = TestContextProvider()
        userListViewModel = UserListViewModel(coroutineContext, userRepository, loginRepository, sharedPreferences)
    }

    @After
    fun tearDown() { unmockkAll() }


    @Test
    fun `getUserInfo get success response then sets userInfo`(){
        every { sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_ID) } returns "10"
        every { sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_NAME) } returns "Dummy"
        coEvery { userRepository.getUserInfo(any(), any()) } returns
                Resource.Success(
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

        userListViewModel.userInfo.observeForever{
            assertThat(it.name).isEqualTo("TestName")
            assertThat(it.id).isEqualTo("1".toLong())
            assertThat(it.screen_name).isEqualTo("ScreenTestName")
        }

        userListViewModel.fetchUserInfo()
        verify (exactly = 2) { sharedPreferences.getStoredTag(any()) }
    }

    @Test
    fun `getUserInfo gets error response sends Resource Error`(){
        every { sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_ID) } returns "10"
        every { sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_NAME) } returns "Dummy"
        coEvery { userRepository.getUserInfo(any(), any()) } returns Resource.Error("Get User Info Service Encountered an Error")

        userListViewModel.eventHandler.observeForever{
            assertThat(it).isNotNull()
            when(val value = it.getContentIfNotHandled()){
                is Resource.Error -> {
                    assertThat(value.message).isEqualTo("Get User Info Service Encountered an Error")
                }
            }
        }

        userListViewModel.fetchUserInfo()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `signOut gets error response sends Resource Error`(){
        coEvery { loginRepository.signOut() } returns
                Resource.Success(SignOutResponse(
                    "asdasasdfdvcasdda"
                )
            )

        userListViewModel.eventHandler.observeForever{
            assertThat(it).isNotNull()
            when(val value = it.getContentIfNotHandled()){
                is Resource.Info -> {
                    assertThat(value.message).isEqualTo("Signing out. Navigating to Login")
                }
                is Resource.Success -> {
                    assertThat(value.data).isEqualTo("Signed out....")
                }
            }
        }

        userListViewModel.signOut()

        verify( exactly = 1) { sharedPreferences.clear() }

        coroutineContext.testCoroutineDispatcher.advanceUntilIdle()
    }



}