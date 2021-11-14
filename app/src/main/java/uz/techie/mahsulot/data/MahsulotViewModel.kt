package uz.techie.mahsulot.data

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import uz.techie.mahsulot.model.Category
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.model.ProductResponse
import uz.techie.mahsulot.model.User
import uz.techie.mahsulot.util.Resource
import javax.inject.Inject

@HiltViewModel
class MahsulotViewModel @Inject constructor(
    private val repository: MahsulotRepository
) : ViewModel() {
    private val TAG = "MahsulotViewModel"

    val products:MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val productsByCategory:MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val searchProducts:MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val categories:MutableLiveData<Resource<List<Category>>> = MutableLiveData()




    fun loadProducts() = viewModelScope.launch {
        products.postValue(Resource.Loading())
        try {
            val response = repository.loadProducts()
            products.postValue(handleProductsResponse(response))
        } catch (e: Exception) {
            products.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun loadProductsByCategory(catId:Int) = viewModelScope.launch {
        productsByCategory.postValue(Resource.Loading())
        try {
            val response = repository.loadProductsByCategory(catId)
            productsByCategory.postValue(handleProductsResponse(response))
        } catch (e: Exception) {
            productsByCategory.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun searchProducts(text:String) = viewModelScope.launch {
        searchProducts.postValue(Resource.Loading())
        try {
            val  response = repository.searchProducts(text)
            searchProducts.postValue(handleProductsResponse(response))
        }catch (e:Exception){
            searchProducts.postValue(Resource.Error(message = e.toString()))
        }
    }


    fun loadCategories() = viewModelScope.launch {
        categories.postValue(Resource.Loading())
        try {
            val response = repository.loadCategories()
            categories.postValue(handleCategoriesResponse(response))
        }catch (e:Exception){
            categories.postValue(Resource.Error(message = e.toString()))
        }
    }



    private fun handleProductsResponse(response: Response<List<Product>>): Resource<List<Product>> {
        Log.d(TAG, "handleProductsResponse: +"+response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(response.body()!!)

            }
        }
        return Resource.Error(response.message())
    }

    private fun handleCategoriesResponse(response: Response<List<Category>>): Resource<List<Category>> {
        Log.d(TAG, "handleCategoriesResponse: +"+response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(response.body()!!)

            }
        }
        return Resource.Error(response.message())
    }






    //database

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }

    fun deleteUser() = viewModelScope.launch {
        repository.deleteUser()
    }

    fun getUser() = repository.getUser()







}