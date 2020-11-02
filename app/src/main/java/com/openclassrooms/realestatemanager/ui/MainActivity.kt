package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.openclassrooms.realestatemanager.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)

        setupNavigationComponent()

    }

    // Setup Navigation Component
    private fun setupNavigationComponent() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
                setOf(R.id.mapFragment, R.id.listFragment, R.id.searchFragment, R.id.loanFragment)
        )

        setSupportActionBar(toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottom_nav_view.setupWithNavController(navController)
        bottom_nav_view.setOnNavigationItemReselectedListener { /* */ }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    toolbar.visibility = View.GONE
                    bottom_nav_view.visibility = View.GONE
                    fab_add.visibility = View.GONE
                }
                R.id.loanFragment -> {
                    toolbar.visibility = View.VISIBLE
                    bottom_nav_view.visibility = View.VISIBLE
                    fab_add.visibility = View.GONE
                }
                else -> {
                    toolbar.visibility = View.VISIBLE
                    bottom_nav_view.visibility = View.VISIBLE
                    fab_add.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}