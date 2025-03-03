package com.example.cinemaatl.ui.success

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.FragmentPaymentSuccessBinding


class PaymentSuccess : Fragment() {

    private var binding: FragmentPaymentSuccessBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPaymentSuccessBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.btBackHome?.setOnClickListener{
            findNavController().navigate(R.id.baseFragment)
        }
        binding?.btViewTicket?.setOnClickListener {
            findNavController().navigate(R.id.userTicketsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }



}