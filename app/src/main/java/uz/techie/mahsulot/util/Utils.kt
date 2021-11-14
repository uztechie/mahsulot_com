package uz.techie.mahsulot.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import java.lang.NullPointerException
import java.text.NumberFormat

object Utils {


    fun toMoney(value:Int):String{
        val numberFormat = NumberFormat.getNumberInstance()
        return numberFormat.format(value)
    }


//    fun hasInternetConnection(context: Context): Boolean {
//        return try {
//            val manager =
//                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            var networkInfo: NetworkInfo? = null
//            run {
//                networkInfo = manager.activeNetworkInfo
//                networkInfo != null && networkInfo!!.isConnected
//            }
//        } catch (e: NullPointerException) {
//            false
//        }
//    }

     fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }



}