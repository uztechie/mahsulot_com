package uz.techie.mahsulot.util

import android.os.Parcelable
import uz.techie.mahsulot.dialog.InternetDialog
import uz.techie.mahsulot.model.Product

object Constants {
    const val BASE_URL = "https://mahsulot.com/"
    var homeRecyclerPosition = 0
    var homeRecyclerState:Parcelable? = null
    var productList = mutableListOf<Product>()
    var internetDialog:InternetDialog? = null
}