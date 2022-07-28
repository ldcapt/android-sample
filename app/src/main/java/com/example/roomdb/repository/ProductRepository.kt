package com.example.roomdb.repository

import androidx.lifecycle.LiveData
import com.example.roomdb.dao.ProductDao
import com.example.roomdb.entities.ProductEntity

class ProductRepository(private val productDao: ProductDao) {
    val allProducts: LiveData<List<ProductEntity>> = productDao.getAllProduct()

    suspend fun insert(product: ProductEntity) {
        productDao.insertProduct(product)
    }

    suspend fun delete(product: ProductEntity) {
        productDao.deleteProduct(product)
    }

    suspend fun update(product: ProductEntity) {
        productDao.updateProduct(product)
    }
}