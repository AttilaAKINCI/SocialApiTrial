package com.akinci.twitterapitrial.feature.login.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.akinci.twitterapitrial.R
import com.akinci.twitterapitrial.databinding.FragmentLoginBinding
import com.akinci.twitterapitrial.databinding.FragmentSplashBinding
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
            NavHostFragment.findNavController(this).navigate(R.id.action_loginFragment_to_dashboardFragment)
        }

        Timber.d("LoginFragment created..")
        return binding.root
    }

}