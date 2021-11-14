package uz.techie.mahsulot.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.techie.mahsulot.db.MahsulotDatabase
import uz.techie.mahsulot.network.RetrofitApi
import uz.techie.mahsulot.util.Constants
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit =
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Provides
    @Singleton
    fun provideRetrofitApi(retrofit: Retrofit): RetrofitApi =
        retrofit.create(RetrofitApi::class.java)


    @Provides
    @Singleton
    fun provideDatabase(app:Application):MahsulotDatabase =
        Room.databaseBuilder(app, MahsulotDatabase::class.java, "mahsulot.db")
            .fallbackToDestructiveMigration()
            .build()

}