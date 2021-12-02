package uz.techie.mahsulot.model

data class Stream(
    val id: Int?=null,
    val yangi: Int?=null,
    val qabul_qilindi: Int?=null,
    val yetkazilmoqda: Int?=null,
    val yetqazib_berildi: Int?=null,
    val arxivlandi: Int?=null,
    val qayta_qungiroq: Int?=null,
    val nosoz_mahsulot: Int?=null,
    val spam: Int?=null,
    val arxivlanmoqda: Int?=null,
    val muzlatildi: Int?=null,
    val price: Int?=null,
    val bonus: Int?=null,
    val marja: Int?=null,
    val name: String?=null,
    val status: String?=null,
    val url: String?=null,
    val reklama: String?=null,
    val reklama_bot: String?=null,
    val create_at: String?=null,
    val update_at: String?=null,
    val product_id: Int?=null,
    val seller_id: Int?=null,
    val click:Int? = null
)