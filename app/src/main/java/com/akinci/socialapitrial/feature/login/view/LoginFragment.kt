package com.akinci.socialapitrial.feature.login.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.akinci.socialapitrial.BuildConfig
import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.common.component.SnackBar
import com.akinci.socialapitrial.common.helper.Resource
import com.akinci.socialapitrial.common.storage.LocalPreferenceConfig
import com.akinci.socialapitrial.common.storage.Preferences
import com.akinci.socialapitrial.databinding.FragmentLoginBinding
import com.akinci.socialapitrial.feature.dashboard.DashboardRootActivity
import com.akinci.socialapitrial.feature.login.viewmodel.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    lateinit var binding: FragmentLoginBinding
    private val loginViewModel : LoginViewModel by viewModels()

    private lateinit var twitterDialog : Dialog

    @Inject
    lateinit var sharedPreferences : Preferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.vm = loginViewModel

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginViewModel.authorizeEventHandler.observe(viewLifecycleOwner, { event ->
            // only one time consume this event
            val content = event.getContentIfNotHandled()
            content?.let {
                when (it) {
                    is Resource.Success -> {
                        // request Token service is successful, I can navigate user to
                        // authorize page.
                        it.data?.let { data ->
                            SnackBar.make(binding.root, "Authorize: $data", SnackBar.LENGTH_LONG).show()
                            setupTwitterWebViewDialog(data)
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

    @SuppressLint("SetJavaScriptEnabled")
    fun setupTwitterWebViewDialog(url: String) {
        val context = requireContext()
        twitterDialog = Dialog(context)
        val webView = WebView(context)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = TwitterWebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
        twitterDialog.setContentView(webView)
        twitterDialog.show()
    }

    inner class TwitterWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(
            view: WebView?,
            request: WebResourceRequest?
        ): Boolean {
            if (request?.url.toString().startsWith(BuildConfig.CALLBACK_URL)) {

                Timber.d("Authorization URL: ${request?.url.toString()}")
                handleUrl(request?.url.toString())

                // Close the dialog after getting the oauth_verifier
                if (request?.url.toString().contains(BuildConfig.CALLBACK_URL)) { twitterDialog.dismiss() }
                return true
            }
            return false
        }

        // Get the oauth_verifier
        private fun handleUrl(url: String) {
            val uri = Uri.parse(url)
            val oauthVerifier = uri.getQueryParameter("oauth_verifier") ?: ""

            sharedPreferences.setStoredTag(LocalPreferenceConfig.OAUTH_TOKEN_VERIFIER, oauthVerifier)
            SnackBar.make(binding.root, "AuthVerifier : $oauthVerifier", SnackBar.LENGTH_LONG).show()

            //call access token service in VM.
            loginViewModel.getAccessToken()
        }
    }

}