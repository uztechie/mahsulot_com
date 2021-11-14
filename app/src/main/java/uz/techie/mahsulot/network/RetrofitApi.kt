package uz.techie.mahsulot.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uz.techie.mahsulot.model.Category
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.model.ProductResponse

interface RetrofitApi {

    @GET("api/products-list/")
    suspend fun loadProducts(): Response<List<Product>>


    @GET("api/products-category/")
    suspend fun loadCategories():Response<List<Category>>

    @GET("api/products-category-by/")
    suspend fun loadProductsByCategory(
        @Query("search") catId:Int
    ):Response<List<Product>>


    @GET("api/products-search/")
    suspend fun searchProducts(
        @Query("search") searchText:String
    ):Response<List<Product>>




}