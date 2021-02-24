package com.akinci.socialapitrial.feature.secure.userlist.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.akinci.socialapitrial.feature.secure.userlist.view.ViewPagerContentFragment

class FragmentAdapter(
        activity: FragmentActivity
): FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        val fragmentMode =
            if(position == 0){ ViewPagerMode.FOLLOWERS }
            else{ ViewPagerMode.FRIENDS }

        return ViewPagerContentFragment(fragmentMode)
    }

    /** Followers / Friends **/
    override fun getItemCount(): Int { return 2 }
}