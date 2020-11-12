package com.openclassrooms.realestatemanager.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.other.Constants.ACTION_SHOW_LIST_FRAGMENT
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigateToListFragmentIfNeeded(intent)

        setupNavigationComponent()
        binding.fabAdd.setOnClickListener{
            navController.navigate(R.id.addFragment)
        }

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToListFragmentIfNeeded(intent)
    }

    // Setup Navigation Component
    private fun setupNavigationComponent() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
                setOf(R.id.mapFragment, R.id.listFragment, R.id.searchFragment, R.id.loanFragment)
        )

        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavView.setupWithNavController(navController)
        binding.bottomNavView.setOnNavigationItemReselectedListener { /* */ }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    binding.toolbar.visibility = View.GONE
                    binding.bottomNavView.visibility = View.GONE
                    binding.fabAdd.visibility = View.GONE
                }
                R.id.loanFragment, R.id.searchFragment -> {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.bottomNavView.visibility = View.VISIBLE
                    binding.fabAdd.visibility = View.GONE
                }
                R.id.addFragment, R.id.detailsFragment -> {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.bottomNavView.visibility = View.INVISIBLE
                    binding.fabAdd.visibility = View.INVISIBLE
                }
                else -> {
                    binding.toolbar.visibility = View.VISIBLE
                    binding.bottomNavView.visibility = View.VISIBLE
                    binding.fabAdd.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun navigateToListFragmentIfNeeded(intent: Intent?) {
        if (intent?.action == ACTION_SHOW_LIST_FRAGMENT) {
            navController.navigate(R.id.action_global_list_fragment)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}