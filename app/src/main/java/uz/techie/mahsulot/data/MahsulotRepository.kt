package uz.techie.mahsulot.data

import uz.techie.mahsulot.db.MahsulotDao
import uz.techie.mahsulot.db.MahsulotDatabase
import uz.techie.mahsulot.model.User
import uz.techie.mahsulot.network.RetrofitApi
import uz.techie.mahsulot.util.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MahsulotRepository
@Inject constructor(private val retrofitApi: RetrofitApi, private val mahsulotDatabase: MahsulotDatabase) {
    private val mahsulotDao = mahsulotDatabase.mahsulotDao()


    suspend fun loadProducts() = retrofitApi.loadProducts()

    suspend fun loadCategories() = retrofitApi.loadCategories()

    suspend fun loadProductsByCategory(catId:Int) = retrofitApi.loadProductsByCategory(catId)

    suspend fun searchProducts(text:String) = retrofitApi.searchProducts(text)

    suspend fun loadTopProducts() = retrofitApi.loadTopProducts()

    suspend fun loadBanners() = retrofitApi.loadBanners()

    suspend fun sendSms(phone:String) = retrofitApi.sendSms(Constants.MY_TOKEN, phone)

    suspend fun checkSms(phone: String, code:String) = retrofitApi.checkSmsCode(Constants.MY_TOKEN, phone, code)

    suspend fun registerUser(
        phone: String,
        firstName:String,
        lastname:String,
        birthday:String,
        gender:String
    ) = retrofitApi.registerUser(Constants.MY_TOKEN, phone, firstName, lastname, birthday, gender)


    suspend fun loadProfile(token:String) = retrofitApi.loadProfile(token)

    //streams

    suspend fun createStream(
        token: String,
        title:String,
        url:String,
        status:String,
        productId:Int,
        sellerId:Int
    ) = retrofitApi.createStream(token, title, url, status, productId, sellerId)

    suspend fun loadStreams(token: String) = retrofitApi.loadStreams(token)

    suspend fun deleteStream(token: String, id:Int) = retrofitApi.deleteStream(token, id)

    suspend fun searchStream(token: String, search:String) = retrofitApi.searchStream(token, search)

    suspend fun streamStatistics(token: String) = retrofitApi.streamStatistics(token)

    suspend fun loadOrderStatistics(token: String) = retrofitApi.loadOrderStatistics(Constants.MY_TOKEN, token)


    //database

    suspend fun insertUser(user: User) = mahsulotDao.insertUser(user)

    suspend fun deleteUser() = mahsulotDao.deleteUser()

    fun getUser() = mahsulotDao.getUser()




}