package com.akinci.socialapitrial.feature.secure.userdetail.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.common.component.SnackBar
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.databinding.FragmentDetailBinding
import com.akinci.socialapitrial.feature.secure.userdetail.viewmodel.UserDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserDetailFragment : Fragment() {

    lateinit var binding: FragmentDetailBinding
    private val userDetailViewModel : UserDetailViewModel by viewModels()

    var userId = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = userDetailViewModel

        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.title_user_detail_param, "Attila")

        // fetch selected product id
        val userDetailFragmentArgs = UserDetailFragmentArgs.fromBundle(requireArguments())
        userId = userDetailFragmentArgs.userId

        Timber.d("UserDetailFragment created..")
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // fetch user time line
        if(userId != 0L){ userDetailViewModel.getUserTimeLine(userId) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        userDetailViewModel.eventHandler.observe(viewLifecycleOwner, { event ->
            // only one time consume this event
            val content = event.getContentIfNotHandled()
            content?.let {
                when (it) {
                    is Resource.Success -> { }
                    is Resource.Error -> {
                        // show error message on snackBar
                        SnackBar.makeLarge(binding.root, it.message, SnackBar.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

}