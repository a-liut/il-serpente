package it.aliut.ilserpente

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController

    private val navigationListener =
        NavController.OnDestinationChangedListener { _: NavController, navDestination: NavDestination, _: Bundle? ->
            main_bottom_nav_view.visibility =
                if (navDestination.id == R.id.gameFragment) View.GONE else View.VISIBLE
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        navController = findNavController(R.id.nav_host_fragment)
        main_bottom_nav_view.setupWithNavController(navController)
    }

    override fun onResume() {
        super.onResume()

        navController.addOnDestinationChangedListener(navigationListener)
    }

    override fun onPause() {
        super.onPause()

        navController.removeOnDestinationChangedListener(navigationListener)
    }
}
