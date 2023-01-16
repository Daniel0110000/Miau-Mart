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

    private var _binding: FragmentAccessoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var productsByCategory: ProductsByCategory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccessoriesBinding.inflate(inflater, container, false)

        productsByCategory = ProductsByCategory(
            recyclerView = binding.recyclerAccessories,
            viewModel = viewModel,
            fragment = this,
            category = ACCESSORIES
        )

        return binding.root

    }

}