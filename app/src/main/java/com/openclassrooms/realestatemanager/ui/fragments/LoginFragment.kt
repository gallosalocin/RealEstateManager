package com.openclassrooms.realestatemanager.ui.fragments

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
import com.openclassrooms.realestatemanager.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*

@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

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


        tv_register.setOnClickListener {
            val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            findNavController().navigate(action)
        }

        btn_login.setOnClickListener {
            if (agentsList.any { it.username == (et_username.text.toString().trim()) }) {
                if (agentsList.any { it.password == (et_password.text.toString().trim()) }) {
                    val action = LoginFragmentDirections.actionLoginFragmentToListFragment()
                    findNavController().navigate(action)
                } else {
                    et_password.error = "incorrect password"
                }
            } else {
                et_username.error = "username doesn't exist, please register first"
            }
        }
    }
}