package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.openclassrooms.realestatemanager.BuildConfig
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.PhotoAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentDetailsBinding
import com.openclassrooms.realestatemanager.models.database.Property
import com.openclassrooms.realestatemanager.models.database.PropertyPhoto
import com.openclassrooms.realestatemanager.ui.fragments.MapFragment.Companion.isFromMapFragment
import com.openclassrooms.realestatemanager.ui.viewmodels.DetailsViewModel
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add.*
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var editFragment: EditFragment

    private lateinit var photoAdapter: PhotoAdapter
    private val viewModel: DetailsViewModel by viewModels()

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

        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view)
        bottomNavigationView.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        photoAdapter = PhotoAdapter()
        propertyPhotosList = ArrayList()

        editFragment = EditFragment()

        loadPropertyPhotos()
        setupRecyclerView()

        viewModel.getViewStateLiveData().observe(viewLifecycleOwner) { currentPropertyWithAllData ->
            requireActivity().toolbar.title = currentPropertyWithAllData.property.type

            if (currentPropertyWithAllData.photos.none { it.propertyId == (currentPropertyWithAllData.property.id) }) {
                //viewModel.insertPropertyPhoto(PropertyPhoto(currentProperty.coverPhoto, currentProperty.labelPhoto, currentProperty.id))
            } else {
                //propertyPhotosList = propertyPhoto.filter { it.propertyId == currentProperty.id }
            }
            photoAdapter.photosListDetails = propertyPhotosList.reversed()

            val currentProperty = currentPropertyWithAllData.property

            binding.apply {
                glide.load(currentProperty.coverPhoto).centerCrop().into(ivPhoto)

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

                chip_restaurant.isChecked = currentProperty.poi[0].toString().toBoolean()
                chip_bar.isChecked = currentProperty.poi[1].toString().toBoolean()
                chip_store.isChecked = currentProperty.poi[2].toString().toBoolean()
                chip_park.isChecked = currentProperty.poi[3].toString().toBoolean()
                chip_school.isChecked = currentProperty.poi[4].toString().toBoolean()
                chip_hospital.isChecked = currentProperty.poi[5].toString().toBoolean()

                tvEntryDate.text = getString(R.string.entry_date_param, currentProperty.availableDate)
                tvDescription.text = if (currentProperty.description == "") "Write something!!!" else currentProperty.description
                tvArea.text = if (currentProperty.areaInMeters.toString() == "") "0 m²" else currentProperty.areaInMeters.toString() + " m²"
                tvRoom.text = currentProperty.nbrRoom.toString()
                tvBedroom.text = currentProperty.nbrBedroom.toString()
                tvBathroom.text = currentProperty.nbrBathroom.toString()
                tvStreet.text = currentProperty.street
                tvPostcode.text = currentProperty.postcode
                tvCity.text = currentProperty.city
                tvCountry.text = currentProperty.country
//            tvAgent.text = getString(R.string.agent_name, args.currentProperty?.agent?.firstName, args.currentProperty?.agent?.lastName)

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
        }


        binding.tvDescription.movementMethod = ScrollingMovementMethod()

        binding.tvPrice.setOnClickListener { displayConvertedAndFormattedPrice() }

        photoAdapter.setOnItemClickListener {
            glide.load(it.filename).centerCrop().into(binding.ivPhoto)
        }

        if (!isFromMapFragment) {
            binding.ivMap.setOnClickListener {
                isFromDetailsFragment = true
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fl_container, editFragment)
                    addToBackStack(null)
                    commit()
                }
            }
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
    private fun displayConvertedAndFormattedPrice() {
        if (isDollar) {
            val price = Utils.convertDollarToEuro(currentProperty.priceInDollars)
            binding.tvPrice.text = Utils.formatInEuro(price, 0)
            isDollar = !isDollar
        } else {
            binding.tvPrice.text = Utils.formatInDollar(currentProperty.priceInDollars, 0)
            isDollar = !isDollar
        }
    }

    // Load property detail photos
    private fun loadPropertyPhotos() {
        viewModel.getAllPropertiesPhotos.observe(viewLifecycleOwner, { propertyPhoto ->

            if (propertyPhoto.none { it.propertyId == (currentProperty.id) }) {
                viewModel.insertPropertyPhoto(PropertyPhoto(currentProperty.coverPhoto, currentProperty.labelPhoto, currentProperty.id))
            } else {
                propertyPhotosList = propertyPhoto.filter { it.propertyId == currentProperty.id }
            }
            photoAdapter.photosListDetails = propertyPhotosList.reversed()
        })
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_detais_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_edit -> {
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fl_container, editFragment)
                    addToBackStack(null)
                    commit()
                }
                requireActivity().toolbar.title = "Edit property"
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}