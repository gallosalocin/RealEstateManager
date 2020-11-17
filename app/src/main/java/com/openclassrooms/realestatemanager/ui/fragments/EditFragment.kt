package com.openclassrooms.realestatemanager.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentEditBinding
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class EditFragment : Fragment(R.layout.fragment_edit) {

    private lateinit var binding: FragmentEditBinding
    private val viewModel: MainViewModel by viewModels()
    private val args: AddFragmentArgs by navArgs()
    private var formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

    private lateinit var type: String
    private lateinit var room: String
    private lateinit var bedroom: String
    private lateinit var bathroom: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditBinding.bind(view)
        setHasOptionsMenu(true)

        loadProperty()

        setupTypeSpinner()
        setupRoomsSpinner()
        displayOrHideSoldDatepicker()

        binding.etAvailableDate.setOnClickListener { showDatePickerDialog(binding.etAvailableDate) }
    }


    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_add_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_save -> {
                confirmValidation()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Validate necessary fields
    private fun confirmValidation() {
        if (!validateType()
                or (!Utils.validateInputFieldIfNullOrEmpty(binding.etPrice, "Can't be empty"))
                or (!Utils.validateInputFieldIfNullOrEmpty(binding.etStreet, "Can't be empty"))
                or (!Utils.validateInputFieldIfNullOrEmpty(binding.etPostcode, "Can't be empty"))
                or (!Utils.validateInputFieldIfNullOrEmpty(binding.etCity, "Can't be empty"))
                or (!Utils.validateInputFieldIfNullOrEmpty(binding.etCountry, "Can't be empty"))
                or (!Utils.validateInputFieldIfNullOrEmpty(binding.etAvailableDate, "Can't be empty"))
        ) Toast.makeText(requireContext(), "Please fill all the required fields", Toast.LENGTH_SHORT).show()
        else updateProperty()
    }

    // Update property
    private fun updateProperty() {

        val poiList = ArrayList<Boolean>()
        poiList.add(binding.chipRestaurant.isChecked)
        poiList.add(binding.chipBar.isChecked)
        poiList.add(binding.chipStore.isChecked)
        poiList.add(binding.chipPark.isChecked)
        poiList.add(binding.chipSchool.isChecked)
        poiList.add(binding.chipHospital.isChecked)

        val propertyUpdated = Property(
                id = args.currentProperty?.property?.id!!,
                type = type,
                priceInDollars = binding.etPrice.text.toString().toInt(),
                areaInMeters = binding.etArea.text.toString(),
                nbrRoom = room,
                nbrBedroom = bedroom,
                nbrBathroom = bathroom,
                description = binding.etDescription.text.toString(),
                street = binding.etStreet.text.toString(),
                postcode = binding.etPostcode.text.toString(),
                city = binding.etCity.text.toString(),
                country = binding.etCountry.text.toString(),
                poi = poiList,
                isSold = binding.cbIsSold.isChecked,
                availableDate = binding.etAvailableDate.text.toString(),
                soldDate = binding.etSoldDate.text.toString(),
                agentId = args.currentProperty?.property?.agentId!!,
                coverPhoto = args.currentProperty?.property?.coverPhoto!!
        )

        viewModel.updateProperty(propertyUpdated)

        val action = EditFragmentDirections.actionEditFragmentToListFragment()
        findNavController().navigate(action)
    }


    // Load property to edit
    private fun loadProperty() {
        val positionType = resources.getStringArray(R.array.type_of_properties).indexOf(args.currentProperty?.property?.type)
        val positionRoom = resources.getStringArray(R.array.number_of_rooms).indexOf(args.currentProperty?.property?.nbrRoom)
        val positionBedroom = resources.getStringArray(R.array.number_of_rooms).indexOf(args.currentProperty?.property?.nbrBedroom)
        val positionBathroom = resources.getStringArray(R.array.number_of_rooms).indexOf(args.currentProperty?.property?.nbrBathroom)

        binding.apply {
            Glide.with(requireContext())
                    .load(args.currentProperty?.property?.coverPhoto!!)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .error(R.drawable.ic_error)
                    .into(ivPhoto)

            etPrice.setText(args.currentProperty?.property?.priceInDollars.toString())
            etArea.setText(args.currentProperty?.property?.areaInMeters.toString())
            etStreet.setText(args.currentProperty?.property?.street.toString())
            etPostcode.setText(args.currentProperty?.property?.postcode.toString())
            etCity.setText(args.currentProperty?.property?.city.toString())
            etCountry.setText(args.currentProperty?.property?.country.toString())
            etDescription.setText(args.currentProperty?.property?.description.toString())
            etAvailableDate.setText(args.currentProperty?.property?.availableDate.toString())

            spType.post { spType.setSelection(positionType) }
            spRoom.post { spRoom.setSelection(positionRoom) }
            spBedroom.post { spBedroom.setSelection(positionBedroom) }
            spBathroom.post { spBathroom.setSelection(positionBathroom) }

            chipRestaurant.isChecked = args.currentProperty?.property?.poi?.get(0).toString().toBoolean()
            chipBar.isChecked = args.currentProperty?.property?.poi?.get(1).toString().toBoolean()
            chipStore.isChecked = args.currentProperty?.property?.poi?.get(2).toString().toBoolean()
            chipPark.isChecked = args.currentProperty?.property?.poi?.get(3).toString().toBoolean()
            chipSchool.isChecked = args.currentProperty?.property?.poi?.get(4).toString().toBoolean()
            chipHospital.isChecked = args.currentProperty?.property?.poi?.get(5).toString().toBoolean()

            if (args.currentProperty?.property?.isSold == true) {
                cbIsSold.isChecked = true
                etSoldDate.visibility = View.VISIBLE
                etSoldDate.setText(args.currentProperty?.property?.soldDate.toString())
            }
        }
    }

    // Display or hide Datepicker for sold date
    private fun displayOrHideSoldDatepicker() {
        binding.cbIsSold.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                binding.etSoldDate.visibility = View.VISIBLE
                binding.etSoldDate.setOnClickListener {
                    showDatePickerDialog(binding.etSoldDate)
                }
            } else {
                binding.etSoldDate.visibility = View.INVISIBLE
            }
        }
    }

    // Setup Type Spinner
    private fun setupTypeSpinner() {
        val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.type_of_properties,
                android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spType.adapter = adapter

        binding.spType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position)
                if (position > 0) {
                    type = selectedItem.toString()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    // Setup Rooms Spinners
    private fun setupRoomsSpinner() {
        val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.number_of_rooms,
                android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        binding.spRoom.adapter = adapter
        binding.spBedroom.adapter = adapter
        binding.spBathroom.adapter = adapter

        binding.spRoom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position)
                room = selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spBedroom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position)
                bedroom = selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spBathroom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position)
                bathroom = selectedItem.toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    // Setup DatePicker
    private fun showDatePickerDialog(editText: AppCompatEditText) {
        val getDate = Calendar.getInstance()
        val datePicker = DatePickerDialog(requireContext(), { datePicker, year, month, day ->

            val selectDate = Calendar.getInstance()
            selectDate.set(Calendar.YEAR, year)
            selectDate.set(Calendar.MONTH, month)
            selectDate.set(Calendar.DAY_OF_MONTH, day)

            val date = formatDate.format(selectDate.time)
            editText.setText(date)

        }, getDate.get(Calendar.YEAR), getDate.get(Calendar.MONTH), getDate.get(Calendar.DAY_OF_MONTH))
        datePicker.show()

        datePicker.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        datePicker.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
    }

    // Validate property type spinner
    private fun validateType(): Boolean {
        val errorText: TextView = binding.spType.selectedView as TextView

        return if (binding.spType.selectedItem.toString() == "Type") {
            errorText.error = "Choose a type"
            false
        } else {
            errorText.error = null
            true
        }
    }
}