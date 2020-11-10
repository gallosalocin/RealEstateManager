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
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.PhotoDetailsAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentDetailsBinding
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var photoDetailsAdapter: PhotoDetailsAdapter
    private val viewModel: MainViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private var isDollar = true


    private lateinit var photosDetailsList: MutableList<Int> // todo

    companion object {
        var isEditable = false
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        setHasOptionsMenu(true)

        photoDetailsAdapter = PhotoDetailsAdapter()

        loadProperty()

        binding.apply {
            rvDetails.adapter = photoDetailsAdapter
            rvDetails.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        photoDetailsAdapter.photosDetails = listPhotoDetails() // todo

        photoDetailsAdapter.setOnItemClickListener {
            Toast.makeText(requireContext(), "Click OK", Toast.LENGTH_SHORT).show()
        }

        binding.tvDescription.movementMethod = ScrollingMovementMethod()

        binding.tvPrice.setOnClickListener {
            if (isDollar) {
                isDollar = false
                binding.tvPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_euro_focused, 0, 0, 0)
                binding.tvPrice.text = DecimalFormat("#,###").format(args.currentProperty?.priceInDollars?.let { price ->
                    Utils.convertDollarToEuro(price)
                })
            } else {
                binding.tvPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_dollar_focused, 0, 0, 0)
                isDollar = true
                binding.tvPrice.text = DecimalFormat("#,###").format(args.currentProperty?.priceInDollars)
            }
        }
    }

    // todo
    private fun listPhotoDetails(): List<Int> {
        photosDetailsList = ArrayList()
        for (i in 1..10) {
            photosDetailsList.add(R.drawable.test_house_photo)
        }
        return photosDetailsList
    }

    // Load property
    private fun loadProperty() {
        binding.apply {
            tvPrice.text = DecimalFormat("#,###").format(args.currentProperty?.priceInDollars)

            if (args.currentProperty?.isSold == true) {
                tvStatus.text = getString(R.string.sold_cap)
                tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorSold))
                tvSoldDate.visibility = View.VISIBLE
                tvSoldDate.text = getString(R.string.sold_date_param, args.currentProperty?.soldDate)
            } else {
                tvStatus.text = getString(R.string.available_cap)
                tvStatus.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAvailable))
            }

            tvEntryDate.text = getString(R.string.entry_date_param, args.currentProperty?.availableDate)

            tvDescription.text = if (args.currentProperty?.description == "") "Write something!!!" else args.currentProperty?.description
            tvArea.text = if (args.currentProperty?.areaInMeters == "") "0" else args.currentProperty?.areaInMeters
            tvRoom.text = args.currentProperty?.nbrRoom
            tvBedroom.text = args.currentProperty?.nbrBedroom
            tvBathroom.text = args.currentProperty?.nbrBathroom
            tvStreet.text = args.currentProperty?.street
            tvPostcode.text = args.currentProperty?.postcode
            tvCity.text = args.currentProperty?.city
            tvCountry.text = args.currentProperty?.country
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
                isEditable = true
                val action = DetailsFragmentDirections.actionDetailsFragmentToAddFragment()
                findNavController().navigate(action)
                requireActivity().toolbar.title = "Edit property"
            }
        }
        return super.onOptionsItemSelected(item)
    }
}