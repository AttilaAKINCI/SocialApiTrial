package com.akinci.socialapitrial.feature.secure.userlist.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.databinding.FragmentUserListBinding
import com.akinci.socialapitrial.databinding.FragmentViewPagerContentBinding
import com.akinci.socialapitrial.feature.secure.userlist.viewModel.UserListViewModel
import com.akinci.socialapitrial.feature.secure.userlist.viewModel.ViewPagerViewModel
import com.akinci.socialapitrial.feature.secure.userlist.viewpager.ViewPagerMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ViewPagerContentFragment (
    private val mode : ViewPagerMode
) : Fragment() {

    lateinit var binding: FragmentViewPagerContentBinding
    private val viewPagerViewModel : ViewPagerViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_view_pager_content, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = viewPagerViewModel

        return binding.root
    }

    override fun onStart() {
        super.onStart()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}