package com.example.cinemaatl.ui.userticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.FragmentCompletePaymentDialogBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CompletePaymentDialogFragment : DialogFragment() {

    private var binding: FragmentCompletePaymentDialogBinding? = null

    override fun onStart() {
        super.onStart()
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent) // Прозрачный фон окна
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCompletePaymentDialogBinding.inflate(layoutInflater, container, false)
        return binding?.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.close?.setOnClickListener {
            findNavController().navigate(R.id.baseFragment)

            dismiss()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}