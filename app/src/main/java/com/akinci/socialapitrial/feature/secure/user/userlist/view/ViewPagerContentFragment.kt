package com.akinci.socialapitrial.feature.secure.user.userlist.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.common.component.SnackBar
import com.akinci.socialapitrial.common.component.adapter.ShimmerAdapter
import com.akinci.socialapitrial.common.helper.Event
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.databinding.FragmentViewPagerContentBinding
import com.akinci.socialapitrial.feature.secure.user.userlist.adapter.recycler.CommunityListAdapter
import com.akinci.socialapitrial.feature.secure.user.userlist.adapter.viewpager.ViewPagerMode
import com.akinci.socialapitrial.feature.secure.user.data.output.userlist.UserResponse
import com.akinci.socialapitrial.feature.secure.user.userlist.viewModel.ViewPagerViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ViewPagerContentFragment (
    private val mode : ViewPagerMode
) : Fragment() {

    lateinit var binding: FragmentViewPagerContentBinding
    private val viewPagerViewModel : ViewPagerViewModel by activityViewModels()

    private val shimmerAdapter = ShimmerAdapter()
    private lateinit var userListAdapter : CommunityListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager_content, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewPagerViewModel

        // recycler list adapter
        userListAdapter = CommunityListAdapter(clickListener = { userId, screenName, name ->
            // catch user row clicks and navigate to user timeline (user detail) fragment
            Timber.d("Navigation to user timeline (user detail) fragment")

            NavHostFragment.findNavController(this).navigate(
                    UserListFragmentDirections.actionDashboardFragmentToDetailFragment(userId, screenName, name),
                    null
            )
        })

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // fetch followers / friends data.
        viewPagerViewModel.fetchInitialData(mode)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(mode == ViewPagerMode.FOLLOWERS){
            viewPagerViewModel.followersEventHandler.observe(viewLifecycleOwner, { event ->
                handleCommunityData(event)
            })
        }

        if(mode == ViewPagerMode.FRIENDS) {
            viewPagerViewModel.friendsEventHandler.observe(viewLifecycleOwner, { event ->
                handleCommunityData(event)
            })
        }
    }

    private fun handleCommunityData(event : Event<Resource<List<UserResponse>>>){
        // only one time consume this event
        val content = event.getContentIfNotHandled()
        content?.let {
            when (it) {
                is Resource.Loading -> {
                    Timber.d("Shimmer activated")
                    binding.recyclerList.adapter = shimmerAdapter
                }
                is Resource.Success -> {
                    // according to fetch mode followers / friends data is coming here
                    Timber.d("user list is displayed")
                    binding.recyclerList.adapter = userListAdapter
                    userListAdapter.submitList(it.data)
                }
                is Resource.Error -> {
                    // show error message on snackBar
                    SnackBar.makeLarge(binding.root, it.message, SnackBar.LENGTH_LONG).show()
                }
            }
        }
    }
}