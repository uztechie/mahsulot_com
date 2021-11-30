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
@Inject constructor(
    private val retrofitApi: RetrofitApi,
    private val mahsulotDatabase: MahsulotDatabase
) {
    private val mahsulotDao = mahsulotDatabase.mahsulotDao()


    suspend fun loadProducts() = retrofitApi.loadProducts()

    suspend fun loadCategories() = retrofitApi.loadCategories()

    suspend fun loadProductsByCategory(catId: Int) = retrofitApi.loadProductsByCategory(catId)

    suspend fun searchProducts(text: String) = retrofitApi.searchProducts(text)

    suspend fun loadTopProducts() = retrofitApi.loadTopProducts()

    suspend fun loadBanners() = retrofitApi.loadBanners()

    suspend fun sendSms(phone: String) = retrofitApi.sendSms(Constants.MY_TOKEN, phone)

    suspend fun checkSms(phone: String, code: String) =
        retrofitApi.checkSmsCode(Constants.MY_TOKEN, phone, code)

    suspend fun registerUser(
        phone: String,
        firstName: String,
        lastname: String,
        birthday: String,
        gender: String
    ) = retrofitApi.registerUser(Constants.MY_TOKEN, phone, firstName, lastname, birthday, gender)


    suspend fun loadProfile(token: String) = retrofitApi.loadProfile(token)

    //streams

    suspend fun createStream(
        token: String,
        title: String,
        url: String,
        status: String,
        productId: Int,
        sellerId: Int
    ) = retrofitApi.createStream(token, title, url, status, productId, sellerId)

    suspend fun loadStreams(token: String) = retrofitApi.loadStreams(token)

    suspend fun deleteStream(token: String, id: Int) = retrofitApi.deleteStream(token, id)

    suspend fun searchStream(token: String, search: String) =
        retrofitApi.searchStream(token, search)

    suspend fun streamStatistics(token: String) = retrofitApi.streamStatistics(token)

    suspend fun loadOrderStatistics(token: String) =
        retrofitApi.loadOrderStatistics(Constants.MY_TOKEN, token)

    suspend fun makeWithdraw(
        myToken: String,
        token: String,
        sellerId: Int,
        amount: Int,
        payment_card: String,
        status: Int,
        desc: Int,
        seller_bonus: Int
    ) = retrofitApi.makeWithdraw(myToken, token, sellerId, amount, payment_card, status, desc, seller_bonus)

    suspend fun loadWithdrawHistory(token: String) = retrofitApi.loadWithdrawHistory(token)


    suspend fun makeOrder(
        myToken: String,
        token: String,
        map:HashMap<String, Any>
    ) = retrofitApi.makeOrder(myToken, token, map)

    suspend fun updateProfile(
        token: String,
        map: HashMap<String, String>
    ) = retrofitApi.updateProfile(token, map)

    suspend fun makeMarjaOrder(
        myToken: String,
        token: String,
        map:HashMap<String, Any>
    ) = retrofitApi.makeMarjaOrder(myToken, token, map)

    suspend fun loadCompetition(token: String) = retrofitApi.loadCompetition(token)


    //database

    suspend fun insertUser(user: User) = mahsulotDao.insertUser(user)

    suspend fun deleteUser() = mahsulotDao.deleteUser()

    fun getUser() = mahsulotDao.getUser()


}