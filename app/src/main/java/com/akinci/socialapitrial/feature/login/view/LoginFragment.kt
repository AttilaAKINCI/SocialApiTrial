package com.akinci.socialapitrial.feature.login.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.common.component.SnackBar
import com.akinci.socialapitrial.databinding.FragmentLoginBinding
import com.akinci.socialapitrial.feature.dashboard.DashboardRootActivity
import timber.log.Timber

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.btnSignIn.setOnClickListener{
            /** Navigate to DashBoard Page **/
            SnackBar.makeLarge(binding.root, "Twitter Login Clicked", SnackBar.LENGTH_LONG).show()

            Handler(Looper.getMainLooper()).postDelayed({
                /** remove login flow from back stack and start secure dashboard flow. **/
                val intent = Intent(activity, DashboardRootActivity::class.java)
                startActivity(intent)
                activity?.finish()
            }, 1000)
        }

        Timber.d("LoginFragment created..")
        return binding.root
    }

}