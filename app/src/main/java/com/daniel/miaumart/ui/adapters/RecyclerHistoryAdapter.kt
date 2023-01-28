package com.daniel.miaumart.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daniel.miaumart.R
import com.daniel.miaumart.domain.extensions.load
import com.daniel.miaumart.domain.models.History

class RecyclerHistoryAdapter(private val historyList: ArrayList<History>) :
    RecyclerView.Adapter<RecyclerHistoryAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_history_row_design, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.print(position)
    }

    override fun getItemCount(): Int = historyList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val productImage: ImageView = itemView.findViewById(R.id.product_image_his)
        private val productName: TextView = itemView.findViewById(R.id.product_name_his)
        private val quantity: TextView = itemView.findViewById(R.id.quantity_products_his)
        private val totalPrice: TextView = itemView.findViewById(R.id.total_price)
        private val date: TextView = itemView.findViewById(R.id.date_of_purchase)

        fun print(position: Int) {
            productImage.load(historyList[position].productImage)
            productName.text = historyList[position].productName
            quantity.text = historyList[position].quantityProduct
            val endTotalPrice =
                historyList[position].productPrice.toDouble() * historyList[position].quantityProduct.toInt()
            totalPrice.text = endTotalPrice.toString()
            date.text = historyList[position].date

        }

    }

}