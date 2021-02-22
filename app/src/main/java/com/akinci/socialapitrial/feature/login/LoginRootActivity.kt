package com.akinci.socialapitrial.feature.login

import android.os.Bundle
import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.common.activity.RootActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRootActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getNavigationGraph(): Int { return R.navigation.navigation_login }
    override fun getFragmentsThatHidesBackButton(): Set<Int> { return setOf(R.id.splashFragment, R.id.loginFragment) }
}