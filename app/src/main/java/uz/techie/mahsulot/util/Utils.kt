package uz.techie.mahsulot.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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

    fun reformatDate(date: Date):String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(date)
    }

    fun reformatDateFromString(previousDate: String):String {
        var dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        val date = dateFormat.parse(previousDate)
        dateFormat = SimpleDateFormat("dd.MM.yyyy hh:mm")
        return dateFormat.format(date!!)
    }


    fun showMessage(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    fun copyTextToClipboard(context: Context, url: String) {
        val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("url", url)
        clipboardManager.setPrimaryClip(clipData)
    }


    fun streamStatus(statusCode:Int):String{
        var status = ""
        when(statusCode){
            1->{
                status = "Yangi"
            }
            2->{
                status = "Qabul qilindi"
            }
            3->{
                status = "Yetkazilmoqda"
            }
            4->{
                status = "Yetqazib berildi"
            }
            5->{
                status = "Nosoz mahsulot"
            }
            6->{
                status = "Bekor qilindi"
            }
            7->{
                status = "Arxivlandi"
            }
            8->{
                status = "Qayta qo'ng'iroq"
            }
            9->{
                status = "Spam"
            }
            10->{
                status = "Arxivlanmoqda"
            }
            11->{
                status = "Muzlatildi"
            }
        }
        return status
    }

    fun hidePhoneNumber(phone:String):String{
        var newPhone = phone

        if (!phone.contains('+')){
            newPhone = "+$phone"
            Log.d("TAG", "hidePhoneNumber: not contains "+newPhone)
        }

        if (newPhone.length>12){
            newPhone = newPhone.replaceRange(8,11, "****")
        }
        return newPhone
    }


    fun regionList():HashMap<Int, String>{
        val regions = hashMapOf<Int, String>()
        regions[1] = "Toshkent"
        regions[2] = "Namangan"
        regions[3] = "Samarqand"
        regions[4] = "Sirdaryo"
        regions[5] = "Surxondaryo"
        regions[6] = "Qoraqalpogʻiston"
        regions[7] = "Navoiy"
        regions[8] = "Andijon"
        regions[9] = "Buxoro"
        regions[10] = "Fargʻona"
        regions[11] = "Jizzax"
        regions[12] = "Xorazm"
        regions[13] = "Qashqadaryo"
        return regions
    }


    fun selectRegion(id:Int):String{
        var region = regionList()[1]!!
        regionList().forEach {
            if (it.key == id){
                region = it.value
            }
        }
        return region
    }


}