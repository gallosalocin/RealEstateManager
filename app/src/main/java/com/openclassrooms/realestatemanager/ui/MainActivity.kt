package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.MenuItem
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
        setupBottomNavigation(binding)

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.fl_container, LoginFragment())
            }
        }
    }

    // Setup Bottom Navigation
    private fun setupBottomNavigation(binding: ActivityMainBinding) {
        binding.bottomNavView.setOnNavigationItemReselectedListener { /* */ }

        binding.bottomNavView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.listFragment -> {
                    supportFragmentManager.commit {
                        replace(R.id.fl_container, ListFragment())
                    }
                }
                R.id.mapFragment -> {
                    supportFragmentManager.commit {
                        replace(R.id.fl_container, MapFragment())
                    }
                }
                R.id.searchFragment -> {
                    supportFragmentManager.commit {
                        replace(R.id.fl_container, SearchFragment())
                    }
                }
                R.id.loanFragment -> {
                    supportFragmentManager.commit {
                        replace(R.id.fl_container, LoanFragment())
                    }
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