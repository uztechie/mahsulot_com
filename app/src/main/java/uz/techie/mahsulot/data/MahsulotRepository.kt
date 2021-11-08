package uz.techie.mahsulot.data

import uz.techie.mahsulot.network.RetrofitApi
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MahsulotRepository @Inject constructor(private val retrofitApi: RetrofitApi) {


    suspend fun loadProducts() = retrofitApi.loadProducts()

}