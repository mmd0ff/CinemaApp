package com.example.cinemaatl.ui.success

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.FragmentRegisterSuccessBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterSuccess : Fragment() {

    private var binding: FragmentRegisterSuccessBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterSuccessBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btLogin?.setOnClickListener {
            findNavController().navigate(R.id.loginFragment)
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }


}