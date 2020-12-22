package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.PropertyAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentListBinding
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.ui.fragments.DetailsFragment.Companion.isForDetailsFragment
import com.openclassrooms.realestatemanager.ui.fragments.DetailsFragment.Companion.isFromDetailsFragment
import com.openclassrooms.realestatemanager.ui.fragments.MapFragment.Companion.isFromMapFragment
import com.openclassrooms.realestatemanager.ui.viewmodels.ListViewModel
import com.openclassrooms.realestatemanager.utils.Utils.hideDetailsContainer
import com.openclassrooms.realestatemanager.utils.Utils.showDetailsContainer
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("test-> onViewCreated")
        setHasOptionsMenu(true)

        requireActivity().title = getString(R.string.app_name)
        val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view)

        hideDetailsContainer(requireActivity())
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        bottomNavigationView.visibility = View.VISIBLE

        isFromDetailsFragment = false
        propertiesList = ArrayList()

        setupClickOnProperty()
        setupRecyclerView()
        setupClickFabButton()

        viewModel.getAllProperties.observe(viewLifecycleOwner) {
            propertiesList = it
            propertyAdapter.submitList(it)
        }

    }

    // Setup recyclerview
    private fun setupRecyclerView() {
        binding.apply {
            rvList.adapter = propertyAdapter
            rvList.layoutManager = LinearLayoutManager(requireContext())
        }
    }

    // Setup click on property
    private fun setupClickOnProperty() {
        propertyAdapter = PropertyAdapter {
            viewModel.setCurrentPropertyId(it.property.id)
            isFromMapFragment = false
            isForDetailsFragment = true
            parentFragmentManager.commit {
                if (resources.getBoolean(R.bool.isTablet)) {
                    showDetailsContainer(requireActivity())
                    replace(R.id.fl_container_tablet, DetailsFragment(), "detailsFragmentTablet")
                } else {
                    setCustomAnimations(R.anim.from_right, R.anim.to_right, R.anim.from_right, R.anim.to_right)
                    replace(R.id.fl_container, DetailsFragment(), "detailsFragment")
                }
                addToBackStack(null)
            }
        }
    }

    // Setup click on Fab button
    private fun setupClickFabButton() {
        binding.fabAdd.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fl_container, AddFragment())
                addToBackStack(null)
            }
            hideDetailsContainer(requireActivity())
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
}