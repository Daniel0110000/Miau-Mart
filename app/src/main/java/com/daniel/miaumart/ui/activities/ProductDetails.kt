package com.daniel.miaumart.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.miaumart.databinding.ActivityProductDetailsBinding
import com.daniel.miaumart.domain.extensions.load
import com.daniel.miaumart.ui.adapters.ImagesItemClickListener
import com.daniel.miaumart.ui.adapters.RecyclerPreviewPIAdapter
import com.daniel.miaumart.ui.viewModels.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetails : AppCompatActivity(), ImagesItemClickListener {

    private lateinit var binding: ActivityProductDetailsBinding

    private val viewModel: ProductDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initIUI()

    }

    private fun initIUI(){
        binding.details = viewModel
        binding.backLayout.setOnClickListener { finish() }
        viewModel.getProductDetails(intent.getStringExtra("pid").toString(), intent.getStringExtra("category").toString())
        viewModel.units.observe(this){ units -> binding.quantifyProducts.text = units.toString() }
        viewModel.totalPrice.observe(this){ total -> binding.totalPrice.text = total.toString() }
        viewModel.productsByID.observe(this){ product ->
            if(product != null){
                product.productImages?.let { initRecyclerView(it) }
                binding.productNamePd.text = product.productName
                binding.productPricePd.text = product.productPrice
            }
        }

    }

    private fun initRecyclerView(images: ArrayList<String>){
        binding.recyclerPreviewProductImages.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(this@ProductDetails, LinearLayoutManager.HORIZONTAL, false)
            adapter = RecyclerPreviewPIAdapter(images, this@ProductDetails)
        }
    }

    override fun selectedImages(url: String) {
        binding.selectedProductImage.load(url)
    }


}