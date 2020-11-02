package com.openclassrooms.realestatemanager.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.Agent
import com.openclassrooms.realestatemanager.other.Constants.SHARED_PREFERENCES_IS_USER_LOGIN
import com.openclassrooms.realestatemanager.other.Constants.SHARED_PREFERENCES_LOGIN
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var agentsList: List<Agent>
    private lateinit var sharedPref: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref= requireActivity().getSharedPreferences(SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE)
        if (sharedPref.contains(SHARED_PREFERENCES_IS_USER_LOGIN)){
            val action = LoginFragmentDirections.actionLoginFragmentToListFragment()
            findNavController().navigate(action)
        }

        viewModel.getAllAgents.observe(viewLifecycleOwner, Observer {
            agentsList = it
        })


        tv_register.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        btn_login.setOnClickListener {
            if (agentsList.any { it.username == (et_username.text.toString().trim()) }) {
                if (agentsList.any { it.password == (et_password.text.toString().trim()) }) {
                    val editor: SharedPreferences.Editor = sharedPref.edit()
                    editor.putBoolean(SHARED_PREFERENCES_IS_USER_LOGIN, true)
                    editor.apply()
                    val action = LoginFragmentDirections.actionLoginFragmentToListFragment()
                    findNavController().navigate(action)
                } else {
                    et_password.error = "incorrect password"
                }
            } else {
                et_username.error = "This username doesn't exist, please register first"
            }
        }
    }
}