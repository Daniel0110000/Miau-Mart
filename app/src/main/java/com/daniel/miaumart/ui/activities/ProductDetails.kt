package com.daniel.miaumart.ui.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.ActivityProductDetailsBinding
import com.daniel.miaumart.domain.extensions.load
import com.daniel.miaumart.domain.utilities.NetworkStateReceiver
import com.daniel.miaumart.ui.adapters.ImagesItemClickListener
import com.daniel.miaumart.ui.adapters.RecyclerPreviewPIAdapter
import com.daniel.miaumart.ui.commons.BasicUserData
import com.daniel.miaumart.ui.commons.Snackbar
import com.daniel.miaumart.ui.viewModels.ProductDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetails : AppCompatActivity(), ImagesItemClickListener {

    private lateinit var binding: ActivityProductDetailsBinding
    private lateinit var networkStateReceiver: NetworkStateReceiver

    private val viewModel: ProductDetailsViewModel by viewModels()
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initIUI()

    }

    override fun onResume() {
        super.onResume()
        networkStateReceiver = NetworkStateReceiver(binding.noInternetAccessLayoutPd)
        val connectivity = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiver, connectivity)
    }

    private fun initIUI(){

        binding.details = viewModel

        if(BasicUserData.isRegistered){
            viewModel.isFavorites.observe(this){ favorite ->
                isFavorite = favorite
                binding.iconFavorite.setImageResource( if(favorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border )
            }
        }else binding.iconFavorite.setImageResource(R.drawable.ic_favorite_border)

        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        binding.backLayout.setOnClickListener { finish(); Animatoo.animateSlideRight(this) }
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
                Animatoo.animateSlideUp(this)
            }
        }
        binding.addFavorite.setOnClickListener {
            if(BasicUserData.isRegistered){
                if(isFavorite) viewModel.deleteFavorite()
                else viewModel.addToFavorites()
            }else{
                startActivity(Intent(this, Login::class.java))
                finish()
                Animatoo.animateSlideUp(this)
            }
        }

    }

    private fun initRecyclerView(images: ArrayList<String>){
        if(BasicUserData.isRegistered) viewModel.checkProductFavorite()
        binding.recyclerPreviewProductImages.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(this@ProductDetails, LinearLayoutManager.HORIZONTAL, false)
            adapter = RecyclerPreviewPIAdapter(images, this@ProductDetails)
        }
    }

    override fun selectedImages(url: String) = binding.selectedProductImage.load(url)

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkStateReceiver)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.stopListening(BasicUserData.username){}
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
            Animatoo.animateSlideDown(this@ProductDetails)
        }

    }

}