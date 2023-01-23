package com.daniel.miaumart.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.miaumart.databinding.ActivityProductDetailsBinding
import com.daniel.miaumart.domain.extensions.load
import com.daniel.miaumart.ui.adapters.ImagesItemClickListener
import com.daniel.miaumart.ui.adapters.RecyclerPreviewPIAdapter
import com.daniel.miaumart.ui.commons.BasicUserData
import com.daniel.miaumart.ui.commons.Snackbar
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
        viewModel.totalPrice.observe(this){ total -> binding.totalPrice.text = "%.2f".format(total) }
        viewModel.units.observe(this){ units -> binding.quantifyProducts.text = units.toString() }
        viewModel.message.observe(this){ message -> Snackbar.showMessage(message, binding.productDetailsLayout) }
        viewModel.productsByID.observe(this){ product ->
            if(product != null){
                product.productImages?.let { initRecyclerView(it) }
                binding.productNamePd.text = product.productName
                binding.productPricePd.text = product.productPrice
            }
        }
        binding.addToCard.setOnClickListener {
            if(BasicUserData.isRegistered) viewModel.addToCard()
            else {
                startActivity(Intent(this, Login::class.java))
                finish()
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