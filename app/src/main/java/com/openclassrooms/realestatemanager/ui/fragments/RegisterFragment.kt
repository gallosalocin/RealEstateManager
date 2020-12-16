package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentRegisterBinding
import com.openclassrooms.realestatemanager.models.database.Agent
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import com.openclassrooms.realestatemanager.utils.Utils.validateInputFieldIfIsGreaterThan
import com.openclassrooms.realestatemanager.utils.Utils.validateInputFieldIfNullOrEmpty
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var loginFragment: LoginFragment

    private val viewModel: MainViewModel by viewModels()
    private lateinit var agentsList: List<Agent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        val bottomNavigationView: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav_view)
        val toolbar: Toolbar = requireActivity().findViewById(R.id.toolbar)
        bottomNavigationView.visibility = View.GONE
        toolbar.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllAgents.observe(viewLifecycleOwner, {
            agentsList = it
        })

        binding.tvLogin.setOnClickListener {
            loginFragment = LoginFragment()
            parentFragmentManager.beginTransaction().apply {
                replace(R.id.fl_container, loginFragment)
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                commit()
            }
        }

        binding.btnRegister.setOnClickListener {
            if (!validateInputFieldIfNullOrEmpty(binding.etFirstName, "Can't be empty")
                    or (!validateInputFieldIfNullOrEmpty(binding.etLastName, "Can't be empty"))
                    or (!validateInputFieldIfIsGreaterThan(binding.etUsernameRegister, "Min. 4 chars", 4))
                    or (!validateInputFieldIfIsGreaterThan(binding.etPasswordRegister, "Min. 4 chars", 4))
                    or (!validateInputFieldIfIsGreaterThan(binding.etConfirmPasswordRegister, "Min. 4 chars", 4))
            ) return@setOnClickListener

            if (agentsList.any { it.username == (binding.etUsernameRegister.text.toString().trim()) }) {
                binding.etUsernameRegister.error = "This username already exist"
            } else {
                if (binding.etPasswordRegister.text.toString() == binding.etConfirmPasswordRegister.text.toString().trim()) {
                    val firstName = binding.etFirstName.text.toString().trim()
                    val lastName = binding.etLastName.text.toString().trim()
                    val username = binding.etUsernameRegister.text.toString().trim()
                    val password = binding.etConfirmPasswordRegister.text.toString().trim()
                    val newAgent = Agent(firstName, lastName, username, password)
                    viewModel.insertAgent(newAgent)
                    Toast.makeText(requireContext(), "You are registered now", Toast.LENGTH_SHORT).show()

                    loginFragment = LoginFragment()
                    parentFragmentManager.beginTransaction().apply {
                        replace(R.id.fl_container, loginFragment)
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        commit()
                    }
                } else {
                    binding.etConfirmPasswordRegister.error = "Your passwords don't match"
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//package com.openclassrooms.realestatemanager.ui.fragments
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.viewModels
//import androidx.navigation.fragment.findNavController
//import com.openclassrooms.realestatemanager.R
//import com.openclassrooms.realestatemanager.databinding.FragmentRegisterBinding
//import com.openclassrooms.realestatemanager.models.database.Agent
//import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
//import com.openclassrooms.realestatemanager.utils.Utils.validateInputFieldIfIsGreaterThan
//import com.openclassrooms.realestatemanager.utils.Utils.validateInputFieldIfNullOrEmpty
//import dagger.hilt.android.AndroidEntryPoint
//
//@AndroidEntryPoint
//class RegisterFragment : Fragment(R.layout.fragment_register) {
//
//    private var _binding: FragmentRegisterBinding? = null
//    private val binding get() = _binding!!
//
//    private val viewModel: MainViewModel by viewModels()
//    private lateinit var agentsList: List<Agent>
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
//        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        viewModel.getAllAgents.observe(viewLifecycleOwner, {
//            agentsList = it
//        })
//
//        binding.tvLogin.setOnClickListener {
//            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
//            findNavController().navigate(action)
//        }
//
//        binding.btnRegister.setOnClickListener {
//            if (!validateInputFieldIfNullOrEmpty(binding.etFirstName, "Can't be empty")
//                    or (!validateInputFieldIfNullOrEmpty(binding.etLastName, "Can't be empty"))
//                    or (!validateInputFieldIfIsGreaterThan(binding.etUsernameRegister, "Min. 4 chars", 4))
//                    or (!validateInputFieldIfIsGreaterThan(binding.etPasswordRegister, "Min. 4 chars", 4))
//                    or (!validateInputFieldIfIsGreaterThan(binding.etConfirmPasswordRegister, "Min. 4 chars", 4))
//            ) return@setOnClickListener
//
//            if (agentsList.any { it.username == (binding.etUsernameRegister.text.toString().trim()) }) {
//                binding.etUsernameRegister.error = "This username already exist"
//            } else {
//                if (binding.etPasswordRegister.text.toString() == binding.etConfirmPasswordRegister.text.toString().trim()) {
//                    val firstName = binding.etFirstName.text.toString().trim()
//                    val lastName = binding.etLastName.text.toString().trim()
//                    val username = binding.etUsernameRegister.text.toString().trim()
//                    val password = binding.etConfirmPasswordRegister.text.toString().trim()
//                    val newAgent = Agent(firstName, lastName, username, password)
//                    viewModel.insertAgent(newAgent)
//                    Toast.makeText(requireContext(), "You are registered now", Toast.LENGTH_SHORT).show()
//                    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
//                    findNavController().navigate(action)
//                } else {
//                    binding.etConfirmPasswordRegister.error = "Your passwords don't match"
//                }
//            }
//        }
//    }
//
//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
//}