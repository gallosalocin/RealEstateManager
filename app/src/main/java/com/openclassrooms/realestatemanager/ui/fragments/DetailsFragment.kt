package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
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
import com.openclassrooms.realestatemanager.adapters.PhotoDetailsAdapter
import com.openclassrooms.realestatemanager.adapters.PropertyAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentDetailsBinding
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

@AndroidEntryPoint
class DetailsFragment : Fragment(R.layout.fragment_details) {

    private lateinit var binding: FragmentDetailsBinding
    private lateinit var photoDetailsAdapter: PhotoDetailsAdapter
    private val viewModel: MainViewModel by viewModels()

    private lateinit var photosDetailsList: MutableList<Int> // todo

    companion object {
        var isEditable = false
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDetailsBinding.bind(view)
        setHasOptionsMenu(true)

        photoDetailsAdapter = PhotoDetailsAdapter()

        binding.apply {
            rvDetails.adapter = photoDetailsAdapter
            rvDetails.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        photoDetailsAdapter.photosDetails = listPhotoDetails() // todo

        photoDetailsAdapter.setOnItemClickListener {
            Toast.makeText(requireContext(), "Click OK", Toast.LENGTH_SHORT).show()
        }

        binding.tvDescription.movementMethod = ScrollingMovementMethod()
    }

    // todo
    private fun listPhotoDetails(): List<Int> {
        photosDetailsList = ArrayList()
        for (i in 1..10) {
            photosDetailsList.add(R.drawable.test_house_photo)
        }
        return photosDetailsList
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