package uz.techie.mahsulot.model

data class CompetitionResponse(
    val is_active:String? = null,
    val status:Int? = null,
    val concource:Concourse? = null,
    val statistic:List<Competition>? = null
)
