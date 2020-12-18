package com.openclassrooms.realestatemanager.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentMapBinding
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.ui.fragments.DetailsFragment.Companion.isForDetailsFragment
import com.openclassrooms.realestatemanager.ui.fragments.DetailsFragment.Companion.isFromDetailsFragment
import com.openclassrooms.realestatemanager.ui.viewmodels.MapViewModel
import com.openclassrooms.realestatemanager.utils.Utils.formatInDollar
import com.openclassrooms.realestatemanager.utils.Utils.getLocationFromAddress
import com.openclassrooms.realestatemanager.utils.Utils.isGPSEnabled
import com.openclassrooms.realestatemanager.utils.Utils.isInternetConnected
import com.openclassrooms.realestatemanager.utils.Utils.setupAlertDialogToActivateGPS
import com.openclassrooms.realestatemanager.utils.Utils.setupAlertDialogToActivateInternet
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

@AndroidEntryPoint
class MapFragment : Fragment(R.layout.fragment_map), OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MapViewModel by viewModels()

    private lateinit var bottomNavigationView: BottomNavigationView
    private var map: GoogleMap? = null
    private lateinit var menu: Menu
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var propertiesList: List<PropertyWithAllData> = ArrayList()

    companion object {
        var isFromMapFragment: Boolean = false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentMapBinding.inflate(inflater, container, false)

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view)

        if (isFromDetailsFragment) {
            (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
            bottomNavigationView.visibility = View.GONE
        } else {
            requireActivity().toolbar.title = getString(R.string.app_name)
        }

        binding.mapView.onCreate(savedInstanceState)
        binding.mapView.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())

        viewModel.getAllProperties.observe(viewLifecycleOwner) {
            propertiesList = it
            if (isInternetConnected(requireContext()))
                setupMarker()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu_main, menu)
        menu.getItem(0).isVisible = false
        menu.getItem(2).isVisible = false
        this.menu = menu
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_logout -> {
                parentFragmentManager.commit {
                    replace(R.id.fl_container, LogoutFragment())
                }
            }
            R.id.tb_menu_reload -> if (isInternetConnected(requireContext())) setupMarker()
        }
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        Timber.d("onMapReady")
        map = googleMap

        map?.apply {
            isMyLocationEnabled = true
            uiSettings?.isMyLocationButtonEnabled = false
        }

        binding.ivFindLocation.setOnClickListener {
            if (isGPSEnabled(requireContext())) {
                binding.ivFindLocation.setImageResource(R.drawable.ic_gps)
                getDeviceLocation()
            } else {
                binding.ivFindLocation.setImageResource(R.drawable.ic_no_gps)
                setupAlertDialogToActivateGPS(requireContext(), Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), null)
            }
        }

        if (isInternetConnected(requireContext())) {
            setupMarker()
            setupMapLocation()
        } else {
            setupAlertDialogToActivateInternet(
                    requireContext(),
                    Intent(Settings.ACTION_WIFI_SETTINGS),
                    Intent(Settings.ACTION_DATA_ROAMING_SETTINGS),
                    null)
        }

        if (!isFromDetailsFragment) {
            map?.setOnInfoWindowClickListener(this)
        }
    }

    // setup map location displayed
    private fun setupMapLocation() {
        if (isFromDetailsFragment) {
            viewModel.getViewStateLiveData().observe(viewLifecycleOwner) { currentPropertyWithAllData ->

                val currentProperty = currentPropertyWithAllData.property

                requireActivity().toolbar.title = currentProperty.type

                val propertyAddress =
                        "${currentProperty.street} + ${currentProperty.postcode} + ${currentProperty.city} + ${currentProperty.country}"
                val propertyLatLng = getLocationFromAddress(requireContext(), propertyAddress)
                val cameraZoomProperty = CameraUpdateFactory.newLatLngZoom(propertyLatLng, 16F)
                map?.animateCamera(cameraZoomProperty)
            }
        } else {
            val defaultCameraUpdate = CameraUpdateFactory.newLatLngZoom(LatLng(39.868, -102.93), 3F)
            map?.moveCamera(defaultCameraUpdate)
        }
    }

    // setup markers
    private fun setupMarker() {
        for (property in propertiesList) {
            val currentProperty = property.property
            val propertyAddress = "${currentProperty.street} + ${currentProperty.postcode} + ${currentProperty.city} + ${currentProperty.country}"
            val propertyLatLng = getLocationFromAddress(requireContext(), propertyAddress)

            val marker = map?.addMarker(MarkerOptions()
                    .position(propertyLatLng)
                    .title(if (currentProperty.isSold) "Sold" else "Available")
                    .snippet(formatInDollar(currentProperty.priceInDollars, 0)))
            marker?.tag = property.property.id
        }
    }

    // Info Window callback
    override fun onInfoWindowClick(marker: Marker?) {
            viewModel.setCurrentPropertyId(marker?.tag as Int)
            isFromMapFragment = true
            isForDetailsFragment = true
            parentFragmentManager.commit {
                replace(R.id.fl_container, DetailsFragment())
                addToBackStack(null)
            }
    }

    // Find device location
    @SuppressLint("MissingPermission")
    fun getDeviceLocation() {
        Timber.d("getDeviceLocation")
        try {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val latitude = location.latitude
                    val longitude = location.longitude
                    val currentLatLng = LatLng(latitude, longitude)
                    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(currentLatLng, 8F)
                    map?.animateCamera(cameraUpdate)
                }
            }
        } catch (error: SecurityException) {
            Timber.e("Exception: ${error.message}")
        }
    }

    // Handle Map's Lifecycle
    override fun onResume() {
        super.onResume()
        Timber.d("onResume")
        binding.mapView.onResume()
    }

    override fun onStart() {
        super.onStart()
        Timber.d("onStart")
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        Timber.d("onStop")
        binding.mapView.onStop()
    }

    override fun onPause() {
        super.onPause()
        Timber.d("onPause")
        binding.mapView.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.mapView.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}