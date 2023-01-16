package com.daniel.miaumart.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daniel.miaumart.R
import com.daniel.miaumart.domain.extensions.load
import com.daniel.miaumart.domain.models.Products

class RecyclerCategoryAdapter(private val productsList: ArrayList<Products>) :
    RecyclerView.Adapter<RecyclerCategoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_categories_rows_design, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.print(position)
    }

    override fun getItemCount(): Int = productsList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val productImage: ImageView = itemView.findViewById(R.id.product_image)
        private val productName: TextView = itemView.findViewById(R.id.product_name)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)

        fun print(position: Int) {
            productImage.load(productsList[position].productImages!![0])
            productName.text = productsList[position].productName
            productPrice.text = productsList[position].productPrice
        }

    }

}