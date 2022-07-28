package com.example.roomdb.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb.R
import com.example.roomdb.adapter.ProductAdapter
import com.example.roomdb.adapter.ProductClickDeleteInterface
import com.example.roomdb.adapter.ProductClickInterface
import com.example.roomdb.entities.ProductEntity
import com.example.roomdb.viewmodel.ProductViewModel

class MainActivity : AppCompatActivity(), ProductClickInterface, ProductClickDeleteInterface {
    private lateinit var productList: RecyclerView
    private lateinit var viewModel: ProductViewModel
    private lateinit var tvProductName: TextView
    private lateinit var tvProductPrice: TextView
    private lateinit var tvTotalQuantity: TextView
    private lateinit var tvProductQuantity: TextView
    private lateinit var tvProductTotalPrice: TextView
    private lateinit var currentFocusProduct: ProductEntity
    private lateinit var btnDecreaseQuantity: Button
    private lateinit var productAdapter: ProductAdapter
    private lateinit var tvSumUp: TextView
    private lateinit var initialValue: Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_product)
        productAdapter       = ProductAdapter(this, this)
        tvProductName        = findViewById(R.id.tv_detail_item_name)
        tvProductPrice       = findViewById(R.id.tv_product_price)
        productList          = findViewById(R.id.rv_product_list)
        tvProductQuantity    = findViewById(R.id.input_product_quantity)
        tvProductTotalPrice  = findViewById(R.id.tv_product_total_price)
        tvTotalQuantity      = findViewById(R.id.tv_item_count)
        btnDecreaseQuantity  = findViewById(R.id.btnDecrease)
        tvSumUp              = findViewById(R.id.tv_total_cost)

        val btnIncreaseQuantity = findViewById<Button>(R.id.btnIncrease)
        val btnDeleteProduct    = findViewById<Button>(R.id.btnDeleteProduct)
        val btnAddProduct       = findViewById<Button>(R.id.btnScanBarcodeError)

        productList.layoutManager = LinearLayoutManager(this)
        productList.adapter       = productAdapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(ProductViewModel::class.java)

        viewModel.allProduct.observe(
            this,
            Observer {
                list -> list?.let {
                    productAdapter.updateList(it)
                }

                initialValue = productAdapter.getInitialValue()
                tvTotalQuantity.text = initialValue[0]
                tvSumUp.text         = initialValue[1]
            }
        )

        btnAddProduct.setOnClickListener { addNewProduct() }
        btnDeleteProduct.setOnClickListener { onIconDeleteClick(currentFocusProduct) }
        btnIncreaseQuantity.setOnClickListener { changeQuantity(true) }
        btnDecreaseQuantity.setOnClickListener { changeQuantity(false) }
    }

    private fun changeQuantity(isIncreaseMode: Boolean) {
        if (isIncreaseMode) {
            currentFocusProduct.productQuantity++
            if (!btnDecreaseQuantity.isEnabled) {
                btnDecreaseQuantity.isEnabled = true
            }
        } else {
            if(--currentFocusProduct.productQuantity <= 1) {
                currentFocusProduct.productQuantity = 1
                btnDecreaseQuantity.isEnabled = false
            }
        }
        tvProductQuantity.text = currentFocusProduct.productQuantity.toString()
        updateProductQuantity()
    }

    private fun addNewProduct() {
        val newProduct = ProductEntity(
            productName = "味の素ガラスープ",
            productPrice = 157,
            productDiscount = null
        )
        viewModel.addProduct(newProduct)
    }

    private fun loadProductToUI() {
        tvProductName.text = currentFocusProduct.productName
        tvProductPrice.text = currentFocusProduct.productPrice.toString()
        tvProductQuantity.text = currentFocusProduct.productQuantity.toString()
        tvProductTotalPrice.text = applicationContext.resources.getString(
            R.string.total_price,
            currentFocusProduct.productTotalPrice
        )
    }

    private fun updateProductQuantity() {
        currentFocusProduct.productTotalPrice = currentFocusProduct.productQuantity * currentFocusProduct.productPrice
        tvProductTotalPrice.text = applicationContext.resources.getString(
            R.string.total_price,
            currentFocusProduct.productTotalPrice
        )

        viewModel.updateProduct(currentFocusProduct)
    }

    override fun onProductClick(product: ProductEntity) {
        currentFocusProduct = product
        loadProductToUI()
    }

    override fun onIconDeleteClick(product: ProductEntity) {
        viewModel.deleteProduct(product)
    }
}