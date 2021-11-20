package uz.techie.mahsulot.util

import android.os.Parcelable
import android.view.View
import uz.techie.mahsulot.dialog.InternetDialog
import uz.techie.mahsulot.model.Product

object Constants {

    const val BASE_URL = "https://mahsulot.com/"
    const val MY_TOKEN = "slkfjadof23415ewkrjweja2342ghieurasdk934234jkb4jrb432j52k43DSNFInsiodfnewoiea"


    var homeRecyclerPosition = 0
    var homeRecyclerState:Parcelable? = null
    var productList = mutableListOf<Product>()
    var internetDialog:InternetDialog? = null

}