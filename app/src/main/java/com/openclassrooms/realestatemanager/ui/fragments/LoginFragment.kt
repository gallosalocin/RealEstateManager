package com.openclassrooms.realestatemanager.ui.fragments

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentLoginBinding
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.other.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.openclassrooms.realestatemanager.other.Constants.SHARED_PREFERENCES_IS_USER_LOGIN
import com.openclassrooms.realestatemanager.other.Constants.SHARED_PREFERENCES_LOGIN
import com.openclassrooms.realestatemanager.other.Constants.SHARED_PREFERENCES_USERNAME
import com.openclassrooms.realestatemanager.ui.viewmodels.LoginViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login), EasyPermissions.PermissionCallbacks {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var toolbar: Toolbar

    private val viewModel: LoginViewModel by viewModels()
    private lateinit var agentsList: List<Agent>
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)

        bottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view)
        toolbar = requireActivity().findViewById(R.id.toolbar)
        bottomNavigationView.visibility = View.GONE
        toolbar.visibility = View.GONE


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.clLogin.visibility = View.VISIBLE
        requestPermissions()

        sharedPref = requireActivity().getSharedPreferences(SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE)
        if (sharedPref.contains(SHARED_PREFERENCES_IS_USER_LOGIN)) {
            parentFragmentManager.commit {
                replace(R.id.fl_container, ListFragment())
            }

            bottomNavigationView.visibility = View.VISIBLE
            toolbar.visibility = View.VISIBLE
        }

        viewModel.getAllAgents.observe(viewLifecycleOwner, {
            agentsList = it
        })


        binding.tvRegister.setOnClickListener {
            parentFragmentManager.commit {
                replace(R.id.fl_container, RegisterFragment())
            }
        }

        binding.btnLogin.setOnClickListener {
            if (agentsList.any { it.username == (binding.etUsername.text.toString().trim()) }) {
                if (agentsList.any { it.password == (binding.etPassword.text.toString().trim()) }) {

                    sharedPref.edit().apply {
                        putBoolean(SHARED_PREFERENCES_IS_USER_LOGIN, true)
                        putString(SHARED_PREFERENCES_USERNAME, binding.etUsername.text.toString().trim())
                        apply()
                    }

                    parentFragmentManager.commit {
                        replace(R.id.fl_container, ListFragment())
                    }

                    bottomNavigationView.visibility = View.VISIBLE
                    toolbar.visibility = View.VISIBLE
                } else {
                    binding.etPassword.error = "incorrect password"
                }
            } else {
                binding.etUsername.error = "This username doesn't exist, please register first"
            }
        }
    }

    private fun requestPermissions() {
        if (Utils.hasLocationPermissions(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app.",
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                    this,
                    "You need to accept location permissions to use this app.",
                    REQUEST_CODE_LOCATION_PERMISSION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}