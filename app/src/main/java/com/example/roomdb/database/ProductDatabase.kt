package com.example.roomdb.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.roomdb.dao.ProductDao
import com.example.roomdb.entities.ProductEntity

@Database(entities = [ProductEntity::class], version = 1, exportSchema = false)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun getProductDao() : ProductDao

    companion object {
        private var INSTANCE: ProductDatabase? = null

        fun getDatabase(context: Context) : ProductDatabase {
                return INSTANCE ?: synchronized(this) {
                    val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProductDatabase::class.java,
                        "Product DB"
                    ).build()
                    INSTANCE = instance
                    instance
                }
        }
    }
}