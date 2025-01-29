package com.example.cinemaatl

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.cinemaatl.databinding.ActivityMainBinding
import com.example.cinemaatl.fragments.BaseFragment
import com.example.cinemaatl.fragments.UserTicketsFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private  var binding: ActivityMainBinding? = null

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding?.root)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.actContainer) as NavHostFragment

        val controller = navHostFragment.navController
        NavigationUI.setupWithNavController(binding!!.bottomNavigation, controller)



        controller.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("MainActivity", "Current destination: ${destination.id}")
            when (destination.id) {
                R.id.introFragment,R.id.seatsFragment, R.id.filmDetailFragment -> {
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


        val userID = firebaseAuth.currentUser?.uid
        val graph = controller.navInflater.inflate(R.navigation.nav_graph)
        graph.setStartDestination(
            if (userID == null) R.id.registerFragment else R.id.baseFragment
        )
        controller.graph = graph
//
//        val userID = firebaseAuth.currentUser?.uid
//        val graph = controller.navInflater.inflate(R.navigation.nav_graph)
//        if (userID == null) {
//            graph.setStartDestination(R.id.registerFragment)
//        } else {
//            graph.setStartDestination(R.id.baseFragment)
//        }
//        controller.setGraph(graph, null)


    }


//    private fun changeMyFragment(newFragment: Fragment, title: String) {
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.actContainer, newFragment)
//            .commit()
//
//    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}



