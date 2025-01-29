package com.example.cinemaatl.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.cinemaatl.R
import com.example.cinemaatl.UIState
import com.example.cinemaatl.ViewModels.MainVM
import com.example.cinemaatl.adapter.UserTicketsAdapter
import com.example.cinemaatl.databinding.FragmentUserTicketsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserTicketsFragment : Fragment() {
    private val viewModel: MainVM by activityViewModels()
    private  var binding: FragmentUserTicketsBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentUserTicketsBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val adapter = UserTicketsAdapter(emptyList())
        binding?.ticketsRecyclerView?.adapter = adapter

        viewModel.movies.observe(viewLifecycleOwner){state ->
            when(state){
                is UIState.Success -> {
                    adapter.updateMovies(state.data)
                }
                is UIState.Error -> {
                    Toast.makeText(requireContext(), "Error loading movies: ${state.errorMessage}", Toast.LENGTH_SHORT).show()
                }
                is UIState.Loading -> {

                }
            }

        }

        // Загружаем билеты пользователя
        viewModel.loadUserTickets()

        // Подписываемся на состояние загрузки билетов
        viewModel.ticketsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UIState.Loading -> {
                    binding?.progressBar?.visibility = if (state.isLoading) View.VISIBLE else View.GONE
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

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}