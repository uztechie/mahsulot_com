package uz.techie.mahsulot.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(

    @PrimaryKey
    val id:Int,
    val phone:String? = null,
    val birthday:String? = null,
    val gender:String? = null,
    val first_name:String? = null,
    val last_name:String? = null,
    val token:String? = null,
    val bot_token:String? = null
)
