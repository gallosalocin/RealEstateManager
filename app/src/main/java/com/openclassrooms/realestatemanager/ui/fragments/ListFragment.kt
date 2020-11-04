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
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.PropertyAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.random.Random

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    private lateinit var binding: FragmentListBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var menu: Menu

    private lateinit var propertiesList: MutableList<Property>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)

        setHasOptionsMenu(true)


        val propertyAdapter = PropertyAdapter()

        binding.apply {
            rvList.adapter = propertyAdapter
            rvList.layoutManager = LinearLayoutManager(requireContext())
        }

        propertyAdapter.properties = listProperty()

        propertyAdapter.setOnItemClickListener {
            Toast.makeText(requireContext(), it.address, Toast.LENGTH_SHORT).show()
        }
    }

    private fun listProperty(): List<Property> {
        propertiesList = ArrayList()
        for (i in 1..25) {
            propertiesList.add(Property("Villa $i", Random.nextInt(1000000, 10000000), 0f, 0, "", "Paris $i"))
        }
        return propertiesList
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu, menu)
        this.menu = menu
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