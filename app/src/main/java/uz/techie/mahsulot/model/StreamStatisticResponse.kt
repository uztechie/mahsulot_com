package uz.techie.mahsulot.model

data class StreamStatisticResponse(
    val message:String? = null,
    val status:Int? = null,
    val statistic:List<StreamStatistic>? = null
)
