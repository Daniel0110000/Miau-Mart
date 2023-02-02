package com.daniel.miaumart.ui.commons

import android.content.Intent
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.atifsoftwares.animatoolib.Animatoo
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
    private val loadingData: View,
    private val category: String
) : ItemClickListener {

    init {
        getAndDisplayProducts()
    }

    private fun getAndDisplayProducts() {
        viewModel.getProductsByCategory(category)
        CoroutineScope(Dispatchers.Main).launch {
            viewModel.productsByCategory.collect { products -> initRecyclerView(products) }
        }

        viewModel.isLoading.observe(fragment.viewLifecycleOwner) { isLoading ->
            loadingData.visibility = if (isLoading) View.VISIBLE else View.GONE
            recyclerView.visibility = if (isLoading) View.GONE else View.VISIBLE
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
        Intent(fragment.requireContext(), ProductDetails::class.java).apply {
            putExtra("pid", pid)
            putExtra("category", category)
            fragment.requireContext().startActivity(this)
            Animatoo.animateSlideLeft(fragment.requireActivity())

        }
    }
}