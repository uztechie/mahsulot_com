package uz.techie.mahsulot.data

import uz.techie.mahsulot.db.MahsulotDao
import uz.techie.mahsulot.db.MahsulotDatabase
import uz.techie.mahsulot.model.User
import uz.techie.mahsulot.network.RetrofitApi
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

    suspend fun insertUser(user: User) = mahsulotDao.insertUser(user)

    suspend fun deleteUser() = mahsulotDao.deleteUser()

    fun getUser() = mahsulotDao.getUser()




}