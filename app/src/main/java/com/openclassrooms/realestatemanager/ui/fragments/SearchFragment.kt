package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: MainViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        setHasOptionsMenu(true)
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu_main, menu)
        menu.getItem(0).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_currency -> Toast.makeText(requireContext(), "currency", Toast.LENGTH_SHORT).show()
            R.id.tb_menu_logout -> {
                findNavController().navigate(R.id.logoutFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}