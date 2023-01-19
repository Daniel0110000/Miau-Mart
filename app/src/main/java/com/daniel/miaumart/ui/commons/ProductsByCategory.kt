package com.daniel.miaumart.ui.commons

import android.content.Intent
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daniel.miaumart.R
import com.daniel.miaumart.domain.models.Products
import com.daniel.miaumart.ui.activities.ProductDetails
import com.daniel.miaumart.ui.adapters.ItemClickListener
import com.daniel.miaumart.ui.adapters.RecyclerCategoryAdapter
import com.daniel.miaumart.ui.viewModels.HomeViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductsByCategory constructor(
    private val recyclerView: RecyclerView,
    private val viewModel: HomeViewModel,
    private val fragment: Fragment,
    private val category: String
) : ItemClickListener {

    init {
        getAndDisplayProducts()
    }

    private fun getAndDisplayProducts() {
        //viewModel.getProductsByCategory(category)
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.productsByCategory.collect { products ->
                initRecyclerView(products)
            }
        }

        viewModel.isLoading.observe(fragment.viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                fragment.requireView()
                    .findViewById<ConstraintLayout>(R.id.loading_layout).visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                fragment.requireView()
                    .findViewById<ConstraintLayout>(R.id.loading_layout).visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }
        }

    }

    private fun initRecyclerView(productsList: ArrayList<Products>) {
        recyclerView.apply {
            hasFixedSize()
            layoutManager = GridLayoutManager(context, 2)
            adapter = RecyclerCategoryAdapter(productsList, this@ProductsByCategory)
        }
    }

    override fun onItemClickListener(pid: String) {
        val productDetails = Intent(fragment.requireContext(), ProductDetails::class.java)
        productDetails.putExtra("pid", pid)
        productDetails.putExtra("category", category)
        fragment.requireContext().startActivity(productDetails)
    }
}