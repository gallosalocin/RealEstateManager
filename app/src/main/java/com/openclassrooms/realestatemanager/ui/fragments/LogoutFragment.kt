package com.openclassrooms.realestatemanager.ui.fragments

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.databinding.FragmentLogoutBinding
import com.openclassrooms.realestatemanager.other.Constants.SHARED_PREFERENCES_IS_USER_LOGIN
import com.openclassrooms.realestatemanager.other.Constants.SHARED_PREFERENCES_LOGIN
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogoutFragment : Fragment(R.layout.fragment_logout) {

    private lateinit var binding: FragmentLogoutBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLogoutBinding.bind(view)

        val sharedPref = requireActivity().getSharedPreferences(SHARED_PREFERENCES_LOGIN, Context.MODE_PRIVATE)

        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.remove(SHARED_PREFERENCES_IS_USER_LOGIN)
        editor.apply()

        val action = LogoutFragmentDirections.actionLogoutFragmentToLoginFragment()
        findNavController().navigate(action)
    }

}