package com.example.cinemaatl.ui.userticket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.FragmentUserTicketsBinding
import com.example.cinemaatl.model.TicketModel
import com.example.cinemaatl.ui.core.UIState
import com.example.cinemaatl.ui.ticket.TicketVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserTicketsFragment : Fragment() {

    private val ticketVM by activityViewModels<TicketVM>()
    private var binding: FragmentUserTicketsBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentUserTicketsBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = UserTicketsAdapter { ticket ->
            showDeleteConfirmationDialog(ticket)

        }
        binding?.ticketsRecyclerView?.adapter = adapter


        // Подписываемся на состояние загрузки билетов
        ticketVM.ticketState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Loading -> {
                    binding?.progressBar?.visibility =
                        if (state.isLoading) View.VISIBLE else View.GONE
                }

                is UIState.Success -> {
                    binding?.progressBar?.visibility = View.GONE
                    val tickets = state.data
                    adapter.updateList(tickets) // Обновляем список в адаптере
                }

                is UIState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${state.errorMessage}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        ticketVM.loadUserTickets()
    }

    private fun showDeleteConfirmationDialog(ticket: TicketModel) {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.delete_confirmation))
            .setMessage(getString(R.string.are_you_sure_dialog))
            .setPositiveButton(getString(R.string.delete_dialog)) { _, _ ->
                ticketVM.deleteTicket(ticket)
            }
            .setNegativeButton(getString(R.string.cancel_dialog)) { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}