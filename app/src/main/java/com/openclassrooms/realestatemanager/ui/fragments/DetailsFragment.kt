package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.PhotoAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentDetailsBinding
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_add.*

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var photoAdapter: PhotoAdapter
    private val viewModel: MainViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private var isDollar = true
    private lateinit var photosDetailsList: List<String>



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        setHasOptionsMenu(true)

        photoAdapter = PhotoAdapter()
        photosDetailsList = ArrayList()

        loadProperty()

        binding.apply {
            rvDetails.adapter = photoAdapter
            rvDetails.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        photoAdapter.setOnItemClickListener {
            Toast.makeText(requireContext(), "Click OK", Toast.LENGTH_SHORT).show()
        }

        binding.tvDescription.movementMethod = ScrollingMovementMethod()

        binding.tvPrice.setOnClickListener { displayConvertedAndFormattedPrice() }
    }


    // Display price in Dollars or Euro
    private fun displayConvertedAndFormattedPrice() {
        if (isDollar) {
            val price = Utils.convertDollarToEuro(args.currentProperty?.property?.priceInDollars!!)
            binding.tvPrice.text = Utils.formatInEuro(price, 0)
            isDollar = !isDollar
        } else {
            binding.tvPrice.text = Utils.formatInDollar(args.currentProperty?.property?.priceInDollars!!, 0)
            isDollar = !isDollar
        }
    }

    // Load property
    private fun loadProperty() {
        binding.apply {

            Glide.with(requireContext())
                    .load(args.currentProperty?.property?.coverPhoto!!)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivPhoto)

            photoAdapter.photosListDetails = photosDetailsList

            tvPrice.text = Utils.formatInDollar(args.currentProperty?.property?.priceInDollars!!, 0)

            if (args.currentProperty?.property?.isSold == true) {
                tvStatus.text = getString(R.string.sold_cap)
                tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorSold))
                tvSoldDate.visibility = View.VISIBLE
                tvSoldDate.text = getString(R.string.sold_date_param, args.currentProperty?.property?.soldDate)
            } else {
                tvStatus.text = getString(R.string.available_cap)
                tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAvailable))
            }

            chip_restaurant.isChecked = args.currentProperty?.property?.poi?.get(0).toString().toBoolean()
            chip_bar.isChecked = args.currentProperty?.property?.poi?.get(1).toString().toBoolean()
            chip_store.isChecked = args.currentProperty?.property?.poi?.get(2).toString().toBoolean()
            chip_park.isChecked = args.currentProperty?.property?.poi?.get(3).toString().toBoolean()
            chip_school.isChecked = args.currentProperty?.property?.poi?.get(4).toString().toBoolean()
            chip_hospital.isChecked = args.currentProperty?.property?.poi?.get(5).toString().toBoolean()

            tvEntryDate.text = getString(R.string.entry_date_param, args.currentProperty?.property?.availableDate)
            tvDescription.text = if (args.currentProperty?.property?.description == "") "Write something!!!" else args.currentProperty?.property?.description
            tvArea.text = if (args.currentProperty?.property?.areaInMeters == "") "0 m²" else args.currentProperty?.property?.areaInMeters + " m²"
            tvRoom.text = args.currentProperty?.property?.nbrRoom
            tvBedroom.text = args.currentProperty?.property?.nbrBedroom
            tvBathroom.text = args.currentProperty?.property?.nbrBathroom
            tvStreet.text = args.currentProperty?.property?.street
            tvPostcode.text = args.currentProperty?.property?.postcode
            tvCity.text = args.currentProperty?.property?.city
            tvCountry.text = args.currentProperty?.property?.country
            tvAgent.text = getString(R.string.agent_name, args.currentProperty?.agent?.firstName, args.currentProperty?.agent?.lastName)
        }
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_detais_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_edit -> {
                val action = DetailsFragmentDirections.actionDetailsFragmentToEditFragment(args.currentProperty!!)
                findNavController().navigate(action)
                requireActivity().toolbar.title = "Edit property"
            }
            R.id.tb_menu_add_photo -> Toast.makeText(requireContext(), "Photo added", Toast.LENGTH_SHORT).show()
        }
        return super.onOptionsItemSelected(item)
    }
}