package com.example.cinemaatl.ui.intro

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.FragmentIntroBinding
import com.example.cinemaatl.helper.ZoomOutPageTransformer
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class IntroFragment : Fragment() {

    private var binding: FragmentIntroBinding? = null

    @Inject
    lateinit var firebaseAuth: FirebaseAuth


    private val imageList = listOf(
        R.drawable.tenet,
        R.drawable.lor,
        R.drawable.furiosa,
        R.drawable.sincity,
        R.drawable.substance,
        R.drawable.robot,
        R.drawable.deadpool,
        R.drawable.venom,
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): ConstraintLayout? {
        binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            findNavController().navigate(R.id.action_introFragment_to_baseFragment)
        } else {
            setupViewPager()
            binding?.btGo?.setOnClickListener {
                findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToRegisterFragment())

            }

        }


    }

    private fun setupViewPager() {
        val viewPager: ViewPager2 = binding!!.viewPager
        val pagerAdapter = ViewPagerAdapter(imageList) // Создаем адаптер с List<Int>
        viewPager.adapter = pagerAdapter
        viewPager.setPageTransformer(ZoomOutPageTransformer())
        viewPager.offscreenPageLimit = 1
        viewPager.setCurrentItem(1, true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }


}

