package com.akinci.socialapitrial.feature.login.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.akinci.socialapitrial.common.coroutines.TestContextProvider
import com.akinci.socialapitrial.common.helper.Event
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.feature.login.data.output.AccessTokenResponse
import com.akinci.socialapitrial.feature.login.data.output.RequestTokenResponse
import com.akinci.socialapitrial.feature.login.repository.LoginRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertNotNull
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class LoginViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule() // for live data usage

    @MockK
    lateinit var loginRepository: LoginRepository

    @MockK
    lateinit var sharedPreferences: Preferences

    lateinit var loginViewModel: LoginViewModel

    @ExperimentalCoroutinesApi
    // TODO lateinite gerek yok
    private val coroutineContext: TestContextProvider = TestContextProvider()

    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { sharedPreferences.getStoredTag(any()) } returns ""

        loginViewModel = LoginViewModel(coroutineContext, loginRepository, sharedPreferences)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `action sign in with twitter for already logged in user`() {
        // TODO before a mocklaniyor, buna gerek yok
        every { sharedPreferences.getStoredTag(any()) } returns "Dummy"
        loginViewModel = LoginViewModel(coroutineContext, loginRepository, sharedPreferences)

        coEvery { loginRepository.requestToken() } returns Resource.Success(RequestTokenResponse("authToken", "authTokenSecret"))

        loginViewModel.loginEventHandler.observeForever {
            assertThat(it).isNotNull()

            // TODO iki birden check etmeye gerek yok burada, sadece success ya da infoya giriyor muhtemelen
            when (val value = it.getContentIfNotHandled()) {
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

        // TODO before a mocklaniyor, buna gerek yok
        justRun { sharedPreferences.setStoredTag(any(), any()) }

        loginViewModel.authorizeEventHandler.observeForever {
            assertThat(it).isNotNull()
            when (val value = it.getContentIfNotHandled()) {
                is Resource.Success -> {
                    assertThat(value.data).contains("oauth_token")
                }
            }
        }

        loginViewModel.actionSignInWithTwitter()

        verify(exactly = 2) { sharedPreferences.setStoredTag(any(), any()) }
    }

    @Test
    fun `action sign in with twitter, first login request token returns Resource Error`() {
        coEvery { loginRepository.requestToken() } returns Resource.Error("Request Token Service Encountered an Error")

        loginViewModel.authorizeEventHandler.observeForever {
            assertThat(it).isNotNull()
            when (val value = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    assertThat(value.message).isEqualTo("Request Token Service Encountered an Error")
                }
            }
        }

        loginViewModel.actionSignInWithTwitter()
    }

    @ExperimentalCoroutinesApi
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

        // TODO burada observer-slot yapisini da kullanabilirsin
        //  Ayrica burada sadece Resource.Info olabilir deger,
        //  Resource.Success de check etmeye gerek, farkli test daha iyi olur.
        val observer = mockk<Observer<Event<Resource<String>>>>(relaxed = true)
        val slot = slot<Event<Resource<String>>>()

        loginViewModel.loginEventHandler.observeForever(observer)

        loginViewModel.getAccessToken()

        verify { observer.onChanged(capture(slot)) }

        // TODO capture ettigin slotun valuesunu direk alabilirsin
        val value = slot.captured.getContentIfNotHandled() as Resource.Info
        assertThat(value.message).isEqualTo("Access token is acquired. Proceed to Dashboard")

        verify(exactly = 3) { sharedPreferences.getStoredTag(any()) }
        verify(exactly = 5) { sharedPreferences.setStoredTag(any(), any()) }

        coroutineContext.testCoroutineDispatcher.advanceUntilIdle()
    }

    @Test
    fun `get access token, returns Resource Error`() {
        coEvery { loginRepository.getAccessToken(any(), any()) } returns Resource.Error("Get Access Token Service Encountered an Error")

        loginViewModel.loginEventHandler.observeForever {
            // TODO assertNotNull(it) kullanilabilir
            assertThat(it).isNotNull()
            when (val value = it.getContentIfNotHandled()) {
                is Resource.Error -> {
                    // TODO assertEquals() kullanilabilir
                    //assertEquals("Get Access Token Service Encountered an Error", value.message)
                    assertThat(value.message).isEqualTo("Get Access Token Service Encountered an Error")
                }
            }
        }

        loginViewModel.getAccessToken()

        verify(exactly = 3) { sharedPreferences.getStoredTag(any()) }
    }
}