package com.example.cinemaatl.ui.intro

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cinemaatl.helper.ZoomOutPageTransformer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.cinemaatl.R
import com.example.cinemaatl.databinding.FragmentIntroBinding
import dagger.hilt.android.AndroidEntryPoint


//class IntroFragment : Fragment() {
//    private lateinit var binding: FragmentIntroBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        // Inflate the layout for this fragment
//        binding = FragmentIntroBinding.inflate(layoutInflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//        val images = listOf(
//            R.drawable.tenet,
//            R.drawable.lor,
//            R.drawable.furiosa,
//            R.drawable.sincity
//        )
//
//
//
////        val adapter = ViewPagerAdapter(images)
////       binding.viewPager.adapter = adapter
////        binding.viewPager.apply {
////
////            // Устанавливаем отступы
////            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.page_margin)
////            val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
////
////            setPageTransformer { page, position ->
////                val offset = position * -(2 * offsetPx + pageMarginPx)
////                page.translationX = offset
////                val scaleFactor = 1 - (0.15f * Math.abs(position))
////                page.scaleY = scaleFactor
////                page.scaleX = scaleFactor
////            }
////
////            // Настраиваем видимость соседних элементов
////            clipToPadding = false
////            clipChildren = false
////            offscreenPageLimit = 3
////            setPadding(offsetPx, 0, offsetPx, 0)
////        }
////        binding.viewPager.apply {
////            offscreenPageLimit = 3
////
////            val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.page_margin)
////            val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)
////
////            setPageTransformer { page, position ->
////                val offset = position * -(2 * offsetPx + pageMarginPx)
////                page.translationX = offset
////                val scaleFactor = 1 - (0.15f * Math.abs(position))
////                page.scaleY = scaleFactor
////                page.scaleX = scaleFactor
////            }
////
////            clipToPadding = false
////            clipChildren = false
////            setPadding(offsetPx, 0, offsetPx, 0)
////        }
//
//
//
//
//
//
//
//        binding.btGo.setOnClickListener {
//            findNavController().navigate(R.id.action_introFragment_to_baseFragment)
//        }
//
//
//    }
//}
@AndroidEntryPoint
class IntroFragment : Fragment() {

    private var _binding: FragmentIntroBinding? = null
    private val binding get() = _binding!!

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
    ): View {
        _binding = FragmentIntroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager: ViewPager2 = binding.viewPager
        val pagerAdapter = ViewPagerAdapter(imageList) // Создаем адаптер с List<Int>
        viewPager.adapter = pagerAdapter
        viewPager.setPageTransformer(ZoomOutPageTransformer())
        viewPager.offscreenPageLimit = 1

        viewPager.setCurrentItem(1,true)


        // Вот здесь происходит использование extension-функции!

//        viewPager.applyCarouselTransformer(
//
//            pageSpacing = resources.getDimensionPixelSize(R.dimen.page_margin)
//        )


        binding.btGo.setOnClickListener {
            findNavController().navigate(IntroFragmentDirections.actionIntroFragmentToRegisterFragment())

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}

//fun ViewPager2.applyCarouselTransformer(
//    scaleFactor: Float = 0.75f, // Уменьшено для большего эффекта
//    alphaFactor: Float = 0.4f, // Уменьшено для большей прозрачности
//    translationFactor: Float = 0.4f, // Увеличено для большего сдвига
//    pageSpacing: Int = 0,
//    offscreenPageLimit: Int = 3
//) {
//    this.offscreenPageLimit = offscreenPageLimit
//
//    setPageTransformer { page, position ->
//        page.apply {
//            when {
//                position < -1 -> { // Страницы слева от видимой
//                    alpha = alphaFactor
//                    scaleX = scaleFactor
//                    scaleY = scaleFactor
//                    translationX = 0f
//                    translationZ = -1f // Под центральной
//                }
//                position <= 1 -> { // Видимая страница и соседние
//                    val absPosition = abs(position)
//                    alpha = 1 - (absPosition * (1 - alphaFactor))
//                    val scale = 1 - (absPosition * (1 - scaleFactor))
//                    scaleX = scale
//                    scaleY = scale
//                    translationX = page.width * -position * translationFactor
//                    translationZ = 1 - absPosition*0.2F // Центральная страница сверху, боковые под ней
//                }
//                else -> { // Страницы справа от видимой
//                    alpha = alphaFactor
//                    scaleX = scaleFactor
//                    scaleY = scaleFactor
//                    translationX = 0f
//                    translationZ = -1f // Под центральной
//                }
//            }
//        }
//    }
//
//    (getChildAt(0) as? RecyclerView)?.apply {
//        clipToPadding = false
//        clipChildren = false
//        if (pageSpacing != 0) {
//            setPaddingRelative(pageSpacing, 0, pageSpacing, 0)
//        }
//    }
//}

