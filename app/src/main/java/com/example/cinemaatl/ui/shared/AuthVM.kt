package com.example.cinemaatl.ui.shared

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cinemaatl.R
import com.example.cinemaatl.ui.core.UIState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthVM @Inject constructor(
    private val firebaseAuth: FirebaseAuth,

    ) : ViewModel() {

    private val _loginState = MutableLiveData<UIState<LoginState>?>()
    val loginState: LiveData<UIState<LoginState>?> = _loginState

    private val _registerState = MutableLiveData<UIState<RegisterState>?>()
    val registerState: LiveData<UIState<RegisterState>?> = _registerState

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = UIState.Loading(true)
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        val nickname = user?.displayName ?: "User"
                        _loginState.value = UIState.Success(
                            LoginState(
                                isLogined = true,
                                nickname = nickname
                            )
                        )
                    } else {
                        val error = when (task.exception) {
                            is FirebaseAuthInvalidCredentialsException -> UIState.Error(
                                errorResId = R.string.firebase_credential_conflict

                            )

                            else -> UIState.Error(
                                errorMessage = task.exception?.localizedMessage
                            )
                        }

                        _loginState.value = error
                    }
                }
                .addOnFailureListener {
                }
        }

    }

    fun registerUser(email: String, password: String, nickname: String) {
        viewModelScope.launch {
            _registerState.value = UIState.Loading(true)

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = firebaseAuth.currentUser
                        user?.updateProfile(
                            UserProfileChangeRequest.Builder()
                                .setDisplayName(nickname)
                                .build()
                        )?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {

                                _registerState.value = UIState.Success(
                                    RegisterState(
                                        isRegistered = true,
                                        email = email,
                                        password = password,
                                        nickname = nickname
                                    )
                                )
                            } else {
                                _registerState.value =
                                    UIState.Error(100, "Не удалось обновить профиль")
                            }
                        }
                    } else {
                        val error = when (task.exception) {
                            is FirebaseAuthUserCollisionException -> UIState.Error(
                                errorResId = R.string.firebase_email_conflict
                            )

                            else -> UIState.Error(

                                errorMessage = task.exception?.localizedMessage

                            )
                        }
                        _registerState.value = error
                    }
                }
                .addOnFailureListener {

                }

        }
    }

    fun resetAuthSate() {
        _registerState.value = null
        _loginState.value = null
    }

    data class LoginState(
        val isLogined: Boolean,
        val nickname: String? = null
    )

    data class RegisterState(
        val isRegistered: Boolean,
        val email: String? = null,
        val password: String? = null,
        val nickname: String? = null
    )
}