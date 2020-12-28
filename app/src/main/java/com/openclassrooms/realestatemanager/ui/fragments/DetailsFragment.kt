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
import com.openclassrooms.realestatemanager.utils.Utils.hideDetailsContainer
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
    private lateinit var menu: Menu

    // TODO A ne pas injecter
    @Inject
    lateinit var glide: RequestManager

    companion object {
        private const val KEY_IS_FROM_MAP_FRAGMENT = "KEY_IS_FROM_MAP_FRAGMENT"

        var isForDetailsFragment = false
        var isFromDetailsFragment = false

        fun newInstance(isFromMapFragment: Boolean): DetailsFragment {
            val args = Bundle()
            args.putBoolean(KEY_IS_FROM_MAP_FRAGMENT, isFromMapFragment)
            val fragment = DetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("test-> onViewCreated")

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view)

        setHasOptionsMenu(true)

        val detailsFragment: Fragment? = parentFragmentManager.findFragmentByTag("detailsFragment")
        val detailsFragmentTablet: Fragment? = parentFragmentManager.findFragmentByTag("detailsFragmentTablet")

        if (resources.getBoolean(R.bool.isTablet)) {
            (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
            bottomNavigationView.visibility = View.VISIBLE
        } else {
            if (detailsFragment != null) {
                (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
                bottomNavigationView.visibility = View.GONE
            } else if (detailsFragmentTablet != null) {
                (activity as? AppCompatActivity)?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
                bottomNavigationView.visibility = View.VISIBLE
                parentFragmentManager.commit {
                    remove(detailsFragmentTablet)
                }
            }
        }

        photoAdapter = PhotoAdapter()

        loadProperty()
        setupRecyclerView()

        binding.tvDescription.movementMethod = ScrollingMovementMethod()

        photoAdapter.setOnItemClickListener {
            if (it.filename == "") {
                glide.load(R.drawable.real_estate_no_image).into(binding.ivPhoto)
            } else {
                glide.load(it.filename).into(binding.ivPhoto)
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

    // Load property
    private fun loadProperty() {
        viewModel.getViewStateLiveData().observe(viewLifecycleOwner) { detailUiState ->
            requireActivity().title = detailUiState.type

            // TODO A gérer côté ViewModel : c'est lui qui choisit d'insérer des photos ou pas, pas la vue !
            if (currentPropertyWithAllData.photos.none { it.propertyId == (currentPropertyWithAllData.property.id) }) {
                viewModel.insertPropertyPhoto(PropertyPhoto(currentProperty.coverPhoto, currentProperty.labelPhoto, currentProperty.id))
            } else {
                propertyPhotosList = currentPropertyWithAllData.photos.filter { it.propertyId == currentProperty.id }
            }
            photoAdapter.photosListDetails = detailUiState.photos

            binding.apply {

                glide.load(detailUiState.coverPhoto).into(ivPhoto)

                tvPrice.text = detailUiState.price
                tvStatus.text = detailUiState.status

                /*
                if (currentProperty.isSold) {
                    tvStatus.text = getString(R.string.sold_cap)
                    tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorSold))
                    tvSoldDate.visibility = View.VISIBLE
                    tvSoldDate.text = getString(R.string.sold_date_param, currentProperty.soldDate)
                } else {
                    tvStatus.text = getString(R.string.available_cap)
                    tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAvailable))
                }*/

                chipRestaurant.isChecked = detailUiState.isChipRestaurantChecked()
                chipBar.isChecked = currentProperty.poi[1].toString().toBoolean()
                chipStore.isChecked = currentProperty.poi[2].toString().toBoolean()
                chipPark.isChecked = currentProperty.poi[3].toString().toBoolean()
                chipSchool.isChecked = currentProperty.poi[4].toString().toBoolean()
                chipHospital.isChecked = currentProperty.poi[5].toString().toBoolean()

                tvEntryDate.text = getString(R.string.entry_date_param, currentProperty.availableDate)
                // TODO IF dans le VM
                tvDescription.text = if (currentProperty.description == "") "Write something!!!" else currentProperty.description
                tvArea.text = if (currentProperty.areaInMeters.toString() == "") "0 m²" else "${currentProperty.areaInMeters} m²"
                tvRoom.text = if (currentProperty.nbrRoom >= 10) "${currentProperty.nbrRoom}+" else currentProperty.nbrRoom.toString()
                tvBedroom.text = if (currentProperty.nbrBedroom >= 10) "${currentProperty.nbrBedroom}+" else currentProperty.nbrBedroom.toString()
                tvBathroom.text = if (currentProperty.nbrBathroom >= 10) "${currentProperty.nbrBathroom}+" else currentProperty.nbrBathroom.toString()
                tvStreet.text = currentProperty.street
                tvPostcode.text = currentProperty.postcode
                tvCity.text = currentProperty.city
                tvCountry.text = currentProperty.country
                // TODO String à générer côté VM (à tester aussi !)
                tvAgent.text = getString(R.string.agent_name, currentPropertyWithAllData.agent.firstName, currentPropertyWithAllData.agent.lastName)

                // TODO Concaténation de String à faire côté VM (à tester aussi !)
                val currentPropertyAddress = "${currentProperty.street}+${currentProperty.postcode}+${currentProperty.city}"

                // TODO Concaténation de String à faire côté VM (à tester aussi !)
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

            if (arguments?.getBoolean(KEY_IS_FROM_MAP_FRAGMENT) == false) {
                binding.ivMap.setOnClickListener {
                    viewModel.setCurrentPropertyId()
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
            R.id.tb_menu_currency -> viewModel.onCurrencyClicked()
            R.id.tb_menu_edit -> {
                if (resources.getBoolean(R.bool.isTablet)) {
                    hideDetailsContainer(requireActivity())
                }
                viewModel.onEditClicked()
                parentFragmentManager.commit {
                    setCustomAnimations(R.anim.from_right, R.anim.to_right, R.anim.from_right, R.anim.to_right)
                    replace(R.id.fl_container, EditFragment())
                    addToBackStack(null)
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