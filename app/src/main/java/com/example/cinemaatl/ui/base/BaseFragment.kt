package com.example.cinemaatl.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.FragmentBaseBinding
import com.example.cinemaatl.ui.profile.ProfileVM
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BaseFragment : Fragment() {
    private var binding: FragmentBaseBinding? = null
    private val profileVM by viewModels<ProfileVM>()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBaseBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileVM.getCurrentNickname()

        profileVM.userNickname.observe(viewLifecycleOwner) { nickname ->
            binding?.hello?.text = getString(R.string.hi_with_name, nickname)
        }

        val pagerApadter = MoviesPagerAdapter(this)
        binding?.viewPager?.adapter = pagerApadter

        binding?.viewPager?.let {
            binding?.tabLayout?.let { it1 ->
                TabLayoutMediator(it1, it) { tab, position ->
                    tab.text = when (position) {
                        0 -> getString(R.string.now_playing)
                        1 -> getString(R.string.coming_soon)
                        else -> throw IllegalArgumentException("Invalid position")
                    }
                }.attach()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}