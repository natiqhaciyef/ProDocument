package com.natiqhaciyef.prodocument.ui.view.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import coil.size.Dimension
import com.natiqhaciyef.prodocument.R
import com.natiqhaciyef.prodocument.databinding.ActivityMainBinding
import com.natiqhaciyef.core.base.ui.BaseActivity
import com.natiqhaciyef.prodocument.ui.manager.LanguageManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerViewHome) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNavBar, navHostFragment.navController)

        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment,
            R.id.filesFragment,
            R.id.premiumFragment,
            R.id.profileFragment
        ))
        binding.materialToolbar.setupWithNavController(
            navHostFragment.navController,
            appBarConfiguration
        )
        setSupportActionBar(binding.materialToolbar)

        LanguageManager.loadLocale(this)

        actionBar?.setDisplayShowHomeEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}