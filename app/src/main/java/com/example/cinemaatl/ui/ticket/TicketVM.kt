package com.example.cinemaatl.ui.ticket

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaatl.model.TicketModel
import com.example.cinemaatl.ui.core.UIState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TicketVM @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _ticketState: MutableLiveData<UIState<List<TicketModel>>> = MutableLiveData()
    val ticketState: LiveData<UIState<List<TicketModel>>> = _ticketState

    private val _saveTicketState: MutableLiveData<UIState<Unit>> = MutableLiveData()
    val saveTicketState: LiveData<UIState<Unit>> = _saveTicketState


    fun loadUserTickets() {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            _ticketState.value = UIState.Loading(true)
            firestore.collection("tickets")
                .whereEqualTo("userId", currentUser.uid)
                .get()
                .addOnSuccessListener { result ->
                    val tickets = result.documents.map { doc ->
                        val ticket = doc.toObject(TicketModel::class.java)!!
                        ticket.copy(id = doc.id)
                    }

                    _ticketState.value = UIState.Success(tickets)
                }
                .addOnFailureListener { e ->

                    _ticketState.value = UIState.Error(null, "Error: ${e.localizedMessage}")
                }
        } else {
            _ticketState.value = UIState.Error(401, "User not Authenticated")
        }
    }

    fun saveTicket(ticket: TicketModel) {
        val currentUser = firebaseAuth.currentUser

        if (currentUser != null) {
            val ticketWithUser = ticket.copy(userId = currentUser.uid)
            _saveTicketState.value = UIState.Loading(true)

            firestore.collection("tickets")
                .add(ticketWithUser)
                .addOnSuccessListener { documentReference ->
                    _saveTicketState.value = UIState.Success(Unit)

                    loadUserTickets()
                }
                .addOnFailureListener { e ->

                    _saveTicketState.value =
                        UIState.Error(500, "Firestore Error: ${e.localizedMessage}")
                }
        } else {
            _saveTicketState.value = UIState.Error(401, "User not Authenticated")
        }
    }

    fun deleteTicket(ticket: TicketModel) {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            val ticketId = ticket.id
            if (ticketId.isNullOrEmpty()) {
                return
            }
            firestore.collection("tickets").document(ticketId)
                .delete()
                .addOnSuccessListener {

                    loadUserTickets()
                }
                .addOnFailureListener { e ->

                }
        }
    }
}