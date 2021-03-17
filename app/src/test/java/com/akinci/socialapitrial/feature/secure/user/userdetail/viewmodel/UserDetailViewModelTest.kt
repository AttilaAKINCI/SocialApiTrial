package com.akinci.socialapitrial.feature.secure.user.userdetail.viewmodel

import androidx.lifecycle.Observer
import com.akinci.socialapitrial.ahelpers.InstantExecutorExtension
import com.akinci.socialapitrial.ahelpers.TestContextProvider
import com.akinci.socialapitrial.common.helper.Event
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.feature.login.data.output.AccessTokenResponse
import com.akinci.socialapitrial.feature.secure.user.data.output.userdetail.UserTimeLineResponse
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse
import com.akinci.socialapitrial.feature.secure.user.repository.UserRepository
import com.akinci.socialapitrial.jsonresponses.GetAccessToken
import com.akinci.socialapitrial.jsonresponses.GetUserResponse
import com.akinci.socialapitrial.jsonresponses.GetUserTimeLine
import com.google.common.truth.Truth.assertThat
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class)
class UserDetailViewModelTest {

    @MockK
    lateinit var userRepository: UserRepository

    lateinit var userDetailViewModel : UserDetailViewModel

    private val coroutineContext = TestContextProvider()
    private val moshi = Moshi.Builder().build()

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        userDetailViewModel = UserDetailViewModel(coroutineContext, userRepository)
    }

    @AfterEach
    fun tearDown() { unmockkAll() }

    @Test
    fun `getUserInfo get success response then sets userInfo`(){
        coEvery { userRepository.getUserInfo(any(), any()) } returns Resource.Success(
            moshi.adapter(UserResponse::class.java).fromJson(GetUserResponse.userJsonResponse)
        )

        val observer = mockk<Observer<UserResponse>>(relaxed = true)
        val slot = slot<UserResponse>()

        userDetailViewModel.userInfo.observeForever(observer)

        userDetailViewModel.getUserInfo(1L, "SampleName")

        verify { observer.onChanged(capture(slot)) }

        val value = slot.captured
        assertThat(value.name).isEqualTo("Vildan")
        assertThat(value.id).isEqualTo("1336170512814457473".toLong())
        assertThat(value.screen_name).isEqualTo("Vildan")
    }

    @Test
    fun `getUserInfo gets error response sends Resource Error`(){
        coEvery { userRepository.getUserInfo(any(), any()) } returns Resource.Error("Get User Info Service Encountered an Error")

        val observer = mockk<Observer<Event<Resource<List<UserTimeLineResponse>>>>>(relaxed = true)
        val slot = slot<Event<Resource<List<UserTimeLineResponse>>>>()

        userDetailViewModel.eventHandler.observeForever(observer)

        userDetailViewModel.getUserInfo(1L, "SampleName")

        verify { observer.onChanged(capture(slot)) }

        val value = slot.captured.getContentIfNotHandled() as Resource.Error
        assertThat(value.message).isEqualTo("Get User Info Service Encountered an Error")
    }

    @Test
    fun `getUserTimeLine get success response then sends Resource Success`(){
        val type = Types.newParameterizedType(List::class.java, UserTimeLineResponse::class.java)
        val adapter : JsonAdapter<List<UserTimeLineResponse>> = moshi.adapter(type)

        coEvery { userRepository.getUserTimeLine(any(), any()) } returns Resource.Success(
            adapter.fromJson(GetUserTimeLine.userTimeLineJsonResponse)
        )

        val observer = mockk<Observer<Event<Resource<List<UserTimeLineResponse>>>>>(relaxed = true)
        val slot = slot<Event<Resource<List<UserTimeLineResponse>>>>()

        userDetailViewModel.eventHandler.observeForever(observer)

        userDetailViewModel.getUserTimeLine(1L)

        verify { observer.onChanged(capture(slot)) }

        val value = slot.captured.getContentIfNotHandled() as Resource.Success
        assertThat(value.data).isNotEmpty()
        assertThat(value.data?.size).isGreaterThan(0)

        val userTimeLineResponse = value.data?.get(0)

        assertThat(userTimeLineResponse?.user?.name).isEqualTo("Vildan")
    }

    @Test
    fun `getUserTimeLine get error response then sends Resource Error`(){
        coEvery { userRepository.getUserTimeLine(any(), any()) } returns Resource.Error("Get Time Line Service Encountered an Error")

        val observer = mockk<Observer<Event<Resource<List<UserTimeLineResponse>>>>>(relaxed = true)
        val slot = slot<Event<Resource<List<UserTimeLineResponse>>>>()

        userDetailViewModel.eventHandler.observeForever(observer)

        userDetailViewModel.getUserTimeLine(1L)

        verify { observer.onChanged(capture(slot)) }

        val value = slot.captured.getContentIfNotHandled() as Resource.Error
        assertThat(value.message).isEqualTo("Get Time Line Service Encountered an Error")
    }

}