package com.example.cinemaatl.ui.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaatl.UIState
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginVM @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _state: MutableLiveData<UIState<State>> = MutableLiveData()
    val state: LiveData<UIState<State>> = _state


    fun getCurrentUserNickname(): LiveData<String?>{
        return MutableLiveData<String?>().apply {
            value = firebaseAuth.currentUser?.displayName
        }
    }

    fun loginUser(email: String, password: String){
        val isValidEmail = checkEmail(email)
        val isValidPassword = checkPaswword(password)

        if(isValidEmail && isValidPassword){
            loginWithFirebase(email,password)
        } else {
            _state.value = UIState.Success(
                State(
                isLogined = false,
                isValidPassword = isValidPassword
            )
            )
        }
    }


    private fun loginWithFirebase(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = firebaseAuth.currentUser
                    if(user != null){
                        val nickname = user.displayName ?: "User"

                    Log.d("FirebaseEmailTAG", "loginWithFirebase: Success")
                    _state.value = UIState.Success(
                        State(
                            isLogined = true,
                            nickname = nickname

                        )
                    )
                } else {
                        _state.value = UIState.Error(200, task.exception?.localizedMessage)
                    }

                }
            }
            .addOnFailureListener { error ->
                _state.value = UIState.Error(200, error.localizedMessage)
            }


    }

    private fun checkEmail(email:String) : Boolean{
        return email.length >= 6
    }
    private fun checkPaswword(password: String):Boolean{
        return password.length >= 8
    }

    data class State(
        val isLogined: Boolean,
        val isValidMail: Boolean = true,
        val isValidPassword: Boolean = true,
        val nickname:String? = null
    )
}