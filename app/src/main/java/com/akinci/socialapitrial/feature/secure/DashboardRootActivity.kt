package com.akinci.socialapitrial.feature.secure

import android.os.Bundle
import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.common.activity.RootActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardRootActivity : RootActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getNavigationGraph(): Int { return R.navigation.navigation_dashboard }
    override fun getFragmentsThatHidesBackButton(): Set<Int> { return setOf(R.id.dashboardFragment) }

}