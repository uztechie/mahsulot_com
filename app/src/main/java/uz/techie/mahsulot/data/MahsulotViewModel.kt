package uz.techie.mahsulot.data

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import uz.techie.mahsulot.model.*
import uz.techie.mahsulot.util.Resource
import javax.inject.Inject

@HiltViewModel
class MahsulotViewModel @Inject constructor(
    private val repository: MahsulotRepository
) : ViewModel() {
    private val TAG = "MahsulotViewModel"

    //products
    val products:MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val streamProducts:MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val topProducts:MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val productsByCategory:MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val searchProducts:MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val categories:MutableLiveData<Resource<List<Category>>> = MutableLiveData()
    val banners:MutableLiveData<Resource<MutableList<Banner>>> = MutableLiveData()

    //login
    val sms:MutableLiveData<Resource<SmsResponse>> = MutableLiveData()
    val smsConfirmation:MutableLiveData<Resource<SmsResponse>> = MutableLiveData()
    val registration:MutableLiveData<Resource<SmsResponse>> = MutableLiveData()

    //profile
    val profile:MutableLiveData<Resource<ProfileResponse>> = MutableLiveData()

    //streams
    val streams:MutableLiveData<Resource<List<Stream>>> = MutableLiveData()



    fun loadProducts() = viewModelScope.launch {
        products.postValue(Resource.Loading())
        try {
            val response = repository.loadProducts()
            products.postValue(handleProductsResponse(response))
        } catch (e: Exception) {
            products.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun loadStreamProducts() = viewModelScope.launch {
        streamProducts.postValue(Resource.Loading())
        try {
            val response = repository.loadProducts()
            streamProducts.postValue(handleProductsResponse(response))
        } catch (e: Exception) {
            streamProducts.postValue(Resource.Error(message = e.toString()))
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

    fun loadTopProducts() = viewModelScope.launch {
        topProducts.postValue(Resource.Loading())
        try {
            val response = repository.loadTopProducts()
            topProducts.postValue(handleProductsResponse(response))
        } catch (e: Exception) {
            topProducts.postValue(Resource.Error(message = e.toString()))
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

    fun loadBanners() = viewModelScope.launch {
        banners.postValue(Resource.Loading())
        try {
            val response = repository.loadBanners()
            banners.postValue(handleBannerResponse(response))
        }catch (e:Exception){
            banners.postValue(Resource.Error(message = e.toString()))
        }
    }


    fun sendSms(phone:String) = viewModelScope.launch {
        sms.postValue(Resource.Loading())
        try {
            val response = repository.sendSms(phone)
             sms.postValue(handleRegistrationResponse(response))
        }catch (e:Exception){
            sms.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun confirmSms(phone:String, code:String) = viewModelScope.launch {
        smsConfirmation.postValue(Resource.Loading())
        try {
            val response = repository.checkSms(phone, code)
            smsConfirmation.postValue(handleRegistrationResponse(response))
        }catch (e:Exception){
            smsConfirmation.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun registerUser(
        phone: String,
        firstName:String,
        lastname:String,
        birthday:String,
        gender:String
    ) = viewModelScope.launch {
        registration.postValue(Resource.Loading())
        try {
            val response = repository.registerUser(phone, firstName, lastname, birthday, gender)
            registration.postValue(handleRegistrationResponse(response))
        }catch (e:Exception){
            registration.postValue(Resource.Error(message = e.toString()))
        }


    }

    fun loadProfile(token:String) = viewModelScope.launch {
        profile.postValue(Resource.Loading())
        try {
            val response = repository.loadProfile(token)
            profile.postValue(handleProfileResponse(response))
        }catch (e:Exception){
            profile.postValue(Resource.Error(message = e.toString()))
        }
    }


    fun loadStreams(token: String) = viewModelScope.launch {
        streams.postValue(Resource.Loading())
        try {
            val response = repository.loadStreams(token)
            streams.postValue(handleStreamResponse(response))
        }catch (e:Exception){
            streams.postValue(Resource.Error(message = e.toString()))
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

    private fun handleBannerResponse(response:Response<MutableList<Banner>>): Resource<MutableList<Banner>>{
        Log.d(TAG, "handleBannerResponse: "+response)
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleRegistrationResponse(response: Response<SmsResponse>):Resource<SmsResponse>{
        Log.d(TAG, "handleRegistrationResponse: "+response)
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleProfileResponse(response: Response<ProfileResponse>):Resource<ProfileResponse>{
        Log.d(TAG, "handleProfileResponse: "+response)
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }


    private fun handleStreamResponse(response: Response<List<Stream>>):Resource<List<Stream>>{
        Log.d(TAG, "handleStreamResponse: "+response)
        if (response.isSuccessful){
            response.body()?.let {
                return Resource.Success(it)
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