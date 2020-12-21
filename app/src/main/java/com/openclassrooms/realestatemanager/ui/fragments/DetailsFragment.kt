package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.PhotoAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentDetailsBinding
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.PropertyPhoto
import com.openclassrooms.realestatemanager.ui.fragments.MapFragment.Companion.isFromMapFragment
import com.openclassrooms.realestatemanager.ui.viewmodels.DetailsViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.Utils.hideDetailsContainerTabletLandscape
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var currentProperty: Property
    private lateinit var menu: Menu

    @Inject
    lateinit var glide: RequestManager
    private var isDollar = true
    private lateinit var propertyPhotosList: List<PropertyPhoto>


    companion object {
        var isForDetailsFragment = false
        var isFromDetailsFragment = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view)

        setHasOptionsMenu(true)

        if (resources.getBoolean(R.bool.isTablet)) {
            (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            bottomNavigationView.visibility = View.VISIBLE
        } else {
            (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            bottomNavigationView.visibility = View.GONE
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Timber.d("test-> onViewCreated")

        photoAdapter = PhotoAdapter()
        propertyPhotosList = ArrayList()

        loadProperty()
        setupRecyclerView()

        binding.tvDescription.movementMethod = ScrollingMovementMethod()

        photoAdapter.setOnItemClickListener {
            glide.load(it.filename).into(binding.ivPhoto)
        }
    }

    // Setup recyclerview
    private fun setupRecyclerView() {
        binding.rvDetails.apply {
            adapter = photoAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    // Display price in Dollars or Euro
    private fun displayConvertedAndFormattedPrice(currentPropertyPrice: Int) {
        if (isDollar) {
            menu.getItem(0).setIcon(R.drawable.ic_dollar)
            val price = Utils.convertDollarToEuro(currentPropertyPrice)
            binding.tvPrice.text = Utils.formatInEuro(price, 0)
            isDollar = !isDollar
        } else {
            menu.getItem(0).setIcon(R.drawable.ic_euro)
            binding.tvPrice.text = Utils.formatInDollar(currentPropertyPrice, 0)
            isDollar = !isDollar
        }
    }

    // Load property
    private fun loadProperty() {
        viewModel.getViewStateLiveData().observe(viewLifecycleOwner) { currentPropertyWithAllData ->
            currentProperty = currentPropertyWithAllData.property

            requireActivity().title = currentProperty.type

            if (currentPropertyWithAllData.photos.none { it.propertyId == (currentPropertyWithAllData.property.id) }) {
                viewModel.insertPropertyPhoto(PropertyPhoto(currentProperty.coverPhoto, currentProperty.labelPhoto, currentProperty.id))
            } else {
                propertyPhotosList = currentPropertyWithAllData.photos.filter { it.propertyId == currentProperty.id }
            }
            photoAdapter.photosListDetails = propertyPhotosList.reversed()

            binding.apply {
                glide.load(currentProperty.coverPhoto).into(ivPhoto)

                tvPrice.text = Utils.formatInDollar(currentProperty.priceInDollars, 0)

                if (currentProperty.isSold) {
                    tvStatus.text = getString(R.string.sold_cap)
                    tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorSold))
                    tvSoldDate.visibility = View.VISIBLE
                    tvSoldDate.text = getString(R.string.sold_date_param, currentProperty.soldDate)
                } else {
                    tvStatus.text = getString(R.string.available_cap)
                    tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAvailable))
                }

                chipRestaurant.isChecked = currentProperty.poi[0].toString().toBoolean()
                chipBar.isChecked = currentProperty.poi[1].toString().toBoolean()
                chipStore.isChecked = currentProperty.poi[2].toString().toBoolean()
                chipPark.isChecked = currentProperty.poi[3].toString().toBoolean()
                chipSchool.isChecked = currentProperty.poi[4].toString().toBoolean()
                chipHospital.isChecked = currentProperty.poi[5].toString().toBoolean()

                tvEntryDate.text = getString(R.string.entry_date_param, currentProperty.availableDate)
                tvDescription.text = if (currentProperty.description == "") "Write something!!!" else currentProperty.description
                tvArea.text = if (currentProperty.areaInMeters.toString() == "") "0 m²" else "${currentProperty.areaInMeters} m²"
                tvRoom.text = if (currentProperty.nbrRoom >= 10) "${currentProperty.nbrRoom}+" else currentProperty.nbrRoom.toString()
                tvBedroom.text = if (currentProperty.nbrBedroom >= 10) "${currentProperty.nbrBedroom}+" else currentProperty.nbrBedroom.toString()
                tvBathroom.text = if (currentProperty.nbrBathroom >= 10) "${currentProperty.nbrBathroom}+" else currentProperty.nbrBathroom.toString()
                tvStreet.text = currentProperty.street
                tvPostcode.text = currentProperty.postcode
                tvCity.text = currentProperty.city
                tvCountry.text = currentProperty.country
                tvAgent.text = getString(R.string.agent_name, currentPropertyWithAllData.agent.firstName, currentPropertyWithAllData.agent.lastName)

                val currentPropertyAddress = "${currentProperty.street}+${currentProperty.postcode}+${currentProperty.city}"

                glide.load("https://maps.googleapis.com/maps/api/staticmap?" +
                        "center=$currentPropertyAddress" +
                        "&zoom=14" +
                        "&size=200x200" +
                        "&scale=2" +
                        "&maptype=terrain" +
                        "&markers=size:mid%7C$currentPropertyAddress" +
                        "&key=${BuildConfig.ApiKey}")
                        .centerCrop().into(ivMap)
            }

            if (!isFromMapFragment) {
                binding.ivMap.setOnClickListener {
                    viewModel.setCurrentPropertyId(currentProperty.id)
                    isFromDetailsFragment = true
                    parentFragmentManager.commit {
                        replace(R.id.fl_container, MapFragment())
                        addToBackStack(null)
                    }
                }
            }

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
        menu.getItem(3).isVisible = true
        menu.getItem(4).isVisible = false
        menu.getItem(5).isVisible = true
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_currency -> displayConvertedAndFormattedPrice(currentProperty.priceInDollars)
            R.id.tb_menu_edit -> {
                if (resources.getBoolean(R.bool.isTablet)) {
                    hideDetailsContainerTabletLandscape(requireActivity())
                }
                viewModel.setCurrentPropertyId(currentProperty.id)
                parentFragmentManager.commit {
                    replace(R.id.fl_container, EditFragment())
                    addToBackStack(null)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        Timber.d("test-> onStop")
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        bottomNavigationView.visibility = View.VISIBLE
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
}