package com.example.cinemaatl.ui.ticket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cinemaatl.UIState
import com.example.cinemaatl.model.TicketModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class TicketVM @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel()  {

    private val _ticketState:MutableLiveData<UIState<List<TicketModel>>> = MutableLiveData()
    val ticketState: LiveData<UIState<List<TicketModel>>> = _ticketState

    private val _saveTicketState:MutableLiveData<UIState<Unit>> = MutableLiveData()
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
                    Log.d("TicketsVM", "Loaded tickets: ${tickets.size}")
                    _ticketState.value = UIState.Success(tickets)
                }
                .addOnFailureListener { e ->
                    Log.e("TicketsVM", "Error loading tickets: ${e.localizedMessage}")
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
                    Log.d("TicketsVM", "Ticket saved with ID: ${documentReference.id}")

                    _saveTicketState.value = UIState.Success(Unit)

                    loadUserTickets()
                }
                .addOnFailureListener { e ->
                    Log.e("TicketsVM", "Error loading tickets: ${e.localizedMessage}")
                    _saveTicketState.value = UIState.Error(500, "Firestore Error: ${e.localizedMessage}")
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
                Log.e("TicketsVM", "Ticket ID is null or empty. Cannot delete ticket.")
                return
            }
            firestore.collection("tickets").document(ticketId)
                .delete()
                .addOnSuccessListener {
                    Log.d("TicketsVM", "Ticket deleted successfully")
                    loadUserTickets()
                }
                .addOnFailureListener { e ->
                    Log.e("TicketsVM", "Error deleting ticket", e)
                }
        }
    }
}