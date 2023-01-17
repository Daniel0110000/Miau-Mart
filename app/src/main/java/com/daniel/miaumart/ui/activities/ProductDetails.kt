package com.daniel.miaumart.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.daniel.miaumart.databinding.ActivityProductDetailsBinding
import com.daniel.miaumart.ui.adapters.RecyclerPreviewPIAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductDetails : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initIUI()

    }

    private fun initIUI(){
        binding.backLayout.setOnClickListener { finish() }
        initRecyclerView()
    }

    private fun initRecyclerView(){
        binding.recyclerPreviewProductImages.apply {
            hasFixedSize()
            layoutManager = LinearLayoutManager(this@ProductDetails, LinearLayoutManager.HORIZONTAL, false)
            adapter = RecyclerPreviewPIAdapter(arrayListOf(
                "https://firebasestorage.googleapis.com/v0/b/miau-mart.appspot.com/o/foods%2Facana-bountiful-catch-gatos.png?alt=media&token=2fe738ae-e7b6-4e98-a80e-af4e7808efbf",
                "https://firebasestorage.googleapis.com/v0/b/miau-mart.appspot.com/o/foods%2Facana-first-feast-gatitos.png?alt=media&token=183386db-9258-493b-91f4-211d8c7e1d12",
                "https://firebasestorage.googleapis.com/v0/b/miau-mart.appspot.com/o/foods%2Facana-homestead-harvest-gatos.png?alt=media&token=9d43c32c-3240-4180-a692-f9bd60e43f53"
            ))
        }
    }


}