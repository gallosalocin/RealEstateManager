package com.openclassrooms.realestatemanager.ui.fragments

import android.app.DatePickerDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentAddBinding
import com.openclassrooms.realestatemanager.models.Property
import com.openclassrooms.realestatemanager.other.Constants.NOTIFICATION_CHANNEL_ID
import com.openclassrooms.realestatemanager.other.Constants.NOTIFICATION_CHANNEL_NAME
import com.openclassrooms.realestatemanager.other.Constants.NOTIFICATION_ID
import com.openclassrooms.realestatemanager.other.Constants.SHARED_PREFERENCES_LOGIN
import com.openclassrooms.realestatemanager.other.Constants.SHARED_PREFERENCES_USERNAME
import com.openclassrooms.realestatemanager.ui.MainActivity
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@AndroidEntryPoint
class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var binding: FragmentAddBinding
    private val viewModel: MainViewModel by viewModels()
    @Inject
    lateinit var glide: RequestManager
    private lateinit var sharedPref: SharedPreferences
    private var formatDate = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE)

    private lateinit var addPhotoImageView: ImageView
    private var currentAgentId: Int = 0
    private lateinit var type: String
    private lateinit var room: String
    private lateinit var bedroom: String
    private lateinit var bathroom: String
    private var croppedPhoto: String? = null
    private lateinit var croppedPhotoUri: Uri
    private lateinit var labelPhoto: String

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

    private lateinit var cropCoverPhotoLauncher: ActivityResultLauncher<Any?>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)
        setHasOptionsMenu(true)

        sharedPref = requireActivity().getSharedPreferences(SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE)


        checkAgentId()

        setupTypeSpinner()
        setupRoomsSpinner()

        addCoverPhotoLauncher()


        binding.etAvailableDate.setOnClickListener { showDatePickerDialog(binding.etAvailableDate) }

        binding.ivPhoto.setOnClickListener { setupAddPhotoDialog() }
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

    // Check for agent id
    private fun checkAgentId() {
        viewModel.getAllAgents.observe(viewLifecycleOwner, { agentsList ->
            agentsList.forEachIndexed { index, agent ->
                if (agent.username == (sharedPref.getString(SHARED_PREFERENCES_USERNAME, ""))) {
                    currentAgentId = agent.id
                }
            }
        })
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
        else saveProperty()
    }

    private fun saveProperty() {

        val poiList = ArrayList<Boolean>()
        poiList.add(binding.chipRestaurant.isChecked)
        poiList.add(binding.chipBar.isChecked)
        poiList.add(binding.chipStore.isChecked)
        poiList.add(binding.chipPark.isChecked)
        poiList.add(binding.chipSchool.isChecked)
        poiList.add(binding.chipHospital.isChecked)


        val propertySaved = Property(
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
                availableDate = binding.etAvailableDate.text.toString(),
                agentId = currentAgentId,
                coverPhoto = croppedPhoto!!,
                labelPhoto = labelPhoto
        )

        viewModel.insertProperty(propertySaved)
        sendNotification()
        val action = AddFragmentDirections.actionAddFragmentToListFragment()
        findNavController().navigate(action)
    }

    // Change cover photo
    private fun addCoverPhotoLauncher() {
        cropCoverPhotoLauncher = registerForActivityResult(cropActivityResultContract) {
            it?.let {
                croppedPhotoUri = it
                croppedPhoto = it.toString()
                glide.load(croppedPhoto).centerCrop().into(addPhotoImageView)
            }
        }
    }

    // Setup Alert Dialog
    private fun setupAddPhotoDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add_photo, null)

        addPhotoImageView = dialogView.findViewById(R.id.iv_add_photo)
        val addLabel = dialogView.findViewById<EditText>(R.id.et_label)

        addPhotoImageView.setOnClickListener {
            cropCoverPhotoLauncher.launch(null)
        }

        val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setTitle("Add cover photo")
                .setMessage("Click icon to add photo")
                .setPositiveButton("Add", null)
                .setNegativeButton("Cancel") { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }.create()

        dialog.show()

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
        positiveButton.setOnClickListener {
            if (addLabel.text.toString() != "" && croppedPhoto != null) {
                labelPhoto = addLabel.text.toString()
                binding.ivPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                binding.ivPhoto.setImageURI(croppedPhotoUri)
                dialog.dismiss()
            } else
                Toast.makeText(requireContext(), "Add photo & enter label", Toast.LENGTH_SHORT).show()
        }

        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.colorAccent))
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

    // Create Notification
    private fun sendNotification() {
        val city = binding.etCity.text.toString()

        val notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder = NotificationCompat.Builder(requireContext(), NOTIFICATION_CHANNEL_ID)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.real_estate_logo)
                .setContentTitle("Property Saved")
                .setContentText("The $type in $city was successfully saved")
                .setContentIntent(getMainActivityPendingIntent())

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    // Create Pending Intent
    private fun getMainActivityPendingIntent() = PendingIntent.getActivity(
            requireContext(),
            0,
            Intent(requireContext(), MainActivity::class.java),
            0
    )

    // Create notification channel
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }
}