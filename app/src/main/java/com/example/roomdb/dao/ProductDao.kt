package com.example.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.roomdb.entities.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM tbl_product ORDER BY product_id ASC")
    fun getAllProduct() : LiveData<List<ProductEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product : ProductEntity?)

    @Delete
    suspend fun deleteProduct(product: ProductEntity?)

    @Update
    suspend fun updateProduct(product: ProductEntity?)
}