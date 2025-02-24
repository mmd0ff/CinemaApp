package com.example.cinemaatl.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileVM @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _userNickname:MutableLiveData<String> = MutableLiveData()
    val userNickname: LiveData<String> = _userNickname

    private val _userEmail:MutableLiveData<String> = MutableLiveData()
    val userEmail:LiveData<String> = _userEmail

    fun getCurrentuserEmail(){
        val currentUser = firebaseAuth.currentUser
        _userEmail.value = currentUser?.email
    }

    fun getCurrentNickname(){
        val cuurentUser = firebaseAuth.currentUser
        _userNickname.value = cuurentUser?.displayName ?: "Guest"
    }

    fun logOut(){
        firebaseAuth.signOut()
    }


}