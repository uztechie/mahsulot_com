package uz.techie.mahsulot.model

data class OrderResponse(
    val message:String? = null,
    val status:Int? = null,
    val data:List<Order>? = null
)
