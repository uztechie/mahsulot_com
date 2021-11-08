package uz.techie.mahsulot.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Product(
     val id: Int,
     val remained:Int,
     val gallary:List<Gallery>? = null,
     val name:String,
     val desc:String,
     val full_desc:String,
     val photo:String,
     val price: Int,
     val bonus:Int,
     val reklama_link:String,
     val youtube_link:String?=null,
     val status:String,
     val category: List<Int>,
):Serializable