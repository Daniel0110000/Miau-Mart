package com.daniel.miaumart.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.daniel.miaumart.R
import com.daniel.miaumart.ui.widget.BorderCardView
import com.daniel.miaumart.domain.extensions.load

class RecyclerPreviewPIAdapter(private val images: ArrayList<String>): RecyclerView.Adapter<RecyclerPreviewPIAdapter.ViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_preview_pi_row_design, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.print(position)
    }

    override fun getItemCount(): Int = images.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val productImage: ImageView = itemView.findViewById(R.id.product_image_rd)
        private val previewProductImagesLayout: BorderCardView = itemView.findViewById(R.id.preview_product_images_layout)

        fun print(position: Int){
            productImage.load(images[position])
            itemView.setOnClickListener {
                notifyItemChanged(selectedPosition)
                selectedPosition = position
                notifyItemChanged(selectedPosition)
            }
            previewProductImagesLayout.isSelected = position == selectedPosition
        }
    }

}