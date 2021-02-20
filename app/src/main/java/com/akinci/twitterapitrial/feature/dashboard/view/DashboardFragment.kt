package com.akinci.twitterapitrial.feature.dashboard.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.fragment.app.Fragment
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
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dashboard, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        //shows appbar on dashboard screen
        (activity as AppCompatActivity).supportActionBar?.show()
        setHasOptionsMenu(true)

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_menu, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu_log_out) {
            /** initiate log out action here **/
            SnackBar.makeLarge(binding.root, "Logging out..", SnackBar.LENGTH_LONG).show()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}