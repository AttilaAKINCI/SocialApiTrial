package com.akinci.socialapitrial.feature.secure.userlist.viewModel

import androidx.lifecycle.ViewModel
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.feature.secure.userlist.repository.UserListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewPagerViewModel @Inject constructor(
    private val userListRepository: UserListRepository,
    private val sharedPreferences: Preferences
) : ViewModel() {

    init {

    }


}