package uz.techie.mahsulot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.internal.ContextUtils.getActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*
import uz.techie.mahsulot.data.MahsulotViewModel
import uz.techie.mahsulot.dialog.InternetDialog
import uz.techie.mahsulot.receiver.InternetReceiver
import uz.techie.mahsulot.util.Constants
import uz.techie.mahsulot.util.Utils


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private var isFirstLaunch = true
    lateinit var internetReceiver: MainInternetReceiver
    lateinit var internetDialog: InternetDialog

    lateinit var viewModel:MahsulotViewModel
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var bottomNavigationView:BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        updateStatusLight()
        viewModel = ViewModelProvider(this).get(MahsulotViewModel::class.java)

        internetReceiver = MainInternetReceiver()
        internetDialog = InternetDialog(this)


        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragmentContainer) as NavHostFragment
        navController = navHostFragment.findNavController()

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment)
        )

        bottomNavigationView = findViewById(R.id.bottomNavigationView)

        bottomNavigationView.setupWithNavController(navController)


        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            if (destination.id == R.id.homeFragment || destination.id == R.id.searchFragment) {
//                supportActionBar!!.hide()
//            }
//            else {
//                supportActionBar!!.show()
//            }

            if (destination.id == R.id.productDetailsFragment){
                bottomNavigationView.visibility = View.GONE
            }
            else{
                bottomNavigationView.visibility = View.VISIBLE
            }

        }

    }

    fun updateStatusLight() {
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.background_color)
    }

    fun updateStatusBarDark() { // Color must be in hexadecimal fromat

        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.colorAccent)

    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter()
        intentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED")
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(internetReceiver, intentFilter)

        viewModel.loadBanners()
        viewModel.loadTopProducts()
        viewModel.loadProducts()
        viewModel.loadCategories()


    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(internetReceiver)
    }



   inner class MainInternetReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            context?.let {
                if (Utils.isNetworkAvailable(it)){
                    Log.d("TAG", "onReceive: should hide ")

                    internetDialog.dismiss()

                    if (!isFirstLaunch){
                        viewModel.loadBanners()
                        viewModel.loadTopProducts()
                        viewModel.loadProducts()
                        viewModel.loadCategories()
                    }

                    isFirstLaunch = false

                }
                else{
                    internetDialog.show()
                }
            }

        }
    }


}
