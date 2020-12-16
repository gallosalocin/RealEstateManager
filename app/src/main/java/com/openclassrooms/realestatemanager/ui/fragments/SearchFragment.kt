package com.openclassrooms.realestatemanager.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ScrollView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.PropertyAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentEditBinding
import com.openclassrooms.realestatemanager.databinding.FragmentMapBinding
import com.openclassrooms.realestatemanager.databinding.FragmentSearchBinding
import com.openclassrooms.realestatemanager.models.PropertyWithAllData
import com.openclassrooms.realestatemanager.ui.fragments.DetailsFragment.Companion.isForDetailsFragment
import com.openclassrooms.realestatemanager.ui.fragments.MapFragment.Companion.isFromMapFragment
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var logoutFragment: LogoutFragment
    private lateinit var scrollView: ScrollView
    private var isTablet = false

    private lateinit var propertyAdapter: PropertyAdapter
    private var formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)
    private val viewModel: MainViewModel by viewModels()
    private lateinit var type: String
    private var agent: String = ""
    private var priceMin: String = ""
    private var priceMax: String = ""
    private var areaMin: String = ""
    private var areaMax: String = ""
    private var roomMin: String = ""
    private var roomMax: String = ""
    private var bedroomMin: String = ""
    private var bedroomMax: String = ""
    private var bathroomMin: String = ""
    private var bathroomMax: String = ""
    private var entryDateMin: String = ""
    private var entryDateMax: String = ""
    private var soldDateMin: String = ""
    private var soldDateMax: String = ""
    private var city: String = ""
    private var country: String = ""
    private var isResult = false
    private var filteredPropertiesList: List<PropertyWithAllData> = ArrayList()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        isTablet = resources.getBoolean(R.bool.isTablet)

        if (isTablet) {
            scrollView  = requireActivity().findViewById(R.id.sv_right)
            scrollView.visibility = View.GONE
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        Timber.d("onViewCreated")

        setupTypeSpinner()
        setupRecyclerView()

        if (isResult) {
            binding.rvSearch.visibility = View.VISIBLE
            binding.clSearchFields.visibility = View.INVISIBLE
            propertyAdapter.properties = filteredPropertiesList
        }


        binding.apply {
            etEntryDateMin.setOnClickListener { showDatePickerDialog(binding.etEntryDateMin) }
            etEntryDateMax.setOnClickListener { showDatePickerDialog(binding.etEntryDateMax) }
            etSoldDateMin.setOnClickListener { showDatePickerDialog(binding.etSoldDateMin) }
            etSoldDateMax.setOnClickListener { showDatePickerDialog(binding.etSoldDateMax) }


            btnSearch.setOnClickListener {
                setupAllFields()
                Utils.hideKeyboard(requireActivity())
                binding.clSearchFields.visibility = View.INVISIBLE
                viewModel.getAllFilteredProperties(
                        agent, type, priceMin, priceMax, areaMin, areaMax, roomMin, roomMax, bedroomMin, bedroomMax, bathroomMin, bathroomMax,
                        entryDateMin, entryDateMax, soldDateMin, soldDateMax, city, country
                ).observe(viewLifecycleOwner, {
                    filteredPropertiesList = it

                    if (filteredPropertiesList.isEmpty()) {
                        binding.tvNoResult.visibility = View.VISIBLE
                        isResult = false
                    } else {
                        binding.rvSearch.visibility = View.VISIBLE
                        isResult = true
                    }
                    propertyAdapter.properties = filteredPropertiesList
                })
            }

        }

        propertyAdapter.setOnItemClickListener {
            isResult = true
            isFromMapFragment = false
            isForDetailsFragment = true
//            val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(it)
//            findNavController().navigate(action)
//            requireActivity().toolbar.title = it.property.type

        }
    }

    // Setup all fields with variables
    private fun setupAllFields() {
        binding.apply {
            priceMin = if (etPriceMin.text.toString() == "") "0" else etPriceMin.text.toString()
            priceMax = if (etPriceMax.text.toString() == "") "100000000" else etPriceMax.text.toString()
            areaMin = if (etAreaMin.text.toString() == "") "0" else etAreaMin.text.toString()
            areaMax = if (etAreaMax.text.toString() == "") "1000000" else etAreaMax.text.toString()
            roomMin = if (etRoomMin.text.toString() == "") "0" else etRoomMin.text.toString()
            roomMax = if (etRoomMax.text.toString() == "") "100000000" else etRoomMax.text.toString()
            bedroomMin = if (etBedroomMin.text.toString() == "") "0" else etBedroomMin.text.toString()
            bedroomMax = if (etBedroomMax.text.toString() == "") "100000000" else etBedroomMax.text.toString()
            bathroomMin = if (etBathroomMin.text.toString() == "") "0" else etBathroomMin.text.toString()
            bathroomMax = if (etBathroomMax.text.toString() == "") "100000000" else etBathroomMax.text.toString()
            entryDateMin = if (etEntryDateMin.text.toString() == "") "01/01/1900" else etEntryDateMin.text.toString()
            entryDateMax = if (etEntryDateMax.text.toString() == "") "31/12/2100" else etEntryDateMax.text.toString()
            agent = if (etAgent.text.toString() == "") "%" else "%" + etAgent.text.toString() + "%"
            city = if (etCity.text.toString() == "") "%" else "%" + etCity.text.toString() + "%"
            country = if (etCountry.text.toString() == "") "%" else "%" + etCountry.text.toString() + "%"

            if (etSoldDateMin.text.toString() == "" && etSoldDateMax.text.toString() == "") {
                soldDateMin = ""
                soldDateMax = ""
            } else if (etSoldDateMin.text.toString() != "" && etSoldDateMax.text.toString() == "") {
                soldDateMin = etSoldDateMin.text.toString()
                soldDateMax = "31/12/2100"
            } else if (etSoldDateMin.text.toString() == "" && etSoldDateMax.text.toString() != "") {
                soldDateMin = "01/01/1900"
                soldDateMax = etSoldDateMax.text.toString()
            } else {
                soldDateMin = etSoldDateMin.text.toString()
                soldDateMax = etSoldDateMax.text.toString()
            }
        }
    }

    // Setup toolbar
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.toolbar_menu_main, menu)
        menu.getItem(0).isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tb_menu_reload -> {
                clearAllFields()
                binding.tvNoResult.visibility = View.GONE
                binding.rvSearch.visibility = View.GONE
                binding.clSearchFields.visibility = View.VISIBLE
            }
            R.id.tb_menu_search -> {
                binding.tvNoResult.visibility = View.GONE
                binding.rvSearch.visibility = View.GONE
                binding.clSearchFields.visibility = View.VISIBLE
            }
            R.id.tb_menu_logout -> {
                logoutFragment = LogoutFragment()
                parentFragmentManager.beginTransaction().apply {
                    replace(R.id.fl_container, logoutFragment)
                    commit()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Setup recyclerview
    private fun setupRecyclerView() {
        propertyAdapter = PropertyAdapter()

        binding.apply {
            rvSearch.adapter = propertyAdapter
            rvSearch.layoutManager = LinearLayoutManager(requireContext())
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
                type = selectedItem.toString()
                if (type == "Type") type = "%" else type
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

    // Clear All Fields
    private fun clearAllFields() {
        val positionType = resources.getStringArray(R.array.type_of_properties).indexOf("Type")
        isResult = false
        binding.apply {
            spType.post { spType.setSelection(positionType) }
            etAgent.text?.clear()
            etPriceMin.text?.clear()
            etPriceMax.text?.clear()
            etAreaMin.text?.clear()
            etAreaMax.text?.clear()
            etRoomMin.text?.clear()
            etRoomMax.text?.clear()
            etBedroomMin.text?.clear()
            etBedroomMax.text?.clear()
            etBathroomMin.text?.clear()
            etBathroomMax.text?.clear()
            etEntryDateMin.text?.clear()
            etEntryDateMax.text?.clear()
            etSoldDateMin.text?.clear()
            etSoldDateMax.text?.clear()
            etCity.text?.clear()
            etCountry.text?.clear()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isTablet) scrollView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}