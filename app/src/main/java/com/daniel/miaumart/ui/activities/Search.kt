package com.daniel.miaumart.ui.activities

import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.GridLayoutManager
import com.blogspot.atifsoftwares.animatoolib.Animatoo
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.ActivitySearchBinding
import com.daniel.miaumart.domain.models.SearchML
import com.daniel.miaumart.domain.utilities.NetworkStateReceiver
import com.daniel.miaumart.ui.adapters.RecyclerSearchAdapter
import com.daniel.miaumart.ui.adapters.SearchItemClickListener
import com.daniel.miaumart.ui.viewModels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Search : AppCompatActivity(), SearchItemClickListener {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var adapter: RecyclerSearchAdapter
    private lateinit var productsList: ArrayList<SearchML>
    private lateinit var networkStateReceiver: NetworkStateReceiver

    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

    }

    override fun onResume() {
        super.onResume()
        networkStateReceiver = NetworkStateReceiver(binding.noInternetAccessLayoutSearch)
        val connectivity = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiver, connectivity)
    }

    private fun initUI() {
        binding.backLayout.setOnClickListener {
            finish()
            Animatoo.animateSlideDown(this)
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        findViewById<View>(R.id.loading_data_layout_search).visibility = View.VISIBLE
        binding.recyclerSearch.visibility = View.GONE
        viewModel.allProducts.observe(this) { products -> if (products != null) initRecyclerView(products) }

        binding.inputSearch.addTextChangedListener { userFilter ->
            val productsFilter = productsList.filter { products ->
                products.productName.lowercase().contains(userFilter.toString().lowercase())
            }
            binding.recyclerSearch.visibility = if(productsFilter.isEmpty()) View.GONE else View.VISIBLE
            binding.productNotFoundLayout.visibility = if(productsFilter.isEmpty()) View.VISIBLE else View.GONE
            adapter.updateProducts(productsFilter as ArrayList<SearchML>)
        }
    }

    private fun initRecyclerView(products: ArrayList<SearchML>) {
        productsList = products
        adapter = RecyclerSearchAdapter(productsList, this)
        binding.recyclerSearch.hasFixedSize()
        binding.recyclerSearch.layoutManager = GridLayoutManager(this@Search, 2)
        binding.recyclerSearch.adapter = adapter
        findViewById<View>(R.id.loading_data_layout_search).visibility = View.GONE
        binding.recyclerSearch.visibility = View.VISIBLE
    }

    override fun onItemClickListener(pid: String, category: String) {
        Intent(this, ProductDetails::class.java).apply {
            putExtra("pid", pid)
            putExtra("category", category)
            startActivity(this)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(networkStateReceiver)
    }

    private val onBackPressedCallback: OnBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            finish()
            Animatoo.animateSlideDown(this@Search)
        }

    }
}