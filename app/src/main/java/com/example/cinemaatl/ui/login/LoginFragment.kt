package com.example.cinemaatl.ui.login

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.FragmentLoginBinding
import com.example.cinemaatl.ui.core.CoreFragment
import com.example.cinemaatl.ui.core.UIState
import com.example.cinemaatl.ui.shared.AuthVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : CoreFragment() {

    private var binding: FragmentLoginBinding? = null
    private val authVM by activityViewModels<AuthVM>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.buttonLogin?.setOnClickListener {
            onLoginClicked()

        }

        binding?.signUpText?.setOnClickListener {
            authVM.resetAuthSate()
            findNavController().navigate(R.id.registerFragment)

        }


        authVM.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Success -> {
                    if (state.data.isLogined) {
                        Log.d("LoginFragment", "onViewCreated: ")
                        findNavController().navigate(R.id.action_loginFragment_to_baseFragment)
                    }

                }

                is UIState.Loading -> {

                }

                is UIState.Error -> {
                    showError(state)
                }

                else -> {}
            }
        }

    }


    private fun onLoginClicked() {
        binding?.emailInputLayout?.error = null
        binding?.passwordInputLayout?.error = null

        val email = binding?.emailInput?.text.toString()
        val password = binding?.passwordInput?.text.toString()

        var isValid = true

        if (email.isEmpty()) {
            binding?.emailInputLayout?.error = "Email не может быть пустым"
            isValid = false
        }
        if (password.isEmpty()) {
            binding?.passwordInputLayout?.error = "Пароль не может быть пустым"
            isValid = false
        }
        if (isValid) {
            authVM.loginUser(email = email, password = password)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}