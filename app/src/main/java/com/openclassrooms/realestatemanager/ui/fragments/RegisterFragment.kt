package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.openclassrooms.realestatemanager.R
import androidx.navigation.fragment.findNavController
import com.openclassrooms.realestatemanager.databinding.FragmentEditBinding
import com.openclassrooms.realestatemanager.databinding.FragmentMapBinding
import com.openclassrooms.realestatemanager.databinding.FragmentRegisterBinding
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModels()
    private lateinit var agentsList: List<Agent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllAgents.observe(viewLifecycleOwner, {
            agentsList = it
        })

        binding.tvLogin.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        binding.btnRegister.setOnClickListener {
            if (!Utils.validateInputFieldIfNullOrEmpty(binding.etFirstName, "Can't be empty")
                    or (!Utils.validateInputFieldIfNullOrEmpty(binding.etLastName, "Can't be empty"))
                    or (!Utils.validateInputFieldIfIsGreaterThan(binding.etUsernameRegister, "Min. 4 chars", 4))
                    or (!Utils.validateInputFieldIfIsGreaterThan(binding.etPasswordRegister, "Min. 4 chars", 4))
                    or (!Utils.validateInputFieldIfIsGreaterThan(binding.etConfirmPasswordRegister, "Min. 4 chars", 4))
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
                    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                    findNavController().navigate(action)
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