package com.akinci.socialapitrial.common.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.akinci.socialapitrial.R
import com.akinci.socialapitrial.databinding.ActivityRootBinding

open class RootActivity : AppCompatActivity()  {

    private lateinit var navigationController: NavController
    lateinit var binding : ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_root)

        // setup navigation
        val navHostFragment = supportFragmentManager.findFragmentByTag("navHostFragment") as NavHostFragment
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(getNavigationGraph()) // gets navigation graph from each root activity
        navigationController = navHostFragment.navController
        navigationController.graph = navGraph

        // tell navigation controller that which fragments will be at the top of backstack
        // (hides backbutton for fragments which are placed at top)
        val appBarConfiguration = AppBarConfiguration(getFragmentsThatHidesBackButton())

        // remove extra padding between arrow and toolbar title
        binding.toolbar.contentInsetStartWithNavigation = 10

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setupWithNavController(navigationController, appBarConfiguration)
        binding.toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    open fun getNavigationGraph() : Int { return -1 }
    open fun getFragmentsThatHidesBackButton() : Set<Int> { return setOf() }
}