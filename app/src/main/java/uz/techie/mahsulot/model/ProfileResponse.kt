package uz.techie.mahsulot.model

data class ProfileResponse(
    val message: String? = null,
    val status:Int? = null,
    val profiles:User? = null,
    val data:UserData? = null
)