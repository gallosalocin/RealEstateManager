package com.openclassrooms.realestatemanager.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*

@AndroidEntryPoint
class RegisterFragment : Fragment(R.layout.fragment_register) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var agentsList: List<Agent>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllAgents.observe(viewLifecycleOwner, Observer {
            agentsList = it
        })

        tv_login.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        btn_register.setOnClickListener {
            if (!validateUsername() or (!validatePassword()) or (!validateConfirmPassword())) {
                return@setOnClickListener
            }

            if (agentsList.any { it.username == (et_username_register.text.toString().trim()) }) {
                et_username_register.error = "This username already exist"
            } else {
                if (et_password_register.text.toString() == et_confirm_password_register.text.toString().trim()) {
                    val username = et_username_register.text.toString().trim()
                    val password = et_confirm_password_register.text.toString().trim()
                    val newAgent = Agent(username, password)
                    viewModel.insertAgent(newAgent)
                    Toast.makeText(requireContext(), "You are registered now", Toast.LENGTH_SHORT).show()
                    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                    findNavController().navigate(action)
                } else {
                    et_password_register.error = "Your passwords don't match"
                    et_confirm_password_register.error = "Your passwords don't match"
                }
            }
        }
    }

    private fun validateUsername(): Boolean {
        return if (et_username_register.length() < 4) {
            et_username_register.error = "Min. 4 chars"
            false
        } else {
            et_username_register.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        return if (et_password_register.length() < 4) {
            et_password_register.error = "Min. 4 chars"
            false
        } else {
            et_password_register.error = null
            true
        }
    }

    private fun validateConfirmPassword(): Boolean {
        return if (et_confirm_password_register.length() < 4) {
            et_confirm_password_register.error = "Min. 4 chars"
            false
        } else {
            et_confirm_password_register.error = null
            true
        }
    }
}