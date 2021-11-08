package uz.techie.mahsulot

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import uz.techie.mahsulot.data.MahsulotViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var viewModel:MahsulotViewModel
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MahsulotViewModel::class.java)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment)
        )

        bottomNavigationView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            if (destination.id == R.id.homeFragment || destination.id == R.id.searchFragment) {
//                supportActionBar!!.hide()
//            }
//            else {
//                supportActionBar!!.show()
//            }

        }

    }




}
