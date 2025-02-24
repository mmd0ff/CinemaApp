package com.example.cinemaatl.ui.profile

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinemaatl.R
import com.example.cinemaatl.ui.base.MainVM
import com.example.cinemaatl.databinding.FragmentProfileBinding
import com.example.cinemaatl.helper.LocaleHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private  var  binding: FragmentProfileBinding? = null
   private val profileVM by viewModels<ProfileVM>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(layoutInflater,container,false)
        return binding?.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileVM.getCurrentNickname()

        profileVM.getCurrentuserEmail()

        profileVM.userNickname.observe(viewLifecycleOwner){nickname ->
            binding?.name?.text = nickname
        }
        profileVM.userEmail.observe(viewLifecycleOwner){email ->
            binding?.email?.text = email
        }

        binding?.logout?.setOnClickListener {
            profileVM.logOut()
            findNavController().navigate(R.id.loginFragment)
        }
        binding?.myTicket?.setOnClickListener {
            findNavController().navigate(R.id.userTicketsFragment)
        }
        binding?.changeLanguage?.setOnClickListener {
            showLanguageDialog()
        }
    }

    private fun showLanguageDialog() {
        val selectedLanguage = LocaleHelper.getSavedLanguage(requireContext())

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_language, null)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroupLanguages)
        val radioButtonEnglish = dialogView.findViewById<RadioButton>(R.id.radioButtonEnglish)
        val radioButtonRussian = dialogView.findViewById<RadioButton>(R.id.radioButtonRussian)

        when (selectedLanguage) {
            "en" -> radioButtonEnglish.isChecked = true
            "ru" -> radioButtonRussian.isChecked = true
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Select Language")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                val selectedId = radioGroup.checkedRadioButtonId
                when (selectedId) {
                    R.id.radioButtonEnglish -> LocaleHelper.setLocale(requireContext(), "en")
                    R.id.radioButtonRussian -> LocaleHelper.setLocale(requireContext(), "ru")
                }

                requireActivity().recreate()
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}