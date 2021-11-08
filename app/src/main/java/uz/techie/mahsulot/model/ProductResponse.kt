package uz.techie.mahsulot.model

data class ProductResponse(
    val code: Int,
    val message: String,
    val data: List<Product>
)