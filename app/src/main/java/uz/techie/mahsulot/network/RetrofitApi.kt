package uz.techie.mahsulot.network

import retrofit2.Response
import retrofit2.http.*
import uz.techie.mahsulot.model.*
import uz.techie.mahsulot.util.Constants

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

    @GET("api/products-teen-list/")
    suspend fun loadTopProducts(): Response<List<Product>>

    @GET("api/slider-list/")
    suspend fun loadBanners(): Response<MutableList<Banner>>


    @FormUrlEncoded
    @POST("api/send-sms/")
    suspend fun sendSms(
        @Header("MyToken") myToken:String,
        @Field("phone_number") phone:String
    ):Response<SmsResponse>


    @FormUrlEncoded
    @POST("api/check-code/")
    suspend fun checkSmsCode(
        @Header("MyToken") myToken:String,
        @Field("phone_number") phone: String,
        @Field("key") code: String,

    ):Response<SmsResponse>


    @FormUrlEncoded
    @POST("api/register/")
    suspend fun registerUser(
        @Header("MyToken") myToken:String,
        @Field("phone_number") phone: String,
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("birthday") birthday: String,
        @Field("gender") gender: String,

    ):Response<SmsResponse>


    @GET("api/profiles/")
    suspend fun loadProfile(
        @Header("Authorization") token:String
    ):Response<ProfileResponse>


    //stream

    @FormUrlEncoded
    @POST("api/streams-create/")
    suspend fun createStream(
        @Header("Authorization") token:String,
        @Field("name") title:String,
        @Field("url") url:String,
        @Field("status") status:String,
        @Field("product_id") product_id:Int,
        @Field("seller_id") seller_id:Int,
    ):Response<StreamResponse>

    @GET("api/streams-list/")
    suspend fun loadStreams(
        @Header("Authorization") token:String
    ):Response<List<Stream>>


    @GET("api/streams-delete/{id}")
    suspend fun deleteStream(
        @Header("Authorization") token:String,
        @Path("id") id:Int
    ):Response<StreamResponse>

    @GET("api/streams-search/")
    suspend fun searchStream(
        @Header("Authorization")token: String,
        @Query("search")searchText: String
    ):Response<List<Stream>>

    @GET("api/streams-statistic/")
    suspend fun streamStatistics(
        @Header("Authorization")token: String
    ):Response<StreamStatisticResponse>




    //withdraw
    @FormUrlEncoded
    @POST("api/pay-created/")
    suspend fun makeWithdraw(
        @Header("MyToken") myToken: String,
        @Header("Authorization")token: String,
        @Field("seller_id") seller_id: Int,
        @Field("amount") amount: Int,
        @Field("payment_card") payment_card: String,
        @Field("status") status: Int,
        @Field("desc") desc: Int,
        @Field("seller_bonus") seller_bonus: Int,
    ):Response<WithdrawResponse>

    @GET("api/pay-history/")
    suspend fun loadWithdrawHistory(
        @Header("Authorization")token: String
    ):Response<WithdrawHistoryResponse>



    //order
    @GET("api/order-history/")
    suspend fun loadOrderStatistics(
        @Header("MyToken") myToken: String,
        @Header("Authorization")token: String
    ):Response<OrderResponse>



    @FormUrlEncoded
    @POST("api/order-product/")
    suspend fun makeOrder(
        @Header("MyToken") myToken: String,
        @Header("Authorization")token: String,
        @FieldMap fieldMap:HashMap<String, Any>
    ):Response<WithdrawResponse>

    //update profile

    @FormUrlEncoded
    @POST("api/change/")
    suspend fun updateProfile(
        @Header("Authorization")token: String,
        @FieldMap map:HashMap<String, String>
    ):Response<UpdateResponse>


    //marja
    @FormUrlEncoded
    @POST("api/order-marja/")
    suspend fun makeMarjaOrder(
        @Header("MyToken") myToken: String,
        @Header("Authorization")token: String,
        @FieldMap fieldMap:HashMap<String, Any>
    ):Response<WithdrawResponse>


    //konkurs
    @GET("api/concource/")
    suspend fun loadCompetition(
        @Header("Authorization")token: String
    ):Response<CompetitionResponse>

}