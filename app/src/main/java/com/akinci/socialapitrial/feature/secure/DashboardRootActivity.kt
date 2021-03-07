package com.akinci.socialapitrial.feature.secure

import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.common.activity.RootActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardRootActivity : RootActivity() {

    override fun getNavigationGraph(): Int = R.navigation.navigation_dashboard
    override fun getFragmentsThatHidesBackButton(): Set<Int> = setOf(R.id.dashboardFragment)

}