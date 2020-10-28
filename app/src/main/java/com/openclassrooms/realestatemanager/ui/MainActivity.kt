package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigationComponent()

    }

    // Setup Navigation Component
    private fun setupNavigationComponent() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setSupportActionBar(toolbar)

        setupActionBarWithNavController(navController)
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