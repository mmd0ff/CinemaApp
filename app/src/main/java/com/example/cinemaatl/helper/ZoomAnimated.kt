package com.example.cinemaatl.helper

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import kotlin.math.abs

class ZoomOutPageTransformer : ViewPager2.PageTransformer {

    override fun transformPage(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height

        when {
            position < -1 -> {
                view.alpha = 0f
            }
            position <= 1 -> {
                val scaleFactor = Math.max(MIN_SCALE, 1 - abs(position))
                val vertMargin = view.height * (1 - scaleFactor) / 2
                val horzMargin = pageWidth * (1 - scaleFactor) / 2
                view.scaleX = scaleFactor
                view.scaleY = scaleFactor
                view.alpha = MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA)

                view.translationX = -horzMargin * position // Ключевая строка!
            }
            else -> {
                view.alpha = 0f
            }
        }
    }

    companion object {
        private const val MIN_SCALE = 0.5f // Поэкспериментируйте с этим значением
        private const val MIN_ALPHA = 0.5f
    }

}