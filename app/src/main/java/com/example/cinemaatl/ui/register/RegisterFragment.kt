package com.example.cinemaatl.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.FragmentRegisterBinding
import com.example.cinemaatl.ui.core.CoreFragment
import com.example.cinemaatl.ui.core.UIState
import com.example.cinemaatl.ui.shared.AuthVM
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : CoreFragment() {

    private var binding: FragmentRegisterBinding? = null

    private val authVM by activityViewModels<AuthVM>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.buttonRegister?.setOnClickListener {
            onRegisterClicked()

        }

        binding?.signInText?.setOnClickListener {
            authVM.resetAuthSate()
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())

        }
        authVM.registerState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Success -> {
                    if (state.data.isRegistered) {
                        findNavController().navigate(R.id.action_registerFragment_to_registerSuccess)
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


    private fun onRegisterClicked() {
        binding?.emailInputLayout?.error = null
        binding?.passwordInputLayout?.error = null
        binding?.repeatPasswordInputLayout?.error = null
        binding?.name?.error = null

        val email = binding?.emailInput?.text.toString()
        val password = binding?.passwordInput?.text.toString()
        val repeatPassword = binding?.repeatPasswordInput?.text.toString()
        val nickname = binding?.nameInput?.text.toString().orEmpty()


        var isValid = true
        if (email.length < 8) {

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding?.emailInputLayout?.error = getString(R.string.invalid_mail)
            isValid = false
        }
        if (password.length < 8) {
            binding?.passwordInputLayout?.error = getString(R.string.password_length)
        }
        if (password != repeatPassword || repeatPassword.isEmpty()) {
            binding?.repeatPasswordInputLayout?.error = getString(R.string.password_is_not_matched)
            isValid = false
        }
        if (nickname.isEmpty()) {
            binding?.name?.error = getString(R.string.nicmname_empty)
            isValid = false
        }

        // Если всё валидно, отправляем в VM
        if (isValid) {
            authVM.registerUser(email = email, nickname = nickname, password = password)

        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}