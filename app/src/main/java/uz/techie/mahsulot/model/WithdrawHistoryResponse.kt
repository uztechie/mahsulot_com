package uz.techie.mahsulot.model

data class WithdrawHistoryResponse(
    val message: String? = null,
    val status: Int? = null,
    val payments: List<Withdraw>? = null
)