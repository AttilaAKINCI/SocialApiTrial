package com.akinci.socialapitrial.feature.secure.user.userdetail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.common.component.GlideApp
import com.akinci.socialapitrial.common.component.SnackBar
import com.akinci.socialapitrial.common.component.adapter.ShimmerAdapter
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.databinding.FragmentDetailBinding
import com.akinci.socialapitrial.feature.secure.user.userdetail.adapter.TimeLineListAdapter
import com.akinci.socialapitrial.feature.secure.user.userdetail.viewmodel.UserDetailViewModel
import com.akinci.socialapitrial.feature.secure.user.userlist.adapter.recycler.CommunityListAdapter
import com.akinci.socialapitrial.feature.secure.user.userlist.view.UserListFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    lateinit var binding: FragmentDetailBinding
    private val userDetailViewModel : UserDetailViewModel by viewModels()

    /** fragment args. **/
    var userId = 0L
    var screenName = ""

    private var timeLineListAdapter = TimeLineListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = userDetailViewModel

        // fetch selected product id
        val userDetailFragmentArgs = UserDetailFragmentArgs.fromBundle(requireArguments())
        userId = userDetailFragmentArgs.userId
        screenName = userDetailFragmentArgs.screenName
        val name = userDetailFragmentArgs.name

        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.title_user_detail_param, name)


        Timber.d("UserDetailFragment created..")
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // fetch user time line
        userDetailViewModel.fetchTimeLineData(userId, screenName)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userDetailViewModel.userInfo.observe(viewLifecycleOwner, {
            GlideApp.with(binding.userImage.context)
                    .load(it.profile_image_url_https)
                    .centerCrop()
                    .placeholder(R.drawable.ic_person)
                    .into(binding.userImage)

            val backgroundImageUrl = it.profile_banner_url
            if(!backgroundImageUrl.isNullOrEmpty()){
                binding.profileBackgroundImage.visibility = View.VISIBLE
                GlideApp.with(binding.profileBackgroundImage.context)
                        .load(it.profile_banner_url)
                        .centerCrop()
                        .into(binding.profileBackgroundImage)
            }else{
                binding.profileBackgroundImage.visibility = View.GONE
            }
        })

        userDetailViewModel.eventHandler.observe(viewLifecycleOwner, { event ->
            // only one time consume this event
            val content = event.getContentIfNotHandled()
            content?.let {
                when (it) {
                    is Resource.Loading -> {
                        Timber.d("Shimmer activated")
                        binding.recyclerListTweets.visibility = View.GONE
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Resource.Success -> {
                        // user timeline data is coming here
                        Timber.d("timeline tweets is displayed")

                        it.data?.let { data ->
                                if(data.isNotEmpty()){
                                binding.recyclerListTweets.visibility = View.VISIBLE
                                binding.progressBar.visibility = View.GONE

                                binding.recyclerListTweets.adapter = timeLineListAdapter
                                timeLineListAdapter.submitList(it.data)
                            }
                        }
                    }
                    is Resource.Error -> {
                        // show error message on snackBar
                        SnackBar.makeLarge(binding.root, it.message, SnackBar.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

}