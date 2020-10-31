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
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*

class LoginFragment : Fragment (R.layout.fragment_login){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        tv_register.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        btn_login.setOnClickListener {
            if (!validateUsername() or(!validatePassword())) {
                return@setOnClickListener
            } else {
                val action = LoginFragmentDirections.actionLoginFragmentToListFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun validateUsername(): Boolean {
        return if (et_username.length() < 4) {
            et_username.error = "Min. 4 chars"
            false
        } else {
            et_username.error = null
            true
        }
    }

    private fun validatePassword(): Boolean {
        return if (et_password.length() < 4) {
            et_password.error = "Min. 4 chars"
            false
        } else {
            et_password.error = null
            true
        }
    }

}