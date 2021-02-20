package com.akinci.twitterapitrial.feature.dashboard.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.akinci.twitterapitrial.R
import com.akinci.twitterapitrial.common.component.SnackBar
import com.akinci.twitterapitrial.databinding.FragmentDashboardBinding
import timber.log.Timber

class DashboardFragment : Fragment() {
    lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        //shows appbar on dashboard screen
        (activity as AppCompatActivity).supportActionBar?.show()

        binding.btnOpenDetail.setOnClickListener {
            /** Navigate to Detail Page **/
            SnackBar.makeLarge(binding.root, "Navigating to detail page", SnackBar.LENGTH_LONG).show()

            Handler(Looper.getMainLooper()).postDelayed({
                NavHostFragment.findNavController(this).navigate(R.id.action_dashboardFragment_to_detailFragment)
            }, 1000)
        }

        Timber.d("DashboardFragment created..")
        return binding.root
    }

}