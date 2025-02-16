package com.example.cinemaatl.ui.userticket

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.cinemaatl.UIState
import com.example.cinemaatl.databinding.FragmentUserTicketsBinding
import com.example.cinemaatl.model.TicketModel
import com.example.cinemaatl.ui.topmovie.TopMovieVM
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserTicketsFragment : Fragment() {

//    private val viewModelTicket by viewModels<UserTicketVM>()
    private val viewModelTicket by activityViewModels<UserTicketVM>()
    private val viewModelTop: TopMovieVM by viewModels<TopMovieVM>()
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


        val adapter = UserTicketsAdapter(emptyList()) { ticket ->
            showDeleteConfirmationDialog(ticket)

        }
        binding?.ticketsRecyclerView?.adapter = adapter

        viewModelTicket.loadUserTickets()

        viewModelTop.movies.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Success -> {
                    adapter.updateMovies(state.data)
                }

                is UIState.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Error loading movies: ${state.errorMessage}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is UIState.Loading -> {


                }
            }

        }

        // Загружаем билеты пользователя


        // Подписываемся на состояние загрузки билетов
        viewModelTicket.ticketsState.observe(viewLifecycleOwner) { state ->
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
    }

    private fun showDeleteConfirmationDialog(ticket: TicketModel) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Confirmation")
            .setMessage("Are you sure you want to delete this ticket?")
            .setPositiveButton("Delete") { _, _ ->
                viewModelTicket.deleteTicket(ticket)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
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