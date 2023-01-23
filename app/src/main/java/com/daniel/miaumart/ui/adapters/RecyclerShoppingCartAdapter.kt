package com.daniel.miaumart.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daniel.miaumart.R
import com.daniel.miaumart.domain.extensions.load
import com.daniel.miaumart.domain.models.ShoppingCartML


class RecyclerShoppingCartAdapter(private val productsList: ArrayList<ShoppingCartML>, private val listener: CartItemClickListener): RecyclerView.Adapter<RecyclerShoppingCartAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_shopping_cart_row_design, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.print(position)
    }

    override fun getItemCount(): Int = productsList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val productName: TextView = itemView.findViewById(R.id.product_name_sc)
        private val productImage: ImageView = itemView.findViewById(R.id.product_image_sc)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price_sc)
        private val numberItems: TextView = itemView.findViewById(R.id.number_items_sc)
        private val deleteProduct: ImageView = itemView.findViewById(R.id.delete_product_sc)

        fun print(position: Int){
            productName.text = productsList[position].productName
            productImage.load(productsList[position].productImage)
            productPrice.text = "$${productsList[position].productPrice}"
            numberItems.text = "Items: ${productsList[position].numberItems}"
            deleteProduct.setOnClickListener {
                listener.onDeleteItemClickListener(productsList[position].productId)
            }
        }
    }


}