package uz.techie.mahsulot.db

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import uz.techie.mahsulot.model.User
import javax.inject.Singleton

@Dao
interface MahsulotDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("delete from user")
    suspend fun deleteUser()

    @Query("select * from user limit 1")
    fun getUser():LiveData<User>

}