package com.example.cinemaatl

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.cinemaatl.databinding.ActivityMainBinding
import com.example.cinemaatl.helper.LocaleHelper
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var binding: ActivityMainBinding? = null
    private var sharedPreference: SharedPreferences? = null

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)



        setContentView(binding?.root)

        sharedPreference = getPreferences(MODE_PRIVATE)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.actContainer) as NavHostFragment

        val controller = navHostFragment.navController
        NavigationUI.setupWithNavController(binding!!.bottomNavigation, controller)



        controller.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("MainActivity", "Current destination: ${destination.id}")
            when (destination.id) {
                R.id.introFragment, R.id.seatsFragment, R.id.filmDetailFragment, R.id.loginFragment, R.id.registerFragment, R.id.ticketFragment, R.id.paymentSuccess, R.id.registerSuccess -> {
                    Log.d("MainActivity", "Hiding BottomNavigationView")
                    binding?.bottomNavigation?.visibility = View.GONE
                }

                else -> {
                    Log.d("MainActivity", "Showing BottomNavigationView")
                    binding?.bottomNavigation?.visibility = View.VISIBLE
                }

            }
        }


        binding?.bottomNavigation?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.baseFragment -> {
                    controller.navigate(R.id.baseFragment)
                    true
                }


                R.id.searchResultFragment -> {
                    controller.navigate(R.id.searchResultFragment)
                    true
                }

                R.id.userTicketsFragment -> {
                    controller.navigate(R.id.userTicketsFragment)
                    true
                }

                R.id.profileFragment -> {
                    controller.navigate(R.id.profileFragment)
                    true
                }

                else -> false
            }
        }


        val savedLanguage = LocaleHelper.getSavedLanguage(this)
        LocaleHelper.setLocale(this, savedLanguage)

        updateBottomNavigationText()

    }

    private fun updateBottomNavigationText() {
        val menu = binding?.bottomNavigation?.menu
        menu?.let {
            it.findItem(R.id.baseFragment).title = getString(R.string.home)
            it.findItem(R.id.searchResultFragment).title = getString(R.string.search)
            it.findItem(R.id.userTicketsFragment).title = getString(R.string.tickets)
            it.findItem(R.id.profileFragment).title = getString(R.string.profile)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}



