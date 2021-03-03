package com.akinci.socialapitrial.feature.login

import android.os.Bundle
import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.common.activity.RootActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginRootActivity : RootActivity() {

    //TODO Remove
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    // TODO single linelari direk assign edebilirsin
    override fun getNavigationGraph(): Int = R.navigation.navigation_login

    override fun getFragmentsThatHidesBackButton(): Set<Int> {
        return setOf(R.id.splashFragment, R.id.loginFragment)
    }
}