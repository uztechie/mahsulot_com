package uz.techie.mahsulot.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable



data class Product(
     val id: Int? = null,
     val remained:Int? = null,
     val gallary:List<Gallery>? = null,
     val name:String? = null,
     val desc:String? = null,
     val full_desc:String? = null,
     val photo:String? = null,
     val price: Int? = null,
     val bonus:Int? = null,
     val reklama_link:String? = null,
     val youtube_link:String?=null,
     val status:String? = null,
     val category: List<Int>? = null,
     val viewType:Int? = null
):Serializable