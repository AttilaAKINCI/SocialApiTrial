package com.akinci.socialapitrial.feature.secure.userlist.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.common.component.SnackBar
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.databinding.FragmentUserListBinding
import com.akinci.socialapitrial.feature.login.LoginRootActivity
import com.akinci.socialapitrial.feature.secure.DashboardRootActivity
import com.akinci.socialapitrial.feature.secure.userlist.viewModel.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class UserListFragment : Fragment() {

    lateinit var binding: FragmentUserListBinding
    private val userListViewModel : UserListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_list, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = userListViewModel

        //shows appbar on dashboard screen
        (activity as AppCompatActivity).supportActionBar?.show()
        // set title for first fragment. Probably there is a bug during first fragment initiation.
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.title_user_list)
        setHasOptionsMenu(true)

        binding.btnOpenDetail.setOnClickListener {
            /** Navigate to Detail Page **/
            SnackBar.makeLarge(binding.root, "Navigating to detail page", SnackBar.LENGTH_LONG).show()

            Handler(Looper.getMainLooper()).postDelayed({
                NavHostFragment.findNavController(this).navigate(
                        UserListFragmentDirections.actionDashboardFragmentToDetailFragment(728900207782592512),
                        null)
            }, 1000)
        }

        Timber.d("UserListFragment created..")
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        //fetch initial data
        userListViewModel.fetchInitialDashboardData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userListViewModel.eventHandler.observe(viewLifecycleOwner, { event ->
            // only one time consume this event
            val content = event.getContentIfNotHandled()
            content?.let {
                when (it) {
                    is Resource.Success -> {
                        /** Sign out action **/
                        SnackBar.makeLarge(binding.root, "Signing out. Navigating to Login", SnackBar.LENGTH_LONG).show()

                        Handler(Looper.getMainLooper()).postDelayed({
                            /** remove secure flow from back stack and start login dashboard flow. **/
                            val intent = Intent(activity, LoginRootActivity::class.java)
                            startActivity(intent)
                            activity?.finish()
                        }, 1000)
                    }
                    is Resource.Error -> {
                        // show error message on snackBar
                        SnackBar.makeLarge(binding.root, it.message, SnackBar.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.action_bar_menu, menu)
        super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_menu_log_out) {
            /** initiate log out action here **/
            userListViewModel.signOut()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}