package uz.techie.mahsulot.model

data class Order(
    val affiliate_id: Int? = null,
    val amount: Int? = null,
    val created_at: String? = null,
    val customer_name: String? = null,
    val customer_phone: String? = null,
    val customer_region: Int? = null,
    val desc: String? = null,
    val id: Int? = null,
    val marja_amount: Int? = null,
    val operation_data: String? = null,
    val product_bonus: Int? = null,
    val product_id: Int? = null,
    val product_price: Int? = null,
    val status: Int? = null,
    val status_desc: String? = null,
    val streams_id: Int? = null,
    val updated_at: String? = null,
    val product_name: String? = null
)