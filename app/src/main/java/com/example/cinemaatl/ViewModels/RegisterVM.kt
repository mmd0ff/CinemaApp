package com.example.cinemaatl.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaatl.UIState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class RegisterVM @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _state: MutableLiveData<UIState<State>> = MutableLiveData()
    val state: LiveData<UIState<State>> = _state

    fun registerUser(email: String, password: String, repeatPassword: String, nickname: String) {
        val isValidEmail = checkEmail(email)
        val isValidPassword = checkPaswword(password)
        val isValidRepeatPassword = checkRepeatPassword(repeatPassword,password)
        val isValidNickname = checkNickname(nickname)

        if( isValidPassword && isValidEmail && isValidRepeatPassword){
            registerWithFirebase(email,password,nickname)

        } else {
            _state.value = UIState.Success(State(
                isRegistered = false,
                isValidMail = isValidEmail,
                isValidPassword = isValidPassword,
                isValidRepeatPassword = isValidRepeatPassword,
                isValidNickname = isValidNickname
            ))
        }

    }

    private fun checkRepeatPassword(password: String, repeatPassword: String): Boolean{
        return password == repeatPassword && repeatPassword.isNotEmpty()
    }
    private fun checkNickname(nickname: String): Boolean {
        return nickname.isNotEmpty()
    }

    private fun checkPaswword(password: String): Boolean  {
        return password.length >= 6
    }

    private fun checkEmail(email: String): Boolean {
        return email.length >= 8
    }

    private fun registerWithFirebase(email: String, password: String, nickname:String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FirebaseEmailTAG", "registerWithFirebase: Success")
                    _state.value = UIState.Success(
                        State(
                            isRegistered = true,
                            email = email,
                            password = password,
                            nickname = nickname

                        )
                    )

                } else {

                }
            }
            .addOnFailureListener { error ->
                _state.value = UIState.Error(100, error.localizedMessage)
            }


    }




    data class State(
        val isRegistered: Boolean,
        val isValidMail: Boolean = true,
        val isValidPassword: Boolean = true,
        val isValidRepeatPassword: Boolean = true,
        val isValidNickname: Boolean = true,
        val email: String? = null,
        val password: String? = null,
        val nickname: String? = null
    )
}