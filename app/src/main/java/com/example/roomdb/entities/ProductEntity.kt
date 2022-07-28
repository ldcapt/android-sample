package com.example.roomdb.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_product")
data class ProductEntity (
    @ColumnInfo(name = "product_name")
    val productName: String,

    @ColumnInfo(name = "product_price")
    val productPrice: Int,

    @ColumnInfo(name = "product_discount")
    val productDiscount: String?
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    var productId: Int = 0

    @ColumnInfo(name = "product_quantity")
    var productQuantity: Int = 1

    @ColumnInfo(name = "focus_status")
    var isFocus: Boolean = false

    @ColumnInfo(name = "product_total_price")
    var productTotalPrice = productQuantity * productPrice

    @ColumnInfo(name = "product_tax")
    var productTax = "外税　8%"

    @ColumnInfo(name = "product_bar_code")
    var productBarCode = "4901001274574"
}