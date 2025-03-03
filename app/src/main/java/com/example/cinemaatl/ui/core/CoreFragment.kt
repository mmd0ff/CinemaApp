package com.example.cinemaatl.ui.core

import android.widget.Toast
import androidx.fragment.app.Fragment

open class CoreFragment : Fragment() {

    fun showError(error: UIState.Error) {

        if(error.errorResId != 0) {
            Toast.makeText(requireContext(), getString(error.errorResId), Toast.LENGTH_SHORT).show()

        }  else{
            Toast.makeText(requireContext(), error.errorMessage, Toast.LENGTH_SHORT).show()

        }    }
}