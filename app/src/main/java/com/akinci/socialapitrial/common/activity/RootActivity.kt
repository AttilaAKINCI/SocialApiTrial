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

//TODO Direk kullanimi olmadigi icin abstract olabilir, open methodlariyla beraber
abstract class RootActivity : AppCompatActivity() {

    private lateinit var navigationController: NavController

    //TODO dataBinding yerine ViewBinding olabilir cunku view model set etmiyorsun
    lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_root)

        // setup navigation
        //TODO
        /*
        * supportFragmentManager.findFragmentByTag("navHostFragment") su ifade nullable, eger tagin ismi degistirilirse "android:tag="navHostFragment"
        * as ile direk cast edersen crash edebilir -> as NavHostFragment? yapabilirsin
        * findFragmentById daha mantikli olabilir, en azindan isim degisirse compile error verir
        * */
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

    abstract fun getNavigationGraph(): Int

    abstract fun getFragmentsThatHidesBackButton(): Set<Int>
}