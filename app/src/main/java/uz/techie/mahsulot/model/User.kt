package uz.techie.mahsulot.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(

    @PrimaryKey
    val id:Int,
    val name:String? = null,
    val lastname:String? = null,
    val token:String? = null,
    val phone:String? = null,
    val birthdate:String? = null,
    val gender:String? = null,
)
