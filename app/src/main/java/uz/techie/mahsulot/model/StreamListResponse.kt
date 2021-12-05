package uz.techie.mahsulot.model

data class StreamListResponse(
    val message:String? = null,
    val status:Int? = null,
    val total:Stream? = null,
    val data:List<Stream>? = null
)