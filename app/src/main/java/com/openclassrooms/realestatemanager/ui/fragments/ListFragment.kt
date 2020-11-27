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
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    private lateinit var binding: FragmentListBinding
    private lateinit var propertyAdapter: PropertyAdapter
    private val viewModel: MainViewModel by viewModels()
    private lateinit var menu: Menu

    private lateinit var propertiesList: List<PropertyWithAllData>


    companion object {
        var isDollar: Boolean? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        setHasOptionsMenu(true)

        propertyAdapter = PropertyAdapter()
        propertiesList = ArrayList()

        setupRecyclerView()

        viewModel.getAllProperties.observe(viewLifecycleOwner, {
            propertiesList = it
            propertyAdapter.properties = propertiesList
        })


        propertyAdapter.setOnItemClickListener {
            val action = ListFragmentDirections.actionListFragmentToDetailsFragment(it)
            findNavController().navigate(action)
        }

    }

    // Setup recyclerview
    private fun setupRecyclerView() {
        binding.apply {
            rvList.adapter = propertyAdapter
            rvList.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu_main, menu)
        menu.getItem(2).isVisible = false
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_currency -> {
                when (isDollar) {
                    true -> {
                        propertyAdapter.notifyDataSetChanged()
                        isDollar = false
                        menu.getItem(0).setIcon(R.drawable.ic_euro)
                    }

                    null, false -> {
                        propertyAdapter.notifyDataSetChanged()
                        isDollar = true
                        menu.getItem(0).setIcon(R.drawable.ic_dollar)
                    }
                }
            }
            R.id.tb_menu_reload -> {
                Toast.makeText(requireContext(), "List updated", Toast.LENGTH_SHORT).show()
            }
            R.id.tb_menu_logout -> {
                findNavController().navigate(R.id.logoutFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}