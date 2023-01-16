package com.daniel.miaumart.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.daniel.miaumart.R
import com.daniel.miaumart.databinding.FragmentMedicinesBinding
import com.daniel.miaumart.domain.utilities.Constants.MEDICINES
import com.daniel.miaumart.ui.commons.ProductsByCategory
import com.daniel.miaumart.ui.viewModels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Medicines : Fragment() {

    private var _binding: FragmentMedicinesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var productsByCategory: ProductsByCategory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMedicinesBinding.inflate(inflater, container, false)

        productsByCategory = ProductsByCategory(
            recyclerView = binding.recyclerMedicines,
            viewModel = viewModel,
            fragment = this,
            category = MEDICINES
        )

        return binding.root

    }
}