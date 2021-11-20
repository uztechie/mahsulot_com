package uz.techie.mahsulot.model

data class SmsResponse (
    val message:String? = null,
    val status:Int? = null,
    val token:String? = null,
    val user: User? = null
)