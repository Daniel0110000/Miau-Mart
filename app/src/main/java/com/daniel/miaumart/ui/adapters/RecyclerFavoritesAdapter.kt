package com.daniel.miaumart.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daniel.miaumart.R
import com.daniel.miaumart.domain.extensions.load
import com.daniel.miaumart.domain.models.FavoritesML

class RecyclerFavoritesAdapter(
    private var favoritesList: ArrayList<FavoritesML>,
    private val listener: FavoritesItemClickListener
) : RecyclerView.Adapter<RecyclerFavoritesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_favorites_row_design, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.print(position)
    }

    override fun getItemCount(): Int = favoritesList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val productName: TextView = itemView.findViewById(R.id.product_name_fav)
        private val productImage: ImageView = itemView.findViewById(R.id.product_image_fav)
        private val productPrice: TextView = itemView.findViewById(R.id.product_price_fav)
        private val deleteFavorites: ImageView = itemView.findViewById(R.id.delete_favorites)

        fun print(position: Int) {
            productName.text = favoritesList[position].productName
            productImage.load(favoritesList[position].productImage)
            productPrice.text = favoritesList[position].productPrice
            itemView.setOnClickListener {
                listener.onItemClickListener(
                    favoritesList[position].productId,
                    favoritesList[position].productCategory
                )
            }
            deleteFavorites.setOnClickListener { listener.onDeleteClickListener(favoritesList[position].id) }
        }
    }

}