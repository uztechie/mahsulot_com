package uz.techie.mahsulot.db

import androidx.room.Database
import androidx.room.RoomDatabase
import uz.techie.mahsulot.model.User

@Database(entities = [ User::class ], version = 2)
abstract class MahsulotDatabase:RoomDatabase() {
    abstract fun mahsulotDao():MahsulotDao
}