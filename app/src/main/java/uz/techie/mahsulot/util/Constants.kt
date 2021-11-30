package uz.techie.mahsulot.util

import android.os.Parcelable
import android.view.View
import uz.techie.mahsulot.dialog.InternetDialog
import uz.techie.mahsulot.model.Product

object Constants {

    const val ADMIN_TOKEN: String = "Token fa3e2a6c69c061df8cab7ba1edfe621e9f4df993"
    const val BASE_URL = "https://mahsulot.com/"
    const val MY_TOKEN = "slkfjadof23415ewkrjweja2342ghieurasdk934234jkb4jrb432j52k43DSNFInsiodfnewoiea"


    var homeRecyclerPosition = 0
    var homeRecyclerState:Parcelable? = null
    var productList = mutableListOf<Product>()
    var internetDialog:InternetDialog? = null

}