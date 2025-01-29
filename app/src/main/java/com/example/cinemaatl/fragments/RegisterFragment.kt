package com.example.cinemaatl.fragments

import android.os.Bundle
import android.util.Log
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
import com.example.cinemaatl.ViewModels.RegisterVM
import com.example.cinemaatl.databinding.FragmentRegisterBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private  var binding: FragmentRegisterBinding? = null
    private val viewModel by viewModels<RegisterVM>()
    private val loginVM by viewModels<LoginVM>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.buttonRegister?.setOnClickListener {
            onRegisterClicked()
            Log.d("login", "onViewCreated: Oshibka")


        }

        binding?.buttonGoToLogin?.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())


        }
        viewModel.state.observe(viewLifecycleOwner) { state ->
            onStateChange(state)


        }

        loginVM.state.observe(viewLifecycleOwner){state ->
            onLoginStateChange(state)
        }
    }

    private fun onStateChange(state: UIState<RegisterVM.State>) {
        when (state) {
            is UIState.Success -> {
                onSuccess(state.data)

            }

            is UIState.Loading -> {

            }

            is UIState.Error -> {
                Toast.makeText(requireContext(), "${state.errorMessage}", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun onLoginStateChange(state: UIState<LoginVM.State>) {
        when(state) {
            is UIState.Success -> {
                onLoginSuccess(state.data)
            }

            is UIState.Loading -> {
            }

            is UIState.Error -> {
                Toast.makeText(requireContext(), "${state.errorMessage}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun onSuccess(state: RegisterVM.State) {
        if (state.isRegistered) {
            loginVM.loginUser(state.email.orEmpty(), state.password.orEmpty())
            //login
        }else{
            showFieldErr0r(state.isValidMail,state.isValidPassword, state.isValidRepeatPassword,state.isValidNickname)

        }


    }
    private fun onLoginSuccess(data: LoginVM.State) {
        if(data.isLogined){
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToBaseFragment())
        }

    }

    private fun showFieldErr0r(isValidEmail:Boolean, isValidPassword:Boolean, isValidRepeatPassword:Boolean, isValidNickname:Boolean){
        if (!isValidEmail)
            binding?.emailInputLayout?.error = "Email is not correct"

        if(!isValidPassword)
            binding?.passwordInputLayout?.error = "Password is not correct"

        if(!isValidRepeatPassword)
            binding?.repeatPasswordInputLayout?.error = "Password is not matched"
        if(!isValidNickname)
            binding?.name?.error = "Nickname is not correct"

    }




    private fun onRegisterClicked() {
        binding?.emailInputLayout?.error = null
        binding?.passwordInputLayout?.error = null
        binding?.repeatPasswordInputLayout?.error = null
        binding?.name?.error = null

        val email = binding?.emailInput?.text.toString()
        val password = binding?.passwordInput?.text.toString()
        val repeatPassword = binding?.repeatPasswordInput?.text.toString()
        val nickname = binding?.nameInput?.text.toString()
        viewModel.registerUser(email, password, repeatPassword, nickname)



    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}