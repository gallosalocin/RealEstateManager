package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.openclassrooms.realestatemanager.R
import kotlinx.android.synthetic.main.fragment_register.*

class RegisterFragment : Fragment(R.layout.fragment_register) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tv_login.setOnClickListener {
            val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
            findNavController().navigate(action)
        }

        btn_register.setOnClickListener {
            if (!validateUsername() or(!validatePassword()) or(!validateConfirmPassword())) {
                return@setOnClickListener
            } else {
                if (et_password_register.text.toString() == et_confirm_password_register.text.toString()) {
                    Toast.makeText(requireContext(), "You are registered now", Toast.LENGTH_SHORT).show()
                    val action = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
                    findNavController().navigate(action)
                } else {
                    Toast.makeText(requireContext(), "Your passwords don't match", Toast.LENGTH_SHORT).show()
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