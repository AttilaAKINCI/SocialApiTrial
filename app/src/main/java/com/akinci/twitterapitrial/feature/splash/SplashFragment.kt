package com.akinci.twitterapitrial.feature.splash

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
import com.akinci.twitterapitrial.databinding.FragmentSplashBinding
import timber.log.Timber

class SplashFragment : Fragment() {
    lateinit var binding: FragmentSplashBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        Timber.d("SplashFragment created..")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //hide appbar on splash screen
        (activity as AppCompatActivity).supportActionBar?.hide()
        navigateToLogin()
    }

    private fun navigateToLogin(){
        Handler(Looper.getMainLooper()).postDelayed({
            /** Navigate to Login Page **/
            NavHostFragment.findNavController(this).navigate(R.id.action_splashFragment_to_loginFragment)
        }, 1000)
    }

}