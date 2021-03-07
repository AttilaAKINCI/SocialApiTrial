package com.akinci.socialapitrial.feature.login

import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.common.activity.RootActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRootActivity : RootActivity() {

    override fun getNavigationGraph(): Int = R.navigation.navigation_login
    override fun getFragmentsThatHidesBackButton(): Set<Int> = setOf(R.id.splashFragment, R.id.loginFragment)

}