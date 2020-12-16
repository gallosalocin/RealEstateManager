package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
import com.openclassrooms.realestatemanager.ui.fragments.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        supportFragmentManager.commit {
            replace(R.id.fl_container, LoginFragment())
        }

        if (resources.getBoolean(R.bool.isTablet)) {
            supportFragmentManager.commit  {
                replace(R.id.fl_container_details, AddFragment())
            }
        }

        setupBottomNavigation(binding)
    }

    // Setup Bottom Navigation
    private fun setupBottomNavigation(binding: ActivityMainBinding) {
        binding.bottomNavView.setOnNavigationItemReselectedListener { /*  */ }

        binding.bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.listFragment -> supportFragmentManager.commit {
                    replace(R.id.fl_container, ListFragment())
                }
                R.id.mapFragment -> supportFragmentManager.commit {
                    replace(R.id.fl_container, MapFragment())
                }
                R.id.searchFragment -> supportFragmentManager.commit {
                    replace(R.id.fl_container, SearchFragment())
                }
                R.id.loanFragment -> supportFragmentManager.commit {
                    replace(R.id.fl_container, LoanFragment())
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}


//        appBarConfiguration = AppBarConfiguration(
//                setOf(R.id.mapFragment, R.id.listFragment, R.id.searchFragment, R.id.loanFragment)
//        )
//
//        setSupportActionBar(binding.toolbar)
//
//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            when (destination.id) {
//                R.id.loginFragment, R.id.registerFragment -> {
//                    binding.toolbar.visibility = View.GONE
//                    binding.bottomNavView.visibility = View.GONE
//                    binding.fabAdd.visibility = View.GONE
//                    if (isTablet) binding.svRight?.visibility = View.GONE
//                }
//                R.id.addFragment, R.id.detailsFragment, R.id.editFragment -> {
//                    binding.toolbar.visibility = View.VISIBLE
//                    binding.bottomNavView.visibility = View.GONE
//                    binding.fabAdd.visibility = View.GONE
//                    if (isTablet) binding.svRight?.visibility = View.GONE
//                }
//                R.id.listFragment -> {
//                    binding.toolbar.visibility = View.VISIBLE
//                    binding.bottomNavView.visibility = View.VISIBLE
//                    binding.fabAdd.visibility = View.VISIBLE
//                    if (isTablet) binding.svRight?.visibility = View.VISIBLE
//                }
//                else -> {
//                    binding.toolbar.visibility = View.VISIBLE
//                    binding.bottomNavView.visibility = View.VISIBLE
//                    binding.fabAdd.visibility = View.GONE
//                    if (isTablet) binding.svRight?.visibility = View.VISIBLE
//                }
//            }
//        }
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        finishAffinity()
//    }


//package com.openclassrooms.realestatemanager.ui
//
//import android.os.Bundle
//import android.view.View
//import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.NavController
//import androidx.navigation.fragment.NavHostFragment
//import androidx.navigation.fragment.findNavController
//import androidx.navigation.ui.AppBarConfiguration
//import androidx.navigation.ui.setupActionBarWithNavController
//import androidx.navigation.ui.setupWithNavController
//import com.openclassrooms.realestatemanager.R
//import com.openclassrooms.realestatemanager.databinding.ActivityMainBinding
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityMainBinding
//    private lateinit var navController: NavController
//    private lateinit var appBarConfiguration: AppBarConfiguration
//    private var isTablet = false
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setTheme(R.style.AppTheme)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        isTablet = resources.getBoolean(R.bool.isTablet)
//
//        setupNavigationComponent()
//
//        binding.fabAdd.setOnClickListener { navController.navigate(R.id.addFragment) }
//    }
//
//    // Setup Navigation Component
//    private fun setupNavigationComponent() {
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHostFragment.findNavController()
//
//        appBarConfiguration = AppBarConfiguration(
//                setOf(R.id.mapFragment, R.id.listFragment, R.id.searchFragment, R.id.loanFragment)
//        )
//
//        setSupportActionBar(binding.toolbar)
//        setupActionBarWithNavController(navController, appBarConfiguration)
//        binding.bottomNavView.setupWithNavController(navController)
//        binding.bottomNavView.setOnNavigationItemReselectedListener { /* */ }
//
//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            when (destination.id) {
//                R.id.loginFragment, R.id.registerFragment -> {
//                    binding.toolbar.visibility = View.GONE
//                    binding.bottomNavView.visibility = View.GONE
//                    binding.fabAdd.visibility = View.GONE
//                    if (isTablet) binding.svRight?.visibility = View.GONE
//                }
//                R.id.addFragment, R.id.detailsFragment, R.id.editFragment -> {
//                    binding.toolbar.visibility = View.VISIBLE
//                    binding.bottomNavView.visibility = View.GONE
//                    binding.fabAdd.visibility = View.GONE
//                    if (isTablet) binding.svRight?.visibility = View.GONE
//                }
//                R.id.listFragment -> {
//                    binding.toolbar.visibility = View.VISIBLE
//                    binding.bottomNavView.visibility = View.VISIBLE
//                    binding.fabAdd.visibility = View.VISIBLE
//                    if (isTablet) binding.svRight?.visibility = View.VISIBLE
//                }
//                else -> {
//                    binding.toolbar.visibility = View.VISIBLE
//                    binding.bottomNavView.visibility = View.VISIBLE
//                    binding.fabAdd.visibility = View.GONE
//                    if (isTablet) binding.svRight?.visibility = View.VISIBLE
//                }
//            }
//        }
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp() || super.onSupportNavigateUp()
//    }
//
//    override fun onBackPressed() {
//        super.onBackPressed()
//        finishAffinity()
//    }
//}