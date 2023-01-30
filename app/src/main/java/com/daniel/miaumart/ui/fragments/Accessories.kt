package com.daniel.miaumart.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.FragmentAccessoriesBinding
import com.daniel.miaumart.domain.utilities.Constants.ACCESSORIES
import com.daniel.miaumart.ui.commons.ProductsByCategory
import com.daniel.miaumart.ui.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Accessories : Fragment() {

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var productsByCategory: ProductsByCategory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_accessories, container, false)

        productsByCategory = ProductsByCategory(
            recyclerView = view.findViewById(R.id.recycler_accessories),
            viewModel = viewModel,
            fragment = this,
            loadingData = view.findViewById(R.id.loading_data_layout_accessories),
            category = ACCESSORIES
        )

        return view

    }

}