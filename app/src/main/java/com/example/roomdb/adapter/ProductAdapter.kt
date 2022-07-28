package com.example.roomdb.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb.R
import com.example.roomdb.entities.ProductEntity

class ProductAdapter(
    val context: Context,
    private val productClickInterface: ProductClickInterface
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private val allProduct = ArrayList<ProductEntity>()
    var focusView: View? = null
    private var prevFocusIndex = 0

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvItemNo: TextView       = itemView.findViewById(R.id.tvItemNo)
        private val tvItemName: TextView     = itemView.findViewById(R.id.tvItemName)
        private val tvItemQuantity: TextView = itemView.findViewById(R.id.tvItemQuantity)
        private val tvItemDiscount: TextView = itemView.findViewById(R.id.tvItemDiscount)
        private val tvItemPrice: TextView    = itemView.findViewById(R.id.tv_product_price)

        fun bind(product: ProductEntity, position: Int) {
            tvItemNo.text   = context.resources.getString(R.string.item_no, position + 1)
            tvItemName.text = context.resources.getString(
                R.string.item_name,
                product.productName,
                product.productId
            )
            tvItemQuantity.text = product.productQuantity.toString()
            tvItemDiscount.text = product.productDiscount
            tvItemPrice.text    = product.productTotalPrice.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.item_table_row,
            parent,
            false
        )
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (prevFocusIndex == position) {
            holder.itemView.setBackgroundResource(R.drawable.bg_table_item_selected_row)
        } else {
            if (position % 2 != 0) {
                holder.itemView.setBackgroundResource(R.drawable.bg_table_item_even_row)
            } else {
                holder.itemView.setBackgroundResource(R.drawable.bg_table_item)
            }
        }

        holder.itemView.setOnClickListener {
            productClickInterface.onProductClick(allProduct[position])
            if (prevFocusIndex % 2 != 0) {
                focusView?.setBackgroundResource(R.drawable.bg_table_item_even_row)
            } else {
                focusView?.setBackgroundResource(R.drawable.bg_table_item)
            }
            focusView = holder.itemView
            prevFocusIndex = holder.adapterPosition
            holder.itemView.setBackgroundResource(R.drawable.bg_table_item_selected_row)
        }

        Log.d("DEBUG", "OnBind $position")

        holder.bind(allProduct[position], position)
    }

    override fun getItemCount(): Int {
        return allProduct.size
    }

    fun updateList(newList: List<ProductEntity>) {
        allProduct.clear()
        allProduct.addAll(newList)
        notifyDataSetChanged()
    }

    fun getInitialValue() : Array<String> {
        var totalQuantity = 0
        var totalPrice = 0

        for (item in allProduct) {
            totalQuantity += item.productQuantity
            totalPrice += item.productTotalPrice
        }

        return arrayOf(
            context.resources.getString(R.string.total_item_in_cart, totalQuantity),
            context.resources.getString(R.string.total_price, totalPrice)
        )
    }
}

interface ProductClickInterface {
    fun onProductClick(product: ProductEntity)
}

interface ProductClickDeleteInterface {
    fun onIconDeleteClick(product: ProductEntity)
}
