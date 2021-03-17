package com.akinci.socialapitrial.feature.login.viewmodel

import androidx.lifecycle.Observer
import com.akinci.socialapitrial.ahelpers.InstantExecutorExtension
import com.akinci.socialapitrial.ahelpers.TestContextProvider
import com.akinci.socialapitrial.common.helper.Event
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.feature.login.data.output.AccessTokenResponse
import com.akinci.socialapitrial.feature.login.data.output.RequestTokenResponse
import com.akinci.socialapitrial.feature.login.repository.LoginRepository
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
class LoginViewModelTest {

    @MockK
    lateinit var loginRepository: LoginRepository

    @MockK
    lateinit var sharedPreferences: Preferences

    lateinit var loginViewModel : LoginViewModel

    private val coroutineContext = TestContextProvider()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { sharedPreferences.getStoredTag(any()) } returns ""

        loginViewModel = LoginViewModel(coroutineContext, loginRepository, sharedPreferences)
    }

    @AfterEach
    fun tearDown() { unmockkAll() }

    @Test
    fun `action sign in with twitter for already logged in user`() {
        every { sharedPreferences.getStoredTag(any()) } returns "Dummy"
        loginViewModel = LoginViewModel(coroutineContext, loginRepository, sharedPreferences)

        coEvery { loginRepository.requestToken() } returns Resource.Success(RequestTokenResponse("authToken", "authTokenSecret"))

        loginViewModel.loginEventHandler.observeForever {
            assertThat(it).isNotNull()

            when(val value = it.getContentIfNotHandled()){
                is Resource.Info -> {
                    assertThat(value.message).isEqualTo("User already logged-in")
                }
                is Resource.Success -> {
                    assertThat(value.data).isNull()
                }
            }
        }

        loginViewModel.actionSignInWithTwitter()
        coroutineContext.testCoroutineDispatcher.advanceUntilIdle()
    }

    @Test
    fun `action sign in with twitter, first login, set tokens to shared preferences, send authorizeEventHandler success for Success Resource`() {
        coEvery { loginRepository.requestToken() } returns Resource.Success(RequestTokenResponse("authToken", "authTokenSecret"))
        justRun { sharedPreferences.setStoredTag(any(), any()) }

        val observer = mockk<Observer<Event<Resource<String>>>>(relaxed = true)
        val slot = slot<Event<Resource<String>>>()

        loginViewModel.authorizeEventHandler.observeForever(observer)

        loginViewModel.actionSignInWithTwitter()

        verify { observer.onChanged(capture(slot)) }

        val value = slot.captured.getContentIfNotHandled() as Resource.Success
        assertThat(value.data).contains("oauth_token")

        verify (exactly = 2) { sharedPreferences.setStoredTag(any(), any()) }
    }

    @Test
    fun `action sign in with twitter, first login request token returns Resource Error`() {
        coEvery { loginRepository.requestToken() } returns Resource.Error("Request Token Service Encountered an Error")

        val observer = mockk<Observer<Event<Resource<String>>>>(relaxed = true)
        val slot = slot<Event<Resource<String>>>()

        loginViewModel.authorizeEventHandler.observeForever(observer)

        loginViewModel.actionSignInWithTwitter()

        verify { observer.onChanged(capture(slot)) }

        val value = slot.captured.getContentIfNotHandled() as Resource.Error
        assertThat(value.message).isEqualTo("Request Token Service Encountered an Error")
    }

    @Test
    fun `get access token, successful service return`() {
        coEvery { loginRepository.getAccessToken(any(), any()) } returns Resource.Success(
            AccessTokenResponse(
                "jlkashdlkjashndjl",
                "uhdushdushdu",
                "10",
                "UnitTest"
                )
        )

        loginViewModel.loginEventHandler.observeForever {
            assertThat(it).isNotNull()
            when(val value = it.getContentIfNotHandled()){
                is Resource.Info -> {
                    assertThat(value.message).isEqualTo("Access token is acquired. Proceed to Dashboard")
                }
                is Resource.Success -> {
                    assertThat(value.data).isNull()
                }
            }
        }

        loginViewModel.getAccessToken()

        verify (exactly = 3) { sharedPreferences.getStoredTag(any()) }
        verify (exactly = 5) { sharedPreferences.setStoredTag(any(), any()) }

        coroutineContext.testCoroutineDispatcher.advanceUntilIdle()
    }

    @Test
    fun `get access token, returns Resource Error`() {
        coEvery { loginRepository.getAccessToken(any(), any()) } returns Resource.Error("Get Access Token Service Encountered an Error")

        val observer = mockk<Observer<Event<Resource<String>>>>(relaxed = true)
        val slot = slot<Event<Resource<String>>>()

        loginViewModel.loginEventHandler.observeForever(observer)

        loginViewModel.getAccessToken()

        verify { observer.onChanged(capture(slot)) }

        val value = slot.captured.getContentIfNotHandled() as Resource.Error
        assertThat(value.message).isEqualTo("Get Access Token Service Encountered an Error")

        verify (exactly = 3) { sharedPreferences.getStoredTag(any()) }
    }

}