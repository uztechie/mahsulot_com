package uz.techie.mahsulot.data

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import uz.techie.mahsulot.model.Product
import uz.techie.mahsulot.model.ProductResponse
import uz.techie.mahsulot.util.Resource
import javax.inject.Inject

@HiltViewModel
class MahsulotViewModel @Inject constructor(
    private val repository: MahsulotRepository
) : ViewModel() {
    private val TAG = "MahsulotViewModel"
    val products:MutableLiveData<Resource<List<Product>>> = MutableLiveData()

    init {
        loadProducts()
    }

    private fun loadProducts() = viewModelScope.launch {
        products.postValue(Resource.Loading())
        try {
            val response = repository.loadProducts()
            products.postValue(handleProductsResponse(response))
        } catch (e: Exception) {
            products.postValue(Resource.Error(message = e.toString()))
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


}