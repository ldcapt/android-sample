package com.example.roomdb.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.roomdb.database.ProductDatabase
import com.example.roomdb.entities.ProductEntity
import com.example.roomdb.repository.ProductRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(app: Application): AndroidViewModel(app) {
    val allProduct: LiveData<List<ProductEntity>>
    private val repository: ProductRepository

    init {
        val dao = ProductDatabase.getDatabase(app).getProductDao()
        repository = ProductRepository(dao)
        allProduct = repository.allProducts
    }

    fun deleteProduct(product: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(product)
    }

    fun updateProduct(product: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.update(product)
    }

    fun addProduct(product: ProductEntity) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(product)
    }
}