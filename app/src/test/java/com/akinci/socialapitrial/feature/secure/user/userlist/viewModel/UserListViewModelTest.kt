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
import com.akinci.socialapitrial.jsonresponses.GetSignOutResponse
import com.akinci.socialapitrial.jsonresponses.GetUserResponse
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.Moshi
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
    private val moshi = Moshi.Builder().build()

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
        coEvery { userRepository.getUserInfo(any(), any()) } returns Resource.Success(
            moshi.adapter(UserResponse::class.java).fromJson(GetUserResponse.userJsonResponse)
        )

        val observer = mockk<Observer<UserResponse>>(relaxed = true)
        val slot = slot<UserResponse>()

        userListViewModel.userInfo.observeForever(observer)

        userListViewModel.fetchUserInfo()

        verify { observer.onChanged(capture(slot)) }

        val value = slot.captured
        assertThat(value.name).isEqualTo("Vildan")
        assertThat(value.id).isEqualTo("1336170512814457473".toLong())
        assertThat(value.screen_name).isEqualTo("Vildan")

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
        coEvery { loginRepository.signOut() } returns Resource.Success(
            moshi.adapter(SignOutResponse::class.java).fromJson(GetSignOutResponse.signOutResponse)
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