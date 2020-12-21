package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.PropertyAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.ui.fragments.DetailsFragment.Companion.isForDetailsFragment
import com.openclassrooms.realestatemanager.ui.fragments.DetailsFragment.Companion.isFromDetailsFragment
import com.openclassrooms.realestatemanager.ui.fragments.MapFragment.Companion.isFromMapFragment
import com.openclassrooms.realestatemanager.ui.viewmodels.ListViewModel
import com.openclassrooms.realestatemanager.utils.Utils.hideDetailsContainerTabletLandscape
import com.openclassrooms.realestatemanager.utils.Utils.showDetailsContainerTabletLandscape
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ListFragment : Fragment(R.layout.fragment_list) {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ListViewModel by viewModels()
    private lateinit var propertyAdapter: PropertyAdapter

    private lateinit var menu: Menu
    private lateinit var propertiesList: List<PropertyWithAllData>


    companion object {
        var isDollar: Boolean? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        requireActivity().title = getString(R.string.app_name)

        hideDetailsContainerTabletLandscape(requireActivity())
        isFromDetailsFragment = false

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("test-> onViewCreated")
        setHasOptionsMenu(true)

        propertyAdapter = PropertyAdapter()
        propertiesList = ArrayList()

        setupRecyclerView()

        viewModel.getAllProperties.observe(viewLifecycleOwner) {
            propertiesList = it
            propertyAdapter.submitList(it)
        }

        propertyAdapter.setOnItemClickListener {
            viewModel.setCurrentPropertyId(it.property.id)
            isFromMapFragment = false
            isForDetailsFragment = true
            parentFragmentManager.commit {
                if (resources.getBoolean(R.bool.isTablet)) {
                    showDetailsContainerTabletLandscape(requireActivity())
                    replace(R.id.fl_container_tablet, DetailsFragment())
                } else {
                    replace(R.id.fl_container, DetailsFragment())
                }
                addToBackStack(null)
            }
        }

        binding.fabAdd.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fl_container, AddFragment())
                addToBackStack(null)
            }
            hideDetailsContainerTabletLandscape(requireActivity())
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
        Timber.d("test-> onCreateOptionsMenu")
        inflater.inflate(R.menu.custom_toolbar, menu)
        menu.getItem(0).isVisible = true
        menu.getItem(1).isVisible = false
        menu.getItem(2).isVisible = false
        menu.getItem(3).isVisible = false
        menu.getItem(4).isVisible = false
        menu.getItem(5).isVisible = true
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
            R.id.tb_menu_logout -> {
                parentFragmentManager.commit {
                    replace(R.id.fl_container, LogoutFragment())
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onResume() {
        super.onResume()
        Timber.d("test-> onResume")
    }

    override fun onStart() {
        super.onStart()
        Timber.d("test-> onStart")
    }

    override fun onPause() {
        super.onPause()
        Timber.d("test-> onPause")
    }

    override fun onStop() {
        super.onStop()
        Timber.d("test-> onStop")
    }
}