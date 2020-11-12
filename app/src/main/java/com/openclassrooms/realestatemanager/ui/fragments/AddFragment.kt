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
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentAddBinding
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var binding: FragmentAddBinding
    private val viewModel: MainViewModel by viewModels()
    private val args: AddFragmentArgs by navArgs()

    private var formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

    private lateinit var type: String
    private lateinit var room: String
    private lateinit var bedroom: String
    private lateinit var bathroom: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)
        setHasOptionsMenu(true)

        if (DetailsFragment.isEditable) {
            binding.cbIsSold.visibility = View.VISIBLE
            loadProperty()
        }

        setupTypeSpinner()
        setupRoomsSpinner()

        displayOrHideSoldDatepicker()

        binding.etAvailableDate.setOnClickListener {
            showDatePickerDialog(binding.etAvailableDate)
        }

        binding.btnAddPhoto.setOnClickListener {
        }

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

    // Update property
    private fun updateProperty(): Property {
        return Property(
                id = args.currentProperty?.id,
                type = type,
                priceInDollars = binding.etPrice.text.toString().toInt(),
                areaInMeters = binding.etArea.text.toString(),
                nbrRoom = room,
                nbrBedroom = bedroom,
                nbrBathroom = bathroom,
//                photo = listOf(R.drawable.test_house_photo.toString()),
                description = binding.etDescription.text.toString(),
                street = binding.etStreet.text.toString(),
                postcode = binding.etPostcode.text.toString(),
                city = binding.etCity.text.toString(),
                country = binding.etCountry.text.toString(),
//                poi = listOf(
//                        binding.chipRestaurant.isChecked,
//                        binding.chipBar.isChecked,
//                        binding.chipStore.isChecked,
//                        binding.chipPark.isChecked,
//                        binding.chipSchool.isChecked,
//                        binding.chipHospital.isChecked,
//                ),
                isSold = binding.cbIsSold.isChecked,
                availableDate = binding.etAvailableDate.text.toString(),
                soldDate = binding.etSoldDate.text.toString()
        )
    }

    // Save property into Room
    private fun saveProperty(): Property {
        return Property(
                type = type,
                priceInDollars = binding.etPrice.text.toString().toInt(),
                areaInMeters = binding.etArea.text.toString(),
                nbrRoom = room,
                nbrBedroom = bedroom,
                nbrBathroom = bathroom,
//                photo = listOf(R.drawable.test_house_photo.toString()),
                description = binding.etDescription.text.toString(),
                street = binding.etStreet.text.toString(),
                postcode = binding.etPostcode.text.toString(),
                city = binding.etCity.text.toString(),
                country = binding.etCountry.text.toString(),
//                poi = listOf(
//                        binding.chipRestaurant.isChecked,
//                        binding.chipBar.isChecked,
//                        binding.chipStore.isChecked,
//                        binding.chipPark.isChecked,
//                        binding.chipSchool.isChecked,
//                        binding.chipHospital.isChecked,
//                ),
                availableDate = binding.etAvailableDate.text.toString(),
        )
    }

    // Load property for edit
    private fun loadProperty() {
        val positionType = resources.getStringArray(R.array.type_of_properties).indexOf(args.currentProperty?.type)
        val positionRoom = resources.getStringArray(R.array.number_of_rooms).indexOf(args.currentProperty?.nbrRoom)
        val positionBedroom = resources.getStringArray(R.array.number_of_rooms).indexOf(args.currentProperty?.nbrBedroom)
        val positionBathroom = resources.getStringArray(R.array.number_of_rooms).indexOf(args.currentProperty?.nbrBathroom)

        binding.apply {
            etPrice.setText(args.currentProperty?.priceInDollars.toString())
            etArea.setText(args.currentProperty?.areaInMeters.toString())
            etStreet.setText(args.currentProperty?.street.toString())
            etPostcode.setText(args.currentProperty?.postcode.toString())
            etCity.setText(args.currentProperty?.city.toString())
            etCountry.setText(args.currentProperty?.country.toString())
            etDescription.setText(args.currentProperty?.description.toString())
            etAvailableDate.setText(args.currentProperty?.availableDate.toString())

            spType.post { spType.setSelection(positionType) }
            spRoom.post { spRoom.setSelection(positionRoom) }
            spBedroom.post { spBedroom.setSelection(positionBedroom) }
            spBathroom.post { spBathroom.setSelection(positionBathroom) }

            if (args.currentProperty?.isSold == true) {
                cbIsSold.isChecked = true
                etSoldDate.visibility = View.VISIBLE
                etSoldDate.setText(args.currentProperty?.soldDate.toString())
            }
        }
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
        ) return

        if (DetailsFragment.isEditable) {
            DetailsFragment.isEditable = false
            Toast.makeText(requireContext(), "Update Successfully", Toast.LENGTH_SHORT).show()
            viewModel.updateProperty(updateProperty())
            val action = AddFragmentDirections.actionAddFragmentToListFragment()
            findNavController().navigate(action)
        } else {
            viewModel.insertProperty(saveProperty())
            val action = AddFragmentDirections.actionAddFragmentToListFragment()
            findNavController().navigate(action)
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