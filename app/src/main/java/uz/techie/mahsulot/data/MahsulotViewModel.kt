package uz.techie.mahsulot.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import uz.techie.mahsulot.model.*
import uz.techie.mahsulot.util.Resource
import java.io.InterruptedIOException
import java.net.UnknownHostException
import javax.inject.Inject

@HiltViewModel
class MahsulotViewModel @Inject constructor(
    private val repository: MahsulotRepository
) : ViewModel() {
    private val TAG = "MahsulotViewModel"

    //products
    val products: MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val streamProducts: MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val topProducts: MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val productsByCategory: MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val searchProducts: MutableLiveData<Resource<List<Product>>> = MutableLiveData()
    val categories: MutableLiveData<Resource<List<Category>>> = MutableLiveData()
    val banners: MutableLiveData<Resource<MutableList<Banner>>> = MutableLiveData()

    //login
    val sms: MutableLiveData<Resource<SmsResponse>> = MutableLiveData()
    val smsConfirmation: MutableLiveData<Resource<SmsResponse>> = MutableLiveData()
    val registration: MutableLiveData<Resource<SmsResponse>> = MutableLiveData()

    //profile
    val profile: MutableLiveData<Resource<ProfileResponse>> = MutableLiveData()

    //streams
    val streams: MutableLiveData<Resource<List<Stream>>> = MutableLiveData()
    val streamResponse: MutableLiveData<Resource<StreamResponse>> = MutableLiveData()
    val streamStatistics: MutableLiveData<Resource<StreamStatisticResponse>> = MutableLiveData()

    //order
    val orderStatistics: MutableLiveData<Resource<OrderResponse>> = MutableLiveData()
    val order: MutableLiveData<Resource<WithdrawResponse>> = MutableLiveData()

    //withdraw
    val withdraw: MutableLiveData<Resource<WithdrawResponse>> = MutableLiveData()
    val withdrawHistory: MutableLiveData<Resource<WithdrawHistoryResponse>> = MutableLiveData()

    //update
    val update: MutableLiveData<Resource<UpdateResponse>> = MutableLiveData()

    val competition: MutableLiveData<Resource<CompetitionResponse>> = MutableLiveData()


    fun loadProducts() = viewModelScope.launch {
        products.postValue(Resource.Loading())
        try {
            val response = repository.loadProducts()
            products.postValue(handleProductsResponse(response))
        } catch (e: UnknownHostException) {
            products.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            products.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            products.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun loadStreamProducts() = viewModelScope.launch {
        streamProducts.postValue(Resource.Loading())
        try {
            val response = repository.loadProducts()
            streamProducts.postValue(handleProductsResponse(response))
        } catch (e: UnknownHostException) {
            streamProducts.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            streamProducts.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            streamProducts.postValue(Resource.Error(message = e.toString()))
        }
    }


    fun loadProductsByCategory(catId: Int) = viewModelScope.launch {
        productsByCategory.postValue(Resource.Loading())
        try {
            val response = repository.loadProductsByCategory(catId)
            productsByCategory.postValue(handleProductsResponse(response))
        } catch (e: UnknownHostException) {
            productsByCategory.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            productsByCategory.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            productsByCategory.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun searchProducts(text: String) = viewModelScope.launch {
        searchProducts.postValue(Resource.Loading())
        try {
            val response = repository.searchProducts(text)
            searchProducts.postValue(handleProductsResponse(response))
        } catch (e: UnknownHostException) {
            searchProducts.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            searchProducts.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            searchProducts.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun loadTopProducts() = viewModelScope.launch {
        topProducts.postValue(Resource.Loading())
        try {
            val response = repository.loadTopProducts()
            topProducts.postValue(handleProductsResponse(response))
        } catch (e: UnknownHostException) {
            topProducts.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            topProducts.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            topProducts.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun loadCategories() = viewModelScope.launch {
        categories.postValue(Resource.Loading())
        try {
            val response = repository.loadCategories()
            categories.postValue(handleCategoriesResponse(response))
        } catch (e: UnknownHostException) {
            categories.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            categories.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            categories.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun loadBanners() = viewModelScope.launch {
        banners.postValue(Resource.Loading())
        try {
            val response = repository.loadBanners()
            banners.postValue(handleBannerResponse(response))
        } catch (e: UnknownHostException) {
            banners.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            banners.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            banners.postValue(Resource.Error(message = e.toString()))
        }
    }


    fun sendSms(phone: String) = viewModelScope.launch {
        sms.postValue(Resource.Loading())
        try {
            val response = repository.sendSms(phone)
            sms.postValue(handleRegistrationResponse(response))
        } catch (e: UnknownHostException) {
            sms.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            sms.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            sms.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun confirmSms(phone: String, code: String) = viewModelScope.launch {
        smsConfirmation.postValue(Resource.Loading())
        try {
            val response = repository.checkSms(phone, code)
            smsConfirmation.postValue(handleRegistrationResponse(response))
        } catch (e: UnknownHostException) {
            smsConfirmation.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            smsConfirmation.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            smsConfirmation.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun registerUser(
        phone: String,
        firstName: String,
        lastname: String,
        birthday: String,
        gender: String
    ) = viewModelScope.launch {
        registration.postValue(Resource.Loading())
        try {
            val response = repository.registerUser(phone, firstName, lastname, birthday, gender)
            registration.postValue(handleRegistrationResponse(response))
        } catch (e: UnknownHostException) {
            registration.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            registration.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            registration.postValue(Resource.Error(message = e.toString()))
        }


    }

    fun loadProfile(token: String) = viewModelScope.launch {
        profile.postValue(Resource.Loading())
        try {
            val response = repository.loadProfile(token)
            profile.postValue(handleProfileResponse(response))
        } catch (e: UnknownHostException) {
            profile.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            profile.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            profile.postValue(Resource.Error(message = e.toString()))
        }
    }


    fun createStream(
        token: String,
        title: String,
        url: String,
        status: String,
        productId: Int,
        sellerId: Int
    ) = viewModelScope.launch {
        streamResponse.postValue(Resource.Loading())
        try {
            val response = repository.createStream(token, title, url, status, productId, sellerId)
            streamResponse.postValue(handleStreamCreationResponse(response))

        } catch (e: UnknownHostException) {
            streamResponse.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            streamResponse.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            streamResponse.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun loadStreams(token: String) = viewModelScope.launch {
        streams.postValue(Resource.Loading())
        try {
            val response = repository.loadStreams(token)
            streams.postValue(handleStreamResponse(response))
        } catch (e: UnknownHostException) {
            streams.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            streams.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            streams.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun deleteStream(token: String, id: Int) = viewModelScope.launch {
        streamResponse.postValue(Resource.Loading())
        try {
            val response = repository.deleteStream(token, id)
            streamResponse.postValue(handleStreamCreationResponse(response))

        } catch (e: UnknownHostException) {
            streamResponse.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            streamResponse.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            streamResponse.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun searchStreams(token: String, search: String) = viewModelScope.launch {
        streams.postValue(Resource.Loading())
        try {
            val response = repository.searchStream(token, search)
            streams.postValue(handleStreamResponse(response))
        } catch (e: UnknownHostException) {
            streams.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            streams.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            streams.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun loadSreamsStatistics(token: String) = viewModelScope.launch {
        streamStatistics.postValue(Resource.Loading())
        try {
            val response = repository.streamStatistics(token)
            streamStatistics.postValue(handleStreamStatisticsResponse(response))
        } catch (e: UnknownHostException) {
            streamStatistics.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            streamStatistics.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            streamStatistics.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun loadOrderStatistics(token: String) = viewModelScope.launch {
        orderStatistics.postValue(Resource.Loading())
        try {
            val response = repository.loadOrderStatistics(token)
            orderStatistics.postValue(handleOrderStatisticsResponse(response))
        } catch (e: UnknownHostException) {
            orderStatistics.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            orderStatistics.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            orderStatistics.postValue(Resource.Error(message = e.toString()))
        }

    }

    fun makePayment(
        myToken: String,
        token: String,
        sellerId: Int,
        amount: Int,
        payment_card: String,
        status: Int,
        desc: Int,
        seller_bonus: Int
    ) = viewModelScope.launch {
        withdraw.postValue(Resource.Loading())
        try {
            val response = repository.makeWithdraw(
                myToken,
                token,
                sellerId,
                amount,
                payment_card,
                status,
                desc,
                seller_bonus
            )
            withdraw.postValue(handleWithdrawResponse(response))
        } catch (e: UnknownHostException) {
            withdraw.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            withdraw.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            withdraw.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun loadWithdrawHistory(token: String) = viewModelScope.launch {
        withdrawHistory.postValue(Resource.Loading())
        try {
            val response = repository.loadWithdrawHistory(token)
            withdrawHistory.postValue(handleWithdrawHistoryResponse(response))
        } catch (e: UnknownHostException) {
            withdrawHistory.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            withdrawHistory.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            withdrawHistory.postValue(Resource.Error(message = e.toString()))
        }
    }


    fun makeOder(myToken: String, token: String, map: HashMap<String, Any>) =
        viewModelScope.launch {
            order.postValue(Resource.Loading())
            try {
                val response = repository.makeOrder(myToken, token, map)
                order.postValue(handleWithdrawResponse(response))
            } catch (e: UnknownHostException) {
                order.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
            } catch (e: InterruptedIOException) {
                order.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
            } catch (e: Exception) {
                order.postValue(Resource.Error(message = e.toString()))
            }
        }

    fun makeMarjaOder(myToken: String, token: String, map: HashMap<String, Any>) =
        viewModelScope.launch {
            order.postValue(Resource.Loading())
            try {
                val response = repository.makeMarjaOrder(myToken, token, map)
                order.postValue(handleWithdrawResponse(response))
            } catch (e: UnknownHostException) {
                order.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
            } catch (e: InterruptedIOException) {
                order.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
            } catch (e: Exception) {
                order.postValue(Resource.Error(message = e.toString()))
            }
        }


    fun updateProfile(token: String, map: HashMap<String, String>) = viewModelScope.launch {
        update.postValue(Resource.Loading())
        try {
            val response = repository.updateProfile(token, map)
            update.postValue(handleUpdateResponse(response))
        } catch (e: UnknownHostException) {
            update.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            update.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            update.postValue(Resource.Error(message = e.toString()))
        }
    }

    fun loadCompetition(token: String) = viewModelScope.launch {
        competition.postValue(Resource.Loading())
        try {
            val response = repository.loadCompetition(token)
            competition.postValue(handleCompetitionResponse(response))
        } catch (e: UnknownHostException) {
            competition.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: InterruptedIOException) {
            competition.postValue(Resource.Error("Internetga bog'lanishda xatolik!"))
        } catch (e: Exception) {
            competition.postValue(Resource.Error(message = e.toString()))
        }
    }


//handle

    private fun handleProductsResponse(response: Response<List<Product>>): Resource<List<Product>> {
        Log.d(TAG, "handleProductsResponse: +" + response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(response.body()!!)

            }
        }
        return Resource.Error(response.message())
    }

    private fun handleCategoriesResponse(response: Response<List<Category>>): Resource<List<Category>> {
        Log.d(TAG, "handleCategoriesResponse: +" + response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(response.body()!!)

            }
        }
        return Resource.Error(response.message())
    }

    private fun handleBannerResponse(response: Response<MutableList<Banner>>): Resource<MutableList<Banner>> {
        Log.d(TAG, "handleBannerResponse: " + response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleRegistrationResponse(response: Response<SmsResponse>): Resource<SmsResponse> {
        Log.d(TAG, "handleRegistrationResponse: " + response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }

    private fun handleProfileResponse(response: Response<ProfileResponse>): Resource<ProfileResponse> {
        Log.d(TAG, "handleProfileResponse: " + response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }

        return Resource.Error(response.message())
    }


    private fun handleStreamResponse(response: Response<List<Stream>>): Resource<List<Stream>> {
        Log.d(TAG, "handleStreamResponse: " + response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleStreamCreationResponse(response: Response<StreamResponse>): Resource<StreamResponse> {
        Log.d(TAG, "handleStreamResponse: " + response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleStreamStatisticsResponse(response: Response<StreamStatisticResponse>): Resource<StreamStatisticResponse> {
        Log.d(TAG, "handleStreamStatisticsResponse: " + response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleOrderStatisticsResponse(response: Response<OrderResponse>): Resource<OrderResponse> {
        Log.d(TAG, "handleOrderStatisticsResponse: " + response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleWithdrawResponse(response: Response<WithdrawResponse>): Resource<WithdrawResponse> {
        Log.d(TAG, "handleWithdrawResponse: " + response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleWithdrawHistoryResponse(response: Response<WithdrawHistoryResponse>): Resource<WithdrawHistoryResponse> {
        Log.d(TAG, "handleWithdrawHistoryResponse: " + response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleUpdateResponse(response: Response<UpdateResponse>): Resource<UpdateResponse> {
        Log.d(TAG, "handleUpdateResponse: " + response)
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleCompetitionResponse(response: Response<CompetitionResponse>): Resource<CompetitionResponse> {
        Log.d(TAG, "handleCompetitionResponse: " + response)
        if (response.isSuccessful) {
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