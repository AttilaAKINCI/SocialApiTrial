package com.akinci.socialapitrial.feature.login.view

import android.animation.Animator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.databinding.FragmentSplashBinding
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

        binding.animation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) { navigateToLogin() }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })

        Timber.d("SplashFragment created..")
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.animation.playAnimation()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //hide appbar on splash screen
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    private fun navigateToLogin(){
        Handler(Looper.getMainLooper()).postDelayed({
            /** Navigate to Login Page **/
            NavHostFragment.findNavController(this).navigate(R.id.action_splashFragment_to_loginFragment)
        }, 100)
    }

}