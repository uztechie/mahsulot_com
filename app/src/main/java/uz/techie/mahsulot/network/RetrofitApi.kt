package uz.techie.mahsulot.network

import retrofit2.Response
import retrofit2.http.GET
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.model.ProductResponse

interface RetrofitApi {

    @GET("api/products-list/")
    suspend fun loadProducts(): Response<List<Product>>


}