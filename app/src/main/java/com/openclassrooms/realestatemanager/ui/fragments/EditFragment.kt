package com.openclassrooms.realestatemanager.ui.fragments

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.PhotoAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentEditBinding
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.models.PropertyPhoto
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class EditFragment : Fragment(R.layout.fragment_edit) {

    private lateinit var binding: FragmentEditBinding
    private val viewModel: MainViewModel by viewModels()
    private val args: AddFragmentArgs by navArgs()
    @Inject
    lateinit var glide: RequestManager
    private lateinit var photoAdapter: PhotoAdapter
    private var formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

    private lateinit var addPhotoImageView: ImageView
    private lateinit var type: String
    private var room: Int = 0
    private var bedroom: Int = 0
    private var bathroom: Int = 0
    private var croppedPhoto: String? = null
    private lateinit var labelPhoto: String
    private lateinit var currentProperty: Property
    private lateinit var propertyPhotosList: List<PropertyPhoto>
    private var coverPhoto: String = ""
    private var coverLabelPhoto: String = ""


    private val cropActivityResultContract = object : ActivityResultContract<Any?, Uri?>() {
        override fun createIntent(context: Context, input: Any?): Intent {
            return CropImage.activity()
                    .setAspectRatio(16, 9)
                    .getIntent(requireContext())
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
            return CropImage.getActivityResult(intent)?.uri
        }
    }

    private lateinit var cropPhotoLauncher: ActivityResultLauncher<Any?>



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Timber.d("Launch : onCreate" )

        binding = FragmentEditBinding.bind(view)
        setHasOptionsMenu(true)
        DetailsFragment.isDetailsFragment = false

        photoAdapter = PhotoAdapter()
        currentProperty = args.currentProperty?.property!!
        setupRecyclerView()

        loadProperty()
        loadPropertyPhotos()

        setupTypeSpinner()
        setupRoomsSpinner()
        displayOrHideSoldDatepicker()

        addDetailPhotoLauncher()


        binding.etAvailableDate.setOnClickListener { showDatePickerDialog(binding.etAvailableDate) }

        binding.ivAddPhoto.setOnClickListener { setupAddPhotoDialog() }

        photoAdapter.setOnItemClickListener {
            coverPhoto = it.filename
            coverLabelPhoto = it.label
            glide.load(coverPhoto).centerCrop().into(binding.ivPhoto)
        }

        photoAdapter.setOnItemDeleteListener { setupDeleteDialog(it, it.filename) }

    }

    // Add details photo
    private fun addDetailPhotoLauncher() {
        cropPhotoLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let {
                croppedPhoto = it.toString()
                glide.load(croppedPhoto).centerCrop().into(addPhotoImageView)
            }
        }
    }

    // Setup recyclerview
    private fun setupRecyclerView() {
        binding.rvAdd.apply {
            adapter = photoAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
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
                areaInMeters = binding.etArea.text.toString().toInt(),
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
                agentId = currentProperty.agentId,
                coverPhoto = if (coverPhoto == "") currentProperty.coverPhoto else coverPhoto,
                labelPhoto = if (coverLabelPhoto == "") currentProperty.labelPhoto else coverLabelPhoto
        )

        viewModel.updateProperty(propertyUpdated)

        val action = EditFragmentDirections.actionEditFragmentToListFragment()
        findNavController().navigate(action)
    }

    // Load property detail photos
    private fun loadPropertyPhotos() {
        viewModel.getAllPropertiesPhotos.observe(viewLifecycleOwner, { propertyPhoto ->
            propertyPhotosList = propertyPhoto.filter { it.propertyId == currentProperty.id  }
            photoAdapter.photosListDetails = propertyPhotosList.reversed()
        })
    }

    // Load property to edit
    private fun loadProperty() {

        val positionType = resources.getStringArray(R.array.type_of_properties).indexOf(currentProperty.type)
        val positionRoom = resources.getStringArray(R.array.number_of_rooms).indexOf(currentProperty.nbrRoom.toString())
        val positionBedroom = resources.getStringArray(R.array.number_of_rooms).indexOf(currentProperty.nbrBedroom.toString())
        val positionBathroom = resources.getStringArray(R.array.number_of_rooms).indexOf(currentProperty.nbrBathroom.toString())

        binding.apply {
            glide.load(currentProperty.coverPhoto).centerCrop().into(ivPhoto)

            etPrice.setText(currentProperty.priceInDollars.toString())
            etArea.setText(currentProperty.areaInMeters.toString())
            etStreet.setText(currentProperty.street)
            etPostcode.setText(currentProperty.postcode)
            etCity.setText(currentProperty.city)
            etCountry.setText(currentProperty.country)
            etDescription.setText(currentProperty.description)
            etAvailableDate.setText(currentProperty.availableDate)

            spType.post { spType.setSelection(positionType) }
            spRoom.post { spRoom.setSelection(positionRoom) }
            spBedroom.post { spBedroom.setSelection(positionBedroom) }
            spBathroom.post { spBathroom.setSelection(positionBathroom) }

            chipRestaurant.isChecked = currentProperty.poi[0].toString().toBoolean()
            chipBar.isChecked = currentProperty.poi[1].toString().toBoolean()
            chipStore.isChecked = currentProperty.poi[2].toString().toBoolean()
            chipPark.isChecked = currentProperty.poi[3].toString().toBoolean()
            chipSchool.isChecked = currentProperty.poi[4].toString().toBoolean()
            chipHospital.isChecked = currentProperty.poi[5].toString().toBoolean()

            if (currentProperty.isSold) {
                cbIsSold.isChecked = true
                etSoldDate.visibility = View.VISIBLE
                etSoldDate.setText(currentProperty.soldDate)
            }
        }
    }

    // Setup Alert Dialog
    private fun setupAddPhotoDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_photo, null)

        addPhotoImageView = dialogView.findViewById(R.id.iv_add_photo)
        val addLabel = dialogView.findViewById<EditText>(R.id.et_label)

        addPhotoImageView.setOnClickListener {
            cropPhotoLauncher.launch(null)
        }

        val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setTitle("Add photo")
                .setMessage("Click icon to add photo")
                .setPositiveButton("Save", null)
                .setNegativeButton("Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }.create()

        dialog.show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        positiveButton.setOnClickListener {
            if (addLabel.text.toString() != "" && croppedPhoto != null) {
                labelPhoto = addLabel.text.toString()
                viewModel.insertPropertyPhoto(PropertyPhoto(croppedPhoto!!, labelPhoto, args.currentProperty?.property?.id!!))
                dialog.dismiss()
            } else
                Toast.makeText(requireContext(), "Add photo & enter label", Toast.LENGTH_SHORT).show()
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
    }

    // Setup Delete Dialog
    private fun setupDeleteDialog(propertyPhoto: PropertyPhoto, photoToDelete: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_delete_photo, null)

        val deletePhotoImageView = dialogView.findViewById<ImageView>(R.id.iv_delete_photo)

        val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setIcon(R.drawable.ic_delete)
                .setTitle("Delete photo")
                .setMessage("Are you sure ?")
                .setPositiveButton("Yes") { dialogInterface, _ ->
                    viewModel.deletePropertyPhoto(propertyPhoto)
                }
                .setNegativeButton("Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }.create()

        glide.load(photoToDelete).centerCrop().into(deletePhotoImageView)

        dialog.show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
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
                room = selectedItem.toString().toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spBedroom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position)
                bedroom = selectedItem.toString().toInt()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        binding.spBathroom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedItem = parent!!.getItemAtPosition(position)
                bathroom = selectedItem.toString().toInt()
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

    override fun onStart() {
        super.onStart()
        Timber.d("Launch : onStart" )
        DetailsFragment.isDetailsFragment = false
    }

    override fun onStop() {
        super.onStop()
        Timber.d("Launch : onStop" )
        DetailsFragment.isDetailsFragment = true
    }
}