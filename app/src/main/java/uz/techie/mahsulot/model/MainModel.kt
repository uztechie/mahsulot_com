package uz.techie.mahsulot.model

data class MainModel(
    val type: Int,
    val list: List<Banner>? = null,
    val product: Product? = null

)