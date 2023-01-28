package com.daniel.miaumart.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daniel.miaumart.R
import com.daniel.miaumart.domain.extensions.load
import com.daniel.miaumart.domain.models.SearchML

class RecyclerSearchAdapter(
    private var productList: ArrayList<SearchML>,
    private val listener: SearchItemClickListener
) : RecyclerView.Adapter<RecyclerSearchAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_categories_rows_design, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.print(position)
    }

    override fun getItemCount(): Int = productList.size

    fun updateProducts(productsList: ArrayList<SearchML>) {
        this.productList = productsList
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val productImage: ImageView = itemView.findViewById(R.id.product_image)
        private val productName: TextView = itemView.findViewById(R.id.product_name)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price)

        fun print(position: Int) {
            productImage.load(productList[position].productImages!![0])
            productName.text = productList[position].productName
            productPrice.text = productList[position].productPrice
            itemView.setOnClickListener {
                listener.onItemClickListener(
                    productList[position].productId,
                    productList[position].category
                )
            }
        }
    }

}