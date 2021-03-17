package com.akinci.socialapitrial.feature.secure.user.userlist.viewModel

import androidx.lifecycle.Observer
import com.akinci.socialapitrial.ahelpers.InstantExecutorExtension
import com.akinci.socialapitrial.ahelpers.TestContextProvider
import com.akinci.socialapitrial.common.helper.Event
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
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class UserListViewModelTest {

    @MockK
    lateinit var loginRepository: LoginRepository

    @MockK
    lateinit var userRepository: UserRepository

    @MockK
    lateinit var sharedPreferences: Preferences

    lateinit var userListViewModel : UserListViewModel

    private val coroutineContext = TestContextProvider()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        userListViewModel = UserListViewModel(coroutineContext, userRepository, loginRepository, sharedPreferences)
    }

    @AfterEach
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

        val observer = mockk<Observer<UserResponse>>(relaxed = true)
        val slot = slot<UserResponse>()

        userListViewModel.userInfo.observeForever(observer)

        userListViewModel.fetchUserInfo()

        verify { observer.onChanged(capture(slot)) }

        val value = slot.captured
        assertThat(value.name).isEqualTo("TestName")
        assertThat(value.id).isEqualTo("1".toLong())
        assertThat(value.screen_name).isEqualTo("ScreenTestName")

        verify (exactly = 2) { sharedPreferences.getStoredTag(any()) }
    }

    @Test
    fun `getUserInfo gets error response sends Resource Error`(){
        every { sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_ID) } returns "10"
        every { sharedPreferences.getStoredTag(LocalPreferenceConfig.USER_NAME) } returns "Dummy"
        coEvery { userRepository.getUserInfo(any(), any()) } returns Resource.Error("Get User Info Service Encountered an Error")

        val observer = mockk<Observer<Event<Resource<String>>>>(relaxed = true)
        val slot = slot<Event<Resource<String>>>()

        userListViewModel.eventHandler.observeForever(observer)

        userListViewModel.fetchUserInfo()

        verify { observer.onChanged(capture(slot)) }

        val value = slot.captured.getContentIfNotHandled() as Resource.Error
        assertThat(value.message).isEqualTo("Get User Info Service Encountered an Error")
    }

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