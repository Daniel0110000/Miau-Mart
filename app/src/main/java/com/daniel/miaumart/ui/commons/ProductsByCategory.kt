package com.daniel.miaumart.ui.commons

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.daniel.miaumart.domain.models.Products
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
) {

    init {
        getAndDisplayProducts()
    }

    private fun getAndDisplayProducts() {
        viewModel.getProductsByCategory(category)
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.productsByCategory.collect { products ->
                initRecyclerView(products)
            }
        }
    }

    private fun initRecyclerView(productsList: ArrayList<Products>) {
        recyclerView.apply {
            hasFixedSize()
            layoutManager = GridLayoutManager(context, 2)
            adapter = RecyclerCategoryAdapter(productsList)
        }
    }
}