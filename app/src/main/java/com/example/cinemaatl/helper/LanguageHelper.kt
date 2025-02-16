package com.example.cinemaatl.helper

import android.annotation.SuppressLint
import android.content.Context
import java.util.Locale


class LocaleHelper(private val context: Context) {

    companion object {
        private const val SELECTED_LANGUAGE = "Locale.Helper.Selected.Language"
        private const val PREF_NAME = "Locale.Helper.Prefs"

        fun setLocale(context: Context, language: String) {
            persistLanguage(context, language)
            updateResources(context, language)
        }

        @SuppressLint("ApplySharedPref")
        private fun persistLanguage(context: Context, language: String) {
            val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            preferences.edit().putString(SELECTED_LANGUAGE, language).commit()
        }

        private fun updateResources(context: Context, language: String) {
            val locale = Locale(language)
            Locale.setDefault(locale)
            val resources = context.resources
            val configuration = resources.configuration
            configuration.setLocale(locale)
            resources.updateConfiguration(configuration, resources.displayMetrics)
        }

        fun getSavedLanguage(context: Context): String {
            val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return preferences.getString(SELECTED_LANGUAGE, Locale.getDefault().language) ?: "en"
        }
    }
}
