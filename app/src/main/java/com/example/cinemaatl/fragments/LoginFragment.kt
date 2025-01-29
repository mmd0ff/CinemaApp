package com.example.cinemaatl.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.cinemaatl.R
import com.example.cinemaatl.UIState
import com.example.cinemaatl.ViewModels.LoginVM
import com.example.cinemaatl.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private  var binding: FragmentLoginBinding? = null
    private val viewModel by viewModels<LoginVM>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.buttonLogin?.setOnClickListener {
            binding?.emailInputLayout?.error = null
            binding?.passwordInputLayout?.error = null

            val email = binding?.emailInput?.text.toString()
            val password = binding?.passwordInput?.text.toString()

            viewModel.loginUser(email,password)

        }

        binding?.buttonBackToRegister?.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)

        }

        viewModel.state.observe(viewLifecycleOwner){state ->
            onStateChange(state)

        }

        }
        private fun onStateChange(state: UIState<LoginVM.State>){
            when(state){
                is UIState.Success -> {
                    onSuccess(state.data)
                }
                is UIState.Loading ->{

                }
                is UIState.Error -> {
                    Toast.makeText(requireContext(), "${state.errorMessage}", Toast.LENGTH_SHORT).show()
                }
            }
        }


        private fun onSuccess(state:LoginVM.State){
            if (state.isLogined){
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToBaseFragment())
            }
            showFieldError(state.isValidMail, state.isValidPassword)


    }
    private fun showFieldError(isValidEmail: Boolean, isValidPassword: Boolean){
        if( !isValidEmail)
            binding?.emailInputLayout?.error = "Email is not correct"

        if(!isValidPassword)
            binding?.passwordInputLayout?.error = "Password is not correct"

    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}