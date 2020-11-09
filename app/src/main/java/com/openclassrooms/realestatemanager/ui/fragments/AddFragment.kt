package com.openclassrooms.realestatemanager.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentAddBinding
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var binding: FragmentAddBinding
    private val viewModel: MainViewModel by viewModels()

    private var formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)
        setHasOptionsMenu(true)

        if (DetailsFragment.isEditable) {
            binding.cbIsSold.visibility = View.VISIBLE
            DetailsFragment.isEditable = false
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

    // Display Datepicker for sold date
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

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_add_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_save -> {
                Toast.makeText(requireContext(), "Save", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
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
                    Toast.makeText(requireContext(), "Type is : $selectedItem", Toast.LENGTH_SHORT).show()
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
                Toast.makeText(requireContext(), "Nbr is : $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spBedroom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position)
                Toast.makeText(requireContext(), "Nbr is : $selectedItem", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spBathroom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position)
                Toast.makeText(requireContext(), "Nbr is : $selectedItem", Toast.LENGTH_SHORT).show()
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
}